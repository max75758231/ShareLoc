package maxzonov.shareloc.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import maxzonov.shareloc.R;

public class PreferencesHelper {

    private SharedPreferences preferences;

    public PreferencesHelper(Context context, String key) {
         preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public String readFromPrefs(Context context, String key) {
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

    /**
     * There is using commit() instead of apply() because an app can be killed immediately,
     * so apply() can be not finished
     */
    @SuppressLint("ApplySharedPref")
    public void writeToPrefs(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}