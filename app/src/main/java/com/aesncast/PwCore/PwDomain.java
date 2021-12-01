package com.aesncast.PwCore;

import com.aesncast.PwCore.PwUser;

import java.util.HashMap;
import java.util.Map;

public class PwDomain {
    public String name;
    public Map<String, PwUser> users;

    public PwDomain(String name)
    {
        this.name = name;
        this.users = new HashMap<>();
    }
}
