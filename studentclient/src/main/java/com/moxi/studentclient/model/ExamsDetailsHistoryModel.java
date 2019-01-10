package com.moxi.studentclient.model;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 16/9/29.
 */
public class ExamsDetailsHistoryModel extends BaseModel {

    private ExamsDetailsResult result;

    public ExamsDetailsResult getResult() {
        return result;
    }

    public void setResult(ExamsDetailsResult result) {
        this.result = result;
    }

    public class ExamsDetailsResult{
        private int totalScore;
        private List<ExamsDetails> exerciseList;

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public List<ExamsDetails> getExerciseList() {
            return exerciseList;
        }

        public void setExerciseList(List<ExamsDetails> exerciseList) {
            this.exerciseList = exerciseList;
        }
    }
}
