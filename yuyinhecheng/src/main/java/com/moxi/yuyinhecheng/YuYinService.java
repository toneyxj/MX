package com.moxi.yuyinhecheng;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.moxi.yuyinhecheng.YuYinmanager.MsgConfig;
import com.moxi.yuyinhecheng.listener.SpeekListener;

/**
 * Created by xiajun on 2019/5/10.
 */

public class YuYinService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private YuYinUtils yuYinUtils;
    private Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();
        yuYinUtils = new YuYinUtils(this, new SpeekListener() {
            @Override
            public void onSpeekOver() {
                sendBroadcast(new Intent(MsgConfig.speek_send_over));
            }

            @Override
            public void onSpeekStart() {
                sendBroadcast(new Intent(MsgConfig.speek_send_start));
            }

            @Override
            public void onSpeekError(Exception e) {
                Intent intent=new Intent(MsgConfig.speek_send_error);
                intent.putExtra("error",e.getMessage());
                sendBroadcast(intent);
            }
        });
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    private void showToast(String msg) {
        toast.setText(msg);
        toast.show();
    }

    //最好换成HandlerThread的形式
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgfromClient) {
            switch (msgfromClient.what) {
                case MsgConfig.sendMsg:
                    yuYinUtils.speak(msgfromClient.getData().getString("data"));
                    break;
                case MsgConfig.speekStop:
                    yuYinUtils.stop();
                    break;
            }

            super.handleMessage(msgfromClient);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        yuYinUtils.onDestroy();
    }
}
