package com.moxi.classRoom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 人员选择View
 * Created by Administrator on 2016/10/25.
 */
public class PhotoSelectView extends ImageView {
    private int arcColor=0xFF5D5D5D;
    private boolean isSelect=false;
    private RectF mRectF = new RectF();
    private Paint paint;

    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
        invalidate();
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void changeSelect(){
        this.isSelect=!this.isSelect;
        invalidate();
    }

    public PhotoSelectView(Context context) {
        super(context);
    }

    public PhotoSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        if (width==0)width=height;
        if (height==0)height=width;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isSelect)return;

        int width=getMeasuredWidth();

        int borderWidth=width/6;
        if (paint==null){
            paint=new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
        }
        paint.setColor(arcColor);
        paint.setStrokeWidth(borderWidth);

        //绘制圆环
        mRectF.left = borderWidth/2; // 左上角x
        mRectF.top = borderWidth/2; // 左上角y
        mRectF.right =  width-borderWidth/2; // 左下角x
        mRectF.bottom =width-borderWidth/2; // 右下角y
        canvas.drawArc(mRectF, -90, 360, false, paint);

    }
//    int width=getMeasuredWidth();
//    int height=getMeasuredHeight();
//    boolean iswidth=height>width;
//
//    int borderWidth=(iswidth?width:height)/6;
//    if (paint==null){
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//    }
//    paint.setColor(arcColor);
//    paint.setStrokeWidth(borderWidth);
//
//    //绘制圆环
//    mRectF.left = (iswidth?0:((width-height)/2))+borderWidth/2; // 左上角x
//    mRectF.top = (iswidth?((height-width)/2):0)+borderWidth/2; // 左上角y
//    mRectF.right =  (iswidth?width:((width-height)/2+height))-borderWidth/2; // 左下角x
//    mRectF.bottom =(iswidth?((height-width)/2+width):height)-borderWidth/2; // 右下角y
//    canvas.drawArc(mRectF, -90, 360, false, paint);
}
