package com.moxi.handwritten.model;

/**
 * Created by Administrator on 2016/8/5.
 */
public class WriteDrawBeen {
    public float eventX;
    public float eventy;
    /**
     * 0铅笔，1圆珠笔，2钢笔，3毛笔，4橡皮差
     */
    public int lineStyle;

    public WriteDrawBeen(float eventX, float eventy, int lineStyle) {
        this.eventX = eventX;
        this.eventy = eventy;
        this.lineStyle = lineStyle;
    }
}
