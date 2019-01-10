package com.moxi.taskstudent.view;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.view.View;

import com.mx.mxbase.utils.ImageLoadUtils;


/**
 * 自定义绘制
 * Created by Administrator on 2016/8/5.
 */
public class PaintInvalidateRectView extends View implements View.OnClickListener{

    Paint drawLine=new Paint();
    Paint clearLine=new Paint();

    Path pathDraw=new Path();
    Path pathClear=new Path();
    private Bitmap bitmap = null;
    private boolean preventClearScreen=false;

    public PaintInvalidateRectView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
        initPaintView();
    }

    public PaintInvalidateRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    public PaintInvalidateRectView(Context context) {
        super(context);
        initPaintView();
    }

    /**
     * 是否阻止清屏操作
     * @param preventClearScreen false为不阻止清屏操作
     */
    public void setPreventClearScreen(boolean preventClearScreen) {
        this.preventClearScreen = preventClearScreen;
    }

    /**
     * 保存清除线
     */
    private void initPaintView() {
        setOnClickListener(this);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        drawLine.setAntiAlias(true); // 去除锯齿
        drawLine.setStrokeWidth(3);
        drawLine.setStyle(Paint.Style.STROKE);
//        drawLine.setStrokeJoin(Paint.Join.ROUND);
//        drawLine.setStrokeCap(Paint.Cap.ROUND);
        drawLine.setColor(Color.BLUE);

        clearLine.setAntiAlias(true); // 擦除
        clearLine.setStrokeWidth(16);
        clearLine.setStyle(Paint.Style.STROKE);
//        clearLine.setStrokeJoin(Paint.Join.ROUND);
//        clearLine.setStrokeCap(Paint.Cap.ROUND);
        clearLine.setColor(Color.TRANSPARENT);
        Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        clearLine.setXfermode(xFermode);
    }

    private int paintStyle = 0;

    private int offest=0;

    /**
     *设置
     * @param style 0:笔，1:橡皮插
     * @return
     */
    public boolean setPaint(int style) {
        if (paintStyle == style) return false;
        paintStyle = style;
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
        switch (swi) {
            case 0:
                return  1;
            case 1:
                return  8;
            default:
                break;
        }
        return 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            Paint paint = new Paint();
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
        canvas.drawPath(pathDraw, drawLine);
        canvas.drawPath(pathClear, clearLine);
    }

    private boolean isClearLine = false;

    /**
     * 是否绘制清楚线
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int tooolType = event.getToolType(0);
        if (tooolType == 4) {
            isClearLine = true;
        }
        int iDeviceId = event.getDeviceId();
        if (!(iDeviceId == MotionEvent.TOOL_TYPE_FINGER||iDeviceId >= MotionEvent.TOOL_TYPE_ERASER)) {
            return false;
        }
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

                if ((event.getAction() == MotionEvent.ACTION_UP) && (isClearLine || paintStyle == 1)) {//
                    CurrentBitmap();
                    isClearLine = false;
                }
            }
            break;
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

    public void clearScreen(){
        if (!preventClearScreen) {
            bitmap = null;
            ClearPath();
            invalidate();
        }
    }
    private void ClearPath(){
        pathClear = new Path();
        pathDraw = new Path();
    }

    /**
     * 获得当前的绘制图片
     * @return
     */
    public Bitmap CurrentBitmap() {
        Bitmap bitmap1=bitmap;
        bitmap = getBitmap();
        if (bitmap1!=null){
            ImageLoadUtils.recycleBitmap(bitmap1);
        }
        ClearPath();
        invalidate();
        return bitmap;
    }

    private Bitmap getBitmap() {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        /** 如果不设置canvas画布为白色，则生成透明 */

        layout(0, 0, w, h);
        draw(c);
        return bmp;
    }

    @Override
    public void onClick(View v) {

    }
}