package com.aesncast.pw_android.util;

public class Base64 {
    private static java.util.Base64.Decoder _decoder = java.util.Base64.getDecoder();
    private static java.util.Base64.Encoder _encoder = java.util.Base64.getEncoder();

    public static String encode(byte[] input) {
        return _encoder.encodeToString(input);
    }

    public static byte[] decode(byte[] input) {
        return _decoder.decode(input);
    }

    public static byte[] decode(String input) {
        return _decoder.decode(input);
    }
}
