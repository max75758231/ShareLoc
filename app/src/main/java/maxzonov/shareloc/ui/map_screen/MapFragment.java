package maxzonov.shareloc.ui.map_screen;

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
    private MapView mapView;
    private View mView;

    private String latitude;
    private String longitude;

    public static final String PREFERENCES_LATITUDE_TAG = "latitude";
    public static final String PREFERENCES_LONGITUDE_TAG = "longitude";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        PreferencesHelper preferencesHelperLatitude =
                new PreferencesHelper(PREFERENCES_LATITUDE_TAG, getActivity());
        PreferencesHelper preferencesHelperLongitude =
                new PreferencesHelper(PREFERENCES_LONGITUDE_TAG, getActivity());

        latitude = preferencesHelperLatitude.readFromPrefs(PREFERENCES_LATITUDE_TAG, getActivity());
        longitude = preferencesHelperLongitude.readFromPrefs(PREFERENCES_LONGITUDE_TAG, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_map, containter, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map_view);
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
                Double.parseDouble(longitude))).title("VyatSU"));

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                .zoom(15).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
