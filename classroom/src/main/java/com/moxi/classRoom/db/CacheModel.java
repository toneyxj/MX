package com.moxi.classRoom.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CacheModel extends DataSupport {
    @Column(unique = true)
    public long id;
    /**
     * 数据标示值
     */
    public String code;
    /**
     * 数据json字符串
     */
    public String json;
    /**
     * 保存截止时间-1为永远保存
     */
    public long time=-1;

    public CacheModel(long id, String code, String json, long time) {
        this.id = id;
        this.code = code;
        this.json = json;
        this.time = time;
    }

    public CacheModel(String code, String json) {
        this.code = code;
        this.json = json;
    }

    public CacheModel(String code, String json, long time) {
        this.code = code;
        this.json = json;
        this.time = time;
    }
}
