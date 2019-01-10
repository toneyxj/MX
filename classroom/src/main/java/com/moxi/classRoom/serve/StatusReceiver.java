package com.moxi.classRoom.serve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moxi.classRoom.config.StatusInformation;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;

/**
 * Created by Administrator on 2016/10/26.
 */
public class StatusReceiver extends BroadcastReceiver{
    public static final String STATUS_RECEIVER="com.moxi.classRoom.serve.StatusReceiver";
    private static StatusListener listener;

    public static void setListener(StatusListener listener) {
        StatusReceiver.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(STATUS_RECEIVER)){
            int status=intent.getIntExtra("status",0);
            APPLog.e("status="+status);
            StatusInformation.getInstance().setStatus(status);
            if (listener!=null){
                if (status==-2||status==-3){
                    //打开提示
                    ToastUtils.getInstance().showToastLong("服务器链接异常请练习管理人员");
                }
                listener.onStatus(status);
            }
        }
    }
    public interface StatusListener{
        public void onStatus(int status);
    }
}
