package maxzonov.shareloc.di.component;

import javax.inject.Singleton;

import dagger.Component;
import maxzonov.shareloc.di.LocationFragmentModule;
import maxzonov.shareloc.di.MapFragmentModule;
import maxzonov.shareloc.di.ScreensComponent;
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
