package com.moxi.classRoom.serve;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mx.mxbase.constant.APPLog;

/**
 * Created by Administrator on 2016/10/25.
 */
public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            startService(context);
        }
    }
    private void startService(Context context){
        boolean isServiceRunning = false;
        //检查Service状态
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            APPLog.e(service.service.getClassName());
            if ("com.moxi.classRoom.serve.MainService".equals(service.service.getClassName())) {
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
