package maxzonov.shareloc.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import maxzonov.shareloc.R;

public class LocaleManager {

    private static final String SELECTED_LANGUAGE = "language";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    public static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    /**
     * There is using commit() instead of apply() because an app can be killed immediately,
     * so apply() can be not finished
     */
    @SuppressLint("ApplySharedPref")
    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.commit();
    }

    /**
     * Method to update language on Android 7.0+
     * @param language default language
     * @return context with updated language
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    /**
     * Method to update language on Android between 4.1 and 7.0
     * @param language default language
     * @return context with updated language
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = null;
        Resources res = context.getResources();

        if (language.equals(res.getString(R.string.prefs_language_ru_value))) {
            locale = new Locale(language, res.getString(R.string.prefs_language_ru_country_code));
        } else if (language.equals(res.getString(R.string.prefs_language_en_value))) {
            locale = new Locale(language, res.getString(R.string.prefs_language_en_country_code));
        }

        Locale.setDefault(locale);

        Configuration configuration = res.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        res.updateConfiguration(configuration, res.getDisplayMetrics());

        return context;
    }
}