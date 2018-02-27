package maxzonov.shareloc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {

    private static final String LANGUAGE_RUSSIAN = "ru";
    private static final String LANGUAGE_KEY = "language";

    public static Context setLocale(Context context) {
        return updateResources(context, getLanguage(context));
    }

    public static Context setNewLocale(Context context, String language) {
        saveLanguage(context, language);
        return updateResources(context, language);
    }

    private static String getLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_RUSSIAN);
    }

    @SuppressLint("ApplySharedPref")
    private static void saveLanguage(Context context, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
}