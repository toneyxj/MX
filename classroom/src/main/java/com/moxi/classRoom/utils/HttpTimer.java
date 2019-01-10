package com.moxi.classRoom.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * http定时请求类
 * Created by Administrator on 2016/11/7.
 */
public class HttpTimer {
    /**
     * 计时器请求时间
     */
    private int timerNumber = 3000;
    /**
     * 计时器
     */
    private Timer timer;

    private Context context;
    private List<ReuestKeyValues> list;
    private RequestCallBack back;
    private String code;
    private String url;
    private boolean showHide;
    private boolean isGet;

    /**
     * http定时请求
     * @param context 上下文
     * @param list 数据请求集合
     * @param back 请求回调接口
     * @param code 请求唯一标示
     * @param url 请求路径
     * @param showHide 是否显示请求结果
     * @param isGet 是否是get请求
     */
    public HttpTimer(Context context, List<ReuestKeyValues> list, RequestCallBack back, String code, String url, boolean showHide, boolean isGet) {
        this.context = context;
        this.list = list;
        this.back = back;
        this.code = code;
        this.url = url;
        this.showHide = showHide;
        this.isGet = isGet;
        startTimer();
    }
    /**
     * http定时请求 ,默认get请求
     * @param context 上下文
     * @param list 数据请求集合
     * @param back 请求回调接口
     * @param code 请求唯一标示
     * @param url 请求路径
     */
    public HttpTimer(Context context, List<ReuestKeyValues> list, RequestCallBack back, String code, String url) {
        this.context = context;
        this.list = list;
        this.back = back;
        this.code = code;
        this.url = url;
        this.showHide = false;
        this.isGet = true;
        startTimer();
    }

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<HttpTimer> reference;

        public MyHandler(HttpTimer context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            HttpTimer activity = (HttpTimer) reference.get();
            if (activity != null&&activity.timer!=null) {
                activity.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message msg) {
        requestData();
    }

    private void requestData() {
        if (isGet) {
            new HttpGetRequest(context, back, list, code, url, showHide).execute();
        } else {
            new HttpPostRequest(context, back, list, code, url, showHide).execute();
        }
    }

    /**
     * 设置计时器的计时值
     *
     * @param timerNumber 单位秒
     */
    public void setTimerNumber(int timerNumber) {
        this.timerNumber = timerNumber * 1000;
        stopTimer();
        startTimer();
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // 定义一个消息传过去
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 100, timerNumber);
    }

    /**
     * 停止计时发送请求
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
