package com.moxi.handwritten.utils;

import android.graphics.Path;
import android.graphics.Point;

import com.moxi.handwritten.model.PathPointBeen;
import com.mx.mxbase.base.BaseApplication;

import java.util.ArrayList;

/**
 * 文字矢量缩放类
 * Created by Administrator on 2016/8/22.
 */
public class DrawTextZoomUtils {
    private boolean isStartChange = false;
    /**
     * 绘制转换后文本的高
     */
    private int drawTextHeight;

    public DrawTextZoomUtils(int drawTextHeight) {
        this.drawTextHeight = drawTextHeight;
    }

    public ArrayList<PathPointBeen> listData = new ArrayList<>();
    private float xLeft = BaseApplication.ScreenWidth;
    private float xRight = 0;
    private float yLeft = BaseApplication.ScreenHeight;
    private float yRight = 0;

    private int startCount = 0;
    private float roomMultiple = 0;

    public void setEventPoint(float eventx, float eventy, boolean isStart) {
        if (isStartChange) return;
        if (isStart) startCount++;
        //绘制线不得小于0
        if (eventx < 0 || eventy < 0) return;

        if (eventx > xRight) {
            xRight = eventx;
        }
        if (eventx < xLeft) {
            xLeft = eventx;
        }

        if (eventy < yLeft) {
            yLeft = eventy;
        }
        if (eventy > yRight) {
            yRight = eventy;
        }
        listData.add(new PathPointBeen(eventx, eventy, isStart));
    }

    /**
     * 开启装换数据
     */
    public Path StartChange(int linewidth) {
        boolean iswidth = false;
        isStartChange = true;
        float drawHeight = yRight - yLeft;//绘制文字的高度
        float drawWidth = xRight - xLeft;//绘制文字的宽度
        float middleHeight = 0;//如果按宽度计算值需要

        roomMultiple = drawHeight / drawTextHeight;

        float currWidth = drawWidth / roomMultiple;//变换后线宽
        //选则宽度为设定值当只有一笔,且当前变换后的宽度大于高度的1.5倍，且高度又小于自身高度的0.5
        if (startCount == 1 && roomMultiple < 1 && currWidth > (drawTextHeight * 1.5)) {
            if (drawWidth <= drawTextHeight / 2) {
                //不需要进行缩放按原大小摆放
                roomMultiple = 1;
                middleHeight = Math.abs(drawTextHeight - drawHeight-5);
                iswidth = true;
            } else {
                float middle = drawWidth / (drawTextHeight / 2);
                if (middle > roomMultiple) {
                    roomMultiple = middle;
                    middleHeight = Math.abs((drawTextHeight - (drawHeight / roomMultiple)) / 2);
                    iswidth = true;
                }
            }
        } else if (roomMultiple <= 0.6 && drawWidth <= drawTextHeight * 0.6) {
            //不需要进行缩放按原大小摆放
            roomMultiple = 1;
            middleHeight = Math.abs(drawTextHeight - drawHeight-5);
            iswidth = true;
        }
        /**
         * 作废绘制线
         */
        if (roomMultiple < 0.1 || roomMultiple >= 20) {
            return null;
        }

        Path path = new Path();

        for (PathPointBeen been : listData) {
            Point point = been.changePoint(roomMultiple, (int) yLeft, (int) xLeft, linewidth, iswidth, (int) middleHeight);

            if (been.isStart) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        isStartChange = false;
        return path;
    }

    /**
     * 获得缩放后绘制控件的宽度
     *
     * @return
     */
    public int getViewWidth() {
        float drawHeight = yRight - yLeft;//绘制文字的高度
        float roomMultiple = drawHeight / drawTextHeight;
        if (this.roomMultiple == 0) {
            roomMultiple = this.roomMultiple;
        }
        if (roomMultiple <= 1) {
            return ((int) ((xRight - xLeft + 8) / this.roomMultiple));
        } else {
            return ((int) ((xRight - xLeft) / this.roomMultiple)) + 4;
        }

    }

    /**
     * 清空数据
     */
    public void clearData() {
        if (isStartChange) return;
        listData = new ArrayList<>();
        startCount = 0;
        roomMultiple = 0;
        xLeft = BaseApplication.ScreenWidth;
        xRight = 0;
        yLeft = BaseApplication.ScreenHeight;
        yRight = 0;
    }

    /**
     * 获得绘制点数据
     *
     * @return
     */
    public ArrayList<PathPointBeen> getListData() {
        return listData;
    }

}
