package com.mx.teacher.entity;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 16/9/19.
 */
public class AllExamsModel extends BaseModel {
    private List<AllExams> result;

    public List<AllExams> getResult() {
        return result;
    }

    public void setResult(List<AllExams> result) {
        this.result = result;
    }

    public class AllExams {
        private int arrStatus;
        private String arrTime;
        private String chapter;
        private int id;
        private int replyStatus;
        private int state;
        private String subject;
        private String teacher;
        private String title;

        public int getArrStatus() {
            return arrStatus;
        }

        public void setArrStatus(int arrStatus) {
            this.arrStatus = arrStatus;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
