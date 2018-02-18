package maxzonov.shareloc.ui.location;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxzonov.shareloc.R;

public class LocationFragment extends MvpAppCompatFragment implements LocationView,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @InjectPresenter LocationPresenter locationPresenter;

    private static final int REQUEST_LOCATION_PERMISSION_ID = 1;

    @BindView(R.id.tv_location_latitude_result) TextView textViewLatitude;
    @BindView(R.id.tv_location_longitude_result) TextView textViewLongitude;
    @BindView(R.id.tv_location_address_result) TextView textViewAddress;
    @BindView(R.id.tv_location_message_result) TextView textViewMessage;
    @BindView(R.id.tv_location_google_link) TextView textViewGoogleLink;
    @BindView(R.id.tv_location_yandex_link) TextView textViewYandexLink;

    @BindString(R.string.location_tv_address) String stringAddress;
    @BindString(R.string.location_tv_google) String stringGoogle;
    @BindString(R.string.location_tv_yandex) String stringYandex;
    @BindString(R.string.all_share_title) String shareTitle;

    private FusedLocationProviderClient fusedLocationClient;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_info, container, false);
        ButterKnife.bind(this, view);

        setupSharedPreferences();
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();

        if (args != null) {
            String latitude = args.getString(getString(R.string.all_latitude_intent_key));
            String longitude = args.getString(getString(R.string.all_longitude_intent_key));
            String address = args.getString(getString(R.string.all_address_intent_key));

            textViewLatitude.setText(latitude);
            textViewLongitude.setText(longitude);
            textViewAddress.setText(address);
            String googleLink = getString(R.string.all_google_maps_link, latitude, longitude);
            String yandexMapsLink = getString(R.string.all_yandex_maps_link, latitude, longitude);
            textViewGoogleLink.setText(googleLink);
            textViewGoogleLink.setText(yandexMapsLink);
        }
    }

    @OnClick(R.id.btn_location_getLocation)
    void onGetLocationButtonClick() {
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_ID);
        } else {
            locationPresenter.getLocationClicked(getActivity(), fusedLocationClient);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_ID:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(view.getContext(), getString(R.string.location_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @OnClick(R.id.iv_location_share)
    void onShareButtonClick() {
        String sendInfo = textViewMessage.getText() + "\n"
                + stringAddress + "\n"
                + textViewAddress.getText() + "\n"
                + stringGoogle + " " + textViewGoogleLink.getText() + "\n"
                + stringYandex + " " + textViewYandexLink.getText();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendInfo);
        startActivity(Intent.createChooser(intent, shareTitle));
    }

    @Override
    public void showInfo(String latitude, String longitude, String address) {
        textViewAddress.setText(address);
        textViewLatitude.setText(latitude);
        textViewLongitude.setText(longitude);

        String googleLink = "";
        String yandexMapsLink = "";

        if (!latitude.equals(getString(R.string.location_geolocation_error)) &&
                !longitude.equals(getString(R.string.location_geolocation_error))) {

            googleLink = getString(R.string.all_google_maps_link, latitude, longitude);
            yandexMapsLink = getString(R.string.all_yandex_maps_link, latitude, longitude);
        }

        textViewGoogleLink.setText(googleLink);
        textViewYandexLink.setText(yandexMapsLink);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        textViewMessage.setText(preferences.getString(getString(R.string.prefs_message_key),
                getString(R.string.prefs_message_default)));

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefs_message_key))) {
            textViewMessage.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_message_default)));
        } else if (key.equals(getString(R.string.prefs_latitude_key))) {
            textViewLatitude.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_latitude_default)));
        } else if (key.equals(getString(R.string.prefs_longitude_key))) {
            textViewLongitude.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_longitude_default)));
        }
    }
}