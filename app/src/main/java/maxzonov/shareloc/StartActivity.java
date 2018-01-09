package maxzonov.shareloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import maxzonov.shareloc.di.DaggerScreensComponent;
import maxzonov.shareloc.di.ScreensComponent;
import maxzonov.shareloc.ui.settings_screen.SettingsActivity;
import maxzonov.shareloc.ui.location_info_screen.LocationFragment;

public class StartActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragmentLocation, fragmentMap;

    private boolean isMapFragmentVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreensComponent daggerScreensComponent = DaggerScreensComponent.builder()
                .build();

        fragmentLocation = daggerScreensComponent.getLocationFragment();
        fragmentMap = daggerScreensComponent.getMapFragment();

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            isMapFragmentVisible = savedInstanceState.getBoolean("isMainVisible");
        } else {
            replaceFragment(new LocationFragment());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_map:

                if (isMapFragmentVisible) {
                    break;
                } else {
                    replaceFragment(fragmentMap);
                    isMapFragmentVisible = true;
                }
                return true;

            case R.id.bottom_navigation_location:

                if (!isMapFragmentVisible) {
                    break;
                } else {
                    replaceFragment(fragmentLocation);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isMapFragmentVisible", isMapFragmentVisible);
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}
