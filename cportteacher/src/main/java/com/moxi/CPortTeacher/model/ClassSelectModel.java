package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassSelectModel {

//    "email": "13911111111@qq.com",
//            "grade": "高一",
//            "headimg": "1",
//            "id": 4,
//            "lasttime": null,
//            "lesson": 0,
//            "mobile": "13911111111",
//            "name": "张三",
//            "online": 0,
//            "parentMobile": "13911111111",
//            "password": "e10adc3949ba59abbe56e057f20f883e",
//            "school": "重庆一中",
//            "subject": null,
//            "subjectId": null,
//            "type": 0


    private String headimg;
    private String email;
    private String grade;
    private int id;
    private int lesson;
    private String mobile;
    private String name;
    private int online;
    private String parentMobile;
    private String password;
    private String school;
    private int type;
    private int state;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getParentMobile() {
        return parentMobile;
    }

    public void setParentMobile(String parentMobile) {
        this.parentMobile = parentMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
