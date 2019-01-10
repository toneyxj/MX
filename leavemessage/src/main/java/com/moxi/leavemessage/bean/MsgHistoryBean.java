package com.moxi.leavemessage.bean;

import java.util.List;

/**
 * Created by qiaokeli on 16/12/21.
 */

public class MsgHistoryBean {
    /**
     * code : 0
     * msg : 获取成功
     * result : [{"id":7,"lastSendContent":"同学们好","lastSendTime":"2016-12-22 11:46:34","lastSendUser":1,"state":0,"u1Headimg":"1","u2Headimg":"1","uName1":"赵老师","uName2":"小强","user1":1,"user2":10},{"id":6,"lastSendContent":"同学们好","lastSendTime":"2016-12-22 11:46:34","lastSendUser":1,"state":0,"u1Headimg":"1","u2Headimg":"3","uName1":"赵老师","uName2":"小明","user1":1,"user2":9},{"id":5,"lastSendContent":"同学们好","lastSendTime":"2016-12-22 11:46:34","lastSendUser":1,"state":0,"u1Headimg":"1","u2Headimg":"2","uName1":"赵老师","uName2":"田七","user1":1,"user2":8},{"id":4,"lastSendContent":"老师好","lastSendTime":"2016-12-22 11:46:44","lastSendUser":7,"state":0,"u1Headimg":"1","u2Headimg":"1","uName1":"赵老师","uName2":"赵六","user1":1,"user2":7},{"id":3,"lastSendContent":"老师您好","lastSendTime":"2016-12-22 11:48:09","lastSendUser":6,"state":0,"u1Headimg":"1","u2Headimg":"3","uName1":"赵老师","uName2":"王五","user1":1,"user2":6}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }


    public static class ResultBean {
        /**
         * id : 7
         * lastSendContent : 同学们好
         * lastSendTime : 2016-12-22 11:46:34
         * lastSendUser : 1
         * state : 0
         * u1Headimg : 1
         * u2Headimg : 1
         * uName1 : 赵老师
         * uName2 : 小强
         * user1 : 1
         * user2 : 10
         */

        private int id;
        private String lastSendContent;
        private String lastSendTime;
        private int lastSendUser;
        private int state;
        private String u1Headimg;
        private String u2Headimg;
        private String uName1;
        private String uName2;
        private int user1;
        private int user2;

        private boolean isEdit;
        private boolean isDelEdit;

        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
        }

        public boolean isDelEdit() {
            return isDelEdit;
        }

        public void setDelEdit(boolean delEdit) {
            isDelEdit = delEdit;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLastSendContent() {
            return lastSendContent;
        }

        public void setLastSendContent(String lastSendContent) {
            this.lastSendContent = lastSendContent;
        }

        public String getLastSendTime() {
            return lastSendTime;
        }

        public void setLastSendTime(String lastSendTime) {
            this.lastSendTime = lastSendTime;
        }

        public int getLastSendUser() {
            return lastSendUser;
        }

        public void setLastSendUser(int lastSendUser) {
            this.lastSendUser = lastSendUser;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getU1Headimg() {
            return u1Headimg;
        }

        public void setU1Headimg(String u1Headimg) {
            this.u1Headimg = u1Headimg;
        }

        public String getU2Headimg() {
            return u2Headimg;
        }

        public void setU2Headimg(String u2Headimg) {
            this.u2Headimg = u2Headimg;
        }

        public String getUName1() {
            return uName1;
        }

        public void setUName1(String uName1) {
            this.uName1 = uName1;
        }

        public String getUName2() {
            return uName2;
        }

        public void setUName2(String uName2) {
            this.uName2 = uName2;
        }

        public int getUser1() {
            return user1;
        }

        public void setUser1(int user1) {
            this.user1 = user1;
        }

        public int getUser2() {
            return user2;
        }

        public void setUser2(int user2) {
            this.user2 = user2;
        }
    }
}
