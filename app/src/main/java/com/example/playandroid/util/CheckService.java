package com.example.playandroid.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class CheckService {
    public static boolean isRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) return false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(30);
        Log.d("TAG", "isRunning: " + runningServices.size());
        for (int i = 0; i < runningServices.size(); i ++) {
            Log.d("TAG", "isRunning: " + runningServices.get(i).service.getClassName());
            if (runningServices.get(i).service.getClassName().equals(serviceName)) return true;
        }
        return false;
    }
}
