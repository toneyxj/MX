package com.moxi.taskstudent;

import com.moxi.taskstudent.utils.ToastUtils;
import com.mx.mxbase.base.MyApplication;

/**
 * Created by Administrator on 2016/12/21.
 */
public class TaskApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化提示
        ToastUtils.getInstance().initToast(applicationContext);
    }
}
