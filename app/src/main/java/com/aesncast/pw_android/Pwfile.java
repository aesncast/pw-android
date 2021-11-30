package com.aesncast.pw_android;

import java.util.HashMap;
import java.util.Map;

public class Pwfile {
    public Map<String, PwDomain> domains;
    public Map<String, Sequence> sequences;
    public String default_sequence_name;

    public Pwfile()
    {
        this.domains = new HashMap<>();
        this.sequences = new HashMap<>();
        this.default_sequence_name = "";

        this.init_legacy_sequences();
    }

    private void init_legacy_sequences()
    {
        Sequence legacy1 = new Sequence("LEGACY1");
        Segment legacy1seg = new Segment("bad_legacy1");
        legacy1seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "key"));
        legacy1seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "domain"));
        legacy1.segments.add(legacy1seg);
        this.sequences.put("LEGACY1", legacy1);

        Sequence legacy2 = new Sequence("LEGACY2");
        Segment legacy2seg = new Segment("bad_legacy2");
        legacy2seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "key"));
        legacy2seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "domain"));
        legacy2seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "user"));
        legacy2.segments.add(legacy2seg);
        this.sequences.put("LEGACY2", legacy2);
    }

    public Sequence getSequence(String name)
    {
        if (name == "DEFAULT")
            name = this.default_sequence_name;

        return this.sequences.get(name);
    }
}
