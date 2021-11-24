package com.aesncast.pw_android.util;

/*
    class to generate the same random numbers as with python random.
    https://github.com/python/cpython/blob/3.10/Lib/random.py
 */

import android.provider.Contacts;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class PyRandom {
    private static int N = 624;
    /*
    private static int M = 397;
    private static int MATRIX_A = 0x9908b0df;    // constant vector a
    private static int UPPER_MASK = 0x80000000;  // most significant w-r bits
    private static int LOWER_MASK = 0x7fffffff;  // least significant r bits
    */

    UInt32 index = new UInt32(0);
    public UInt32[] state = new UInt32[N]; // mersenne twister state

    public PyRandom()
    {
        for (int i = 0; i < N; ++i)
            state[i] = new UInt32();
    }

    public void init_genrand(int s)
    {
        UInt32 mti = new UInt32(1);
        this.state[0].set(s);

        for (; mti.lt(N); mti.inc()) {
            // state[mti] = (1812433253U * (state[mti-1] ^ (state[mti-1] >> 30)) + mti);
            UInt32 a = new UInt32(state[mti.toInt()-1]);
            UInt32 b = new UInt32(state[mti.toInt()-1]);
            b.rshift(30);
            a.xor(b);
            a.mul(1812433253L);
            a.add(mti);
            state[mti.toInt()].set(a);
        }

        this.index.set(mti);
    }

    public void init_by_array(UInt32[] key)
    {
        this.init_genrand(19650218);

        // correct
        //for (int i = 0; i < key.length; ++i)
        //    System.out.println(key[i].toLong());

        int i=1;
        int j=0;

        int k = key.length;

        if (N > key.length)
            k = N;

        for (; k > 0; k--) {
            //state[i] = (state[i] ^ ((state[i-1] ^ (state[i-1] >> 30)) * 1664525)) + key[j] + j; /* non linear */
            UInt32 a = new UInt32(state[i]);
            UInt32 b = new UInt32(state[i-1]);
            UInt32 c = new UInt32(state[i-1]);
            c.rshift(30);
            b.xor(c);
            b.mul(1664525L);
            a.xor(b);
            a.add(key[j]);
            a.add(j);

            state[i].set(a);

            i++;
            j++;

            if (i >= N) {
                state[0].set(state[N-1]);
                i = 1;
            }

            if (j >= key.length)
                j = 0;
        }

        for (k = N-1; k > 0; k--) {
            // state[i] = (state[i] ^ ((state[i-1] ^ (state[i-1] >> 30)) * 1566083941)) - i; /* non linear */
            UInt32 a = new UInt32(state[i]);
            UInt32 b = new UInt32(state[i-1]);
            UInt32 c = new UInt32(state[i-1]);
            c.rshift(30);
            b.xor(c);
            b.mul(1566083941L);
            a.xor(b);
            a.sub(i);

            state[i].set(a);
            i++;

            if (i >= N) {
                state[0].set(state[N-1]);
                i = 1;
            }
        }

        state[0].set(0x80000000L); /* MSB is 1; assuring non-zero initial array */
    }

    public BigInteger objectToSeed(Object i) throws NoSuchAlgorithmException
    {
        BigInteger _seed = BigInteger.ZERO;

        if (i instanceof BigInteger)
        {
            _seed = (BigInteger)i;
        }
        else if (i instanceof String)
        {
            String s = (String)i;
            byte[] b = s.getBytes();
            byte[] sha = SHA.getSHA512(b);
            byte[] full = Array.appendCopy(b, sha);
            _seed = new BigInteger(1, full);
        }
        else if (i instanceof byte[])
        {
            byte[] b = (byte[])i;
            byte[] sha = SHA.getSHA512(b);
            byte[] full = Array.appendCopy(b, sha);
            _seed = new BigInteger(1, full);
        }

        return _seed;
    }

    public void seed(Object obj) throws NoSuchAlgorithmException
    {
        BigInteger _seed = this.objectToSeed(obj);
        byte[] bytes = _seed.toByteArray();
        Array.reverse(bytes);

        // https://stackoverflow.com/a/11438071
        // maybe reverse
        IntBuffer intBuf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] ints = new int[intBuf.remaining()];
        intBuf.get(ints);

        UInt32[] key = new UInt32[ints.length];

        for (int i = 0; i < ints.length; ++i)
            key[i] = new UInt32(ints[i]);

        this.init_by_array(key);
    }

    public long randint(long min, long max) {
        return 1;
    }
}
