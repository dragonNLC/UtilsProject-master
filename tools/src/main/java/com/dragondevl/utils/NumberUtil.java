package com.dragondevl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/27.
 */

public class NumberUtil {

    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\+?-?[0-9]*\\.?[0-9]?");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
