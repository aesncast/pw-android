package com.aesncast.pw_android;

/*
    transformation functions to mimic
    https://github.com/aesncast/pw-py/blob/master/pw/transform.py
 */

import com.aesncast.pw_android.util.Array;
import com.aesncast.pw_android.util.Base58;
import com.aesncast.pw_android.util.Base64;
import com.aesncast.pw_android.util.PyRandom;
import com.aesncast.pw_android.util.SHA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class Transform {
    private static PyRandom _random = new PyRandom();

    private static String toString(Object i)
    {
        if (i instanceof String)
            return (String)i;

        if (i instanceof byte[])
            return new String((byte[])i);

        return "";
    }

    /*private*/public static BigInteger to_int(Object i)
    {
        byte[] bytes = {};

        if (i instanceof String)
            bytes = ((String) i).getBytes(StandardCharsets.UTF_8);

        if (i instanceof byte[])
            bytes = (byte[])i;

        if (i instanceof BigInteger)
            return (BigInteger)i;

        Array.reverse(bytes);

        return new BigInteger(1, bytes);
    }

    private static long seed(Object i, long min, long max)
    {
        try {
            _random.seed(i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0l;
        }

        return _random.randint(min, max);
    }

    private static long seed(Object i, long min)
    {
        return seed(i, min, (2 << 32) - 1);
    }

    private static long seed(Object i)
    {
        return seed(i, 0);
    }

    public static byte[] base58(Object s)
    {
        return base58(toString(s));
    }

    public static byte[] base64(Object s)
    {
        return Base64.decode(toString(s));
    }

    public static byte[] sha256(Object s) {
        try {
            if (s instanceof String)
                return SHA.getSHA256((String)s);
            else if (s instanceof byte[])
                return SHA.getSHA256((byte[])s);
            return new byte[]{};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    public static byte[] sha512(Object s) {
        try {
            if (s instanceof String)
                return SHA.getSHA512((String)s);
            else if (s instanceof byte[])
                return SHA.getSHA512((byte[])s);
            return new byte[]{};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    public static String append(Object s, String b)
    {
        return toString(s) + b;
    }

    public static String prepend(Object s, String b)
    {
        return b + toString(s);
    }

    /*
    Dear java: this is supposed to work. It doesn't. Fix your shit.
    public static <T> String init(T s, String key, String domain, String user)
    {

        String _s = append(_s, key);
        _s = append(_s, ":");
        _s = append(_s, user);
        _s = append(_s, "@");
        _s = append(_s, domain);
        return _s;
    }
    */

    public static String init(Object s, String key, String domain, String user)
    {
        String _s = "";

        if (s instanceof String)
            _s = append((String)s, key);

        if (s instanceof byte[])
            _s = append((byte[])s, key);

        _s = append(_s, ":");
        _s = append(_s, user);
        _s = append(_s, "@");
        _s = append(_s, domain);
        return _s;
    }

    public static String cut(Object s, int from, int to)
    {
        String _s = toString(s);

        if (_s.isEmpty())
            return "";

        if (from < 0)
            from = 0;

        if (from >= _s.length())
            from = _s.length() - 1;

        if (to < 0)
            to = 0;

        if (to > _s.length())
            to = _s.length();

        if (from >= to)
            return "";

        return _s.substring(from, to);
    }

    public static String limit(Object s, int len)
    {
        return cut(s, 0, len);
    }

    public static String replace(Object s, String to_replace, String replacement)
    {
        String _s = toString(s);
        return _s.replace(to_replace, replacement);
    }

    public static String make_unambiguous(Object s)
    {
        String _s = toString(s);

        if (_s.isEmpty())
            return _s;

        String safe_characters = "abcdefghkmnpqrsuvwxyz";
        char[] unsafe_characters = "ZlLtTiIjJoO012".toCharArray();

        for (char u: unsafe_characters) {
            String tmp = _s + u;
            // System.out.format("seed: %s, len: %d\n", _s + u, safe_characters.length() - 1);
            int i = (int)seed(tmp, 0, safe_characters.length() - 1);

            if (_s.indexOf(u) != -1)
            {
                char replacement = safe_characters.charAt(i);
                _s = replace(_s, String.valueOf(u), String.valueOf(replacement));
            }
        }

        return _s;
    }
}
