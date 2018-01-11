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
import maxzonov.shareloc.utils.GetAddressClass;
import maxzonov.shareloc.utils.OnGetAddressCompleted;

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> implements OnGetAddressCompleted {

    private String latitude;
    private String longitude;

    private Location lastLocation;

    private Context context;
    private Resources res = context.getResources();

    private FusedLocationProviderClient fusedLocationClient;

    private NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    //Button click "Get Location" handling
    void getLocationClicked(Context context, FusedLocationProviderClient fusedLocationClient) {
        this.context = context;
        this.fusedLocationClient = fusedLocationClient;
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
                new GetAddressClass(context, this).execute(lastLocation);
            } else {
                String error = res.getString(R.string.location_error);
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
                        .setContentTitle(res.getString(R.string.app_name))
                        .setContentText(res.getString(R.string.gps_searching))
                        .setOngoing(true);
        Notification notification = builder.build();

        notificationManager.notify(res.getInteger(R.integer.NOTIFICATION_FIND_LOCATION_ID),
                notification);
    }

    private void cancelNotification() {
        notificationManager.cancel(res.getInteger(R.integer.NOTIFICATION_FIND_LOCATION_ID));
    }
}
