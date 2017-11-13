package maxzonov.shareloc.ui.location_info_screen;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Maxim Zonov on 02.11.2017.
 */

public class LocationFragment extends MvpAppCompatFragment implements LocationView {

    @InjectPresenter LocationPresenter locationPresenter;
    PreferencesHelper preferencesHelperMessage;
    PreferencesHelper preferencesHelperLatitude;
    PreferencesHelper preferencesHelperLongtitude;

    @BindView(R.id.tv_location_latitude_result) TextView textViewLatitude;
    @BindView(R.id.tv_location_longtitude_result) TextView textViewLongtitude;
    @BindView(R.id.tv_location_address_result) TextView textViewAddress;
    @BindView(R.id.tv_location_message_result) TextView textViewMessage;
    @BindView(R.id.tv_location_google_link) TextView textViewGoogleLink;
    @BindView(R.id.tv_location_yandex_link) TextView textViewYandexLink;

    @BindString(R.string.tv_location_address) String stringAddress;
    @BindString(R.string.tv_location_google) String stringGoogle;
    @BindString(R.string.tv_location_yandex) String stringYandex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_info, containter, false);
        ButterKnife.bind(this, view);

        preferencesHelperMessage = new PreferencesHelper("message", getActivity());
        preferencesHelperLatitude = new PreferencesHelper("latitude", getActivity());
        preferencesHelperLongtitude = new PreferencesHelper("longtitude", getActivity());

        textViewMessage.setText(preferencesHelperMessage.readFromPrefs("message", getActivity()));
        return view;
    }

    @OnClick(R.id.btn_location_getLocation)
    public void onGetLocationClick() {
        locationPresenter.getLocation(getActivity());
    }

    @OnClick(R.id.iv_location_share)
    public void onShareClick() {
        String sendInfo = stringAddress + " " + textViewAddress.getText() + "\n"
                + textViewMessage.getText() + "\n"
                + stringGoogle + " " + textViewGoogleLink.getText() + "\n"
                + stringYandex + " " + textViewYandexLink.getText();


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendInfo);
        startActivity(Intent.createChooser(intent, "Shave via"));
    }

    @Override
    public void showInfo(String latitude, String longtitude) {
        textViewLatitude.setText(latitude);
        textViewLongtitude.setText(longtitude);
    }

    @Override
    public void showLinks(String latitude, String longtitude) {
        textViewGoogleLink.setText("maps.google.com/maps?q=loc:" + latitude + "," + longtitude);
        textViewYandexLink.setText("maps.yandex.ru/?text=" + latitude + "," + longtitude);
    }

    @Override
    public void showAddress(String address) {
        textViewAddress.setText(address);
    }
}
