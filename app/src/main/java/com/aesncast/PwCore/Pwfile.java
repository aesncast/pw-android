package com.aesncast.PwCore;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        legacy1.readonly = true;
        Segment legacy1seg = new Segment("bad_legacy1");
        legacy1seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "key"));
        legacy1seg.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "domain"));
        legacy1.segments.add(legacy1seg);
        this.sequences.put("LEGACY1", legacy1);

        Sequence legacy2 = new Sequence("LEGACY2");
        legacy2.readonly = true;
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

    // gets all non-builtin sequences
    public List<Sequence> getSequences()
    {
        List<Sequence> ret = new ArrayList<>(sequences.values());

        ret.removeIf(x -> BUILTIN_SEQUENCE_NAMES.contains(x.name));

        return ret;
    }

    public void setDefaultSequence(String sequenceName)
    {
        if (this.default_sequence_name.equals(sequenceName))
            return;

        if (sequenceName.isEmpty() || sequenceName.equals("DEFAULT"))
            return;

        this.default_sequence_name = sequenceName;

        for (Sequence s : this.sequences.values())
            s.is_default = s.name.equals(sequenceName);
    }

    @NonNull
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
            s.append("\n");
        }

        String seqString = String.join("\n\n", this.sequences.values().stream()
                .filter(seq -> !BUILTIN_SEQUENCE_NAMES.contains(seq.name))
                .map(seq -> seq.toString())
                .toArray(String[]::new)
        );

        s.append(seqString);
        s.append("\n");

        return s.toString();
    }

    public void clearSequences() {
        this.sequences.entrySet().removeIf(e -> !(e.getValue().readonly || e.getValue().is_default));
    }
}
