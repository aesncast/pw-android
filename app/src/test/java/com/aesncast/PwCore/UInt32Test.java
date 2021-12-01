package com.aesncast.PwCore;

import static org.junit.Assert.assertEquals;

import com.aesncast.PwCore.util.UInt32;

import org.junit.Test;

public class UInt32Test {
    @Test
    public void uint32_conforms_to_c() throws Exception
    {
        assertEquals(4294967295L, new UInt32(-1).toLong());
        assertEquals(2147483647, new UInt32(2147483647).toInt());
        assertEquals(-2147483648, new UInt32(2147483648L).toInt());


        UInt32 u = new UInt32();
        u.add(2147483647);
        assertEquals(2147483647L, u.toLong());
        u.add(1);
        assertEquals(2147483648L, u.toLong());
        u.add(2147483647);
        assertEquals(4294967295L, u.toLong());
        u.add(1);
        assertEquals(0L, u.toLong());


        u.set(0);
        u.sub(1);
        assertEquals(4294967295L, u.toLong());


        u.set(2147483);
        u.mul(u);
        assertEquals(3183326681L, u.toLong());


        u.set(2147483);
        u.div(7);
        assertEquals(306783, u.toInt());
    }
}
