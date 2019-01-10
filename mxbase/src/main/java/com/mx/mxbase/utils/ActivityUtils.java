package com.mx.mxbase.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class ActivityUtils {
    /**
     *
     * @Description: TODO 判断应用是否在运行
     * @param context 上下文
     * @param intent intent携带activity
     * @return boolean true为在运行，false为已结束
     */
    public static boolean isRuning(Context context, Intent intent) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks.size() > 0 && tasks.get(0).baseActivity.equals(intent.getComponent())) {
            return true;
        } else {
            return false;
        }
    }
    /**
     *
     * @Description: TODO 判断activity是否在应用的最顶层
     * @param context 上下文
     * @param intent intent携带activity
     * @return boolean true为在最顶层，false为否
     */
    public static boolean isTop(Context context, Intent intent) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(1);
        if (appTask.size() > 0 && appTask.get(0).topActivity.equals(intent.getComponent())) {
            return true;
        } else {
            return false;
        }
    }
}
