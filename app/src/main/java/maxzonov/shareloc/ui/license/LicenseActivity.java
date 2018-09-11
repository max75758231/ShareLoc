package maxzonov.shareloc.ui.license;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import maxzonov.shareloc.R;
import maxzonov.shareloc.utils.LocaleManager;

public class LicenseActivity extends AppCompatActivity {

    private static final String LICENSE_FILE_EN = "license_en.txt";
    private static final String LICENSE_FILE_RU = "license_ru.txt";

    @BindView(R.id.license_tv) TextView tvLicense;

    private StringBuilder licenseText = new StringBuilder();
    private BufferedReader reader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        chooseLicenseFileAndConvertToString();
        tvLicense.setText(licenseText);
    }

    private void chooseLicenseFileAndConvertToString() {
        try {
            chooseFileByLanguage();
            convertFileToString();
        } catch (IOException e) {
            tvLicense.setText(getString(R.string.license_error));
        } finally {
            closeReader();
        }
    }

    private void chooseFileByLanguage() throws IOException {
        String currentLanguage = LocaleManager.getPersistedData(this, getString(R.string.prefs_language_ru_value));
        if (currentLanguage.equals(getString(R.string.prefs_language_ru_value))) {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(LICENSE_FILE_RU)));
        } else {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(LICENSE_FILE_EN)));
        }
    }

    private void convertFileToString() throws IOException {
        String mLine;
        while ((mLine = reader.readLine()) != null) {
            licenseText.append(mLine);
            licenseText.append('\n');
        }
    }

    private void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}