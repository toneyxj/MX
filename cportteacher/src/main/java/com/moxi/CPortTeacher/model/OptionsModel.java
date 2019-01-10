package com.moxi.CPortTeacher.model;

import java.util.List;

/**
 * Created by King on 2017/3/22.
 */

public class OptionsModel {

    /**
     * options : [{"ABCDS":[{"id":"A","desc":"\t\t\t\t\t\t\t\t\t\t\t\t\t组词\r\n\t\t\t\t\t\t\t\t\t\t\t\t"},{"id":"B","desc":"\t\t\t\t\t\t\t\t\t\t\t\t\t粗词\r\n\t\t\t\t\t\t\t\t\t\t\t\t"},{"id":"C","desc":"\t\t\t\t\t\t\t\t\t\t\t\t\t组粮\r\n\t\t\t\t\t\t\t\t\t\t\t\t"},{"id":"D","desc":"\t\t\t\t\t\t\t\t\t\t\t\t\t分粗\r\n\t\t\t\t\t\t\t\t\t\t\t\t"}]}]
     * type : 101101
     */

    private String type;
    private List<OptionsBean> options;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public static class OptionsBean {
        private List<ABCDSBean> ABCDS;

        public List<ABCDSBean> getABCDS() {
            return ABCDS;
        }

        public void setABCDS(List<ABCDSBean> ABCDS) {
            this.ABCDS = ABCDS;
        }

        public static class ABCDSBean {
            /**
             * id : A
             * desc : 													组词
             */

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
    }
}
