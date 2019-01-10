package com.moxi.CPortTeacher.model;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassExaminationModel {

    private String id_nums;
    private int id;
    private String analysis;
    private String answer;
    private String knowledge;
    private int subjectId;
    private String title;
    private int type;//type表示题型，0为选择题，1为主观题
    private String option;

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    private String suject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuject() {
        return suject;
    }

    public void setSuject(String suject) {
        this.suject = suject;
    }

    public String getId_nums() {
        return id_nums;
    }

    public void setId_nums(String id_nums) {
        this.id_nums = id_nums;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
