package com.aesncast.pw_android;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    public String name;
    public List<Segment> segments;
    // public boolean is_default;

    public Sequence()
    {
        this("");
    }

    public Sequence(String name)
    {
        this.name = name;
        this.segments = new ArrayList<>();
        // this.is_default = false;
    }

    public String execute(String key, String domain, String user) throws NoSuchMethodException {
        String acc = "";

        for (Segment seg : this.segments)
            acc = seg.execute(acc, key, domain, user);

        return acc;
    }
}

