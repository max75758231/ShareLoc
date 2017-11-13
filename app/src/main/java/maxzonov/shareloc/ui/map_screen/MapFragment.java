package maxzonov.shareloc.ui.map_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
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
import maxzonov.shareloc.ui.location_info_screen.LocationModel;

/**
 * Created by Maxim Zonov on 02.11.2017.
 */

public class MapFragment extends MvpAppCompatFragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;

    String latitude;
    String longtitude;

    public static final String LATITUDE = "latitude";
    public static final String LONGTITUDE = "longtitude";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        PreferencesHelper preferencesHelperLatitude = new PreferencesHelper(LATITUDE, getActivity());
        PreferencesHelper preferencesHelperLongtitude = new PreferencesHelper(LONGTITUDE, getActivity());

        latitude = preferencesHelperLatitude.readFromPrefs(LATITUDE, getActivity());
        longtitude = preferencesHelperLongtitude.readFromPrefs(LONGTITUDE, getActivity());
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

        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude))).title("VyatSU"));

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude)))
                .zoom(15).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
