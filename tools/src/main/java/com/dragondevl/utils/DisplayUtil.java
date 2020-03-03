package com.dragondevl.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.dragondevl.clog.CLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lb on 2019/1/22.
 */

public class DisplayUtil {

    public static final int DEFAULT_LIGHT = 255;//默认屏幕亮度最大比例
    public static final int DEFAULT_CUT_LIGHT = 50;//设置屏幕亮度比例

    //获取屏幕亮度
    public static float getLight(Activity context) {
        if (context != null) {
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            return lp.screenBrightness;
        } else {
            return 0f;
        }
    }

    //设置屏幕亮度
    public static void setLight(Activity activity, float brightness) {
        if (activity != null) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = brightness * (1f / 255f);
            activity.getWindow().setAttributes(lp);
        }
    }

    //拿到存储有屏幕参数的对象（获取到的是系统的）
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    //拿到存储有屏幕参数的对象（获取到的是APP自己的）
    public static DisplayMetrics getRealMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (wm != null) {
                wm.getDefaultDisplay().getRealMetrics(dm);
            }
        }
        return dm;
    }

    //打印屏幕参数
    public static DisplayMetrics printDisplayInfo(DisplayMetrics dm) {
        StringBuilder sb = new StringBuilder();
        sb.append("---- 屏幕显示属性 ----");
        sb.append("\ndensity        :").append(dm.density);
        sb.append("\ndensityDpi     :").append(dm.densityDpi);
        sb.append("\nheightPixels   :").append(dm.heightPixels);
        sb.append("\nwidthPixels    :").append(dm.widthPixels);
        sb.append("\nscaledDensity  :").append(dm.scaledDensity);
        sb.append("\nxdpi           :").append(dm.xdpi);
        sb.append("\nydpi           :").append(dm.ydpi);
        CLog.i("ContentValue", sb.toString());
        return dm;
    }

    //dip转换成px
    public static int dip2px(Context context, float dpValue) {
        float scale = getRealMetrics(context).density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px和dip互相转换的比例density
    //px转换成dip
    public static int px2dip(Context context, float pxValue) {
        float scale = getRealMetrics(context).density;
        return (int) (pxValue / scale + 0.5f);
    }

    //sp和px互相转换的比例scaledDensity
    //sp转换成px
    public static int sp2px(Context context, float spValue) {
        float fontScale = getRealMetrics(context).scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //px转换成sp
    public static int px2sp(Context context, float pxValue) {
        float fontScale = getRealMetrics(context).scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //获取系统宽度数据
    public static int getSystemSWPixels(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    //获取系统高度数据
    public static int getSystemSHPixels(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    //获取APP真实高度数据
    public static int getScreenRealSHPixels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getSystemSHPixels(context);
        } else {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display ds = wm.getDefaultDisplay();
                DisplayMetrics dm = new DisplayMetrics();
                try {
                    Method method = Class.forName("android.view.Display").
                            getMethod("getRealMetrics", DisplayMetrics.class);
                    method.invoke(ds, dm);
                    return dm.heightPixels;
                } catch (Exception e) {
                    e.printStackTrace();
                    return getSystemSHPixels(context);
                }
            }
            return getSystemSHPixels(context);
        }
    }

    //获得状态栏高度数据
    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight(Context context) {
        int statusBarSH = 0;
        Class<?> c = null;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(object).toString());
            statusBarSH = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarSH;
    }

    //获取虚拟导航栏高度
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(identifier);
    }

    //转换dip成实际密度区间文件表示
    public static String getCoverDensity(Context context) {
        String coverDensity = "";
        int density = getDisplayMetrics(context).densityDpi;
        if (density > 0 && density <= 120) {
            coverDensity = "LDPI";
        } else if (density > 120 && density <= 160) {
            coverDensity = "MDPI";
        } else if (density > 160 && density <= 213) {
            coverDensity = "TVDPI";
        } else if (density > 213 && density <= 240) {
            coverDensity = "HDPI";
        } else if (density > 240 && density <= 320) {
            coverDensity = "HDPI";
        } else if (density > 320 && density <= 400) {
            coverDensity = "XMHDPI";
        } else if (density > 400 && density <= 480) {
            coverDensity = "XXHDPI";
        } else if (density > 480 && density <= 640) {
            coverDensity = "XXXHDPI";
        } else {
            coverDensity = "UNKNOW";
        }
        return coverDensity;
    }

}
