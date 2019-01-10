package com.moxi.classRoom.config;

import android.content.Intent;
import android.content.SharedPreferences;

import com.moxi.classRoom.RoomApplication;
import com.moxi.classRoom.serve.StatusReceiver;
import com.mx.mxbase.base.MyApplication;

/**
 * 登录状态信息
 * Created by Administrator on 2016/10/26.
 */
public class StatusInformation {
    //链接状态说明
    // 0：服务器链接中
    // 1：服务器链接成功，
    // 2：用户登录成功，长连接完成
    //3: 链接断开

    //-2:服务器链接异常
    //-3:服务器无法链接

    //-1：服务器链接失败,可重新链接

    // 初始化类实列
    private static StatusInformation instatnce = null;

    public static StatusInformation getInstance() {
        if (instatnce == null) {
            synchronized (StatusInformation.class) {
                if (instatnce == null) {
                    instatnce = new StatusInformation();
                }
            }
        }
        return instatnce;
    }

    public void setStatus(int status){
            SharedPreferences.Editor editor = RoomApplication.editor;
            editor.putInt("status", status);// 登录名
            editor.commit();// 提交
    }

    public int getStatus(){
        return  MyApplication.preferences.getInt("status", 0);
    }
    public void sendStatusRecevier(int status){
        Intent intent=new Intent(StatusReceiver.STATUS_RECEIVER);
        intent.putExtra("status",status);
        RoomApplication.applicationContext.sendBroadcast(intent);
    }

}
