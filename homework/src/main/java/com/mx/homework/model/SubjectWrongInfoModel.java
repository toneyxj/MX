package com.mx.homework.model;

import java.util.List;

/**
 * Created by zhengdelong on 16/9/23.
 */

public class SubjectWrongInfoModel {

    //题目
    private String title;
    //选项
    private List<SubjectAnswerInfoModel> option;
    //题目解析
    private String analysis;
    //正确答案
    private String answer;
    //错误答案,为当前用户上次的作答
    private String wrongAnswer;
    //总共回答次数
    private int allCount;
    //正确率
    private int rightCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubjectAnswerInfoModel> getOption() {
        return option;
    }

    public void setOption(List<SubjectAnswerInfoModel> option) {
        this.option = option;
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

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }
}
