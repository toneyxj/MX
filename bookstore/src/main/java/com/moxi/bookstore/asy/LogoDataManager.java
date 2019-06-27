package com.moxi.bookstore.asy;

import com.alibaba.fastjson.JSON;
import com.moxi.bookstore.modle.LogoData;
import com.moxi.bookstore.modle.LogoDataDb;
import com.moxi.bookstore.request.NetUtil;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.constant.APPLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by xiajun on 2019/6/26.
 */

public class LogoDataManager {
    // 初始化类实列
    private static LogoDataManager instatnce = null;
    private boolean isstart = false;
private long deleteTime=0;
    public static LogoDataManager getInstance() {
        if (instatnce == null) {
            synchronized (LogoDataManager.class) {
                if (instatnce == null) {
                    instatnce = new LogoDataManager();
                }
            }
        }
        return instatnce;
    }

    public  void saveData() {
        if (isstart) return;
        isstart = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long time=System.currentTimeMillis();
                    LogoDataDb dataDb = new LogoDataDb();
                    dataDb.banben = LogoData.getInstance().getBanben();
                    dataDb.event_id = LogoData.getInstance().getEvent_id();
                    dataDb.from_platform = LogoData.getInstance().getFrom_platform();
                    dataDb.user_id = LogoData.getInstance().getUser_id();
                    dataDb.permanent_id = LogoData.getInstance().getPermanent_id(time);
                    dataDb.time = time;
                    dataDb.save();
                    if (NetUtil.checkNetworkInfo(MyApplication.applicationContext)) {
                        int size = DataSupport.count(LogoDataDb.class);
                        APPLog.e("listSize="+size);
                        int limit=100;
                        if (size >= limit) {
                            //开始传参数
                            List<LogoDataDb> listData=DataSupport.limit(limit).find(LogoDataDb.class);
                            //删除以往记录
                            deleteTime=listData.get(listData.size()-1).time;
                            List<List<String>> list=new ArrayList<List<String>>();
                            for (LogoDataDb ldb:listData){
                                List<String> value=new ArrayList<String>();
                                value.add(LogoData.getInstance().getDevice_sn());
                                value.add(ldb.permanent_id);
                                value.add(ldb.user_id);
                                value.add(ldb.event_id);
                                value.add(ldb.from_platform);
                                value.add(ldb.banben);
                                list.add(value);
                            }
                            String arryjson= JSON.toJSONString(list);
                            String tines=String.valueOf(time).substring(0,10);
                            String token=LogoData.getInstance().getToken(listData.get(0),tines);
                            APPLog.e("log_data:"+arryjson);
                            APPLog.e("timestamp:"+tines);
                            APPLog.e("token:"+token);
                            //发送日志
                            OkHttpUtils.post().url("http://databack.dangdang.com/obook.php")
                                    .addParams("log_data", arryjson)
                                    .addParams("timestamp",tines )
                                    .addParams("token", token)
                                    .build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    APPLog.e("obook",e.getMessage());
                                    isstart = false;
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    APPLog.e("obook",response);
                                    isstart = false;
                                    deleteData(deleteTime);
                                }
                            });
                        }else {
                            isstart = false;
                        }
                    }else {
                        isstart = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isstart = false;
                }
            }
        }).start();
    }

    public void deleteData(long time) {
        DataSupport.deleteAll(LogoDataDb.class, "time<=?", String.valueOf(time));
    }


}
