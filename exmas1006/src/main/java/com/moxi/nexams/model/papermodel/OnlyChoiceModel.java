package com.moxi.nexams.model.papermodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by King on 2017/4/11.
 */

public class OnlyChoiceModel implements Serializable {

    private List<PersonsBean> persons;

    public List<PersonsBean> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonsBean> persons) {
        this.persons = persons;
    }

    public static class PersonsBean implements Serializable {
        /**
         * id : A
         * desc : <span><spanstyle="font-size: 14px;font-family: 'TimesNewRoman'">200</span></span>
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
