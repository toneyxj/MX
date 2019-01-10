package com.moxi.bookstore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moxi.bookstore.R;

/**
 * 底部画线的textview
 * Created by Administrator on 2016/10/28.
 */
public class BottomLineTextview extends TextView {
    private boolean drawLine=false;
    private Paint paint;
    private int lineWidth;
    private Context context;

    public void setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
        invalidate();
    }

    public BottomLineTextview(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BottomLineTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BottomLineTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    private void init(){
        lineWidth= dip2px(4);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getResources().getColor(R.color.colorBlack));
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawLine){
            int width=getMeasuredWidth();
            int height=getMeasuredHeight();
            canvas.drawLine(0,height-lineWidth/2,width,height-lineWidth/2,paint);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
