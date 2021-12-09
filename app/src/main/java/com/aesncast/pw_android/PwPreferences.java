package com.aesncast.pw_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.PwfileParser;
import com.aesncast.PwCore.util.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PwPreferences implements SharedPreferences {
    private final SharedPreferences preferences;
    private static final String PREFS_SETTINGS_NAME = "pw_settings";

    private static final String PREFS_PWLIST4_KEY = "pwlist4";
    private static final String PREFS_RECENT_KEY = "recent_users";
    private static final String PREFS_RECENT_LIMIT_KEY = "recent_users_limit";
    private static final int PREFS_RECENT_LIMIT_DEFAULT = 12;
    private static final String PREFS_START_ON_PASSWORD_GEN_KEY = "start_on_password_generator";

    public PwPreferences(Context ctx)
    {
        preferences = ctx.getSharedPreferences(PREFS_SETTINGS_NAME, Context.MODE_PRIVATE);
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
        String actualSrc = getString(PREFS_PWLIST4_KEY, defaultSrc);
        Pwfile ret = PwfileParser.parsePwlist4(actualSrc);
        return ret;
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

    @SuppressLint("DefaultLocale")
    public void pushRecentUser(String domainName, String userName, String sequenceName)
    {
        if (!contains(PREFS_RECENT_KEY)) {
            Editor e = edit();
            e.putStringSet(PREFS_RECENT_KEY, new HashSet<>());
            e.apply();
        }

        List<String> current = new ArrayList<>(getStringSet(PREFS_RECENT_KEY, null));
        String domainUser = String.format("%s:%s", domainName, userName);

        current.sort((x, y) -> {
            try {
                String[] sp1 = x.split(":");
                String[] sp2 = y.split(":");

                return Integer.valueOf(sp1[0]).compareTo(Integer.valueOf(sp2[0]));
            }
            catch (Exception e)
            {
                return 1;
            }
        });

        int i = 0;
        for (; i < current.size(); ++i)
            if (current.get(i).matches("^.*:"+domainUser+":.*$"))
                break;

        if (i < current.size() && i > 0) {
            Collections.swap(current, 0, i);

            current.set(0, String.format("%d:%s:%s", 0, domainUser, sequenceName));

            String prev = current.get(i);
            String[] sp = prev.split(":");
            current.set(i, String.format("%d:%s:%s:%s", i, sp[1], sp[2], sp[3]));
        }
        else if (i >= current.size())
        {
            current.add(0, String.format("%d:%s:%s", 0, domainUser, sequenceName));

            for (i = 1; i < current.size(); ++i) {
                String prev = current.get(i);
                String[] sp = prev.split(":");
                current.set(i, String.format("%d:%s:%s:%s", i, sp[1], sp[2], sp[3]));
            }

            int limit = getRecentUsersLimit();

            while (current.size() > limit)
                current.remove(current.size()-1);
        }
        else
            current.set(0, String.format("%d:%s:%s", 0, domainUser, sequenceName));

        Set<String> toSave = new HashSet<>(current);
        Editor e = edit();
        e.putStringSet(PREFS_RECENT_KEY, toSave);
        e.apply();
    }

    public void clearRecentUsers()
    {
        Set<String> toSave = new HashSet<>();
        Editor e = edit();
        e.putStringSet(PREFS_RECENT_KEY, toSave);
        e.apply();
    }

    private List<RecentUserEntry> getSortedRecentUsers()
    {
        List<RecentUserEntry> ret = new ArrayList<>();

        if (!contains(PREFS_RECENT_KEY)) {
            Editor e = edit();
            e.putStringSet(PREFS_RECENT_KEY, new HashSet<>());
            e.apply();
        }

        List<String> current = new ArrayList<>(getStringSet(PREFS_RECENT_KEY, null));

        if (current.isEmpty())
            return ret;

        current.sort((x, y) -> {
            try {
                String[] sp1 = x.split(":");
                String[] sp2 = y.split(":");

                return Integer.valueOf(sp1[0]).compareTo(Integer.valueOf(sp2[0]));
            }
            catch (Exception e)
            {
                return 1;
            }
        });

        return ret;
    }

    public List<RecentUserEntry> getRecentUsers()
    {
        List<RecentUserEntry> ret = new ArrayList<>();

        if (!contains(PREFS_RECENT_KEY)) {
            Editor e = edit();
            e.putStringSet(PREFS_RECENT_KEY, new HashSet<>());
            e.apply();
        }

        List<String> current = new ArrayList<>(getStringSet(PREFS_RECENT_KEY, null));

        if (current.isEmpty())
            return ret;

        current.sort((x, y) -> {
            try {
                String[] sp1 = x.split(":");
                String[] sp2 = y.split(":");

                return Integer.valueOf(sp1[0]).compareTo(Integer.valueOf(sp2[0]));
            }
            catch (Exception e)
            {
                return 1;
            }
        });

        for (String s : current) {
            String[] sp = s.split(":");
            ret.add(new RecentUserEntry(sp[1], sp[2], sp[3]));
        }

        return ret;
    }

    public int getRecentUsersLimit()
    {
        return getInt(PREFS_RECENT_LIMIT_KEY, PREFS_RECENT_LIMIT_DEFAULT);
    }

    public void setRecentUsersLimit(int limit)
    {
        if (limit < 0)
            return;

        int current_limit = getRecentUsersLimit();

        Editor e = edit();
        e.putInt(PREFS_RECENT_LIMIT_KEY, limit);

        if (limit >= current_limit) {
            e.apply();
            return;
        }

        // shrink
        List<RecentUserEntry> current = getRecentUsers();

        List<String> n = new ArrayList<>();

        for (int i = 0; i < limit; ++i)
        {
            RecentUserEntry f = current.get(i);
            n.add(String.format("%d:%s:%s:%s", i, f.domainName, f.userName, f.sequenceName));
        }

        e.putStringSet(PREFS_RECENT_KEY, new HashSet<>(n));
        e.apply();
    }

    public boolean getStartOnPasswordGenerator() {
        return getBoolean(PREFS_START_ON_PASSWORD_GEN_KEY, false);
    }

    public void setStartOnPasswordGenerator(boolean value) {
        Editor e = edit();
        e.putBoolean(PREFS_START_ON_PASSWORD_GEN_KEY, value);
        e.apply();
    }

    // Interface
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
