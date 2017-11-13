package maxzonov.shareloc.ui.settings_screen;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxzonov.shareloc.R;
import maxzonov.shareloc.preferences.PreferencesHelper;

/**
 * Created by Maxim Zonov on 05.11.2017.
 */

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView,
        CompoundButton.OnCheckedChangeListener {

    @InjectPresenter SettingsPresenter settingsPresenter;
    PreferencesHelper preferencesHelper;

    LinearLayout linearLayout;

    @BindView(R.id.et_settings_message) EditText editTextMessage;
    @BindView(R.id.switch_internet) SwitchCompat switchInternet;
    @BindView(R.id.switch_wifi) SwitchCompat switchWifi;
    @BindView(R.id.switch_gps) SwitchCompat switchGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferencesHelper = new PreferencesHelper("message", this);

        linearLayout = findViewById(R.id.layout_settings);
        switchInternet.setOnCheckedChangeListener(this);
        switchWifi.setOnCheckedChangeListener(this);
        switchGps.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.btn_settings_ok)
    public void btnClick() {
        preferencesHelper.writeToPrefs("message", editTextMessage.getText().toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_internet:
                if (isChecked) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    startActivity(intent);
                } else {

                }
                break;
            case R.id.switch_wifi:
                if (isChecked) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                } else {

                }
                break;
            case R.id.switch_gps:
                if (isChecked) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {

                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
