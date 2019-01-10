package com.mx.cqbookstore.http;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mx.cqbookstore.activity.ResouceDetailActivity;
import com.mx.mxbase.constant.APPLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/12/20.
 */

public class SoapRequest extends AsyncTask<LinkedHashMap,Integer,String> {
    Handler handler;
    SoapUtil su;
    Context ctx;
    int reqflge;
    String action;
    Message msg=Message.obtain();

    public SoapRequest(Handler handler, SoapUtil su,Context ctx,int requstflag,String action) {
        this.handler = handler;
        this.su = su;
        this.ctx=ctx;
        this.reqflge=requstflag;
        this.action=action;
    }

    @Override
    protected String doInBackground(LinkedHashMap... params) {
        if (NetUtil.isNetworkAvailable(ctx)) {
            msg.arg1=reqflge;
            String result=su.doCall(params[0], handler,action);
            try {
                if (null==result){
                    msg.what=MyHandler.REQFEIL;
                    msg.obj="请求失败,请重试";
                    handler.sendMessage(msg);
                    return null;
                }
                JSONObject jsonObject = new JSONObject(result);
                // TODO: 2016/12/20 请求成功
                if (jsonObject.getBoolean("IsSuccess")) {
                    String content=jsonObject.getString("Content");
                    if (reqflge== ResouceDetailActivity.PROBTION){
                        APPLog.e("html:"+content);
                        content=content.replaceAll("\n|<head>|<html[^>]+>|<[!,?,l,b][^>]+>","").replaceAll("</h[^>]+>","\n");
                        content=content.replaceAll("<h2[^>]+>","\n");
                        //replaceAll("</[^>]+>","\n")
                        content=content.replaceAll("<p>","     ").replaceAll("</p>","\n").replaceAll("<[^>]+>","");
                        File file=new File(Config.DOWNPATH,"probtion.html");
                        try {
                            FileOutputStream fos=new FileOutputStream(file);
                            fos.write(content.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    return content;

                } else{
                    // isSuccess=false 请求失败
                    msg.what=MyHandler.REQFEIL;
                    msg.obj=jsonObject.getString("ErrorMes");
                    handler.sendMessage(msg);
                    return null ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //json解析错误
                handler.sendEmptyMessage(MyHandler.JSONFEIL);
                return null ;
            }

        }else {
            //无网络
            handler.sendEmptyMessage(MyHandler.NONET);
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if (s!=null) {
            msg.what=MyHandler.SUCEESS;
            msg.obj=s;
            handler.sendMessage(msg);
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
