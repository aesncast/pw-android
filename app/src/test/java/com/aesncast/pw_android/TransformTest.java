package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.math.BigInteger;

public class TransformTest {
    @Test
    public void to_int_conforms_to_pwpy() {
        assertEquals(new BigInteger("0"), Transform.to_int(""));
        assertEquals(new BigInteger("6513249"), Transform.to_int("abc"));
        assertEquals(new BigInteger("3291835376408573590478209986637364656599265025014012802863049622424083630783948306431999498413285667939592978357630573418285899181951386474024455144309711"),
                     Transform.to_int(Transform.sha512("")));
    }

    @Test
    public void seed_conforms_to_pwpy() {
        assertEquals(0L, Transform.seed(0, 0, 0));
        assertEquals(1L, Transform.seed(0, 0, 1));
        assertEquals(1L, Transform.seed(0, 0, 2));
        assertEquals(3L, Transform.seed(0, 0, 3));
        assertEquals(3L, Transform.seed(0, 0, 4));
        assertEquals(50494L, Transform.seed(0, 0, 65535));
        assertEquals(17611L, Transform.seed(1, 0, 65535));
        assertEquals(7412L,  Transform.seed(2, 0, 65535));
        assertEquals(31190L, Transform.seed(3, 0, 65535));
        assertEquals(30939L, Transform.seed(4, 0, 65535));
        assertEquals(59569L, Transform.seed("", 0, 65535));
        assertEquals(60319L, Transform.seed("abc", 0, 65535));
        assertEquals(46370L, Transform.seed("hello", 0, 65535));
        assertEquals(53137L, Transform.seed("hello world", 0, 65535));
    }

    @Test
    public void append_conforms_to_pwpy()
    {
        assertEquals("", Transform.append("", ""));
        assertEquals("a", Transform.append("a", ""));
        assertEquals("a", Transform.append("", "a"));
        assertEquals("ab", Transform.append("a", "b"));
        assertEquals("helloworld", Transform.append("hello", "world"));
    }

    @Test
    public void prepend_conforms_to_pwpy()
    {
        assertEquals("", Transform.prepend("", ""));
        assertEquals("a", Transform.prepend("a", ""));
        assertEquals("a", Transform.prepend("", "a"));
        assertEquals("ba", Transform.prepend("a", "b"));
        assertEquals("worldhello", Transform.prepend("hello", "world"));
    }

    @Test
    public void cut_conforms_to_pwpy()
    {
        assertEquals("", Transform.cut("", 0, 0));
        assertEquals("", Transform.cut("a", 0, 0));
        assertEquals("", Transform.cut("hello world", 0, 0));
        assertEquals("hello", Transform.cut("hello world", 0, 5));
        assertEquals("world", Transform.cut("hello world", 6, 11));
        assertEquals("world", Transform.cut("hello world", 6, 200));
        assertEquals("hello world", Transform.cut("hello world", -200, 200));
        assertEquals("", Transform.cut("hello world", 200, -200));
    }

    @Test
    public void replace_conforms_to_pwpy()
    {
        assertEquals("", Transform.replace("", "", ""));
        assertEquals("", Transform.replace("a", "a", ""));
        assertEquals("b", Transform.replace("a", "a", "b"));
        assertEquals("hello", Transform.replace("henno", "n", "l"));
        assertEquals("heabcabco worabcd", Transform.replace("hello world", "l", "abc"));
    }

    @Test
    public void replace_at_conforms_to_pwpy()
    {
        assertEquals("", Transform.replace_at("", 0, 'a'));
        assertEquals("b", Transform.replace_at("a", 0, 'b'));
        assertEquals("ab1", Transform.replace_at("abc", 200, '1'));
        assertEquals("1bc", Transform.replace_at("abc", -200, '1'));
        assertEquals("hello1world", Transform.replace_at("hello world", 5, '1'));
    }

    @Test
    public void insert_conforms_to_pwpy()
    {
        assertEquals("", Transform.insert("", 0, ""));
        assertEquals("a", Transform.insert("", 0, "a"));
        assertEquals("a", Transform.insert("a", 0, ""));
        assertEquals("ab", Transform.insert("a", 1, "b"));
        assertEquals("ba", Transform.insert("a", 0, "b"));
        assertEquals("ab", Transform.insert("a", 200, "b"));
        assertEquals("ba", Transform.insert("a", -200, "b"));
        assertEquals("hello", Transform.insert("heo", 2, "ll"));
        assertEquals("hello world", Transform.insert("helld", 3, "lo wor"));
    }

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

    @Test
    public void diceware_conforms_to_pwpy()
    {
        assertEquals("movie extra beat chap", Transform.diceware("", 1, 4));
        assertEquals("solon cough vigil mew", Transform.diceware("abc", 1, 4));
        assertEquals("spiro keel dow", Transform.diceware("hello", 1, 4));
        assertEquals("aspen spoon 567 scrap", Transform.diceware("hello world", 1, 4));
        assertEquals("ajax toss", Transform.diceware("aspen spoon 567 scrap", 1, 4));

        assertEquals("movie extra beat chap", Transform.diceware("", 4, 4));
        assertEquals("solon cough vigil mew", Transform.diceware("abc", 4, 4));
        assertEquals("spiro keel dow curd", Transform.diceware("hello", 4, 4));
        assertEquals("aspen spoon 567 scrap", Transform.diceware("hello world", 4, 4));
        assertEquals("ajax toss gules filch", Transform.diceware("aspen spoon 567 scrap", 4, 4));
    }
}
