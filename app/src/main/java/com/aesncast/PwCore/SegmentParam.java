package com.aesncast.PwCore;

/*
 normally this would be an inner class of Segment, but alas, Java is retarded when it comes
 to inner classes and doesn't allow static members (e.g. enums) in inner classes for no
 foreseeable reason.
 fuck you java.
 */
public class SegmentParam {
    public enum ParamType
    {
        Field,
        String,
        Number
    }

    public ParamType type;
    public Object value;

    public SegmentParam(ParamType t, Object val)
    {
        this.type = t;
        this.value = val;
    }

    public String getField(String key, String domain, String user)
    {
        String s = (String)value;

        switch (s) {
            case "key":
                return key;
            case "domain":
                return domain;
            case "user":
                return user;
            default:
                return "";
        }
    }

    public String getString()
    {
        return (String)value;
    }

    public int getNumber()
    {
        return (int)value;
    }

    public Class<?> getValueClass()
    {
        switch (this.type)
        {
            case Field:
            case String:
                return String.class;
            case Number:
                return int.class;
        }

        return Object.class;
    }

    public boolean equals(SegmentParam other)
    {
        return this.type == other.type && this.value.equals(other.value);
    }

    public String toString()
    {
        switch (this.type)
        {
            case Field:
                return "$" + this.value;
            case String:
                return "\"" + this.value + "\"";
            case Number:
                return String.valueOf(this.value);
        }

        return "";
    }
}
