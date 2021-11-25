package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import com.aesncast.pw_android.util.PyRandom;
import com.aesncast.pw_android.util.UInt32;

import org.junit.Test;

import java.math.BigInteger;

public class PyRandomTest {

    @Test
    public void pyrandom_conforms_to_python_random() throws Exception
    {
        PyRandom r = new PyRandom();
        assertEquals(new BigInteger("10868450558671247443152026947160338505683745266658651051718065983487878962987857602829315249215796444208488632888003673539585986066311769564391053988452926"),
                r.objectToSeed(""));

        assertEquals(new BigInteger("433193597737895078209865792410939156400009702269981117903286999239921129135190377233869728481982643344531267587789827921831486976264544736255360362822794745986752585254229481972783409"),
                r.objectToSeed("hello worldZ"));

        assertEquals(new BigInteger("537138095214198139212096549948278929856663516828609163018597693996056435507049622567828124679151804342196971665129024783340798935882604602280694296651110397473639338690197163748586032824797393291000537687243717041289544"),
                r.objectToSeed("abcdefghijklmnopqrstuvwxyzZ"));

        /*
        r.seed("");
        for (UInt32 u : r.state)
            System.out.println(u.toLong());
        */

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

        r.seed("abcdefghijklmnopqrstuvwxyzZ");
        assertEquals(12L, r.randint(0, 20));
        assertEquals(20L, r.randint(0, 20));
        assertEquals(14L, r.randint(0, 20));
        assertEquals(4L, r.randint(0, 20));
        assertEquals(13L, r.randint(0, 20));
    }
}
