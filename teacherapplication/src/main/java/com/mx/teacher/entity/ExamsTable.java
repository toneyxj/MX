package com.mx.teacher.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Archer on 16/9/22.
 */
public class ExamsTable extends DataSupport {
    private String id;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
