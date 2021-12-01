package com.aesncast.PwCore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {
    public static String[] multisplit(String s, String... delims)
    {
        if (delims.length <= 0)
            return new String[]{s};

        String[] r = s.split(delims[0]);
        List<String> ret = new ArrayList<>();

        for (String sp : r)
        {
            String[] tmp = multisplit(sp, Arrays.stream(delims).skip(1).toArray(String[]::new));

            for (String str : tmp)
                ret.add(str);
        }

        return ret.toArray(new String[0]);
    }
}
