package com.aesncast.pw_android.util;

/*
    Sincerely java: i fucking hate you, you absolute piece of trash language.
 */
public class UInt32 implements Comparable<UInt32> {
    private static long BITS = 0xFFFFFFFFL;
    private long value = 0L;

    public UInt32(){}

    public UInt32(int val)
    {
        set(val);
    }

    public UInt32(long val)
    {
        set(val);
    }

    public UInt32(UInt32 val)
    {
        set(val);
    }

    private void trunc()
    {
        value = value & BITS;
    }

    public void set(int val)
    {
        value = val;
        trunc();
    }

    public void set(long val)
    {
        value = val;
        trunc();
    }

    public void set(UInt32 val)
    {
        value = val.value;
        trunc();
    }

    public void add(int val)
    {
        value = value + val;
        trunc();
    }

    public void add(long val)
    {
        value = value + val;
        trunc();
    }

    public void add(UInt32 val)
    {
        value = value + val.value;
        trunc();
    }

    public void sub(int val)
    {
        value = value - val;
        trunc();
    }

    public void sub(long val)
    {
        value = value - val;
        trunc();
    }

    public void sub(UInt32 val)
    {
        value = value - val.value;
        trunc();
    }

    public void mul(int val)
    {
        value = value * val;
        trunc();
    }

    public void mul(long val)
    {
        value = value * val;
        trunc();
    }

    public void mul(UInt32 val)
    {
        value = value * val.value;
        trunc();
    }

    public void div(int val)
    {
        value = value / val;
        trunc();
    }

    public void div(long val)
    {
        value = value / val;
        trunc();
    }

    public void div(UInt32 val)
    {
        value = value / val.value;
        trunc();
    }

    public void lshift(int val)
    {
        value = value << val;
        trunc();
    }

    public void lshift(long val)
    {
        value = value << val;
        trunc();
    }

    public void lshift(UInt32 val)
    {
        value = value << val.value;
        trunc();
    }

    public void rshift(int val)
    {
        value = value >> val;
        trunc();
    }

    public void rshift(long val)
    {
        value = value >> val;
        trunc();
    }

    public void rshift(UInt32 val)
    {
        value = value >> val.value;
        trunc();
    }

    public void and(int val)
    {
        value = value & val;
        trunc();
    }

    public void and(long val)
    {
        value = value & val;
        trunc();
    }

    public void and(UInt32 val)
    {
        value = value & val.value;
        trunc();
    }

    public void or(int val)
    {
        value = value | val;
        trunc();
    }

    public void or(long val)
    {
        value = value | val;
        trunc();
    }

    public void or(UInt32 val)
    {
        value = value | val.value;
        trunc();
    }

    public void xor(int val)
    {
        value = value ^ val;
        trunc();
    }

    public void xor(long val)
    {
        value = value ^ val;
        trunc();
    }

    public void xor(UInt32 val)
    {
        value = value ^ val.value;
        trunc();
    }

    public void inc()
    {
        value = value + 1L;
        trunc();
    }

    public void dec()
    {
        value = value - 1L;
        trunc();
    }

    public boolean lt(int val)
    {
        return value < (val & 0xFFFFFFFFL);
    }

    public boolean lt(long val)
    {
        return value < (val & 0xFFFFFFFFL);
    }

    public boolean lt(UInt32 val)
    {
        return value < val.value;
    }

    public boolean gt(int val)
    {
        return value > (val & 0xFFFFFFFFL);
    }

    public boolean gt(long val)
    {
        return value > (val & 0xFFFFFFFFL);
    }

    public boolean gt(UInt32 val)
    {
        return value > val.value;
    }

    public boolean leq(int val)
    {
        return value <= (val & 0xFFFFFFFFL);
    }

    public boolean leq(long val)
    {
        return value <= (val & 0xFFFFFFFFL);
    }

    public boolean leq(UInt32 val)
    {
        return value <= val.value;
    }

    public boolean geq(int val)
    {
        return value >= (val & 0xFFFFFFFFL);
    }

    public boolean geq(long val)
    {
        return value >= (val & 0xFFFFFFFFL);
    }

    public boolean geq(UInt32 val)
    {
        return value >= val.value;
    }

    public boolean eq(int val)
    {
        return value == (val & 0xFFFFFFFFL);
    }

    public boolean eq(long val)
    {
        return value == (val & 0xFFFFFFFFL);
    }

    public boolean eq(UInt32 val)
    {
        return value == val.value;
    }

    public int toInt()
    {
        return (int)value;
    }

    public long toLong()
    {
        return value;
    }

    @Override
    public int compareTo(UInt32 o) {
        return (int)(this.value - o.value);
    }
}
