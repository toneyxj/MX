package com.moxi.studentclient.bean;

import com.mx.mxbase.model.BaseModel;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class WeekOutResult extends BaseModel {
    private WOResult result;

    public WOResult getResult() {
        return result;
    }

    public void setResult(WOResult result) {
        this.result = result;
    }

    public class WOResult {
        private String isRight;

        public String getIsRight() {
            return isRight;
        }

        public void setIsRight(String isRight) {
            this.isRight = isRight;
        }
    }
}
