package com.mx.cqbookstore;

import android.app.Application;

import com.mx.cqbookstore.http.JsonUtil;
import com.mx.cqbookstore.http.SoapUtil;
import com.mx.cqbookstore.http.bean.Userbean;
import com.mx.cqbookstore.http.imageloader.GlideUtils;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2016/12/21.
 */

public class MyApplication extends Application {
    JsonUtil mjsonUtil;
    SoapUtil msoapUtil;
    GlideUtils mglide;
    Userbean user;
    private static MyApplication mInstence;

    public static MyApplication getMyApplication(){
        return mInstence;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstence=this;
        initUtil();
    }

    public void setUser(Userbean u){
        this.user=u;
    }
    public Userbean getUser(){
        return user;
    }
    public JsonUtil getMjsonUtil() { return mjsonUtil;}
    public SoapUtil getMsoapUtil() { return msoapUtil;}
    public GlideUtils getMglide() { return mglide;}
    private void initUtil() {
        mjsonUtil=new JsonUtil();
        msoapUtil=new SoapUtil();
        mglide=GlideUtils.getInstance();
        LitePalApplication.initialize(mInstence);
    }


}
