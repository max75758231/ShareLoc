package maxzonov.shareloc.ui.location;

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
import maxzonov.shareloc.utils.AddressReceiver;
import maxzonov.shareloc.utils.OnAddressReceivedListener;

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> implements OnAddressReceivedListener {

    private String latitude, longitude;

    private Location lastLocation;

    private Context context;
    private Resources res;

    private static final int NOTIFICATION_FIND_LOCATION_ID = 1;
    private static final String NOTIFICATION_FIND_LOCATION_CHANNEL = "Channel_1";

    private FusedLocationProviderClient fusedLocationClient;

    private NotificationManager notificationManager;
    private PreferencesHelper prefsHelperLatitude, prefsHelperLongitude;

    /**
     * Location searching method
     */
    protected void getLocationClicked(Context context, FusedLocationProviderClient fusedLocation) {
        this.context = context;
        res = context.getResources();
        this.fusedLocationClient = fusedLocation;

        prefsHelperLatitude = new PreferencesHelper(context, res.getString(R.string.prefs_latitude_key));
        prefsHelperLongitude = new PreferencesHelper(context, res.getString(R.string.prefs_longitude_key));

        getLocation();
    }

    /**
     * Suppression caused by the fact that permission has been checked in LocationFragment
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lastLocation = location;
                latitude = String.valueOf(lastLocation.getLatitude());
                longitude = String.valueOf(lastLocation.getLongitude());

                prefsHelperLatitude.writeToPrefs(res.getString(R.string.prefs_latitude_key), latitude);
                prefsHelperLongitude.writeToPrefs(res.getString(R.string.prefs_longitude_key), longitude);

                new AddressReceiver(context, this).execute(lastLocation);
            } else {
                getViewState().onLocationResponseError();
            }
        });
    }

    /**
     * It's a response from the get address callback
     */
    @Override
    public void onAddressReceived(String address) {
        getViewState().showInfo(latitude, longitude, address);
        cancelNotification(notificationManager);
    }

    /**
     * Notification is displayed during the application is searching for satellites
     */
    void showNotification(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_FIND_LOCATION_CHANNEL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.notification_gps_searching))
                        .setOngoing(true); // immunity to clearing
        Notification notification = builder.build();

        notificationManager.notify(NOTIFICATION_FIND_LOCATION_ID,
                notification);
    }

    void cancelNotification(NotificationManager notificationManager) {
        notificationManager.cancel(NOTIFICATION_FIND_LOCATION_ID);
    }
}