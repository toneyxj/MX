package com.moxi.CPortTeacher.model;

/**
 * Created by Administrator on 2016/11/1.
 */
public class OttoBeen {
    /**
     * 传输内容
     */
    public Object style;
    /**
     * 0tto唯一标示
     */
    public String code;
    /**
     * 辅助标记
     */
    public boolean is;

    public OttoBeen(Object style, String code) {
        this.style = style;
        this.code = code;
    }

    public OttoBeen(Object style, String code, boolean is) {
        this.style = style;
        this.code = code;
        this.is = is;
    }
}
