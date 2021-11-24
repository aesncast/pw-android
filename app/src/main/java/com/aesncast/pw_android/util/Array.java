package com.aesncast.pw_android.util;

import java.util.Arrays;

public class Array {
    public static <T> void reverse(T[] arr) {
        int l = arr.length;
        T tmp;

        for (int i = 0; i < l / 2; i++) {
            tmp = arr[i];
            arr[i] = arr[l - i - 1];
            arr[l - i - 1] = tmp;
        }
    }

    /*
    Dear java: fuck you.
     */
    public static void reverse(byte[] arr) {
        int l = arr.length;
        byte tmp;

        for (int i = 0; i < l / 2; i++) {
            tmp = arr[i];
            arr[i] = arr[l - i - 1];
            arr[l - i - 1] = tmp;
        }
    }

    public static void reverse(int[] arr) {
        int l = arr.length;
        int tmp;

        for (int i = 0; i < l / 2; i++) {
            tmp = arr[i];
            arr[i] = arr[l - i - 1];
            arr[l - i - 1] = tmp;
        }
    }

    public static <T> T[] appendCopy(T[] arr1, T[] arr2)
    {
        T[] ret = Arrays.copyOf(arr1, arr1.length + arr2.length);

        for (int i = 0; i < arr2.length; ++i)
            ret[arr1.length + i] = arr2[i];

        return ret;
    }

    public static byte[] appendCopy(byte[] arr1, byte[] arr2)
    {
        byte[] ret = Arrays.copyOf(arr1, arr1.length + arr2.length);

        for (int i = 0; i < arr2.length; ++i)
            ret[arr1.length + i] = arr2[i];

        return ret;
    }
}
