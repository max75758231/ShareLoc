package maxzonov.shareloc.di.module;

import android.support.v4.app.Fragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import maxzonov.shareloc.ui.map_screen.MapFragment;

@Module
public class MapFragmentModule {

    @Named("fragment_map")
    @Provides
    public Fragment mapFragment() {
        return new MapFragment();
    }
}