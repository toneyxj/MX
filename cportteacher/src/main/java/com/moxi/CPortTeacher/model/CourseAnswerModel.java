package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/21.
 */

public class CourseAnswerModel {

//    "studentId": 6,			<--学生用户ID
//    "exerciseId": 19,		<--练习题ID
//    "studentName": "王五",	<--学生姓名
//    "studentAnswer": "B",	<--学生作答答案
//    "studentScore": 10		<--学生本题得分

    private int studentId;
    private int exerciseId;
    private String studentName;
    private String studentAnswer;
    private int studentScore;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }
}
