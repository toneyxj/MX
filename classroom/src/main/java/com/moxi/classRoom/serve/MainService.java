package com.moxi.classRoom.serve;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.moxi.classRoom.connection.PushClient;

import java.lang.ref.WeakReference;

/**
 * 主后台服务
 * Created by Administrator on 2016/10/24.
 */
public class MainService extends Service {
    public static boolean isToDo = true;

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<MainService> reference;

        public MyHandler(MainService context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MainService activity = (MainService) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message msg) {
        startSocekt();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        flags = START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startSocekt();
    }

    private void startSocekt() {
        if (!PushClient.isOpen() && isToDo) {
            //启动与服务器的链接
            PushClient.start(null);
        }
        handler.sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(BootReceiver.NAME);
        sendBroadcast(intent);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
