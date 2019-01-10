package com.mx.teacher.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Archer on 16/9/22.
 */
public class Result extends DataSupport implements Serializable {
    public List<ExamList> examList;

    private AResult result;

    public AResult getResult() {
        return result;
    }

    public void setResult(AResult result) {
        this.result = result;
    }

    public class AResult{
        private String answer;
        private String answerImg;
        private String comment;
        private int howId;
        private int id;
        private String time;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswerImg() {
            return answerImg;
        }

        public void setAnswerImg(String answerImg) {
            this.answerImg = answerImg;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getHowId() {
            return howId;
        }

        public void setHowId(int howId) {
            this.howId = howId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public List<ExamList> getExamList() {
        return examList;
    }

    public void setExamList(List<ExamList> examList) {
        this.examList = examList;
    }

}
