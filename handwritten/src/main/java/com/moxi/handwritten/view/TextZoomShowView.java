package com.moxi.handwritten.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/8/22.
 */
public class TextZoomShowView extends View {
    public TextZoomShowView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        initPaintView();
    }

    public TextZoomShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView();
    }

    private int width;
    private int height;

    public TextZoomShowView(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        initPaintView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 设置光标显示位置
     *
     * @param left
     * @param top
     */
    public void setMagin(int left, int top) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        setLayoutParams(params);
    }

    /**
     * 保存清除线
     */
    private void initPaintView() {

        //关闭硬件加速
        mPaint1.setStrokeJoin(Paint.Join.ROUND);

        mPaint1.setAntiAlias(true); // 去除锯齿
        mPaint1.setStrokeCap(Paint.Cap.ROUND);
        mPaint1.setStrokeWidth(3);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setColor(Color.BLACK);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPath1 != null)
            canvas.drawPath(mPath1, mPaint1);
    }

    private Paint mPaint1 = new Paint(Paint.DITHER_FLAG);

    private Path mPath1;

    public void setPath(Path mPath1) {
        this.mPath1 = mPath1;
        invalidate();
    }

}