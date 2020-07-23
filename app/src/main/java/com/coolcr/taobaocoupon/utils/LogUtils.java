package com.coolcr.taobaocoupon.utils;

import android.util.Log;

public class LogUtils {
    // 当前等级
    private static int currentLev = 4;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 4;
    private static final int WARNING_LEV = 4;
    private static final int ERROR_LEV = 4;

    public static void d(Class clazz, String msg) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void i(Class clazz, String msg) {
        if (currentLev >= INFO_LEV) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void w(Class clazz, String msg) {
        if (currentLev >= WARNING_LEV) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void e(Class clazz, String msg) {
        if (currentLev >= ERROR_LEV) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }
}
