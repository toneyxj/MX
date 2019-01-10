package com.mx.homework.model;

import java.util.List;

/**
 * Created by zhengdelong on 16/9/23.
 */

public class SubjectAnswerInfoModel {

    private String answer;
    private List<SubjectModel> ans;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<SubjectModel> getAns() {
        return ans;
    }

    public void setAns(List<SubjectModel> ans) {
        this.ans = ans;
    }
}
