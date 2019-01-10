package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/30.
 */

public class ThesugestionAnswerListModel {

//    "studentId": 4,
//            "headimg": "1",
//            "studentName": "张三",
//            "answer": "A"

    private int studentId ;
    private String headimg;
    private String studentName;
    private String answer;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
