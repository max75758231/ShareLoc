package maxzonov.shareloc;

import android.app.Application;
import android.content.Context;

import maxzonov.shareloc.di.component.AppComponent;
import maxzonov.shareloc.di.component.DaggerAppComponent;
import maxzonov.shareloc.di.module.AppModule;
import maxzonov.shareloc.utils.LocaleManager;

public class App extends Application {

    private AppComponent appComponent;

    private static final String DEFAULT_LANGUAGE_RU = "ru";

    public static AppComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleManager
                .onAttach(context, DEFAULT_LANGUAGE_RU));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}