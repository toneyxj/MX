package com.moxi.taskteacher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.mx.mxbase.utils.DensityUtil;


/**
 * 底部画线的textview
 * Created by Administrator on 2016/10/28.
 */
public class BottomLineTextview extends TextView {
    private boolean drawLine = false;
    private Paint paint;
    private int lineWidth;

    public void setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
        invalidate();
    }

    public BottomLineTextview(Context context) {
        super(context);
        lineWidth = DensityUtil.dip2px(context, 4);
        init();
    }

    public BottomLineTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        lineWidth = DensityUtil.dip2px(context, 4);
        init();
    }

    public BottomLineTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lineWidth = DensityUtil.dip2px(context, 4);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getResources().getColor(R.color.colorgray50));
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawLine) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            canvas.drawLine(0, height - lineWidth / 2, width, height - lineWidth / 2, paint);
        }
    }
}
