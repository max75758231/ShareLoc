package maxzonov.shareloc.di.module;

import dagger.Module;
import dagger.Provides;
import maxzonov.shareloc.di.scope.PerActivity;
import maxzonov.shareloc.navigation.AppNavigator;

@Module
public class NavigatorModule {

    private final AppNavigator navigator;

    public NavigatorModule(AppNavigator navigator) {
        this.navigator = navigator;
    }

    @PerActivity
    @Provides
    AppNavigator provideAppNavigator() {
        return navigator;
    }
}
