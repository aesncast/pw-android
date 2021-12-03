package com.aesncast.pw_android;

import android.content.SharedPreferences;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.PwfileParser;

public class PwfileController {
    private Pwfile instance;
    private final PwPreferences preferences;

    public PwfileController(PwPreferences prefs)
    {
        this.preferences = prefs;
    }

    public void load()
    {
        instance = preferences.getDefaultPwfile();
    }

    public void save()
    {
        preferences.putDefaultPwfile(instance);
    }

    public Pwfile get()
    {
        return instance;
    }
}
