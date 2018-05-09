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

    @BindView(R.id.license_tv)
    TextView tvLicense;

    private StringBuilder licenseText = new StringBuilder();
    private BufferedReader reader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        try {
            chooseFileByLanguage();

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                licenseText.append(mLine);
                licenseText.append('\n');
            }
        } catch (IOException e) {
            tvLicense.setText(getString(R.string.license_error));
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tvLicense.setText(licenseText);
        }
    }

    private void chooseFileByLanguage() throws IOException {
        String currentLanguage = LocaleManager.getPersistedData(this, "ru");
        if (currentLanguage.equals(getString(R.string.prefs_language_ru_value))) {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("license_ru.txt")));
        } else {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("license_en.txt")));
        }
    }
}