package com.moxi.taskteacher;

import com.moxi.taskteacher.utils.ToastUtils;
import com.mx.mxbase.base.MyApplication;

/**
 * Created by Administrator on 2016/12/21.
 */
public class TaskApplication extends MyApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.getInstance().initToast(applicationContext);
    }
}
