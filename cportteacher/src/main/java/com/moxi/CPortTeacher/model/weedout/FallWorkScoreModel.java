package com.moxi.CPortTeacher.model.weedout;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * 淘汰答题查看结果界面
 * Created by Archer on 16/11/12.
 */
public class FallWorkScoreModel extends BaseModel {
    private FallWorkScore result;

    public FallWorkScore getResult() {
        return result;
    }

    public void setResult(FallWorkScore result) {
        this.result = result;
    }

    public class FallWorkScore {
        private int count;
        private int failedCount;
        private String failedList;
        private List<ScoreList> scoreList;

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

        public List<ScoreList> getScoreList() {
            return scoreList;
        }

        public void setScoreList(List<ScoreList> scoreList) {
            this.scoreList = scoreList;
        }

        public class ScoreList {
            private int usr_id;
            private String usr_headimg;
            private String success;
            private String useTime;
            private String usr_name;

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

            public String getSuccess() {
                return success;
            }

            public void setSuccess(String success) {
                this.success = success;
            }

            public String getUseTime() {
                return useTime;
            }

            public void setUseTime(String useTime) {
                this.useTime = useTime;
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
