package com.mx.teacher.entity;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class TeacherGridEntry {

    private int arrStatus;
    private String arrTime;
    private String chapter;
    private int id;
    private int howId;
    private int replyStatus;
    private int state;
    private String subject;
    private String teacher;
    private String title;

    public int getHowId() {
        return howId;
    }

    public void setHowId(int howId) {
        this.howId = howId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(int replyStatus) {
        this.replyStatus = replyStatus;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getArrStatus() {
        return arrStatus;
    }

    public void setArrStatus(int arrStatus) {
        this.arrStatus = arrStatus;
    }
}
