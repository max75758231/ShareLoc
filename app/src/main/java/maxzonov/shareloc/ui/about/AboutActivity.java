package maxzonov.shareloc.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import maxzonov.shareloc.R;
import maxzonov.shareloc.ui.license.LicenseActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.about_license_layout)
    void onLicenseClick() {
        startActivity(new Intent(AboutActivity.this, LicenseActivity.class));
    }

    @OnClick(R.id.about_github_layout)
    void onGithubClick() {
        Uri uri = Uri.parse(getString(R.string.about_github_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}