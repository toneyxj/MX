package com.moxi.studentclient.model;

/**
 * Created by Archer on 16/10/17.
 */
public class ChoseExamsModel {
    private ExamsDetails examsDetails;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ExamsDetails getExamsDetails() {
        return examsDetails;
    }

    public void setExamsDetails(ExamsDetails examsDetails) {
        this.examsDetails = examsDetails;
    }
}
