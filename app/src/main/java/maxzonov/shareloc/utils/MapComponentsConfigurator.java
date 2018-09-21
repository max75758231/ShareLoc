package maxzonov.shareloc.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapComponentsConfigurator {

    private Double latitude;
    private Double longitude;
    private CameraPosition cameraPosition;

    public MapComponentsConfigurator(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CameraPosition buildCameraPosition() {

        if (areCoordsEmpty()) {
            buildDefaultCameraPosition();
        } else {
            buildDeterminedCameraPosition();
        }

        return cameraPosition;
    }

    public GoogleMap buildMap(GoogleMap.OnMarkerDragListener listener, GoogleMap googleMap, String markerAddress) {

        googleMap.setOnMarkerDragListener(listener);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        if (!areCoordsEmpty()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .draggable(true).title(markerAddress));
        }

        return googleMap;
    }

    private void buildDefaultCameraPosition() {
        cameraPosition = CameraPosition.builder()
                .target(new LatLng(latitude, longitude))
                .zoom(0)
                .bearing(0)
                .tilt(45)
                .build();
    }

    private void buildDeterminedCameraPosition() {
        cameraPosition = CameraPosition.builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
    }

    private boolean areCoordsEmpty() {
        return (latitude == 0.0d) && (longitude == 0.0d);
    }
}
