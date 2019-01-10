package com.moxi.handwritten.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FloderBeen implements Serializable{
    public String fileName;
    public String dbTableName;

    public FloderBeen(String fileName, String dbTableName) {
        this.fileName = fileName;
        this.dbTableName = dbTableName;
    }

    @Override
    public String toString() {
        return "FloderBeen{" +
                "fileName='" + fileName + '\'' +
                ", dbTableName='" + dbTableName + '\'' +
                '}';
    }
}
