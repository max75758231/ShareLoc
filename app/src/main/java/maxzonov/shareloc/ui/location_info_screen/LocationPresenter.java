package maxzonov.shareloc.ui.location_info_screen;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import maxzonov.shareloc.R;

@InjectViewState
public class LocationPresenter extends MvpPresenter<LocationView> {

    private String latitude = "58.6376";
    private String longitude = "49.617219";

    private List<Address> addresses;

    private Geocoder geocoder;

    private Context context;


    void getLocationClicked(Context context) {
        this.context = context;
        getLocation();
    }

    private void getLocation() {
        showNotification();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        String address = getAddress();
        getViewState().showInfo(latitude, longitude, address);
    }

    private String getAddress() {
        geocoder = new Geocoder(context, Locale.getDefault());
        String fullAddress = "default";
        try {

            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);

            fullAddress = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d("myLog", String.valueOf(e));
        }
        return fullAddress;
    }

    private void showNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ShareLoc")
                        .setContentText("Поиск спутников GPS")
                        .setOngoing(true);
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
