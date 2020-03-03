package com.dragondevl.utils;

public class StringUtils {

    public static final String DEFAULT_CHARS = "0123456789ABCDEF";

    public static String bytes2HexStr(byte[] request) {
        if (request == null) {
            return null;
        }
        char[] chars = DEFAULT_CHARS.toCharArray();
        StringBuilder sb = new StringBuilder();
        int bit;
        for (byte b : request) {
            bit = (b & 0xf0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0xf0;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    //去除文件扩展名后的文字
    public static String tailoringExtensionName(String fileName) {
        String output = "";
        String[] name = fileName.split("\\.");
        if (name.length > 1) {
            int endTagLength = name[name.length - 1].length();
            output = fileName.substring(0, fileName.length() - endTagLength - 1);
        } else {
            output = fileName;
        }
        return output;
    }

}
