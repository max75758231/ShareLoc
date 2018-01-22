package maxzonov.shareloc.ui.map_screen;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.MarkerOptions;

import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;

public class MapFragment extends MvpAppCompatFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private View mView;
    private Resources res;

    private String latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        res = getResources();

        setupSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_map, container, false);
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

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude),
                Double.parseDouble(longitude))));

        CameraPosition cameraPosition =
                CameraPosition.builder()
                        .target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setupSharedPreferences() {

        PreferencesHelper prefsHelperLatitude =
                new PreferencesHelper(res.getString(R.string.prefs_latitude_key), getActivity());
        PreferencesHelper prefsHelperLongitude =
                new PreferencesHelper(res.getString(R.string.prefs_longitude_key), getActivity());

        latitude = prefsHelperLatitude.readFromPrefs(res.getString(R.string.prefs_latitude_key),
                getActivity());
        longitude = prefsHelperLongitude.readFromPrefs(res.getString(R.string.prefs_longitude_key),
                getActivity());
    }


}
