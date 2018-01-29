package maxzonov.shareloc.di.module;

import android.support.v4.app.Fragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import maxzonov.shareloc.ui.location_info_screen.LocationFragment;

@Module
public class LocationFragmentModule {

    @Named("fragment_location")
    @Provides
    public Fragment locationFragment() {
        return new LocationFragment();
    }
}