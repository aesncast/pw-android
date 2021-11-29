package com.aesncast.pw_android.util;

import java.nio.charset.StandardCharsets;

public class Base64 {
    private static java.util.Base64.Decoder _decoder = java.util.Base64.getDecoder();
    private static java.util.Base64.Encoder _encoder = java.util.Base64.getEncoder();

    public static byte[] encode(byte[] input) {
        return _encoder.encode(input);
    }

    public static byte[] encode(String input) {
        return _encoder.encode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(byte[] input) {
        return _decoder.decode(input);
    }

    public static byte[] decode(String input) {
        return _decoder.decode(input);
    }
}
