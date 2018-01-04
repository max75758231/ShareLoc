package maxzonov.shareloc.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private SharedPreferences preferences;

    public PreferencesHelper(String key, Context context) {
         preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public void writeToPrefs(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readFromPrefs(String key, Context context) {
        preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preferences.getString(key, "0.0");
    }
}
