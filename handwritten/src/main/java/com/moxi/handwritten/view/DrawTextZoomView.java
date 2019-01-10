package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.moxi.handwritten.model.PathPointBeen;
import com.moxi.handwritten.utils.DrawTextZoomUtils;

import java.util.ArrayList;


/**
 * 字体书写类
 * Created by Administrator on 2016/8/22.
 */
public class DrawTextZoomView extends View {
    /**
     * 缩放工具类
     */
    private DrawTextZoomUtils zoomUtils;
    private ZoomListener listener;

    public DrawTextZoomView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        initPaintView();
    }

    public DrawTextZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    public DrawTextZoomView(Context context) {
        super(context);
        initPaintView();
    }

    /**
     * 初始化缩放至多大
     *
     * @param zoomHeight 缩放后高度
     */
    public void initZoom(int zoomHeight, ZoomListener listener) {
        zoomUtils = new DrawTextZoomUtils(zoomHeight);
        this.listener = listener;
    }

    /**
     * 保存清除线
     */
    private void initPaintView() {

        //关闭硬件加速
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);

//        mPaint1.setStrokeMiter(360.0f);

        mPaint1.setAntiAlias(false); // 去除锯齿
        mPaint1.setStrokeCap(Paint.Cap.ROUND);
        mPaint1.setStrokeWidth(4);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setColor(Color.BLUE);

        DrawTextZoomView.this.setFocusable(true);
        DrawTextZoomView.this.setFocusableInTouchMode(true);
    }

    /**
     * 获得缩放后的宽度
     * @return
     */
    public int getDrawWidth() {
        return zoomUtils.getViewWidth();
    }

    /**
     * 获得缩放后的绘制路径
     * @return
     */
    public Path getPath() {
        return zoomUtils.StartChange(4);
    }
    public ArrayList<PathPointBeen> getListData(){
        return zoomUtils.getListData();
    }

    /**
     * 清空绘制面板
     */
    public void clearDraw(){
        zoomUtils.clearData();
        clearPath();
        invalidate();
    }

    public Path getmPath() {
        return mPath1;
    }

    private int getPaintSize() {
        return 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath1, mPaint1);
    }

    private boolean isClearLine = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int tooolType = event.getToolType(0);
        if (tooolType == 4) {
            isClearLine = true;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否绘制清楚线
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int iDeviceId = event.getDeviceId();
        if (iDeviceId != MotionEvent.TOOL_TYPE_FINGER) {
            return false;
        }
        float eventX = event.getX();
        float eventY = event.getY();
        /**
         * 保存绘制路径以及划线风格
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getmPath().moveTo(eventX, eventY);
                mLastTouchX = eventX;
                mLastTouchY = eventY;
                listener.endDown();
                zoomUtils.setEventPoint(eventX, eventY, true);
            }
            return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);

                    zoomUtils.setEventPoint(historicalX, historicalY, false);

                    getDirtyRect(historicalX, historicalY);
                    getmPath().lineTo(historicalX, historicalY);
                }
                getmPath().lineTo(eventX, eventY);

                int paintSize = getPaintSize();
                invalidate((int) (mDirtyRect.left - paintSize),
                        (int) (mDirtyRect.top - paintSize),
                        (int) (mDirtyRect.right + paintSize),
                        (int) (mDirtyRect.bottom + paintSize));

                mLastTouchX = eventX;
                mLastTouchY = eventY;

                zoomUtils.setEventPoint(eventX, eventY, false);

                if ((event.getAction() == MotionEvent.ACTION_UP)) {//
                    //手指抬起
                    listener.startUp();
//                    if ((isClearLine || paintStyle == 4)) {
//                        isClearLine = false;
//                    }
                }
            }
            break;
            default:
                return false;
        }
        return super.onTouchEvent(event);
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
    private Paint mPaint1 = new Paint();

    private Path mPath1 = new Path();

    private void clearPath() {
        mPath1 = new Path();
    }

    public interface ZoomListener {
        /**
         * 按下，结束计时
         */
        public void endDown();

        /**
         * 松开，开始计时
         */
        public void startUp();
    }


}