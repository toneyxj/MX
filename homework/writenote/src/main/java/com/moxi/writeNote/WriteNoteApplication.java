package com.moxi.writeNote;

import com.mx.mxbase.base.MyApplication;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2017/2/16.
 */
public class WriteNoteApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "f4f63df370", true);
    }
}
