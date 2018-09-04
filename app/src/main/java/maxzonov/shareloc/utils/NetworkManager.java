package maxzonov.shareloc.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import maxzonov.shareloc.R;

public class NetworkManager {

    private static final String NETWORK_2G = "2G";
    private static final String NETWORK_3G = "3G";
    private static final String NETWORK_4G = "4G";

    public String getNetworkInfo(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        } else {
            return activity.getString(R.string.location_internet_error);
        }
        boolean isConnected = info != null && info.isConnectedOrConnecting();
        if (isConnected) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return activity.getString(R.string.location_internet_wifi);
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getConnectivityType(info.getSubtype(), info.getSubtypeName());
            }
        } else {
            return activity.getString(R.string.location_internet_error);
        }
        return "";
    }

    private String getConnectivityType(int type, String typeName) {
        String networkType = "";
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case 16: // GSM
                networkType = generateNetworkTypeString(NETWORK_2G, typeName);
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                networkType = generateNetworkTypeString(NETWORK_3G, typeName);
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case TelephonyManager.NETWORK_TYPE_IWLAN:
            case 19: // LTE Carrier Aggregation
                networkType = generateNetworkTypeString(NETWORK_4G, typeName);
                break;
        }
        return networkType;
    }

    private String generateNetworkTypeString(String type, String typeName) {
        return type + " (" + typeName + ")";
    }
}
