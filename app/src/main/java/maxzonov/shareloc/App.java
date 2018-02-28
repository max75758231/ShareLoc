package maxzonov.shareloc;

import android.app.Application;
import android.content.Context;

import maxzonov.shareloc.di.component.AppComponent;
import maxzonov.shareloc.di.component.DaggerAppComponent;
import maxzonov.shareloc.di.module.AppModule;
import maxzonov.shareloc.utils.LocaleManager;

public class App extends Application {

    private AppComponent appComponent;

    public static AppComponent getAppComponent(Context context) {
        return ((App) context.getApplicationContext()).appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleManager
                .onAttach(context, getString(R.string.prefs_language_ru_value)));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}