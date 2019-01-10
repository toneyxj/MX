package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 夏君 on 2016/7/5.
 */
public abstract class  BaseView extends View {
    private MyThread myThread;
    public int refureshTime=300;

    public void setRefureshTime(int refureshTime) {
        this.refureshTime = refureshTime;
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context) {
        super(context);
    }

    protected abstract void onDrawSub(Canvas canvas);//绘制图像
    protected abstract void logic();//逻辑方法  子类实现

    @Override
    protected void onDraw(Canvas canvas) {
        if(null==myThread){
            myThread=new MyThread();
            myThread.start();
        }else{
            onDrawSub(canvas);
        }
    }

    private boolean running=true;//控制循环

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    protected void onDetachedFromWindow() {
        running=false;//销毁View的时候设置成false,退出无限循环
        super.onDetachedFromWindow();
    }

    //开启一个子线程绘制ui
    private class MyThread extends Thread{
        @Override
        public void run() {
            while(running){
                logic();
                postInvalidate();//重新绘制,会调用onDraw
                try {
                    Thread.sleep(refureshTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
