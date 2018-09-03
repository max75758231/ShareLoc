package maxzonov.shareloc.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
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

import butterknife.Unbinder;
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

    private PreferencesHelper prefsHelperLatitude;
    private PreferencesHelper prefsHelperLongitude;

    @BindView(R.id.map_bottom_sheet_layout) NestedScrollView bottomSheetLayout;

    @BindString(R.string.all_share_title) String shareTitle;
    @BindString(R.string.location_tv_google) String stringGoogle;
    @BindString(R.string.location_tv_yandex) String stringYandex;

    private Unbinder unbinder;

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        try {
            listener = (OnLocationChangedListener) context;
        } catch (ClassCastException e) {
            listener = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        initSharedPreferences();

        if (!latitude.equals(getString(R.string.prefs_latitude_default))
                && !longitude.equals(getString(R.string.prefs_longitude_default))) {
            mapPresenter.getAddress(activity, latitude, longitude);
        }

        view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        View bottomSheet = view.findViewById(R.id.map_bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);

        bottomSheetLayout.setBackgroundResource(android.R.color.transparent);

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
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(activity);

        this.googleMap = googleMap;

        googleMap.setOnMarkerDragListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        CameraPosition cameraPosition;

        if (latitude.equals(getString(R.string.prefs_latitude_default))
                && longitude.equals(getString(R.string.prefs_longitude_default))) {

            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(makeDouble(latitude), makeDouble(longitude)))
                    .zoom(0)
                    .bearing(0)
                    .tilt(45)
                    .build();
        } else {

            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(makeDouble(latitude), makeDouble(longitude)))
                    .zoom(15)
                    .bearing(0)
                    .tilt(45)
                    .build();

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(makeDouble(latitude), makeDouble(longitude)))
                    .draggable(true).title(address));
        }

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (activity != null)
            activity = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (activity != null)
            activity = null;
    }

    private void initSharedPreferences() {

        prefsHelperLatitude = new PreferencesHelper(activity,
                getString(R.string.prefs_latitude_key));
        prefsHelperLongitude = new PreferencesHelper(activity,
                getString(R.string.prefs_longitude_key));

        latitude = prefsHelperLatitude.readFromPrefs(activity,
                getString(R.string.prefs_latitude_key));
        longitude = prefsHelperLongitude.readFromPrefs(activity,
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

    /**
     * Users can move the marker with onDrag methods
     */

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

        mapPresenter.getAddressAndSetTitle(activity, latitude, longitude, marker);
    }

    @OnClick(R.id.btn_bottom_sheet)
    void onBottomSheetClicked() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        prefsHelperLatitude.writeToPrefs(getString(R.string.prefs_latitude_key), latitude);
        prefsHelperLatitude.writeToPrefs(getString(R.string.prefs_longitude_key), longitude);
        
        if (listener != null) {
            listener.onLocationChanged(latitude, longitude, address);
        } else {
            Toast.makeText(activity, getString(R.string.bottom_sheet_listener_error),
                    Toast.LENGTH_SHORT).show();
        }
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

    private double makeDouble(String coordinate) {
        return Double.parseDouble(coordinate);
    }
}