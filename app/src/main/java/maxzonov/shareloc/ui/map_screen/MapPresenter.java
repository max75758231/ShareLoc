package maxzonov.shareloc.ui.map_screen;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {

    private String address;

    private Geocoder geocoder;
    private List<Address> addresses;

    void getAddress(Context context, String latitude, String longitude) {
        geocoder = new Geocoder(context, Locale.getDefault());

        requestAddressFromNetwork(latitude, longitude);

        getViewState().showAddressInMarker(address);
    }

    void getAddressAndSetTitle(String latitude, String longitude, Marker marker) {
        requestAddressFromNetwork(latitude, longitude);
        getViewState().showAndRefreshAddressInMarker(address, marker);
    }

    private void requestAddressFromNetwork(String latitude, String longitude){
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);
            buildAddressString();
        } catch (IOException e) {
            address = "";
        }
    }

    private void buildAddressString() {
        address = addresses.get(0).getLocality() + ", " +
                addresses.get(0).getThoroughfare() + ", " +
                addresses.get(0).getSubThoroughfare();
    }
}
