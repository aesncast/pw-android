package com.aesncast.pw_android;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.aesncast.PwCore.Pwfile;

public class SettingsFragment extends PreferenceFragmentCompat {
    PwPreferences preferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        preferences = new PwPreferences(this.getContext());

        setupStartOnPasswordGeneratorPreference();
        setupRecentUsersLimitPreference();
        setupClearRecentUsersPreference();
        setupClearDomainsAndUsers();
        setupClearAllSequences();
    }

    private void setupStartOnPasswordGeneratorPreference() {
        PreferenceManager man = getPreferenceManager();
        SwitchPreference pref = man.findPreference("start_on_password_generator");
        pref.setChecked(preferences.getStartOnPasswordGenerator());

        pref.setOnPreferenceChangeListener((x, val_) -> {
            boolean val = (boolean)val_;

            preferences.setStartOnPasswordGenerator(val);
            return true;
        });
    }

    private void setupRecentUsersLimitPreference() {
        PreferenceManager man = getPreferenceManager();
        EditTextPreference pref = man.findPreference("recent_users_limit");
        pref.setText(String.valueOf(preferences.getRecentUsersLimit()));

        pref.setOnPreferenceChangeListener((x, val_) -> {
            int val = -1;

            try {
                val = Integer.parseInt((String) val_);
            }
            catch (Exception ignored)
            {}

            if (val < 0) {
                Toast.makeText(this.getContext(), R.string.recent_users_negative_limit, Toast.LENGTH_LONG).show();
                return false;
            }

            preferences.setRecentUsersLimit(val);

            return true;
        });
    }

    private void setupClearAllSequences() {
        PreferenceManager man = getPreferenceManager();
        Preference clearSequences = man.findPreference("clear_all_sequences");

        setConfirmDialogPreferenceClickListener(clearSequences,
                getString(R.string.clear_all_sequences),
                getString(R.string.clear_all_sequences_description),
                f -> {
                    Pwfile instance = PwfileSingleton.instance.get();
                    instance.clearSequences();
                    PwfileSingleton.instance.save();
                    return true;
                });
    }

    private void setupClearDomainsAndUsers() {
        PreferenceManager man = getPreferenceManager();
        Preference clearDomainsAndUsers = man.findPreference("clear_all_domains_and_users");

        setConfirmDialogPreferenceClickListener(clearDomainsAndUsers,
                getString(R.string.clear_all_domains_and_users),
                getString(R.string.clear_all_domains_and_users_description),
                f -> {
                    Pwfile instance = PwfileSingleton.instance.get();
                    instance.domains.clear();
                    PwfileSingleton.instance.save();
                    return true;
                });
    }

    private void setupClearRecentUsersPreference()
    {
        PreferenceManager man = getPreferenceManager();
        Preference clearRecentUsers = man.findPreference("clear_all_recent_users");

        setConfirmDialogPreferenceClickListener(clearRecentUsers,
                getString(R.string.clear_all_recent_users),
                getString(R.string.clear_all_recent_users_description),
                f -> {
                    preferences.clearRecentUsers();
                    return true;
                });
    }

    private void setConfirmDialogPreferenceClickListener(Preference pref, String title, String message, Preference.OnPreferenceClickListener callback)
    {
        pref.setOnPreferenceClickListener(f -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                callback.onPreferenceClick(pref);
                dialog.dismiss();
            });

            alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            alert.show();

            return true;
        });
    }
}