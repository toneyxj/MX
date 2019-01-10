package com.moxi.classRoom;

import com.moxi.classRoom.connection.PushClient;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.base.MyApplication;

/**
 * Created by Administrator on 2016/10/25.
 */
public class RoomApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        PushClient.create();
        //初始化提示
        ToastUtils.getInstance().initToast(applicationContext);
        //初始化本地缓存数据库
        CacheDbUtils.getInstance();
    }
}
