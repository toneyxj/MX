package com.moxi.yuyinhecheng;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.baidu.tts.client.TtsMode;
import com.moxi.yuyinhecheng.YuYinmanager.MsgConfig;
import com.moxi.yuyinhecheng.listener.SpeekListener;
import com.moxi.yuyinhecheng.util.NetWorkUtil;

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
        isfirst=true;
        if (yuYinUtils==null){
            startTTS();
        }
        return START_STICKY;
    }

    private YuYinUtils yuYinUtils;
    private Toast toast;
    private boolean isfirst;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            yuYinUtils.onDestroy();
            yuYinUtils.initialTts();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        startTTS();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }
    private void startTTS(){
        yuYinUtils = new YuYinUtils(this,speekListener );
    }
    private SpeekListener speekListener=new SpeekListener() {
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
            sendErrorMsg(e.getMessage());
        }

        @Override
        public void reStartYuYin() {
            //语音初始化失败重新配置语音
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1,4000);
        }

        @Override
        public void onStartSucess() {

        }
    };

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
                    if (yuYinUtils==null){
                        sendErrorMsg("语音系统初始化中");
                        startTTS();
                        return;
                    }
                    if (!yuYinUtils.isStartEnd()){
                        sendErrorMsg("语音系统初始化中");
                    }else {
                        yuYinUtils.speak(msgfromClient.getData().getString("data"));
                    }
                    break;
                case MsgConfig.speekStop:
                    if (yuYinUtils==null){
                        sendErrorMsg("语音系统初始化中");
//                        startTTS();
                        return;
                    }
                    if (!yuYinUtils.isStartEnd()){
                        sendErrorMsg("语音系统初始化中");
                    }else {
                        yuYinUtils.stop();
                    }
                    break;
            }

            super.handleMessage(msgfromClient);
        }
    });
    private synchronized void sendErrorMsg(String msg){
        if (yuYinUtils==null)return;
        Intent intent = new Intent(MsgConfig.speek_send_error);
        if (!NetWorkUtil.isNetworkConnected(this)) {
            msg = "请检查网络连接";
        }
        intent.putExtra("error", msg);
        sendBroadcast(intent);
        if (yuYinUtils.ttsMode==TtsMode.MIX){
            yuYinUtils.onDestroy();
            yuYinUtils=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (yuYinUtils!=null)
        yuYinUtils.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
