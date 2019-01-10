package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 16/9/22.
 */

public class SubjectWrongModel {

    private boolean isMaxShow;
    private int wrongCount;
    private String subjectName;
    private int subjectId;

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isMaxShow() {
        return isMaxShow;
    }

    public void setMaxShow(boolean maxShow) {
        isMaxShow = maxShow;
    }
}
