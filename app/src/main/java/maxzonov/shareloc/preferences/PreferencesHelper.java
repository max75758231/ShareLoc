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
        Resources res = context.getResources();
        preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return preferences.getString(key, res.getString(R.string.prefs_message_default));
    }
}
