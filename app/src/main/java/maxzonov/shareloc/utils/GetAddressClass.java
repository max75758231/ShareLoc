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

//    private Context context;
    private WeakReference<Context> context;
    private OnGetAddressCompleted listener;

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

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);

            if (addresses == null || addresses.size() == 0) {
                if (resultMessage.isEmpty()) {
                    resultMessage = context.getString(R.string.no_address_found);
                }
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }
                resultMessage = TextUtils.join("\n", addressParts);
            }
        } catch (IOException ioException) {
            resultMessage = context.getString(R.string.no_address_found);
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgException) {
            resultMessage = context.getString(R.string.no_address_found);
            illegalArgException.printStackTrace();
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onGetAddressCompleted(result);
        super.onPostExecute(result);
    }
}
