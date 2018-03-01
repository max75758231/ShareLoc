package maxzonov.shareloc.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import maxzonov.shareloc.R;

public class GetAddressClass extends AsyncTask<Location, Void, String> {

    private WeakReference<Context> context;
    private final OnGetAddressCompleted listener;

    public GetAddressClass(Context context, OnGetAddressCompleted listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        final Context context = this.context.get();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Location location = locations[0];

        List<Address> addresses = null;
        String resultMessage = "";
        String onAddressErrorMessage = context.getString(R.string.all_address_error);

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            resultMessage = onAddressErrorMessage;
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgException) {
            resultMessage = onAddressErrorMessage;
            illegalArgException.printStackTrace();
        } finally {
            if (addresses == null || addresses.size() == 0) {
                if (resultMessage.isEmpty()) {
                    resultMessage = onAddressErrorMessage;
                }
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }
                resultMessage = TextUtils.join("\n", addressParts);
            }
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onGetAddressCompleted(result);
        super.onPostExecute(result);
    }
}