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

public class AddressReceiver extends AsyncTask<Location, Void, String> {

    /**
     * Weak reference is a solution to the context memory leaks
     */
    private WeakReference<Context> context;
    private final OnAddressReceivedListener listener;

    private List<Address> addresses = null;
    private Geocoder geocoder;
    private Location location;

    public AddressReceiver(Context context, OnAddressReceivedListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {

        return getAddressFromGeocoderAndConstructAddressString(locations);
    }

    /**
     * Sending the result to LocationPresenter
     * @param resultAddress Received response from geocoder
     */
    @Override
    protected void onPostExecute(String resultAddress) {
        listener.onAddressReceived(resultAddress);
        super.onPostExecute(resultAddress);
    }

    private String getAddressFromGeocoderAndConstructAddressString(Location... locations) {
        final Context context = this.context.get();

        geocoder = new Geocoder(context, Locale.getDefault());
        location = locations[0];

        return getAddressFromGeocoderByCoords();
    }

    private String getAddressFromGeocoderByCoords() {
        String address = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            address = showAddressErrorMessage();
        } catch (IllegalArgumentException illegalArgException) {
            address = showAddressErrorMessage();
        } finally {
            address = checkIfAddressIsEmptyAndConstructString(address);
        }

        return address;
    }

    private String checkIfAddressIsEmptyAndConstructString(String addressLine) {
        if (isAddressEmpty(addressLine)) {
            addressLine = showAddressErrorMessage();
        } else {
            addressLine = constructAddressString();
        }
        return addressLine;
    }

    private Boolean isAddressEmpty(String addressLine) {
        return (addresses == null || addresses.size() == 0) && !addressLine.isEmpty();
    }

    private String constructAddressString() {
        Address address = addresses.get(0);
        ArrayList<String> addressParts = new ArrayList<>();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressParts.add(address.getAddressLine(i));
        }
        return TextUtils.join("\n", addressParts);
    }

    private String showAddressErrorMessage() {
        return context.get().getString(R.string.all_address_error);
    }
}