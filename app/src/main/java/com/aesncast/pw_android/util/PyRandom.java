package com.aesncast.pw_android.util;

/*
    class to generate the same random numbers as with python random.
    https://github.com/python/cpython/blob/3.10/Lib/random.py
 */

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class PyRandom {
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

    public void seed(Object i) throws NoSuchAlgorithmException
    {
        BigInteger _seed = objectToSeed(i);
        System.out.println(_seed.toString());
    }

    public long randint(long min, long max) {
        return 1;
    }
}
