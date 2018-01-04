package maxzonov.shareloc.ui.location_info_screen;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import maxzonov.shareloc.preferences.PreferencesHelper;

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> {

    private Context context;

    private String latitude = "58.6376";
    private String longitude = "49.617219";

    private List<Address> addresses;

    private Geocoder geocoder;


    public void getLocationClicked(Context context) {
        getLocation(context);
    }

    public void locationIsReady(String latitude, String longitude, String address) {
        getViewState().showInfo(latitude, longitude, address);
    }

    private void getLocation(Context context) {

        String address = getAddress(context);
        getViewState().showInfo(latitude, longitude, address);
    }

    private String getAddress(Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
        String fullAddr = "default";
        try {

            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);

            fullAddr = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d("myLog", String.valueOf(e));
        }
        return fullAddr;
    }
}
