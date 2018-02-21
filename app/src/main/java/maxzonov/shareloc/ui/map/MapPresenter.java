package maxzonov.shareloc.ui.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import maxzonov.shareloc.R;
import maxzonov.shareloc.utils.exceptions.AddressNotFoundException;

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {

    private String address;

    private Geocoder geocoder;
    private List<Address> addresses;

    void getAddress(Context context, String latitude, String longitude) {
        geocoder = new Geocoder(context, Locale.getDefault());

        requestAddressFromNetwork(latitude, longitude, context);

        getViewState().showAddressInMarker(address);
    }

    void getAddressAndSetTitle(String latitude, String longitude, Marker marker, Context context) {
        requestAddressFromNetwork(latitude, longitude, context);
        getViewState().showAndRefreshAddressInMarker(address, marker);
    }

    private void requestAddressFromNetwork(String latitude, String longitude, Context context) {
        try {
            if (geocoder == null) {
                geocoder = new Geocoder(context, Locale.getDefault());
            }
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);
            buildAddressString();
        } catch (IOException ioException) {
            address = context.getString(R.string.all_address_error);
        } catch (AddressNotFoundException addrNotFoundException) {
            address = context.getString(R.string.all_address_error);
        }
    }

    private void buildAddressString() throws AddressNotFoundException {

        if (addresses.size() == 0) {
            throw new AddressNotFoundException("address not found");
        }

        String adminArea = addresses.get(0).getAdminArea();
        String locality = addresses.get(0).getLocality();
        String thoroughfare = addresses.get(0).getThoroughfare();
        String subThoroughfare = addresses.get(0).getSubThoroughfare();
        String feature = addresses.get(0).getFeatureName();

        if (thoroughfare != null && subThoroughfare != null) {
            address = adminArea + ", " + locality + ", " + thoroughfare + ", " + subThoroughfare;
        } else if (locality != null) {
            address = adminArea + ", " + locality + ", " + feature;
        } else {
            address = adminArea;
        }
    }
}
