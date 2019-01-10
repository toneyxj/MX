package com.moxi.classRoom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 分数统计
 * Created by Administrator on 2016/10/25.
 */
public class StatisticsGradeView extends View {
    private RectF mRectF = new RectF();
    private Paint paint;
    private String txt = "85.6%";

    public void setTxt(String txt) {
        this.txt = txt;
        invalidate();
    }

    public StatisticsGradeView(Context context) {
        super(context);
    }

    public StatisticsGradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatisticsGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        boolean iswidth = height > width;
        int viewWeight = (iswidth ? width : height);
        float borderWidth = viewWeight / 70f;
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF5D5D5D);
        paint.setStrokeWidth(borderWidth);

        //绘制圆环
        mRectF.left = (iswidth ? 0 : ((width - height) / 2)) + borderWidth / 2; // 左上角x
        mRectF.top = (iswidth ? ((height - width) / 2) : 0) + borderWidth / 2; // 左上角y
        mRectF.right = (iswidth ? width : ((width - height) / 2 + height)) - borderWidth / 2; // 左下角x
        mRectF.bottom = (iswidth ? ((height - width) / 2 + width) : height) - borderWidth / 2; // 右下角y
        canvas.drawArc(mRectF, -90, 360, false, paint);

        //#DDDDDD
        mRectF.left += viewWeight * 0.07;
        mRectF.top += viewWeight * 0.07;
        mRectF.right -= viewWeight * 0.07;
        mRectF.bottom -= viewWeight * 0.07;
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(0xFFDDDDDD);
        canvas.drawArc(mRectF, -90, 360, false, paint);

        mRectF.left += viewWeight * 0.06;
        mRectF.top += viewWeight * 0.06;
        mRectF.right -= viewWeight * 0.06;
        mRectF.bottom -= viewWeight * 0.06;

        paint.setColor(0xFF5D5D5D);
        canvas.drawArc(mRectF, -90, 360, false, paint);

        if (txt != null) {
            float  textHeight= viewWeight / 5f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xFFFFFFFF);
            paint.setStrokeWidth(viewWeight / 30);

            paint.setTextSize(textHeight);
            int textWidth = (int) paint.measureText(txt, 0, txt.length());
            paint.setStyle(Paint.Style.FILL);
//            float x=iswidth?((viewWeight-textWidth)/2):((width-viewWeight)/2+(viewWeight-textWidth)/2);
            float x=(width-textWidth)/2;
            float y=(height+textHeight)/2;
            canvas.drawText(txt, x, y, paint);
        }
    }
}
