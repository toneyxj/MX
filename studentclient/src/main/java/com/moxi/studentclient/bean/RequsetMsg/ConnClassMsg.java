package com.moxi.studentclient.bean.RequsetMsg;

import com.moxi.studentclient.bean.ConnClassResult;

/**
 * Created by Administrator on 2016/11/8.
 */

public class ConnClassMsg extends BaseRequestMsg {
    private ConnClassResult result;

    public ConnClassResult getResult() {
        return result;
    }

    public void setResult(ConnClassResult result) {
        this.result = result;
    }
}
