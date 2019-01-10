package com.moxi.CPortTeacher.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2016/10/26.
 */
public class TheardBus extends Bus {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //直接通过反射调用
            super.post(event);
        } else {
            //通过handler把异步任务发送到UI线程，然后再反射调用
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TheardBus.super.post(event);
                }
            });
        }
    }
}
