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

    public String readFromPrefs(String key, Context context) {
        preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        Resources res = context.getResources();

        String result = "";
        if (key.equals(res.getString(R.string.prefs_message_key))) {
            result = preferences.getString(key,
                    context.getResources().getString(R.string.prefs_message_default));
        } else if (key.equals(res.getString(R.string.prefs_latitude_key))) {
            result = preferences.getString(key,
                    context.getResources().getString(R.string.prefs_latitude_default));
        } else if (key.equals(res.getString(R.string.prefs_longitude_key))) {
            result = preferences.getString(key,
                    context.getResources().getString(R.string.prefs_longitude_default));
        }
        return result;
    }

    public void writeToPrefs(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
