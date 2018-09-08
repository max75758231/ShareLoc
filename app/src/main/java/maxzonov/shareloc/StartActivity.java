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
import maxzonov.shareloc.utils.MyLocation;

public class StartActivity extends AppCompatActivity implements OnLocationChangedListener {

    @Inject @Named("fragment_location") Fragment fragmentLocation;
    @Inject @Named("fragment_map") Fragment fragmentMap;

    @BindView(R.id.bottom_navigation) BottomNavigationView navigationView;

    private AppNavigator navigator;

    private Bundle args;

    /**
     * isMapFragmentVisible - boolean variable to handle lifecycle of activity
     * isBackButtonClicked - boolean variable to handle double-clicked back button exit
     */
    private boolean isMapFragmentVisible = false;
    private boolean isBackButtonClicked = false;

    /**
     * attachBaseContext method applies a previously persisted language
     */
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleManager.onAttach(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVariables();
        injectDaggerDependencies();

        if (savedInstanceState != null) {
            isMapFragmentVisible = savedInstanceState.getBoolean("isMainVisible");
        } else {
            navigator.navigateToFragment(fragmentLocation);
        }

        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void initVariables() {
        navigator = new AppNavigator(getSupportFragmentManager());
        args = new Bundle();
    }

    private void injectDaggerDependencies() {
        ScreensComponent screensComponent = App.getAppComponent(this)
                .screensComponent(new NavigatorModule(navigator));
        screensComponent.inject(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
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

        int backPressedDelay = 2000;

        if (isBackButtonClicked) {
            finish();
            return;
        }

        isBackButtonClicked = true;

        Toast.makeText(this, getString(R.string.all_exit_button), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> isBackButtonClicked = false, backPressedDelay);
    }

    /**
     * Method that handle maps mark movement
     * @param latitude - new latitude coordinate
     * @param longitude new longitude coordinate
     * @param address - new address by these coordinates
     */
    @Override
    public void onLocationChanged(final String latitude, final String longitude, final String address) {

        MyLocation myLocation = new MyLocation(latitude, longitude, address);
        saveNewLocationAndNavigateToLocationScreen(myLocation);
    }

    private void saveNewLocationAndNavigateToLocationScreen(MyLocation location) {
        saveNewLocation(location);
        navigateToLocationScreen();
    }

    private void saveNewLocation(MyLocation location) {
        args.putString(getString(R.string.all_latitude_intent_key), location.getLatitude());
        args.putString(getString(R.string.all_longitude_intent_key), location.getLongitude());
        args.putString(getString(R.string.all_address_intent_key), location.getAddress());
    }

    private void navigateToLocationScreen() {
        fragmentLocation.setArguments(args);
        navigator.navigateToFragment(fragmentLocation);
        navigationView.setSelectedItemId(R.id.bottom_navigation_location);
    }
}