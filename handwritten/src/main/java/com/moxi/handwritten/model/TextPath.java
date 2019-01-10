package com.moxi.handwritten.model;

import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;

/**
 * 一个文字的绘制路径以及位置信息
 * Created by Administrator on 2016/8/25.
 */
public class TextPath {
    ArrayList<PathPointBeen> pathPointBeens=new ArrayList<>();

    /**
     * 行数，当前显示的是第几行
     */
    public int lineNumber;

    /**
     * 记录光标的位置信息
     */
    public int leftpoint;
    public int toppoint;
    /**
     * 字体宽度
     */
    public int textWidth;
    public void changeTop(int ketHeight,int interspace){
        toppoint=(lineNumber * ketHeight + interspace);
    }

    /**
     * 改变坐标位置
     */
    public void changePosition(int screenWidth,int ketHeight,int interspace,int textWidth){
        APPLog.e("屏幕宽度"+screenWidth);
        if((gettextDrawWid()+textWidth)>screenWidth){
            lineNumber++;
            changeTop(ketHeight,interspace);
            leftpoint=5;
        }else{
            leftpoint+=textWidth;
        }

    }

    public void setPoint(int lineNumber,int leftpoint, int toppoint) {
        this.leftpoint = leftpoint;
        this.toppoint = toppoint;
        this.lineNumber = lineNumber;
    }

    public TextPath(int lineNumber, int leftpoint, int toppoint) {
        this.lineNumber = lineNumber;
        this.leftpoint = leftpoint;
        this.toppoint = toppoint;
    }

    public TextPath(ArrayList<PathPointBeen> pathPointBeens, int lineNumber, int leftpoint, int toppoint,  int textWidth) {
        this.pathPointBeens = pathPointBeens;
        this.lineNumber = lineNumber;
        this.leftpoint = leftpoint;
        this.toppoint = toppoint;
        this.textWidth = textWidth;
    }
    public int gettextDrawWid(){
        return leftpoint+textWidth+10;
    }
}
