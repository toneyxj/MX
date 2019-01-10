package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 光标显示view
 * Created by Administrator on 2016/8/25.
 */
public class CursorView extends BaseView{
    public CursorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorView(Context context) {
        super(context);
    }
    private int cursorHeight;
    private int cursorWidth=4;
    private Paint paint;
    private boolean isShow=true;

    /**
     * 设置光标显示高度
     * @param cursorHeight
     */
    public void setCursorHeight(int cursorHeight) {
        this.cursorHeight = cursorHeight;
        paint = new Paint();
        paint.setAntiAlias(true); // 去除锯齿
        paint.setStrokeWidth(cursorWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 设置光标显示位置
     * @param left
     * @param top
     */
    public void setMagin(int left,int top){
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin=left;
        params.topMargin=top;
        setLayoutParams(params);
    }

    @Override
    protected void onDrawSub(Canvas canvas) {
        if (cursorHeight!=0) {
            isShow=!isShow;
            if (isShow){
                paint.setColor(Color.BLACK);
            }else{
                paint.setColor(Color.TRANSPARENT);
            }
            canvas.drawLine(0, (int)(cursorHeight*0.1), 0, cursorHeight-1, paint);
        }
    }

    @Override
    protected void logic() {

    }
}
