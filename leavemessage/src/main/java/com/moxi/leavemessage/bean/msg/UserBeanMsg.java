package com.moxi.leavemessage.bean.msg;

import com.moxi.leavemessage.bean.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class UserBeanMsg {
    private int code;

    private String msg;

    private List<UserBean> result ;

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

    public List<UserBean> getResult() {
        return result;
    }

    public void setResult(List<UserBean> result) {
        this.result = result;
    }
}
