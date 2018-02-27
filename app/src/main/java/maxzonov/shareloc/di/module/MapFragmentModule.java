package maxzonov.shareloc.di.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

import maxzonov.shareloc.ui.map.MapFragment;

@Module
public class MapFragmentModule {

    @Named("fragment_map")
    @Provides
    public Fragment mapFragment() {
        return new MapFragment();
    }
}