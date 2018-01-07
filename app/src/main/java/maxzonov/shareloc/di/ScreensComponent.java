package maxzonov.shareloc.di;

import dagger.Component;
import maxzonov.shareloc.ui.location_info_screen.LocationFragment;
import maxzonov.shareloc.ui.map_screen.MapFragment;

@Component(modules = {
        LocationFragmentModule.class,
        MapFragmentModule.class
})
@AppScope
public interface ScreensComponent {
    LocationFragment getLocationFragment();
    MapFragment getMapFragment();
}
