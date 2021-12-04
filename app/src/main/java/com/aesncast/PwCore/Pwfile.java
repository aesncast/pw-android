package com.aesncast.PwCore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pwfile {
    // in a good programming language, this would be an array!
    // java is not a good programming language!
    private static final List<String> BUILTIN_SEQUENCE_NAMES = Arrays.asList("LEGACY1", "LEGACY2", "DEFAULT");

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
        if (name.equals("DEFAULT"))
            name = this.default_sequence_name;

        return this.sequences.get(name);
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder();

        s.append("# auto generated pwlist4 file containing sequences, domains and users.\n");
        s.append("# feel free to edit / add but formatting and comments will be lost.");

        for (PwDomain dom : this.domains.values())
        {
            s.append("\n\n" + dom.name + ":");

            for (PwUser usr : dom.users.values())
                s.append("\n    " + usr.name + " - " + usr.sequence_name);
        }

        if (!this.sequences.isEmpty()) {
            s.append("\n\n# Sequences");
            s.append("\n# don't change, only copy & make new ones to be safe,");
            s.append("\n# otherwise you risk losing passwords if you forget");
            s.append("\n# the sequences.");
        }

        for (Sequence seq : this.sequences.values())
        {
            if (BUILTIN_SEQUENCE_NAMES.contains(seq.name))
                continue;

            s.append("\n[");

            if (seq.is_default)
                s.append("+");

            s.append(seq.name + "]");

            for (Segment seg : seq.segments)
            {
                s.append("\n    " + seg.function);
                s.append("(");

                s.append(String.join(", ", seg.parameters.stream().map(x -> x.toString()).toArray(String[]::new)));
                s.append(")");
            }

            s.append("\n");
        }

        return s.toString();
    }
}
