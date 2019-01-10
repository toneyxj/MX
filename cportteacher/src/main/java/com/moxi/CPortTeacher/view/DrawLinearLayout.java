package com.moxi.CPortTeacher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.mx.mxbase.constant.APPLog;
import com.squareup.otto.Subscribe;

/**
 * Created by Administrator on 2016/11/1.
 */
public class DrawLinearLayout extends LinearLayout{
    public static final String pakegeName="DrawLinearLayout";

    Paint drawLine=new Paint();
    Paint clearLine=new Paint();

    Path pathDraw=new Path();
    Path pathClear=new Path();
    private boolean isClearLine = false;

    public DrawLinearLayout(Context context) {
        super(context);
        init();
    }

    public DrawLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        CPortApplication.getBus().register(this);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        drawLine.setAntiAlias(false); // 去除锯齿
        drawLine.setStrokeWidth(5);
        drawLine.setStyle(Paint.Style.STROKE);
        drawLine.setColor(Color.BLUE);

        clearLine.setAntiAlias(false); // 擦除
        clearLine.setStrokeWidth(16);
        clearLine.setStyle(Paint.Style.STROKE);
        clearLine.setColor(Color.TRANSPARENT);
        Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        clearLine.setXfermode(xFermode);
    }
    private int paintStyle = 0;

    private int offest=0;

    /**
     * 设置绘制风格
     * @param been 0 绘制，1为擦除
     * @return
     */
    @Subscribe
    public boolean setPaint(OttoBeen been) {
        String code=been.code;
        if (null==code||!code.equals(pakegeName))return false;
        int style= (int) been.style;
        if (paintStyle == style) return false;
        paintStyle = style;
//        offest=getPaintSize();
        return true;
    }

    public Path getmPath() {
        int swi = paintStyle;
        if (isClearLine) swi = 1;
        switch (swi) {
            case 0:
                return pathDraw;
            case 1:
                return pathClear;
            default:
                break;
        }
        return pathDraw;
    }

    private int getPaintSize() {
        int swi = 0;
        if (isClearLine) swi = 1;
        switch (paintStyle) {
            case 0:
                swi = 3;
                break;
            case 1:
                swi = 16;
                break;
            default:
                break;
        }
        return swi;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(pathDraw, drawLine);
        canvas.drawPath(pathClear, clearLine);
    }
    private int downX;
    private int downY;
    private int mTouchSlop;

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX  = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                APPLog.e("ACTION_DOWNdownX="+downX);
                APPLog.e("ACTION_DOWNdownY="+downY);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                APPLog.e("downX="+downX);
                APPLog.e("downY="+downY);
                APPLog.e("moveX="+moveX);
                APPLog.e("ev.getRawY()="+ev.getRawY());
                if (Math.abs(downX - moveX) > mTouchSlop
                        || Math.abs((int) ev.getRawY() - downY) > mTouchSlop) {
                    int tooolType = ev.getToolType(0);
                    if (tooolType == 4) {
                        isClearLine = true;
                    }
                    int iDeviceId = ev.getDeviceId();
                    if (iDeviceId == MotionEvent.TOOL_TYPE_FINGER) {
                        return true;
                    }else {
                        isClearLine=false;
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
    /**
     * 是否绘制清楚线
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX()-offest;
        float eventY = event.getY()-offest;
        /**
         * 保存绘制路径以及划线风格
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getmPath().moveTo(eventX, eventY);
                mLastTouchX = eventX;
                mLastTouchY = eventY;
            }
            break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i)-offest;
                    float historicalY = event.getHistoricalY(i)-offest;
                    getDirtyRect(historicalX, historicalY);
                    getmPath().lineTo(historicalX, historicalY);
                }
                getmPath().lineTo(eventX, eventY);

                int paintSize = getPaintSize();
                postInvalidate((int) (mDirtyRect.left - paintSize),
                        (int) (mDirtyRect.top - paintSize),
                        (int) (mDirtyRect.right + paintSize),
                        (int) (mDirtyRect.bottom + paintSize));

                mLastTouchX = eventX;
                mLastTouchY = eventY;

                if ((event.getAction() == MotionEvent.ACTION_UP) && (isClearLine || paintStyle == 4)) {//
                    isClearLine = false;
                }
            }
            break;
        }
        return true;
    }

    private void getDirtyRect(float historicalX, float historicalY) {
        if (historicalX < mDirtyRect.left) {
            mDirtyRect.left = historicalX;
        } else if (historicalX > mDirtyRect.right) {
            mDirtyRect.right = historicalX;
        }
        if (historicalY < mDirtyRect.top) {
            mDirtyRect.top = historicalY;
        } else if (historicalY > mDirtyRect.bottom) {
            mDirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {
        mDirtyRect.left = Math.min(mLastTouchX, eventX);
        mDirtyRect.right = Math.max(mLastTouchX, eventX);
        mDirtyRect.top = Math.min(mLastTouchY, eventY);
        mDirtyRect.bottom = Math.max(mLastTouchY, eventY);
    }

    private float mLastTouchX = 0;
    private float mLastTouchY = 0;

    private final RectF mDirtyRect = new RectF();

    /**
     * 清屏
     */
    @Subscribe
    public void clearScreen(OttoBeen been) {
        String code=been.code;
        if (null==code||!code.equals(pakegeName))return ;
        pathClear = new Path();
        pathDraw = new Path();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CPortApplication.getBus().unregister(this);
    }
}
