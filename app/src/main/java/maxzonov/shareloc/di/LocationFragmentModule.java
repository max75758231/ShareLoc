package maxzonov.shareloc.di;

import dagger.Module;
import dagger.Provides;
import maxzonov.shareloc.ui.location_info_screen.LocationFragment;

@Module
public class LocationFragmentModule {

    @Provides
    @AppScope
    public LocationFragment locationFragment() {
        return new LocationFragment();
    }
}
