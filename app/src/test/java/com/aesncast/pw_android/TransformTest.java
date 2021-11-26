package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import com.aesncast.pw_android.util.PyRandom;

import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class TransformTest {
    @Test
    public void to_int_conforms_to_pwpy() {
        assertEquals(new BigInteger("0"), Transform.to_int(""));
        assertEquals(new BigInteger("6513249"), Transform.to_int("abc"));
        assertEquals(new BigInteger("3291835376408573590478209986637364656599265025014012802863049622424083630783948306431999498413285667939592978357630573418285899181951386474024455144309711"),
                     Transform.to_int(Transform.sha512("")));
    }

    // seed is tested in PyRandomTest

    @Test
    public void make_unambiguous_conforms_to_pwpy() {
        assertEquals("", Transform.make_unambiguous(""));
        assertEquals("hewwf wfrwd", Transform.make_unambiguous("hello world"));
        assertEquals("abcdefghcxknmncpqrssuvwxyz", Transform.make_unambiguous("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("5S4rnaxNWwnxssku8uzsagdEphkAghWUF4shwwaXKMsubyfAxg4ybUeuXVWS9HdNcEypmeXn8FeGwnD4wkrn9Dep", Transform.make_unambiguous("5S4rnaTNWonxss1u8LzsaJdEph1AJhWUF4sh2waXKMsutyfAxg4ybUeuXVWS9HdNcEypmeXn8FZGonD4w1rj9DZp"));
    }

    @Test
    public void add_special_characters_conforms_to_pwpy() {
        assertEquals("", Transform.add_some_simple_special_characters(""));
        assertEquals(":abc", Transform.add_some_simple_special_characters("abc"));
        assertEquals("h:ello", Transform.add_some_simple_special_characters("hello"));
        assertEquals("h#ello world", Transform.add_some_simple_special_characters("hello world"));
        assertEquals("aspen spoon 567# scrap", Transform.add_some_simple_special_characters("aspen spoon 567 scrap"));
    }

    @Test
    public void capitalize_some_conforms_to_pwpy() {
        assertEquals("", Transform.capitalize_some(""));
        assertEquals("Abc", Transform.capitalize_some("abc"));
        assertEquals("Hello", Transform.capitalize_some("hello"));
        assertEquals("Hello World", Transform.capitalize_some("hello world"));
        assertEquals("aspen Spoon 567 scrap", Transform.capitalize_some("aspen spoon 567 scrap"));
    }
}
