package com.mx.mxbase.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 开启新的activity跳转
 * Created by xj on 2018/1/10.
 */

public class StartActivityUtils {
    public static final String UserPakege="com.moxi.user";//"com.moxi.mxuser"
    /**
     * 启动图片描图
     *
     * @param context     上下文
     * @param backImgPath 背景图路径
     * @param title       标题
     * @param titleShow   当前是否显示状态栏
     */
    public static void startPicPostil(Context context, String backImgPath, String title, boolean titleShow) {
        try {
            Intent input = new Intent();
            ComponentName cnInput = new ComponentName("com.moxi.writeNote", "com.moxi.writeNote.Activity.PicPostilActivity");
            input.setComponent(cnInput);

            Bundle bundle = new Bundle();
            bundle.putString("backImgPath", backImgPath);
            bundle.putString("title", title);
            bundle.putBoolean("titleShow", titleShow);
            input.putExtras(bundle);

            context.startActivity(input);
        } catch (Exception e) {
            ToastUtils.getInstance().showToastShort("没有安装此模块");
        }
    }
    /**
     * 启动个人中心登录绑定三方账号界面
     *
     * @param context     上下文
     */
    public static void startDDUerBind(Context context) {
        try {
            Intent input = new Intent();
            ComponentName cnInput = new ComponentName(StartActivityUtils.UserPakege, "com.mx.user.activity.DDUserBindActivity");

            input.setComponent(cnInput);
            input.putExtra("is_back",true);
            input.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(input);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.getInstance().showToastShort("没有安装此模块");
        }
    }


}
