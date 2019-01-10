package com.moxi.taskteacher.model;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 2016/12/22.
 */
public class HomeWork extends BaseModel {
    private HomeWorkModel result;

    public HomeWorkModel getResult() {
        return result;
    }

    public void setResult(HomeWorkModel result) {
        this.result = result;
    }

    public class HomeWorkModel {
        private int fromRowId;
        private int page;
        private int pageCount;
        private int rowCount;
        private int rows;
        private List<WorkModel> list;

        public int getFromRowId() {
            return fromRowId;
        }

        public void setFromRowId(int fromRowId) {
            this.fromRowId = fromRowId;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getRowCount() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public List<WorkModel> getList() {
            return list;
        }

        public void setList(List<WorkModel> list) {
            this.list = list;
        }

        public class WorkModel {
            private String file;
            private int id;
            private String name;
            private String subjectName;
            private String teacherName;
            private String type;

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public String getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(String teacherName) {
                this.teacherName = teacherName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
