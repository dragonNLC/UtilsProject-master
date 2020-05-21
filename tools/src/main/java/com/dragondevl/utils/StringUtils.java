package com.dragondevl.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

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

    //获取头部index
    public static String coverSequence(String input) {
        String output;
        String tailString = tailoringExtensionName(input);
        String[] name = tailString.split("-");
        if (name.length > 1) {
            int endTagLength = name[0].length();
            output = tailString.substring(0, endTagLength);
            try {
                Integer.valueOf(output);
            } catch (Exception e) {
                e.printStackTrace();
                output = tailString;
            }
        } else {
            output = tailString;
        }
        return output;
    }

    public static String autoSplitText(final TextView tv) {
        final String rawText = "缩进" + tv.getText().toString().replace("\n", "\n缩进"); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isSpace(String str) {
        return !isEmpty(str) && str.trim().length() == 0;
    }

}
