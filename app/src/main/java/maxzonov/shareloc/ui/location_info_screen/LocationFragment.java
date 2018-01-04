package maxzonov.shareloc.ui.location_info_screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;

public class LocationFragment extends MvpAppCompatFragment implements LocationView {

    @InjectPresenter LocationPresenter locationPresenter;

    private static final String PREFERENCES_MESSAGE_TAG = "message";
    private static final String TAG = "myLog";

    private PreferencesHelper preferencesHelper;

    @BindView(R.id.tv_location_latitude_result) TextView textViewLatitude;
    @BindView(R.id.tv_location_longitude_result) TextView textViewLongitude;
    @BindView(R.id.tv_location_address_result) TextView textViewAddress;
    @BindView(R.id.tv_location_message_result) TextView textViewMessage;
    @BindView(R.id.tv_location_google_link) TextView textViewGoogleLink;
    @BindView(R.id.tv_location_yandex_link) TextView textViewYandexLink;

    @BindString(R.string.tv_location_address) String stringAddress;
    @BindString(R.string.tv_location_google) String stringGoogle;
    @BindString(R.string.tv_location_yandex) String stringYandex;
    @BindString(R.string.share_title) String shareTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_info, containter, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: ");

        preferencesHelper = new PreferencesHelper(PREFERENCES_MESSAGE_TAG, getActivity());

        textViewMessage.setText(preferencesHelper
                .readFromPrefs(PREFERENCES_MESSAGE_TAG, getActivity()));
        return view;
    }

    @OnClick(R.id.btn_location_getLocation)
    void onGetLocationButtonClick() {
        locationPresenter.getLocationClicked(getActivity());
    }

    @OnClick(R.id.iv_location_share)
    void onShareButtonClick() {
        String sendInfo = stringAddress + " " + textViewAddress.getText() + "\n"
                + textViewMessage.getText() + "\n"
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

        String googleLink = getResources()
                .getString(R.string.google_maps_link, latitude, longitude);
        String yandexMapsLink = getResources()
                .getString(R.string.yandex_maps_link, latitude, longitude);

        textViewGoogleLink.setText(googleLink);
        textViewYandexLink.setText(yandexMapsLink);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");
    }
}
