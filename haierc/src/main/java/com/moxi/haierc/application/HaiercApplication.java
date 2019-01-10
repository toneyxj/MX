package com.moxi.haierc.application;

import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.constant.APPLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Archer on 16/10/26.
 */
public class HaiercApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "284f24afd7", APPLog.is);
    }

}
