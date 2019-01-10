package com.moxi.taskteacher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.moxi.taskteacher.model.NewLoginUserModel;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.MXUamManager;
import com.mx.mxbase.utils.Toastor;

/**
 * Created by Archer on 2016/12/29.
 */
public class GetTeacherUserId {

    /**
     * 获取教师用户id
     *
     * @param context
     * @return
     */
    public static String getTeaUserId(Context context, boolean isJump) {
        String teacherId;
        String userJson = MXUamManager.querUserBId(context);
        if (!userJson.equals("")) {
            NewLoginUserModel userInfo = GsonTools.getPerson(userJson, NewLoginUserModel.class);
            teacherId = userInfo.getResult().getId() + "";
        } else {
            teacherId = "";
            if (isJump) {
                try {
                    Intent sound = new Intent();
                    sound.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ComponentName cnSound = new ComponentName("com.moxi.tuser", "com.mx.user.activity.MXLoginActivity");
                    sound.setComponent(cnSound);
                    context.startActivity(sound);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toastor.showToast(context, "启动失败，请检测是否正常安装");
                }
            }
        }
        return teacherId;
    }
}
