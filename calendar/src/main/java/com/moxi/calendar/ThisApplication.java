package com.moxi.calendar;

import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.constant.APPLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ThisApplication extends MyApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "b26e3c0e81", APPLog.is);
    }
}
