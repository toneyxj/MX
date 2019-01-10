package com.moxi.copybook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class PrictiseDrawTextCanvar extends View {
    public PrictiseDrawTextCanvar(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        initPaintView();
    }

    public PrictiseDrawTextCanvar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    public PrictiseDrawTextCanvar(Context context) {
        super(context);
        initPaintView();
    }

    private void initPaintView() {
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint3.setStrokeJoin(Paint.Join.ROUND);

        mPaint1.setAntiAlias(false); // 去除锯齿
        mPaint1.setStrokeWidth(8);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setColor(Color.BLUE);

        mPaint2.setAntiAlias(false); // 去除锯齿
        mPaint2.setStrokeWidth(16);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(Color.BLUE);

        mPaint3.setAntiAlias(false); // 去除锯齿
        mPaint3.setStrokeWidth(24);
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setColor(Color.BLUE);
    }

    private int paintStyle = 1;
    private int offset=0;


    public boolean setPaint(int style) {
        if (paintStyle == style) return false;
        paintStyle = style;
//        offset=getPaintSize()/2;
        return true;
    }

    public Path getmPath() {
        int swi=paintStyle;
//        if (isClearLine)swi=4;
        switch (swi) {
            case 0:
                return mPath1;
            case 1:
                return mPath2;
            case 2:
                return mPath3;
            default:
                break;
        }
        return mPath1;
    }
    private int getPaintSize(){
        int swi=0;
//        if (isClearLine)swi=4;
        switch (paintStyle) {
            case 0:
                swi =8;
                break;
            case 1:
                swi=16;
                break;
            case 2:
                swi=24;
                break;
            default:
                break;
        }
        return swi;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight()!=0){
            int width=getMeasuredHeight();
        setMeasuredDimension(width,width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath1, mPaint1);
        canvas.drawPath(mPath2, mPaint2);
        canvas.drawPath(mPath3, mPaint3);

    }
    private boolean isClearLine=false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int iDeviceId = event.getDeviceId();
        int tooolType=event.getToolType(0);
        if (iDeviceId != MotionEvent.TOOL_TYPE_FINGER) {
            return false;
        }
        if (tooolType==4){
            isClearLine=true;
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getToolType(0)==4){
            isClearLine=true;
            //取消了擦除效果
            reInitPath();
            invalidate();
            return false;
        }
        float eventX = event.getX()-offset;
        float eventY = event.getY()-offset;
/**
 * 保存绘制路径以及划线风格
 */

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getmPath().moveTo(eventX, eventY);
                mLastTouchX = eventX;
                mLastTouchY = eventY;
            }
            return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i)-offset;
                    float historicalY = event.getHistoricalY(i)-offset;
                    getDirtyRect(historicalX, historicalY);
                    getmPath().lineTo(historicalX, historicalY);
                }

                getmPath().lineTo(eventX, eventY);
                int paintSize=getPaintSize();
                invalidate((int) (mDirtyRect.left - paintSize),
                        (int) (mDirtyRect.top - paintSize),
                        (int) (mDirtyRect.right + paintSize),
                        (int) (mDirtyRect.bottom + paintSize));

                mLastTouchX = eventX;
                mLastTouchY = eventY;
            }
            break;

            default:
                return false;
        }
//        }
        isClearLine=false;
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

    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private float mLastTouchX = 0;
    private float mLastTouchY = 0;

    private final RectF mDirtyRect = new RectF();
    private Paint mPaint1 = new Paint();
    private Paint mPaint2 = new Paint();
    private Paint mPaint3 = new Paint();

    private Path mPath1 = new Path();
    private Path mPath2 = new Path();
    private Path mPath3 = new Path();

    public void setPath(Path mPath1,Path mPath2,Path mPath3){
        this.mPath2=mPath2;
        this.mPath1=mPath1;
        this.mPath3=mPath3;
        invalidate();
    }

    public void setPaths(List<Path> paths){
        if (paths!=null){
            mPath1=paths.get(0);
            mPath2=paths.get(1);
            mPath3=paths.get(2);
        }else{
            reInitPath();
        }
        invalidate();
    }
    private void reInitPath(){
        mPath1=new Path();
        mPath2=new Path();
        mPath3=new Path();
    }
    private String DrawTextName;

    /**
     * 获得编辑文本的名字
     * @return
     */
    public String getDrawTextName() {
        return DrawTextName;
    }

    /**
     * 设置编辑文本的名字
     * @param drawTextName
     */
    public void setDrawTextName(String drawTextName) {
        DrawTextName = drawTextName;
    }

    /**
     * 获得绘制路径对象
     * @return
     */
    public List<Path> getPathList(){
        List<Path> paths=new ArrayList<>();
        paths.add(mPath1);
        paths.add(mPath2);
        paths.add(mPath3);
        return paths;
    }
}