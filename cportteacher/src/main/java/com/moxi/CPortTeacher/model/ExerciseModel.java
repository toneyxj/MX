package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/21.
 */

public class ExerciseModel {

//    "title": "What’s the meaning of “Help yourself.”?  A.帮助你。  B.随便吃吧。  C.你真行。",	<--题干
//    "knowledge": "交际用语",	<--知识点
//    "exerciseId": 19,		<--练习题ID
//    "answer": "B",			<--正确答案
//    "type": 0,				<--题型
//    "analysis": "",			<--解析

    private String idNums;
    private String title;
    private int exerciseId;
    private String answer;
    private String knowledge;
    private int type;
    private String analysis;


    public String getIdNums() {
        return idNums;
    }

    public void setIdNums(String idNums) {
        this.idNums = idNums;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
