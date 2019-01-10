package com.moxi.CPortTeacher.model.weedout;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 16/11/9.
 */
public class ExamsTitleModel extends BaseModel {
    private List<ExamsTitle> result;

    public List<ExamsTitle> getResult() {
        return result;
    }

    public void setResult(List<ExamsTitle> result) {
        this.result = result;
    }

    public class ExamsTitle{
        private String analysis;
        private String answer;
        private String knowledge;
        private int id;
        private String option;
        private String title;
        private int subjectId;
        private int type;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
