package com.moxi.CPortTeacher.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengdelong on 2016/11/8.
 */

public class TClassScoreModel {


    private int commitCount;
    private int count;
    private String useTime;
    private String uncommited;

    public String getUncommited() {
        return uncommited;
    }

    public void setUncommited(String uncommited) {
        this.uncommited = uncommited;
    }

    List<ClassScoreModel> data = new ArrayList<>();

    public int getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public List<ClassScoreModel> getData() {
        return data;
    }

    public void setData(List<ClassScoreModel> data) {
        this.data = data;
    }
}
