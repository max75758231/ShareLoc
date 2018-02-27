package maxzonov.shareloc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import javax.inject.Inject;
import javax.inject.Named;

import maxzonov.shareloc.di.component.ScreensComponent;
import maxzonov.shareloc.di.module.NavigatorModule;
import maxzonov.shareloc.navigation.AppNavigator;
import maxzonov.shareloc.ui.map.OnLocationChangedListener;
import maxzonov.shareloc.ui.settings.SettingsActivity;
import maxzonov.shareloc.utils.LocaleManager;

public class StartActivity extends AppCompatActivity implements OnLocationChangedListener {

    @Inject @Named("fragment_location") Fragment fragmentLocation;
    @Inject @Named("fragment_map") Fragment fragmentMap;

    @BindView(R.id.bottom_navigation) BottomNavigationView navigationView;

    private AppNavigator navigator;

    private boolean isMapFragmentVisible = false;
    private boolean isBackButtonClicked = false;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleManager.setLocale(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigator = new AppNavigator(getSupportFragmentManager());

        ScreensComponent screensComponent = App.getAppComponent(this)
                .screensComponent(new NavigatorModule(navigator));
        screensComponent.inject(this);

        if (savedInstanceState != null) {
            isMapFragmentVisible = savedInstanceState.getBoolean("isMainVisible");
        } else {
            navigator.navigateToFragment(fragmentLocation);
        }

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_map:

                if (isMapFragmentVisible) {
                    break;
                } else {
                    navigator.navigateToFragment(fragmentMap);
                    isMapFragmentVisible = true;
                }
                return true;

            case R.id.bottom_navigation_location:

                if (!isMapFragmentVisible) {
                    break;
                } else {
                    navigator.navigateToFragment(fragmentLocation);
                    isMapFragmentVisible = false;
                }
                return true;
        }
        return false;
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isMainVisible", isMapFragmentVisible);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(StartActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        int BACK_PRESSED_DELAY = 2000;

        if (isBackButtonClicked) {
            finish();
            return;
        }

        isBackButtonClicked = true;

        Toast.makeText(this, getString(R.string.all_exit_button), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> isBackButtonClicked = false, BACK_PRESSED_DELAY);
    }

    @Override
    public void onLocationChanged(final String latitude, final String longitude, final String address) {
        Bundle args = new Bundle();

        args.putString(getString(R.string.all_latitude_intent_key), latitude);
        args.putString(getString(R.string.all_longitude_intent_key), longitude);
        args.putString(getString(R.string.all_address_intent_key), address);

        fragmentLocation.setArguments(args);
        navigator.navigateToFragment(fragmentLocation);
        navigationView.setSelectedItemId(R.id.bottom_navigation_location);
    }
}