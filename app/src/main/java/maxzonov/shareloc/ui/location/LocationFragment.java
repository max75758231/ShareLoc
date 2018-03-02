package maxzonov.shareloc.ui.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.tbruyelle.rxpermissions2.RxPermissions;

import maxzonov.shareloc.R;
import maxzonov.shareloc.StartActivity;
import maxzonov.shareloc.utils.LocaleManager;

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

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
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
            String googleLink = getString(R.string.all_google_maps_link, latitude, longitude);
            String yandexMapsLink = getString(R.string.all_yandex_maps_link, latitude, longitude);

            textViewLatitude.setText(latitude);
            textViewLongitude.setText(longitude);
            textViewAddress.setText(address);
            textViewGoogleLink.setText(googleLink);
            textViewYandexLink.setText(yandexMapsLink);
        }
    }

    @OnClick(R.id.btn_location_getLocation)
    void onGetLocationButtonClick() {
        getLocation();
    }

    @SuppressLint("CheckResult")
    private void getLocation() {

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                   if (granted) {
                       locationPresenter.getLocationClicked(activity, fusedLocationClient);
                   } else {
                       Toast.makeText(view.getContext(), getString(R.string.location_permission_denied),
                               Toast.LENGTH_SHORT).show();
                   }
                });
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

        if (!latitude.equals(getString(R.string.location_geolocation_error))
                && !longitude.equals(getString(R.string.location_geolocation_error))) {

            googleLink = getString(R.string.all_google_maps_link, latitude, longitude);
            yandexMapsLink = getString(R.string.all_yandex_maps_link, latitude, longitude);
        }

        textViewGoogleLink.setText(googleLink);
        textViewYandexLink.setText(yandexMapsLink);
    }

    @Override
    public void onLocationResponseError() {
        String errorMessage = getString(R.string.location_no_geo_received_message);
        textViewAddress.setText(errorMessage);
        textViewLatitude.setText(errorMessage);
        textViewLongitude.setText(errorMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(activity)
                .unregisterOnSharedPreferenceChangeListener(this);

        if (activity != null)
            activity = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (activity != null)
            activity = null;
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        textViewMessage.setText(preferences.getString(getString(R.string.prefs_message_key),
                getString(R.string.prefs_message_default)));

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String langKey = getString(R.string.prefs_language_key);
        String langDefault = getString(R.string.prefs_language_ru_value);

        if (key.equals(getString(R.string.prefs_message_key))) {
            textViewMessage.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_message_default)));
        } else if (key.equals(getString(R.string.prefs_latitude_key))) {
            textViewLatitude.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_latitude_default)));
        } else if (key.equals(getString(R.string.prefs_longitude_key))) {
            textViewLongitude.setText(sharedPreferences.getString(key,
                    getString(R.string.prefs_longitude_default)));
        } else if (key.equals(langKey)) {
            LocaleManager
                    .setLocale(activity, sharedPreferences.getString(langKey, langDefault));

            Intent i = new Intent(activity, StartActivity.class);
            startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}