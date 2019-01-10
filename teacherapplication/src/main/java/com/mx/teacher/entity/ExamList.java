package com.mx.teacher.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Archer on 16/9/22.
 */
public class ExamList extends DataSupport implements Serializable {
    public int difficulty;
    public String result;
    public String answer;
    public int howId;
    public int id;
    public int state;
    public String analysis;
    public String title;
    public int type;
    public String updatetime;
    public String knowledge;
    public String option;

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setHowId(int howId) {
        this.howId = howId;
    }

    public int getHowId() {
        return this.howId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnalysis() {
        return this.analysis;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatetime() {
        return this.updatetime;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getKnowledge() {
        return this.knowledge;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }
}
