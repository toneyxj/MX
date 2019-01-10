package com.moxi.CPortTeacher.model.weedout;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * 淘汰答题进度监听model
 * Created by Archer on 16/11/12.
 */
public class FallWorkProgressModel extends BaseModel {
    private FallWorkProgress result;

    public FallWorkProgress getResult() {
        return result;
    }

    public void setResult(FallWorkProgress result) {
        this.result = result;
    }

    public class FallWorkProgress {
        private String commitCount;
        private int count;
        private int failedCount;
        private String failedList;
        private List<ProgressList> progessList;

        public String getCommitCount() {
            return commitCount;
        }

        public void setCommitCount(String commitCount) {
            this.commitCount = commitCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getFailedCount() {
            return failedCount;
        }

        public void setFailedCount(int failedCount) {
            this.failedCount = failedCount;
        }

        public String getFailedList() {
            return failedList;
        }

        public void setFailedList(String failedList) {
            this.failedList = failedList;
        }

        public List<ProgressList> getProgessList() {
            return progessList;
        }

        public void setProgessList(List<ProgressList> progessList) {
            this.progessList = progessList;
        }

        public class ProgressList {
            private int total;
            private int finish;
            private int count;
            private int usr_id;
            private String usr_headimg;
            private String usr_name;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getFinish() {
                return finish;
            }

            public void setFinish(int finish) {
                this.finish = finish;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getUsr_id() {
                return usr_id;
            }

            public void setUsr_id(int usr_id) {
                this.usr_id = usr_id;
            }

            public String getUsr_headimg() {
                return usr_headimg;
            }

            public void setUsr_headimg(String usr_headimg) {
                this.usr_headimg = usr_headimg;
            }

            public String getUsr_name() {
                return usr_name;
            }

            public void setUsr_name(String usr_name) {
                this.usr_name = usr_name;
            }
        }
    }
}
