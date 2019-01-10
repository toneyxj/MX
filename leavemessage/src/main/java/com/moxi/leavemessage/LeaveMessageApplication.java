package com.moxi.leavemessage;

import android.content.Context;

import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.leavemessage.utils.JsonUtil;
import com.mx.mxbase.base.BaseApplication;

/**
 * Created by Administrator on 2016/12/22.
 */

public class LeaveMessageApplication extends BaseApplication {
    public static Context applicationContext;
    private static LeaveMessageApplication instance;
    JsonUtil mjutil;
    public static LeaveMessageApplication getInstence(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        //initImageLoader(applicationContext);

        ToastUtils.getInstance().initToast(applicationContext);
        //初始化本地缓存数据库
        CacheDbUtils.getInstance();

//        exams = this.getResources().getStringArray(R.array.exams_name);
        //初始化litepal
//        LitePalApplication.initialize(applicationContext);
        initUtil();
    }

    private void initUtil() {
        mjutil=new JsonUtil();
    }
    public JsonUtil getJutil(){
        return mjutil;
    }
}
