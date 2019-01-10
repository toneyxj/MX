package com.moxi.copybook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.model.PrictiseTextBeen;


/**
 * Created by Administrator on 2016/8/9.
 */
public class PrictiseTextView extends View {
    /**
     * 控件宽
     */
    private int width;
    /**
     * 控件高
     */
    private int height;
    /**
     * 边框宽度大小
     */
    private int binakuangSize;
    /**
     * 文字大小
     */
    private int textSize;
    /**
     * 文字拼音大小
     */
    private int textPingYinSize;
    /**
     * 画线颜色
     */
    private int lineClocor=0xFF000000;
    /**
     * 拼音颜色
     */
    private int pingyinClocor=0xFF3A3D3E;
    /**
     * 显示文本颜色
     */
    private int textColor=0xFF000000;
    /**
     * 是否需要绘制阴影文字
     */
    private boolean shadowText=false;
//    private Typeface typeface;
//
//    public void setTypeface(Typeface typeface) {
//        this.typeface = typeface;
//        invalidate();
//    }

    /**
     * 练习绘制文本类
     */
    PrictiseTextBeen prictise=new PrictiseTextBeen("kong","空");
    private Paint.FontMetrics fm;
    /**
     * 初始化布局参数
     * @param lineClocor 画线的颜色
     * @param pingyinClocor 拼音颜色
     * @param textColor 文字颜色
     * @param shadowText 是否绘制文字阴影
     * @param prictise 绘制源数据
     */
public void setInitValue(int lineClocor,int pingyinClocor,int textColor,boolean shadowText,PrictiseTextBeen prictise){
    this.lineClocor=lineClocor;
    this.pingyinClocor=pingyinClocor;
    this.shadowText=shadowText;
    this.prictise=prictise;
    this.textColor=textColor;
}
    /**
     * 初始化布局参数
     * @param prictise 绘制源数据
     */
public void setInitValue(PrictiseTextBeen prictise){
    this.prictise=prictise;
    invalidate();
}
    /**
     * 设置显示文字个数来计算显示的宽高
     * @param number 显示文字个数
     */
    public void setShowTextNumber(int number){
        int w= BaseApplication.ScreenWidth;
        number++;
        if (number<=6){
        width=w/6;
        }else if (number<=11){
            width=w/number;
        }else{
            width=w/11;
        }
        height= (int) (width*1.1);
        setWidthAndHeight();
    }

    /**
     * 设置控件宽高
     * @param width 宽度
     */
    public void setWidthAndHeight(int width ){
        this.width=width;
        this.height= (int) (width*1.1);
        setWidthAndHeight();
    }

    /**
     * 设置控件宽高
     */
    public void setWidthAndHeight(){
        textPingYinSize= (int) (width*0.2);
        binakuangSize= (int) (height*0.7);
        textSize= (int) (binakuangSize*0.6);
    }
    public PrictiseTextView(Context context) {
        super(context);
        init();
    }

    public PrictiseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PrictiseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void  init(){
        setShowTextNumber(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (prictise==null)return;

        drawPingYin(canvas);
        drawFrame(canvas);
        drawFrameDottedLine(canvas);
        drawText(canvas);
    }

    private void drawPingYin(Canvas canvas){
        int pinfyinSize= (int) (width*0.15);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(pinfyinSize);
        paint.setColor(pingyinClocor);
        setTypeFace(paint);
        fm = paint.getFontMetrics();
        //文本的宽度
        float textWidth = paint.measureText(prictise.pingyin);
       int textCenterX = (int) (textWidth / 2);
        int textBaselineY = (int) -fm.ascent;
        int left= (int) ((width-textWidth)/2)+textCenterX;
        canvas.drawText(prictise.pingyin, left, textBaselineY, paint);
    }

    private void setTypeFace(Paint paint){
        String path= BaseApplication.preferences.getString("fontPath",null);
        if (path!=null){
            paint.setTypeface(Typeface.createFromFile(path));
        }
    }

    private void drawFrame(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(lineClocor);
        paint.setStrokeWidth((float) (width * 0.01));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        int left= (int) (width*0.15);
        int top= (int) (height*0.2);
        int rectw=left+binakuangSize;
        int rectwtop=top+binakuangSize;

        RectF rect = new RectF(left, top, rectw, rectwtop);
        canvas.drawRect(rect, paint);


    }
    private void drawFrameDottedLine(Canvas canvas){
        float heightXu = (float) (width *0.005);
        //绘制虚线
        Paint paint = new Paint();
        paint.setColor(lineClocor);
        paint.setStrokeWidth(heightXu);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        float xuWdith = heightXu * 5;
        float xuWdith10 = heightXu * 10;
        PathEffect effects = new DashPathEffect(new float[]{xuWdith10, xuWdith, xuWdith10, xuWdith}, heightXu);
        paint.setPathEffect(effects);

        int left= (int) (width*0.15);
        int top= (int) (height*0.2);

        int endright=left+binakuangSize;
        int endBottom=top+binakuangSize;

        int middletopx=left+binakuangSize/2;

        int middlebany=top+binakuangSize/2;

        canvas.drawLine(left,top,endright,endBottom,paint);
        canvas.drawLine(endright,top,left,endBottom,paint);

        canvas.drawLine(middletopx,top,middletopx,endBottom,paint);
        canvas.drawLine(left,middlebany,endright,middlebany,paint);

    }
    private  void  drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setStrokeWidth((float) (width * 0.08));
        setTypeFace(paint);

        int left= (int) (width*0.15);
        int top= (int) (height*0.2);
        int centx=left+binakuangSize/2;
        int centy=top+binakuangSize/2;


//        paint.setTypeface(Typeface.SANS_SERIF); //设置字体

        Paint.FontMetrics fm = paint.getFontMetrics();
        int height = (int) (int) Math.ceil(fm.descent - fm.ascent);
        float width = paint.measureText(prictise.text);
        canvas.drawText(prictise.text, centx , (centy + height / 2) + (textSize - height)-(height/10), paint); //画出文字
    }
}
