package com.aesncast.pw_android.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    private static byte[] getDigest(byte[] input, String digest) throws NoSuchAlgorithmException {
        MessageDigest dig = MessageDigest.getInstance(digest);
        return dig.digest(input);
    }

    private static byte[] getDigest(String input, String digest) throws NoSuchAlgorithmException {
        return getDigest(input.getBytes(StandardCharsets.UTF_8), digest);
    }

    public static byte[] getSHA256(byte[] input) throws NoSuchAlgorithmException {
        return getDigest(input, "SHA-256");
    }

    public static byte[] getSHA256(String input) throws NoSuchAlgorithmException {
        return getDigest(input, "SHA-256");
    }

    public static byte[] getSHA512(byte[] input) throws NoSuchAlgorithmException {
        return getDigest(input, "SHA-512");
    }

    public static byte[] getSHA512(String input) throws NoSuchAlgorithmException {
        return getDigest(input, "SHA-512");
    }
}