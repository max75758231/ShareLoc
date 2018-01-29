package maxzonov.shareloc.di.component;

import javax.inject.Singleton;

import dagger.Component;
import maxzonov.shareloc.di.module.LocationFragmentModule;
import maxzonov.shareloc.di.module.MapFragmentModule;
import maxzonov.shareloc.di.module.AppModule;
import maxzonov.shareloc.di.module.NavigatorModule;

@Singleton
@Component(modules = {
                AppModule.class,
                LocationFragmentModule.class,
                MapFragmentModule.class
        }
)
public interface AppComponent {
    ScreensComponent screensComponent(NavigatorModule navigatorModule);
}
