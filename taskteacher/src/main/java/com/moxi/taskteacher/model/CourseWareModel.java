package com.moxi.taskteacher.model;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 2016/12/23.
 */
public class CourseWareModel extends BaseModel {
    private CourseWare result;

    public CourseWare getResult() {
        return result;
    }

    public void setResult(CourseWare result) {
        this.result = result;
    }

    public class CourseWare {
        private List<CourseModel> list;

        public List<CourseModel> getList() {
            return list;
        }

        public void setList(List<CourseModel> list) {
            this.list = list;
        }

        public class CourseModel {
            private int id;
            private String file;
            private String name;
            private int state;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }
    }
}
