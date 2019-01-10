package com.moxi.CPortTeacher.utils;

import android.app.ActivityManager;

import com.mx.mxbase.constant.HomeKeyEventBrodcast;

/**
 * Created by Administrator on 2016/12/2.
 */
public class TeacherCloseBoardCast extends HomeKeyEventBrodcast {
    @Override
    public void closeApp(ActivityManager mActivityManager) {
        DownloadAsy.getInstance().clearAllAsy();
        try {
            mActivityManager.killBackgroundProcesses("com.moxi.CPortTeacher");
        }catch (Exception e){}
        //关闭程序
//        CPortApplication.getBus().post(new OttoBeen("","CoursewareWhiteBoardFragment"));
    }
}
