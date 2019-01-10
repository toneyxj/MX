package com.moxi.assistbooklist;

import com.mx.mxbase.base.MyApplication;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by xj on 2017/9/12.
 */

public class AssisitApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "92e7b74741", true);
    }
}
