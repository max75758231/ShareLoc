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

/**
 * Created by Maxim Zonov on 02.11.2017.
 */

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> {

    private static final String LATITUDE = "latitude";
    private static final String LONGTITUDE = "longtitude";

    private String latitude = "58.6376";
    private String longtitude = "49.617219";

    Geocoder geocoder;
    List<Address> addresses;

    public void getLocation(Context context) {
        PreferencesHelper preferencesHelperLatitude = new PreferencesHelper("latitude", context);
        PreferencesHelper preferencesHelperLongtitude = new PreferencesHelper("longtitude", context);

        preferencesHelperLatitude.writeToPrefs(LATITUDE, latitude);
        preferencesHelperLongtitude.writeToPrefs(LONGTITUDE, longtitude);

        getViewState().showInfo(latitude, longtitude);
        getViewState().showLinks(latitude, longtitude);

        getViewState().showAddress(getAddress(context));
    }

    private String getAddress(Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
        String fullAddr = "default";
        try {

            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longtitude), 1);

            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postcode = addresses.get(0).getPostalCode();

            fullAddr = address + area + city + country + postcode;

            Log.d("myLog", fullAddr);
        } catch (IOException e) {
//            e.printStackTrace();
            Log.d("myLog", String.valueOf(e));
        }

        return fullAddr;
    }
}
