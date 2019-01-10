package com.mx.cqbookstore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mx.cqbookstore.R;
import com.mx.cqbookstore.interfaces.UploadSucess;


/**
 * 圆形进度显示器不带百分比
 * Created by Administrator on 2016/10/21.
 */
public class ProgressArcView extends View {
    private UploadSucess uploadSucess;
    /**
     * 圆环背景色
     */
    private int arcColor=getContext().getResources().getColor(R.color.colorBlack);
    /**
     * 绘制圆的直径
     */
    private int arcWidth;
    /**
     * 绘制圆本来控件的高度
     */
    private int kitHeight;
    /**
     * 绘制圆本来控件的宽度
     */
    private int kitWidth;
    /**
     * 加载总大小设置
     */
    private int endNumber;
    /**
     * 当前加载大小
     */
    private int currentNumber;
    /**
     * 当前进度显示文字100为加载完毕
     */
    private int progressTxt;
    // 画圆所在的距形区域
    private RectF mRectF;
    private  int mCircleLineStrokeWidth = 8;

    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
        invalidate();
    }
public void initsetNumber(int endNumber,int currentNumber){
    this.endNumber=endNumber;
    this.currentNumber=currentNumber;
    setShowTxt(currentNumber / (endNumber * 1f));
}
    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
        setShowTxt(currentNumber / (endNumber * 1f));
    }

    public void setShowTxt(float progress) {
        this.setVisibility(VISIBLE);
        this.progressTxt = (int) (progress * 100);
        invalidate();
    }

    public void setUploadSucess(UploadSucess uploadSucess) {
        this.uploadSucess = uploadSucess;
    }

    public ProgressArcView(Context context) {
        super(context);
        init();
    }


    public ProgressArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mRectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        kitWidth = getMeasuredWidth();
        kitHeight = getMeasuredHeight();
        arcWidth = (kitWidth > kitHeight ? kitHeight : kitWidth);
        mCircleLineStrokeWidth=arcWidth/20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initArc(canvas);
        if (null!=uploadSucess){
            uploadSucess.onUpload(null,endNumber==currentNumber,progressTxt);
        }
        if (endNumber<=currentNumber){
            this.setVisibility(GONE);
        }
    }

    /**
     * 绘制外圆
     *
     * @param canvas
     */
    private void initArc(Canvas canvas) {
        //绘制外圆
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(arcColor);
        paint.setStrokeWidth(mCircleLineStrokeWidth);

        mRectF.left = (kitWidth/2)-arcWidth/2+mCircleLineStrokeWidth/2; // 左上角x
        mRectF.top =  (kitHeight/2)-arcWidth/2+mCircleLineStrokeWidth/2; // 左上角y
        mRectF.right = (kitWidth/2)+arcWidth/2-mCircleLineStrokeWidth/2; // 左下角x
        mRectF.bottom =(kitHeight/2)+arcWidth/2-mCircleLineStrokeWidth/2; // 右下角y
        canvas.drawArc(mRectF, -90, 360, false, paint);
        paint.setColor(arcColor);

        mRectF.left = (float) (mRectF.left+mCircleLineStrokeWidth*1.3); // 左上角x
        mRectF.top = (float) (mRectF.top+mCircleLineStrokeWidth*1.3); // 左上角y
        mRectF.right = (float) (mRectF.right-mCircleLineStrokeWidth*1.3); // 左下角x
        mRectF.bottom = (float) (mRectF.bottom-mCircleLineStrokeWidth*1.3); // 右下角y

        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectF, -90, ((float) progressTxt / 100) * 360, true, paint);
    }

}
