package com.moxi.studentb.application;

import android.content.Intent;

import com.moxi.studentb.MainActivity;
import com.mx.mxbase.base.BaseApplication;

/**
 * Created by Archer on 16/10/26.
 */
public class HaiercApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    class CrashHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, final Throwable ex) {
            ex.printStackTrace();
            Intent restartIntent = new Intent(HaiercApplication.this, MainActivity.class);
            restartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restartIntent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
