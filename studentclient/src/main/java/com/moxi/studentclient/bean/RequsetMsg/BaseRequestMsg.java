package com.moxi.studentclient.bean.RequsetMsg;

/**
 * Created by Administrator on 2016/11/8.
 * 请求响应basemsg
 */

public class BaseRequestMsg {
    private int code;

    private String msg;

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
}
