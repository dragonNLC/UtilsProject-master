package com.dragondevl.clog;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by lb on 2018/12/6.
 * 日志打印，允许选择打印日志的级别，是否输出日志到文件，输出何种级别的日志到文件中
 */

public class CLog {

    public static boolean debug = true;
    private static boolean mWrite = false;
    static long time;
    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;

    public CLog() {
    }

    public static void clearCache() {
    }

    public static void time(String tag) {
        if (time == 0L) {
            time = System.currentTimeMillis();
        } else {
            d(tag + " cost: " + (System.currentTimeMillis() - time));
            time = 0L;
        }
    }

    public static void v() {
        v(null);
    }

    public static void d() {
        d(null);
    }

    public static void i() {
        i(null);
    }

    public static void w() {
        w(null);
    }

    public static void e() {
        e(null);
    }

    public static void v(Object message) {
        v(null, message);
    }

    public static void d(Object message) {
        d(null, message);
    }

    public static void i(Object message) {
        i(null, message);
    }

    public static void w(Object message) {
        w(null, message);
    }

    public static void e(Object message) {
        e(null, message);
    }

    public static void v(String tag, Object message) {
        pLog(1, tag, message);
    }

    public static void d(String tag, Object message) {
        pLog(2, tag, message);
    }

    public static void i(String tag, Object message) {
        pLog(3, tag, message);
    }

    public static void w(String tag, Object message) {
        pLog(4, tag, message);
    }

    public static void e(String tag, Object message) {
        pLog(5, tag, message);
    }

    public static void pLog(int type, String tagStr, Object obj) {
        if (debug) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int index = 5;
            String className = stackTrace[index].getFileName();
            String methodName = stackTrace[index].getMethodName();
            int lineNumber = stackTrace[index].getLineNumber();
            String tag = tagStr == null ? CLog.class.getSimpleName() : tagStr;
            methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[ (").append(className).append(":").append(lineNumber)
                    .append(")#").append(methodName).append(" ] ");
            String msg;
            if (obj == null) {
                msg = "Log with null Object";
            } else {
                msg = obj.toString();
            }

            if (msg != null) {
                stringBuilder.append(msg);
            }

            String logStr = stringBuilder.toString();
            switch (type) {
                case 1:
                    Log.v(tag, logStr);
                    break;
                case 2:
                    Log.d(tag, logStr);
                    break;
                case 3:
                    Log.i(tag, logStr);
                    break;
                case 4:
                    Log.w(tag, logStr);
                    break;
                case 5:
                    Log.e(tag, logStr);
            }
        }
    }

}
