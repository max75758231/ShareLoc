package maxzonov.shareloc.ui.location_info_screen;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;

import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;
import maxzonov.shareloc.utils.GetAddressClass;
import maxzonov.shareloc.utils.OnGetAddressCompleted;

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> implements OnGetAddressCompleted {

    private String latitude, longitude;

    private Location lastLocation;

    private Context context;
    private Resources res;
    private static final int NOTIFICATION_FIND_LOCATION_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;

    private NotificationManager notificationManager;
    private PreferencesHelper prefsHelperLatitude, prefsHelperLongitude;

    void getLocationClicked(Context context, FusedLocationProviderClient fusedLocationClient) {
        this.context = context;
        res = context.getResources();
        this.fusedLocationClient = fusedLocationClient;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        prefsHelperLatitude = new PreferencesHelper(res.getString(R.string.prefs_latitude_key), context);
        prefsHelperLongitude = new PreferencesHelper(res.getString(R.string.prefs_longitude_key), context);

        showNotification();
        getLocation();
    }

    //Suppression caused by the fact that permission has been checked in LocationFragment
    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lastLocation = location;
                latitude = String.valueOf(lastLocation.getLatitude());
                longitude = String.valueOf(lastLocation.getLongitude());

                prefsHelperLatitude.writeToPrefs(res.getString(R.string.prefs_latitude_key), latitude);
                prefsHelperLongitude.writeToPrefs(res.getString(R.string.prefs_longitude_key), longitude);

                new GetAddressClass(context, this).execute(lastLocation);
            } else {
                String error = context.getString(R.string.location_error);
                getViewState().showInfo(error, error, error);
            }
        });
    }

    @Override
    public void onGetAddressCompleted(String address) {
        getViewState().showInfo(latitude, longitude, address);
        cancelNotification();
    }

    private void showNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.gps_searching))
                        .setOngoing(true);
        Notification notification = builder.build();

        notificationManager.notify(NOTIFICATION_FIND_LOCATION_ID,
                notification);
    }

    private void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_FIND_LOCATION_ID);
    }
}