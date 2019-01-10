package com.moxi.handwritten.model;

import android.graphics.Point;

/**
 * 保存path路径点，
 * Created by Administrator on 2016/8/22.
 */
public class PathPointBeen {
    public Point point;//绘制点
    public boolean isStart;//是否是起始点

    public PathPointBeen(float x, float y, boolean isStart) {
        setPoint((int)x,(int)y);
        this.isStart = isStart;
    }

    public void setPoint(int x, int y){
        this.point=new Point(x,y);
    }

    /**
     * 改变当前point点
     * @param multiple 缩放倍数
     * @param totop 左边移动点
     * @param toleft
     * @param linewidth 线宽
     *                  @param iswidth 是否以宽度作为指引
     * @return
     */
    public Point changePoint(float multiple,int totop,int toleft,int linewidth,boolean iswidth,int middleHeight){
        int x=point.x;
        int y=point.y;
        if (multiple<=1){
            x-=toleft;
            y-=totop ;

            x = (int) (x / multiple)+linewidth;
            y = (int) (y / multiple)+linewidth;

        }else{
            x-=toleft-linewidth-linewidth;
            y-= totop - linewidth-linewidth;

            x = (int) (x / multiple);
            y = (int) (y / multiple);

        }

        if (iswidth){
            y += middleHeight;
        }
        point.set(x,y);
        return point;
    }

}
