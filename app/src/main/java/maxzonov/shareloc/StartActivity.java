package maxzonov.shareloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;
import javax.inject.Named;


import butterknife.BindView;
import butterknife.ButterKnife;
import maxzonov.shareloc.di.ScreensComponent;
import maxzonov.shareloc.di.module.NavigatorModule;
import maxzonov.shareloc.navigation.AppNavigator;
import maxzonov.shareloc.ui.settings_screen.SettingsActivity;

public class StartActivity extends AppCompatActivity {

    @Inject @Named("fragment_location") Fragment fragmentLocation;
    @Inject @Named("fragment_map") Fragment fragmentMap;

    @BindView(R.id.bottom_navigation) BottomNavigationView navigationView;

    private AppNavigator navigator;
    private ScreensComponent screensComponent;

    private boolean isMapFragmentVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigator = new AppNavigator(getSupportFragmentManager());

        screensComponent = App.getAppComponent(getApplicationContext())
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (navigator.hasPreviousView()) {
            navigator.navigateToPreviousView();
        } else {
            super.onBackPressed();
        }
    }
}