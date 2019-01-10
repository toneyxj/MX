package com.moxi.studentclient.model;

/**
 * Created by Archer on 16/10/14.
 */
public class WrongExamsModel {
    public String baocunid;
    public String examsDetails;
    public String subjectId;
    public String subjectName;
    public String cos_sem_id;
    public int count;

    public String getBaocunid() {
        return baocunid;
    }

    public void setBaocunid(String baocunid) {
        this.baocunid = baocunid;
    }

    public String getExamsDetails() {
        return examsDetails;
    }

    public void setExamsDetails(String examsDetails) {
        this.examsDetails = examsDetails;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCos_sem_id() {
        return cos_sem_id;
    }

    public void setCos_sem_id(String cos_sem_id) {
        this.cos_sem_id = cos_sem_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
