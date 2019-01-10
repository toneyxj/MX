package com.moxi.CPortTeacher.model;

/**
 * Created by Administrator on 2016/11/4.
 */
public class VoteProgressModel {
    public int maxNumber=0;
    public int curNumber=0;
    public String name;

    public VoteProgressModel(int maxNumber, int curNumber, String name) {
        this.maxNumber = maxNumber;
        this.curNumber = curNumber;
        this.name = name;
    }


    public String getProgress(){
        return String.valueOf(curNumber+"/"+maxNumber);
    }
}
