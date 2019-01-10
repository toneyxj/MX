package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ZoomRelativeLayout extends View {
    public ZoomRelativeLayout(Context context) {
        super(context);
        init();
    }

    public ZoomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ZoomPointListener listener;

    public void setListener(ZoomPointListener listener) {
        this.listener = listener;
    }

    private Paint paint = new Paint();

    private void init() {
        paint.setAntiAlias(true); // 去除锯齿
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
    }

    private int lineHeight;
    private int screenWidth;
    private int number;//画线个数

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setline(int lineHeight, int number, int screenWidth) {
        this.lineHeight = lineHeight;
        this.number = number;
        this.screenWidth = screenWidth;
        invalidate();
    }

    private float sourceX;
    private float sourceY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int iDeviceId = event.getDeviceId();
        if (iDeviceId != MotionEvent.TOOL_TYPE_FINGER) {
            float eventX = event.getX();
            float eventY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sourceX = eventX;
                    sourceY = eventY;

                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(sourceX - eventX) < 5 && Math.abs(sourceY - eventY) < 5) {
                        if (listener != null) listener.ZoomPoint(eventX, eventY);
                    }
                    break;
            }

            return true;
        }
        return false;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineHeight != 0) {
            for (int i = 1; i < number + 1; i++) {
                int y = lineHeight * i;
                canvas.drawLine(0, y, screenWidth, y, paint);
            }
        }
    }

    public interface ZoomPointListener {
        public void ZoomPoint(float x, float y);
    }
}
