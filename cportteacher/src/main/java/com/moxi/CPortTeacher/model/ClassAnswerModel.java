package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassAnswerModel {

    private String icon;
    private int state;
    private String ansed;
    private int currentTopic;
    private int totleTopic;

    private int usr_id;//用户id
    private String name;//用户名
    private int finish;//是否交卷

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
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

    public String getAnsed() {
        return ansed;
    }

    public void setAnsed(String ansed) {
        this.ansed = ansed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
