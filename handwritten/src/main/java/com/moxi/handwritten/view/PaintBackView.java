package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PaintBackView extends View {
    public PaintBackView(Context context) {
        super(context);
    }

    public PaintBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 绘制地板的风格0背景为空，1背景为田字格，2背景为横线
     */
    private int drawStyle=0;

    public void setDrawStyle(int drawStyle) {
        this.drawStyle = drawStyle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initLineBack(canvas);
    }

    public void initLineBack(Canvas canvas){
        if (drawStyle==0)return;
        boolean isDraw=false;
        if (drawStyle==1) isDraw=true;
        Paint paint=new Paint();
        paint.setAntiAlias(true); // 去除锯齿
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);

        int height=getHeight();
        int width=getWidth();
        int lineHeight=height/9;
        int startLine=lineHeight/2;
        for (int i = 0; i < 9; i++) {
            int y=startLine+lineHeight*i;
            canvas.drawLine(0,y,width,y,paint);
        }
        int starty=lineHeight/2;
        int endy=lineHeight/2+lineHeight*8;
        int linex=width/9;
        if (isDraw){
            for (int i = 0; i < 9; i++) {
                int x=linex*i;
                canvas.drawLine(x,starty,x,endy,paint);
            }
        }
    }
}
