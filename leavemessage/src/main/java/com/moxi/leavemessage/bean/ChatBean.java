package com.moxi.leavemessage.bean;

import java.util.List;

/**
 * Created by qiaokeli on 16/12/22.
 */

public class ChatBean {
    /**
     * code : 0
     * msg : 数据获取成功
     * result : [{"catId":7,"content":"同学们好","createtime":"2016-12-22 11:46:34","id":null,"sendHeadimg":"1","sendUname":"赵老师","sendUser":1}]
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
         * catId : 7
         * content : 同学们好
         * createtime : 2016-12-22 11:46:34
         * id : null
         * sendHeadimg : 1
         * sendUname : 赵老师
         * sendUser : 1
         */

        private int catId;
        private String content;
        private String createtime;
        private Object id;
        private String sendHeadimg;
        private String sendUname;
        private int sendUser;

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getSendHeadimg() {
            return sendHeadimg;
        }

        public void setSendHeadimg(String sendHeadimg) {
            this.sendHeadimg = sendHeadimg;
        }

        public String getSendUname() {
            return sendUname;
        }

        public void setSendUname(String sendUname) {
            this.sendUname = sendUname;
        }

        public int getSendUser() {
            return sendUser;
        }

        public void setSendUser(int sendUser) {
            this.sendUser = sendUser;
        }
    }
}
