package com.moxi.leavemessage.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moxi.leavemessage.bean.UserBean;
import com.moxi.leavemessage.bean.msg.UserBeanMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class JsonUtil {
    Gson gson;

    public JsonUtil() {
        this.gson = new Gson();
    }

    public List<UserBean> getUserbeanList(String jsonstr){

            if (null!=jsonstr) {
              UserBeanMsg  msg = gson.fromJson(jsonstr, UserBeanMsg.class);
                return msg.getResult();
            }else
                return null;

    }

    public List<UserBean> getTestList(String str){
        if (null!=str){
            List<UserBean> list =gson.fromJson(str,new TypeToken<List<UserBean>>(){}.getType());
            return list;
        }
        return null;
    }
}
