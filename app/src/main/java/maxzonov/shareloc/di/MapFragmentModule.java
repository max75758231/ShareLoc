package maxzonov.shareloc.di;

import dagger.Module;
import dagger.Provides;
import maxzonov.shareloc.ui.map_screen.MapFragment;

@Module
public class MapFragmentModule {

    @Provides
    @AppScope
    public MapFragment mapFragment() {
        return new MapFragment();
    }
}