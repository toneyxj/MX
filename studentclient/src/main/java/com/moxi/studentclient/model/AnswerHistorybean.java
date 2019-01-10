package com.moxi.studentclient.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/31.
 * 答题记录
 */
public class AnswerHistorybean implements Serializable{
    private String timeSpan;
    private String classWorkTitle;
    private String classWorkTime;
    private String classWorkDate;
    private Integer classWorkId;
    private int ItemType;

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getClassWorkTitle() {
        return classWorkTitle;
    }

    public void setClassWorkTitle(String classWorkTitle) {
        this.classWorkTitle = classWorkTitle;
    }

    public String getClassWorkTime() {
        return classWorkTime;
    }

    public void setClassWorkTime(String classWorkTime) {
        this.classWorkTime = classWorkTime;
    }

    public String getClassWorkDate() {
        return classWorkDate;
    }

    public void setClassWorkDate(String classWorkDate) {
        this.classWorkDate = classWorkDate;
    }

    public Integer getClassWorkId() {
        return classWorkId;
    }

    public void setClassWorkId(Integer classWorkId) {
        this.classWorkId = classWorkId;
    }
}
