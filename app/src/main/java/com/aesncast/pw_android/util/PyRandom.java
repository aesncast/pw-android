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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PyRandom {
    private static int N = 624;
    private static int M = 397;
    private static UInt32 MATRIX_A = new UInt32(0x9908b0dfL);    // constant vector a
    private static UInt32 UPPER_MASK = new UInt32(0x80000000L);  // most significant w-r bits
    private static UInt32 LOWER_MASK = new UInt32(0x7fffffffL);  // least significant r bits

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
        else if (i instanceof Integer)
        {
            _seed = BigInteger.valueOf((Integer)i);
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
        // System.out.format("%d bytes in key\n", bytes.length);

        if (bytes.length < 4) {
            int nlen = 4;
            byte[] tmp = new byte[nlen];
            System.arraycopy(bytes, 0, tmp, nlen - bytes.length, bytes.length);
            bytes = tmp;
        }
        else if (bytes[0] == 0x00)
        {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }

        if (bytes.length % 4 != 0) {
            int nlen = (int)(Math.ceil(bytes.length / 4.0f) * 4);
            byte[] tmp = new byte[nlen];
            System.arraycopy(bytes, 0, tmp, nlen - bytes.length, bytes.length);
            bytes = tmp;
        }

        Array.reverse(bytes);

        // https://stackoverflow.com/a/11438071
        IntBuffer intBuf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] ints = new int[intBuf.remaining()];
        intBuf.get(ints);

        UInt32[] key = new UInt32[ints.length];

        for (int i = 0; i < ints.length; ++i)
            key[i] = new UInt32(ints[i]);

        this.init_by_array(key);
    }

    private UInt32 genrandUInt32() {
        UInt32 y = new UInt32(0);
        UInt32[] mag01 = new UInt32[]{new UInt32(0x0L), MATRIX_A};
        /* mag01[x] = x * MATRIX_A  for x=0,1 */

        if (this.index.geq(N)) { /* generate N words at one time */
            int kk;

            for (kk = 0; kk < N - M; kk++)
            {
                // y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                UInt32 a = new UInt32(state[kk]);
                UInt32 b = new UInt32(state[kk + 1]);
                a.and(UPPER_MASK);
                b.and(LOWER_MASK);
                a.or(b);
                y.set(a);

                // mt[kk] = mt[kk+M] ^ (y >> 1) ^ mag01[y & 0x1U];
                b.set(state[kk+M]);
                a.rshift(1);
                b.xor(a);
                y.and(0x1L);
                b.xor(mag01[y.toInt()]);
                state[kk].set(b);
                // System.out.format("mt[%d] = %d ^ %d ^ %d = %d\n", kk, state[kk+M].toLong(), a.toLong(), mag01[y.toInt()].toLong(), state[kk].toLong());
            }

            for (; kk < N - 1; kk++)
            {
                // y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                UInt32 a = new UInt32(state[kk]);
                UInt32 b = new UInt32(state[kk + 1]);
                a.and(UPPER_MASK);
                b.and(LOWER_MASK);
                a.or(b);
                y.set(a);

                // mt[kk] = mt[kk+(M-N)] ^ (y >> 1) ^ mag01[y & 0x1U];
                b.set(state[kk+(M-N)]);
                a.rshift(1);
                b.xor(a);
                y.and(0x1L);
                b.xor(mag01[y.toInt()]);
                state[kk].set(b);
            }

            // y = (mt[N-1]&UPPER_MASK)|(mt[0]&LOWER_MASK);
            UInt32 a = new UInt32(state[N-1]);
            UInt32 b = new UInt32(state[0]);
            a.and(UPPER_MASK);
            b.and(LOWER_MASK);
            a.or(b);
            y.set(a);

            // mt[N-1] = mt[M-1] ^ (y >> 1) ^ mag01[y & 0x1U];
            b.set(state[M-1]);
            a.rshift(1);
            b.xor(a);
            y.and(0x1L);
            b.xor(mag01[y.toInt()]);
            state[N-1].set(b);

            this.index.set(0);
        }

        // y = mt[self->index++];
        // y ^= (y >> 11);
        // y ^= (y << 7) & 0x9d2c5680U;
        // y ^= (y << 15) & 0xefc60000U;
        // y ^= (y >> 18);
        y = state[index.toInt()];
        index.inc();
        UInt32 y2 = new UInt32(y);
        y2.rshift(11);
        y.xor(y2);

        y2.set(y);
        y2.lshift(7);
        y2.and(0x9d2c5680L);
        y.xor(y2);

        y2.set(y);
        y2.lshift(15);
        y2.and(0xefc60000L);
        y.xor(y2);

        y2.set(y);
        y2.rshift(18);
        y.xor(y2);
        return y;
    }

    private long getrandbits(int len) {
        if (len <= 0)
            return 0;

        if (len <= 32) {
            UInt32 u = this.genrandUInt32();
            // System.out.format("getrandbits len: %d val %d\n", len, u.toLong());
            u.rshift(32 - len);
            // System.out.format("getrandbits after shift: %d\n", u.toLong());
            return u.toLong();
        }

        // TODO: random numbers with more than 32 bits
        return -1;
    }

    public long randbelow(long n)
    {
        if (n == 0)
            return 0;

        int len = BigInteger.valueOf(n).bitLength();
        // System.out.format("randbelow: %d, bits: %d\n", n, len);

        long r = this.getrandbits(len);

        while (r >= n)
            r = this.getrandbits(len);

        return r;
    }

    public long randrange(long min, long max)
    {
        long width = max - min;
        // System.out.format("randrange: generating %d + randbelow(%d)\n", min, width);
        return min + this.randbelow(width);
    }

    public long randint(long min, long max) {
        // System.out.format("randint: generating from %d to %d\n", min, max);
        return this.randrange(min, max+1);
    }

    public <T> T[] sample(T[] population, int k) {
        int n = population.length;

        if (k < 0)
            throw new IllegalArgumentException("k is smaller than 0");

        if (k > n)
            throw new IllegalArgumentException("k is larger than population");

        T[] ret = Arrays.copyOf(population, k);
        int setsize = 21;

        if (k > 5)
            setsize += Math.pow(4, Math.ceil(MathUtil.logB(k * 3, 4)));

        if (n <= setsize)
        {
            T[] pool = Arrays.copyOf(population, population.length);

            for (int i = 0; i < k; ++i) {
                int j = (int)randbelow(n - i);
                ret[i] = pool[j];
                pool[j] = pool[n - i - 1];
            }
        }
        else
        {
            Set<Integer> selected = new HashSet<>();

            for (int i = 0; i < k; ++i) {
                Integer j = (int) randbelow(n);

                while (selected.contains(j))
                    j = (int)randbelow(n);

                selected.add(j);
                ret[i] = population[j];
            }
        }

        return ret;
    }
}
