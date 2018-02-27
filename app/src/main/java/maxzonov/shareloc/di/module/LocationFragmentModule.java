package maxzonov.shareloc.di.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

import maxzonov.shareloc.ui.location.LocationFragment;

@Module
public class LocationFragmentModule {

    @Named("fragment_location")
    @Provides
    public Fragment locationFragment() {
        return new LocationFragment();
    }
}