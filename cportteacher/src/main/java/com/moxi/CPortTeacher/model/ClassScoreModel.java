package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassScoreModel {

    private int ranking;
    private String icon;
    private String name;
    private int currentTopic;
    private int totleTopic;
    private int studentId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(int currentTopic) {
        this.currentTopic = currentTopic;
    }

    public int getTotleTopic() {
        return totleTopic;
    }

    public void setTotleTopic(int totleTopic) {
        this.totleTopic = totleTopic;
    }

}
