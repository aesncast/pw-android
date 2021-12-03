package com.aesncast.pw_android;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.PwfileParser;

import java.util.Map;
import java.util.Set;

public class PwPreferences implements SharedPreferences {
    private SharedPreferences preferences;
    private static final String PREFS_PWFILES_NAME = "pwfiles";
    private static final String PREFS_PWLIST4_KEY = "pwlist4";

    public PwPreferences(Context ctx)
    {
        preferences = ctx.getSharedPreferences(PREFS_PWFILES_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences()
    {
        return preferences;
    }

    public Pwfile getPwfile(String key)
    {
        return PwfileParser.parsePwlist4(getString(key, null));
    }

    public Pwfile getDefaultPwfile()
    {
        String defaultSrc = new Pwfile().toString();
        return PwfileParser.parsePwlist4(getString(PREFS_PWLIST4_KEY, defaultSrc));
    }

    public void putPwfile(String key, Pwfile value)
    {
        Editor e = edit();
        e.putString(key, value.toString());
        e.apply();
    }

    public void putDefaultPwfile(Pwfile value)
    {
        Editor e = edit();
        e.putString(PREFS_PWLIST4_KEY, value.toString());
        e.apply();
    }

    @Override
    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return preferences.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return preferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public Editor edit() {
        return preferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
