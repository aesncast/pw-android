package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.function.Consumer;

public class CompatibilityTest {
    @Test
    public void bad_legacy1_test()
    {
        assertEquals("56wHhmaODaDwK2K9BPRf\n", Transform.bad_legacy1("", "", ""));
        assertEquals("muanOrnvE48mmYEgkG0v\n", Transform.bad_legacy1("", "a", "a"));
        assertEquals("Z4OjHqv2jMwGYPk1wIJi\n", Transform.bad_legacy1("", "a", "b"));
        assertEquals("xTxqrE7jaNpabaXDlUWd\n", Transform.bad_legacy1("", "b", "b"));
        assertEquals("Z1xnYm11s8etfKyjZqKd\n", Transform.bad_legacy1("", "c", "c"));
        assertEquals("CgVRIX6ldLbfKkqVrZYj\n", Transform.bad_legacy1("", "abc", "abc"));
        assertEquals("rsuF1zJBBwGExgRhDJ1Z\n", Transform.bad_legacy1("", "abc", "ghi"));
        assertEquals("k9Dz6RwfbTTZtxnJjcEs\n", Transform.bad_legacy1("", "key", "domain"));
        assertEquals("1rWRXEYFe8sAX0b2Qz32\n", Transform.bad_legacy1("", "1", "1"));
        assertEquals("ZzrusIz7sAuR5ePGC1uj\n", Transform.bad_legacy1("", "1", "2"));
        assertEquals("WiPpahIxLEFVUBPtKltA\n", Transform.bad_legacy1("", "4", "5"));
        assertEquals("E5OsgOaaiZHOpKg8aGq5\n", Transform.bad_legacy1("", "7", "8"));
        assertEquals("rHI2ilhqGMGQiDk1c84D\n", Transform.bad_legacy1("", "0", "0"));
        assertEquals("3IC9zV0iNYUkJO73PN9x\n", Transform.bad_legacy1("", "hello", "world"));
    }

    @Test
    public void bad_legacy2_test()
    {
        assertEquals("6Xucw!MHuL?Gaa8 x9sR\n", Transform.bad_legacy2("", "", "", ""));
        assertEquals("AMSy4DCEWBPMvJE53pRh\n", Transform.bad_legacy2("", "a", "a", "a"));
        assertEquals("nXuBC482PBPFxxaxbgPK\n", Transform.bad_legacy2("", "a", "b", "c"));
        assertEquals("Za7ddHuw8zmyGthzNTrN\n", Transform.bad_legacy2("", "b", "b", "b"));
        assertEquals("yTPv4xEWdqPRrhtVX2GP\n", Transform.bad_legacy2("", "c", "c", "c"));
        assertEquals("BgKch!YutD?MhjB Q4uc\n", Transform.bad_legacy2("", "abc", "abc", "abc"));
        assertEquals("EVQJY4;M8pYr!e7PNd?P\n", Transform.bad_legacy2("", "abc", "ghi", "def"));
        assertEquals("GwsBsNnW5j6nhMrRh3E5\n", Transform.bad_legacy2("", "key", "domain", "user"));
        assertEquals("25zhGBX8Uf,xTapMVSGf\n", Transform.bad_legacy2("", "1", "1", "1"));
        assertEquals("dBr3qrjjNy,NcpQ4hWpg\n", Transform.bad_legacy2("", "1", "2", "3"));
        assertEquals("Kf6aB!fRun?UEDL bC8U\n", Transform.bad_legacy2("", "4", "5", "6"));
        assertEquals("QWk2dXNKNr,bgPndUpEQ\n", Transform.bad_legacy2("", "7", "8", "9"));
        assertEquals("PTjy364VzXQ5rrfhCYvx\n", Transform.bad_legacy2("", "0", "0", "0"));
        assertEquals("fqCCvPrSKQyvTuEFK4XG\n", Transform.bad_legacy2("", "hello", "world", "!"));
    }

    private static String good_password(String key, String domain, String user)
    {
        return Transform.add_some_simple_special_characters(Transform.capitalize_some(Transform.diceware(Transform.init("", key, domain, user), 4, 4)));
    }

    @Test
    public void good_password_test()
    {
        assertEquals("Treat Go&p Tug 5th", good_password("", "", ""));
        assertEquals("leona Asset shar%k utter", good_password( "a", "a", "a"));
        assertEquals("Owens Farad: 6000 Doze_n", good_password( "a", "b", "c"));
        assertEquals("jkl Zing? Chomp phyla]", good_password( "b", "b", "b"));
        assertEquals("Byway Flow vying Subtl=y", good_password( "c", "c", "c"));
        assertEquals("80 #Tone Prom D:rank", good_password( "abc", "abc", "abc"));
        assertEquals("Scald+ qh knurl Braun", good_password( "abc", "ghi", "def"));
        assertEquals("ga_ur fin=e des Wac", good_password( "key", "domain", "user"));
        assertEquals("Hast da.da scol&d Fuel", good_password( "1", "1", "1"));
        assertEquals("Suave t_ube sworn duet", good_password( "1", "2", "3"));
        assertEquals("a]rcana lease= Knapp rare", good_password( "4", "5", "6"));
        assertEquals("Lsi Po*et Spice Sylow", good_password( "7", "8", "9"));
        assertEquals("Aye Seno_r Bi #taft", good_password( "0", "0", "0"));
        assertEquals("junk Zc fl=eck hat", good_password( "hello", "world", "!"));
    }
}
