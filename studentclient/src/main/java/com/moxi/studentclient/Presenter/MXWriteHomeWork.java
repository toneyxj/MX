package com.moxi.studentclient.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.model.OptionModel;
import com.mx.mxbase.constant.Constant;

import db.SQLBookUtil;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class MXWriteHomeWork {
    public void getExamsDetails(final Context context) {
        //26-----http://120.25.193.163/app/exercise/exerciseListByChapterId?courseChapterId=21038---21038
        final String url = Constant.GET_SYNC_EXAMS + "21038";

        final String response = SQLBookUtil.getInstance(context.getApplicationContext()).getExamsDetails("26", "21038");

        if (response != null && response.length() > 40) {
            ACache.get(context.getApplicationContext()).put(url, response);
        } else {
        }


    }
}
