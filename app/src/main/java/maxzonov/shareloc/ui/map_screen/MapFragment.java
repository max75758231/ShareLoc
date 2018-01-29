package maxzonov.shareloc.ui.map_screen;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;

public class MapFragment extends MvpAppCompatFragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap googleMap;

    private View mView;
    private Resources res;

    private String latitude, longitude;

    private BottomSheetBehavior sheetBehavior;

    @BindString(R.string.all_share_title) String shareTitle;
    @BindString(R.string.location_tv_google) String stringGoogle;
    @BindString(R.string.location_tv_yandex) String stringYandex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        res = getResources();

        initSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, mView);

        View bottomSheet = mView.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = mView.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        this.googleMap = googleMap;

        googleMap.setOnMarkerDragListener(this);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                .draggable(true));

        CameraPosition cameraPosition =
                CameraPosition.builder()
                        .target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void initSharedPreferences() {

        PreferencesHelper prefsHelperLatitude =
                new PreferencesHelper(res.getString(R.string.prefs_latitude_key), getActivity());
        PreferencesHelper prefsHelperLongitude =
                new PreferencesHelper(res.getString(R.string.prefs_longitude_key), getActivity());

        latitude = prefsHelperLatitude.readFromPrefs(res.getString(R.string.prefs_latitude_key),
                getActivity());
        longitude = prefsHelperLongitude.readFromPrefs(res.getString(R.string.prefs_longitude_key),
                getActivity());
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
        Log.d("myLog", "onMarkerDragStart: " + String.valueOf(marker.getPosition()));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("myLog", "onMarkerDrag: " + String.valueOf(marker.getPosition()));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        latitude = String.valueOf(marker.getPosition().latitude);
        longitude = String.valueOf(marker.getPosition().longitude);
    }

    @OnClick(R.id.btn_bottom_sheet)
    void onBottomSheetClicked() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.btn_bottom_sheet_share)
    void onBottomSheetShareClicked() {
        String sendInfo = getString(R.string.bottom_sheet_i_am_here_send) + "\n" +
                stringGoogle + " " + getString(R.string.all_google_maps_link, latitude, longitude) + "\n"
                + stringYandex + " " + getString(R.string.all_yandex_maps_link, latitude, longitude);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendInfo);
        startActivity(Intent.createChooser(intent, shareTitle));
    }
}