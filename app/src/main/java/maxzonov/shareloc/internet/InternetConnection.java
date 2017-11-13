package maxzonov.shareloc.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Maxim Zonov on 05.11.2017.
 */

public class InternetConnection {
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return  (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
        } return false;
    }
}
