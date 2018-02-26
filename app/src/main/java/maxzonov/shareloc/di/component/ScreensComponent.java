package maxzonov.shareloc.di.component;

import dagger.Subcomponent;
import maxzonov.shareloc.StartActivity;
import maxzonov.shareloc.di.module.LocationFragmentModule;
import maxzonov.shareloc.di.module.MapFragmentModule;
import maxzonov.shareloc.di.module.NavigatorModule;
import maxzonov.shareloc.di.scope.PerActivity;

@PerActivity
@Subcomponent
        (modules = {
        LocationFragmentModule.class,
        MapFragmentModule.class,
        NavigatorModule.class
        })
public interface ScreensComponent {

    void inject(StartActivity startActivity);
}