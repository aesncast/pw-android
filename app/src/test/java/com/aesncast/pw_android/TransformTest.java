package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import com.aesncast.pw_android.util.PyRandom;

import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class TransformTest {
    @Test
    public void to_int_conforms_to_pwpy() throws Exception
    {
        assertEquals(new BigInteger("0"), Transform.to_int(""));
        assertEquals(new BigInteger("6513249"), Transform.to_int("abc"));
        assertEquals(new BigInteger("3291835376408573590478209986637364656599265025014012802863049622424083630783948306431999498413285667939592978357630573418285899181951386474024455144309711"),
                     Transform.to_int(Transform.sha512("")));
    }
}
