package com.moxi.classRoom.serve;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mx.mxbase.constant.APPLog;

/**
 * Created by Administrator on 2016/9/9.
 */
public class BootReceiver extends BroadcastReceiver {
public static final String NAME="com.moxi.classRoom.serve.BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NAME)) {
            //在这里写重新启动service的相关操作
            //注册后台下载服务
            Intent i = new Intent(context, MainService.class);
            context.startService(i);
        }else{
            startService(context);
        }
    }
    private void startService(Context context){
        boolean isServiceRunning = false;
        //检查Service状态
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            APPLog.e(service.service.getClassName());
            if ("com.moxi.classRoom.serve".equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }
        if (!isServiceRunning) {
            APPLog.e("是否需要重启服务", isServiceRunning);
            Intent i = new Intent(context, MainService.class);
            context.startService(i);
        }
    }
}
