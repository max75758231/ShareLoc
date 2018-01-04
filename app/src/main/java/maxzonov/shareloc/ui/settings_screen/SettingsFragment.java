package maxzonov.shareloc.ui.settings_screen;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import maxzonov.shareloc.R;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.settings_layout);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int countPrefs = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < countPrefs; i++) {
            Preference p = preferenceScreen.getPreference(i);

            String value = sharedPreferences.getString(p.getKey(),
                    getResources().getString(R.string.prefs_message_default));

            p.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            String value = sharedPreferences.getString(preference.getKey(),
                    getResources().getString(R.string.prefs_message_default));
            preference.setSummary(value);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
