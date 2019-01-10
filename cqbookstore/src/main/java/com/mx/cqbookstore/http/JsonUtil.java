package com.mx.cqbookstore.http;

import android.util.Log;

import com.mx.cqbookstore.http.bean.Boughtbean;
import com.mx.cqbookstore.http.bean.DataEbookResouce;
import com.mx.cqbookstore.http.bean.EbookDetail;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mx.cqbookstore.http.bean.EbookWithOtherInf;
import com.mx.cqbookstore.http.bean.Orderbean;
import com.mx.cqbookstore.http.bean.Userbean;

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

    /**
     * 电子书列表
     * @param jsonstr
     * @return
     */
    public DataEbookResouce getBookList(String jsonstr){
        DataEbookResouce data=new DataEbookResouce();
        try {
            JSONObject obj=new JSONObject(jsonstr);
            String str= obj.getString("list");
            data.count=obj.getInt("count");
            if (null!=str) {
                Log.e("listjson:",str);
              data.list = gson.fromJson(str,  new TypeToken<List<EbookResouce>>(){}.getType());
                return data;
            }else
                return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 电子书详情
     * @param jsonstr
     * @return
     */
    public EbookDetail getEbDetial(String jsonstr){
        EbookDetail ed=null;
        ed=gson.fromJson(jsonstr,EbookDetail.class);
        return ed;
    }

    /**
     * 登录user
     * @param jsonstr
     * @return
     */
    public Userbean PreseLoginUser(String jsonstr){
        Userbean user=null;
        user=gson.fromJson(jsonstr,Userbean.class);
        return user;
    }

    public Orderbean preseOrder(String jsonstr){
        return gson.fromJson(jsonstr,Orderbean.class);
    }
    public List<EbookResouce> getResouces(String jsonstr){
        return gson.fromJson(jsonstr,new TypeToken<List<EbookResouce>>(){}.getType());
    }

    public EbookWithOtherInf preseEWO(String jsonstr){
        return gson.fromJson(jsonstr,EbookWithOtherInf.class);
    }

    public List<Boughtbean> preseBoughtList(String jsonstr){
        return gson.fromJson(jsonstr,new TypeToken<List<Boughtbean>>(){}.getType());
    }
}
