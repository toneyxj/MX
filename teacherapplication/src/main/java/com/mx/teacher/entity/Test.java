package com.mx.teacher.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Test extends DataSupport implements Serializable {
    public String msg;
    public int code;
    public Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}