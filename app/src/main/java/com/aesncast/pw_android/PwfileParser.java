package com.aesncast.pw_android;

public class PwfileParser {

    private static Sequence get_good_password_sequence()
    {
        Sequence s = new Sequence("good_password");
        Segment s1 = new Segment("init");
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "key"));
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "domain"));
        s1.parameters.add(new SegmentParam(SegmentParam.ParamType.Field, "user"));
        s.segments.add(s1);

        Segment s2 = new Segment("diceware");
        s2.parameters.add(new SegmentParam(SegmentParam.ParamType.Number, 4));
        s2.parameters.add(new SegmentParam(SegmentParam.ParamType.Number, 4));
        s.segments.add(s2);

        Segment s3 = new Segment("capitalize_some");
        s.segments.add(s3);

        Segment s4 = new Segment("add_some_simple_special_characters");
        s.segments.add(s4);

        return s;
    }
}
