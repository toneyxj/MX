package com.moxi.CPortTeacher.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 10毫秒计数器
 * Created by Administrator on 2016/11/12.
 */
public class MsecTimer {

    private int startTime;
    private int currentTime=0;
    /**
     * 计时器
     */
    private Timer timer;

    /**
     * 记时回调接口
     */
    private TimeListener listener;
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<MsecTimer> reference;

        public MyHandler(MsecTimer context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MsecTimer activity = (MsecTimer) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }

    }

    public void handleMessage(Message msg) {
            if (listener!=null)
                listener.cuttentTime(currentTime);
    }

    /**
     * 时间控件初始化
     *
     * @param startTime 开始时间
     */
    public MsecTimer( int startTime,TimeListener listener) {
        this.startTime = currentTime=startTime;
        this.listener = listener;
    }

    /**

     * 开始计时
     */
    public void startTimer() {
        currentTime=startTime;
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask timerTask = new TimerTask() {
            // 倒数10毫秒
            @Override
            public void run() {
                // 定义一个消息传过去
                Message msg = new Message();
                    msg.what = currentTime++;
                handler.sendMessage(msg);
            }
        };
        if (listener!=null)
            listener.cuttentTime(currentTime);
        timer.schedule(timerTask, 1000, 1000);
    }

    /**
     * 停止计时
     */
    public void stopTimer() {
        if (timer!=null) {
            timer.cancel();
            timer = null;
        }
    }

    public interface TimeListener {
        /**
         * 当前时间
         *
         * @param time 显示时间
         */
        public void cuttentTime(int time);
    }

}
