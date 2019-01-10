package com.moxi.CPortTeacher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.handwritinglibs.WritePadBaseView;
import com.mx.mxbase.constant.APPLog;
import com.squareup.otto.Subscribe;


/**
 * 自定义绘制
 * Created by Administrator on 2016/8/5.
 */
public class PaintInvalidateRectView extends WritePadBaseView {
    public static final String pakegeName = "PaintInvalidateRectView";
    private String code;
    private boolean isNoHiden = true;

//    private boolean preventClearScreen=false;

    public PaintInvalidateRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    public PaintInvalidateRectView(Context context) {
        super(context);
        initPaintView();
    }

    /**
     * @param context
     * @param code    唯一标示进入app
     */
    public PaintInvalidateRectView(Context context, String code) {
        super(context);
        initPaintView();
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 保存清除线
     */
    private void initPaintView() {
        CPortApplication.getBus().register(this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onResume();
//                onPause();
//            }
//        },200);
    }
//    /**
//     * 是否阻止清屏操作
//     * @param preventClearScreen false为不阻止清屏操作
//     */
//    public void setPreventClearScreen(boolean preventClearScreen) {
//        this.preventClearScreen = preventClearScreen;
//    }

    /**
     * 设置绘制风格
     *
     * @param been 0 绘制，1为擦除
     * @return
     */
    @Subscribe
    public boolean setPaint(OttoBeen been) {
        if (been.code.equals("onBackPressed")){
            setleaveScribbleMode();
            return false;
        }
        String code = been.code;
        if (null == code || !code.equals(PaintInvalidateRectView.pakegeName)) return false;
        if (been.style == null) return false;

        int style = (int) been.style;
        setPaint(style);
        return true;
    }

    /**
     * 设置绘制风格
     *
     * @param been 0 绘制，1为擦除
     * @return
     */
    @Subscribe
    public void setisHiden(OttoBeen been) {
        String codes = been.code;
        APPLog.e(codes);
        if (null == codes || !codes.equals(this.code)) return;
        isNoHiden =  been.is;
        if (!isNoHiden){
            mHandler.removeCallbacksAndMessages(null);
            setleaveScribbleMode();
        }
        APPLog.e(this.code,isNoHiden);
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


    /**
     * 清屏
     */
    @Subscribe
    public void clearScreenP(OttoBeen been) {
        String code = been.code;
        if (null == code || !code.equals(PaintInvalidateRectView.pakegeName + "clear")) return;
        clearScreenp();
        APPLog.e("clearScreenP"+this.code,isNoHiden);
    }

    public void clearScreenp() {
        if (!isNoHiden||getVisibility() != VISIBLE) return;
        super.clearScreen();
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
        CPortApplication.getBus().unregister(this);
    }
}