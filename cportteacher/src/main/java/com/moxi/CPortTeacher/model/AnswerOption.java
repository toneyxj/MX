package com.moxi.CPortTeacher.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class AnswerOption {
    private String type;
    private List<Option> options;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

   public class Answer {
        private String id;
        private String desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public class Option {
        private List<Answer> ABCDS;

        public List<Answer> getABCDS() {
            return ABCDS;
        }

        public void setABCDS(List<Answer> ABCDS) {
            this.ABCDS = ABCDS;
        }
    }
}
