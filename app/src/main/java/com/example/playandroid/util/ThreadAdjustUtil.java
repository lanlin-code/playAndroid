package com.example.playandroid.util;


import android.os.Handler;

public class ThreadAdjustUtil {
    private static Handler mHandler;

    public static void initHandler() {
        mHandler = new Handler();
    }

    public static void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
