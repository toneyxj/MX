package com.mx.homework.util;

import java.io.Serializable;
import java.util.List;

public class XZOption implements Serializable {
    public String type;
    public List<Options> options;

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public class Options implements Serializable {
        public String answer;
        public List<ABCDS> ABCDS;

        public List<Options.ABCDS> getABCDS() {
            return ABCDS;
        }

        public void setABCDS(List<Options.ABCDS> ABCDS) {
            this.ABCDS = ABCDS;
        }

        public class ABCDS implements Serializable {
            public String id;
            public String desc;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDesc() {
                return this.desc;
            }

        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return this.answer;
        }

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}