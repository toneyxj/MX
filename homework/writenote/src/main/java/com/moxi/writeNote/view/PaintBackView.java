package com.moxi.writeNote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PaintBackView extends View {
    public final static String parentCode="PaintBackView-saveCode";
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
    private int drawStyle = 0;

    public void setDrawStyle(int drawStyle) {
        this.drawStyle = drawStyle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (drawStyle) {
            case 0:
                break;
            case 1:
                initFieldLineBack(canvas);
                break;
            case 2:
                initLineBack(canvas);
                break;
            default:
                break;
        }

    }

    /**
     * 田字格
     *
     * @param canvas 绘制画布
     */
    public void initFieldLineBack(Canvas canvas) {
        initPaint();
        int height = getHeight();
        int width = getWidth();
        int pading = 10;
        //横排是10个写字格
        //每个格子宽度
        int fieldWidht = (width - pading * 2) / 8;
        //分割线高度
        int splitHeight = (height - width) / 10;
        //绘制外边框
        for (int i = 0; i < 8; i++) {
            int left = pading;
            int top = splitHeight * (2 + i) + fieldWidht * i;
            int right = width - pading;
            int bottom = top + fieldWidht;
            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawRect(rect, linePaint);
            for (int j = 1; j < 9; j++) {
                int leftj = pading + j * fieldWidht;
                int topj = top;
                int rightj = leftj;
                int bottomj = top + fieldWidht;
                if (j != 8)
                    canvas.drawLine(leftj, topj, rightj, bottomj, linePaint);
                //虚线绘制
                canvas.drawLine(leftj - fieldWidht / 2, topj, rightj - fieldWidht / 2, bottomj, xuPaint);
                canvas.drawLine(leftj - fieldWidht, topj, rightj, bottomj, xuPaint);
                canvas.drawLine(leftj, topj, rightj - fieldWidht, bottomj, xuPaint);
            }
            canvas.drawLine(left, top + fieldWidht / 2, right, bottom - fieldWidht / 2, xuPaint);
        }
    }

    private Paint xuPaint;
    private Paint linePaint;

    private void initPaint() {
        if (xuPaint == null) {
            float heightXu = 1f;
            //绘制虚线
            xuPaint = new Paint();
            xuPaint.setColor(Color.BLACK);
            xuPaint.setStrokeWidth(heightXu);
            xuPaint.setAntiAlias(true);
            xuPaint.setStrokeWidth(0.5f);
            xuPaint.setStyle(Paint.Style.STROKE);
            float xuWdith = heightXu * 5;
            float xuWdith10 = heightXu * 10;
            PathEffect effects = new DashPathEffect(new float[]{xuWdith10, xuWdith, xuWdith10, xuWdith}, heightXu);
            xuPaint.setPathEffect(effects);

            linePaint = new Paint();
            linePaint.setAntiAlias(true); // 去除锯齿
            linePaint.setStrokeWidth(1);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setColor(Color.BLACK);
        }
    }

    public void initLineBack(Canvas canvas) {
        initPaint();
        int height = getHeight();
        int width = getWidth();
        int lineHeight = height / 12;
        int startLine = lineHeight / 2;
        for (int i = 0; i < 12; i++) {
            int y = startLine + lineHeight * i;
            canvas.drawLine(0, y, width, y, linePaint);
        }
    }

//    /**
//     * 保存手写背景图片仔细
//     */
//    public void saveWritePad() {
////        WritPadModel model = new WritPadModel("style", parentCode+ drawStyle, 1, parentCode, 0, "0");
////        if (WritePadUtils.getInstance().isSavedWrite(model.saveCode,1))return;
//        final Bitmap bitmap = getBitmap();
////        //更改缓存里面的图片信息
////        new SaveNoteWrite(model, bitmap, new NoteSaveWriteListener() {
////            @Override
////            public void isSucess(boolean is, WritPadModel model) {
////                if (isFinish) return;
////                StringUtils.recycleBitmap(bitmap);
////            }
////        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        try {
//            String path= com.mx.mxbase.utils.StringUtils.getSDPath()+"image"+drawStyle+".png";
//            Log.e("ExportFileAsy-path",path);
//            FileOutputStream fileOutputStream = new FileOutputStream(path);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isFinish = false;
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        isFinish = true;
//    }
//
//    public Bitmap getBitmap() {
//        int w = getWidth();
//        int h = getHeight();
//        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmp);
//        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        /** 如果不设置canvas画布为白色，则生成透明 */
//
//        layout(getLeft(), getTop(), getRight(), getBottom());
//        draw(c);
//        return bmp;
//    }
}
