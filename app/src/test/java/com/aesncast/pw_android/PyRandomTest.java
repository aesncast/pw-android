package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import com.aesncast.pw_android.util.PyRandom;

import org.junit.Test;

import java.math.BigInteger;

public class PyRandomTest {

    @Test
    public void pyrandom_conforms_to_python_random() throws Exception
    {
        PyRandom r = new PyRandom();
        assertEquals(new BigInteger("10868450558671247443152026947160338505683745266658651051718065983487878962987857602829315249215796444208488632888003673539585986066311769564391053988452926"),
                r.objectToSeed(""));

        r.seed(1);
        assertEquals(17611L, r.randint(0, 65535));
        assertEquals(8271L, r.randint(0, 65535));
        assertEquals(33432L, r.randint(0, 65535));
        assertEquals(15455L, r.randint(0, 65535));
        assertEquals(64937L, r.randint(0, 65535));

        r.seed("");
        assertEquals(59569L, r.randint(0, 65535));
        assertEquals(27117L, r.randint(0, 65535));
        assertEquals(50857L, r.randint(0, 65535));
        assertEquals(28945L, r.randint(0, 65535));
        assertEquals(45015L, r.randint(0, 65535));
    }
}
