package com.dragondevl.utils;

/**
 * Created by Administrator on 2017/11/27.
 */

public class NumberUtil {

    public static boolean isNumeric(String str) {
        if (str == null) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
