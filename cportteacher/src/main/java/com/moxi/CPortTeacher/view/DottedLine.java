package com.moxi.CPortTeacher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.moxi.CPortTeacher.R;

/**
 * 虚线
 * Created by Administrator on 2016/11/9.
 */
public class DottedLine extends View{
    private int lineColor=0;
    private Paint paint = new Paint();
    private float[] spaces=new float[]{8, 4, 8, 4};


    public DottedLine(Context context) {
        super(context);
        initPaint();
    }

    public DottedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 设置虚线间隔
     * @param spaces
     */
    public void setSpaces(float[] spaces) {
        this.spaces = spaces;
    }

    /**
     * 设置虚线绘制颜色
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        paint.setColor(lineColor);
        initPaint();
    }

    private void initPaint(){
        if (lineColor==0)
        lineColor=getContext().getResources().getColor(R.color.colorgray50);


        paint.setAntiAlias(true);
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(spaces, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(getHeight());
        canvas.drawLine(0, getHeight() / 2, getWidth(),getHeight() / 2, paint);
    }
}
