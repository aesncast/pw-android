package com.aesncast.PwCore;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    public String name;
    public List<Segment> segments;
    public boolean is_default;
    public boolean readonly;

    public Sequence()
    {
        this("");
    }

    public Sequence(String name)
    {
        this.name = name;
        this.segments = new ArrayList<>();
        this.is_default = false;
        this.readonly = false;
    }

    public String execute(String key, String domain, String user) throws NoSuchMethodException {
        String acc = "";

        for (Segment seg : this.segments)
            acc = seg.execute(acc, key, domain, user);

        return acc;
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder();

        s.append("[");

        if (is_default)
            s.append("+");

        s.append(name).append("]\n");

        String[] segs = segments.stream().map(x -> "    " + x.toString()).toArray(String[]::new);
        String segString = String.join("\n", segs);

        s.append(segString);

        return s.toString();
    }
}

