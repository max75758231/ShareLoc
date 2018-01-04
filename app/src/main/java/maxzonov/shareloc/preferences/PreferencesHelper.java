package maxzonov.shareloc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import maxzonov.shareloc.R;

public class PreferencesHelper {

    private SharedPreferences preferences;

    public PreferencesHelper(String key, Context context) {
         preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public void writeToPrefs(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readFromPrefs(String key, Context context) {
        Resources res = context.getResources();
        preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preferences.getString(key, res.getString(R.string.prefs_message_default));
    }
}
