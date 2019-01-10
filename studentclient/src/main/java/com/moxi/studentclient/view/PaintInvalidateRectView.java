package com.moxi.studentclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;

import com.moxi.handwritinglibs.WritePadBaseView;


/**
 * Created by Administrator on 2016/8/5.
 */
public class PaintInvalidateRectView extends WritePadBaseView {
    public static final String pakegeName = "PaintInvalidateRectView";

    private boolean preventClearScreen = false;

    public PaintInvalidateRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    public PaintInvalidateRectView(Context context) {
        super(context);
        initPaintView();
    }

    /**
     * 保存清除线
     */
    private void initPaintView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();
                onPause();
            }
        },200);
    }
    public void initBitmap(Bitmap bitmap){
        setBitmap(bitmap);
    }

    /**
     * 是否阻止清屏操作
     *
     * @param preventClearScreen false为不阻止清屏操作
     */
    public void setPreventClearScreen(boolean preventClearScreen) {
        this.preventClearScreen = preventClearScreen;
    }

    /**
     * 设置
     *
     * @param style 0:笔，1:橡皮插
     * @return
     */
    public boolean setPaint(int style) {
        setDrawIndex(style);
        return true;
    }


    public void clearScreenp() {
        if (!preventClearScreen) {
            super.clearScreen();
        }
    }

    /**
     * 获得当前的绘制图片
     *
     * @return
     */
    public Bitmap CurrentBitmap() {
        return getBitmap();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}