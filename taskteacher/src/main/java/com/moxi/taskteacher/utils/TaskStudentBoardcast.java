package com.moxi.taskteacher.utils;

import android.app.ActivityManager;

import com.mx.mxbase.constant.HomeKeyEventBrodcast;

/**
 * Created by Administrator on 2017/1/4.
 */
public class TaskStudentBoardcast extends HomeKeyEventBrodcast{
    @Override
    public void closeApp(ActivityManager mActivityManager) {
        DownloadAsy.getInstance().clearAllAsy();
        try {
            mActivityManager.killBackgroundProcesses("com.moxi.taskteacher");
        }catch (Exception e){}
    }
}
