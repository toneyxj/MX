package com.moxi.CPortTeacher.model;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/30.
 */

public class ThesugestionAnswerModel {

//    "question": {
//        "answer": "C",
//                "id": 10,
//                "option": 1,
//                "time": "2016-11-29 17:38:19",
//                "title": "3/3_20161129173819.jpg",
//                "type": 1,
//                "userId": 3


    private String answer;
    private int id;
    private int option;
    private String time;
    private String title;
    private int type;
    private int userId;

    private List<ThesugestionAnswerListModel> answerList;


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ThesugestionAnswerListModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<ThesugestionAnswerListModel> answerList) {
        this.answerList = answerList;
    }
}
