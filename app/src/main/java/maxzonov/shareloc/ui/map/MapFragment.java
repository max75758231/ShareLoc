package maxzonov.shareloc.ui.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;

public class MapFragment extends MvpAppCompatFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        maxzonov.shareloc.ui.map.MapView {

    @InjectPresenter MapPresenter mapPresenter;

    private GoogleMap googleMap;

    private View view;

    private String latitude, longitude, address;

    private BottomSheetBehavior sheetBehavior;
    private OnLocationChangedListener listener;

    @BindString(R.string.all_share_title) String shareTitle;
    @BindString(R.string.location_tv_google) String stringGoogle;
    @BindString(R.string.location_tv_yandex) String stringYandex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        initSharedPreferences();
        if (!latitude.equals(getString(R.string.prefs_latitude_default))
                && !longitude.equals(getString(R.string.prefs_longitude_default))) {
            mapPresenter.getAddress(getActivity(), latitude, longitude);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = this.view.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnLocationChangedListener) context;
        } catch (ClassCastException e) {
            listener = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        this.googleMap = googleMap;

        googleMap.setOnMarkerDragListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(makeDouble(latitude), makeDouble(longitude)))
                .draggable(true).title(address));

        CameraPosition cameraPosition =
                CameraPosition.builder()
                        .target(new LatLng(makeDouble(latitude), makeDouble(longitude)))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void initSharedPreferences() {

        PreferencesHelper prefsHelperLatitude = new PreferencesHelper(getActivity(),
                getString(R.string.prefs_latitude_key));
        PreferencesHelper prefsHelperLongitude = new PreferencesHelper(getActivity(),
                getString(R.string.prefs_longitude_key));

        latitude = prefsHelperLatitude.readFromPrefs(getActivity(),
                getString(R.string.prefs_latitude_key));
        longitude = prefsHelperLongitude.readFromPrefs(getActivity(),
                getString(R.string.prefs_longitude_key));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.toolbar_map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_satellite:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.action_hybrid:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.action_map:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        latitude = String.valueOf(marker.getPosition().latitude);
        longitude = String.valueOf(marker.getPosition().longitude);

        mapPresenter.getAddressAndSetTitle(getActivity(), latitude, longitude, marker);
    }

    @OnClick(R.id.btn_bottom_sheet)
    void onBottomSheetClicked() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (listener != null) {
            listener.onLocationChanged(latitude, longitude, address);
        } else {
            Toast.makeText(getActivity(), getString(R.string.bottom_sheet_listener_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_bottom_sheet_share)
    void onBottomSheetShareClicked() {
        String sendInfo = getString(R.string.bottom_sheet_i_am_here_send) + "\n"
                + stringGoogle + " " + getString(R.string.all_google_maps_link, latitude, longitude) + "\n"
                + stringYandex + " " + getString(R.string.all_yandex_maps_link, latitude, longitude);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendInfo);
        startActivity(Intent.createChooser(intent, shareTitle));
    }

    @Override
    public void showAddressInMarker(String address) {
        this.address = address;
    }

    @Override
    public void showAndRefreshAddressInMarker(String address, Marker marker) {
        this.address = address;
        marker.setTitle(address);
    }

    private double makeDouble(String coord) {
        return Double.parseDouble(coord);
    }
}