package com.moxi.CPortTeacher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Student {
    @SerializedName("headimg")
    public String photo;
    @SerializedName("id")
    public Long id;
    @SerializedName("name")
    public String name;
    public boolean isSelect = false;

    public Student() {
    }

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void isChangeSelect() {
        isSelect=!isSelect;
    }
}
