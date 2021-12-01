package com.aesncast.pw_android;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PwfileTest {
    @Test
    public void pwfile_parse_test() {
        String input = String.join("\n", new String[]{
            "# auto generated pwlist4 file containing sequences, domains and users.",
            "# feel free to edit / add but formatting and comments will be lost.",
            "",
            ":",
            "     - LEGACY2",
            "",
            "github.com:",
            "    aesncast - good_password",
            "",
            "# Sequences",
            "# don't change, only copy & make new ones to be safe,",
            "# otherwise you risk losing passwords if you forget",
            "# the sequences.",
            "[+good_password]",
            "    init($key, $domain, $user)",
            "    diceware(4, 4)",
            "    capitalize_some()",
            "    add_some_simple_special_characters()",
            ""
        });

        Pwfile f = PwfileParser.parsePwlist4(input);

        assertEquals(input, f.toString());
    }
}
