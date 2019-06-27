package com.moxi.bookstore.http;

import com.alibaba.fastjson.JSON;
import com.moxi.bookstore.bean.Message.BaseMeaasge;
import com.moxi.bookstore.http.listener.BackMassage;
import com.moxi.bookstore.modle.vo.MCartListResponseVO;
import com.mx.mxbase.constant.APPLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by xiajun on 2019/6/17.
 */

public class NewCartWorkManager {
    private static boolean isStart=false;
    public static  final String url="http://api.dangdang.com";
    /**
     * 加入购物车
     */
    public static void addCart(String token, String saleId, final BackMassage back){
        OkHttpUtils.get().url(url+"/shoppingcart/third/cart_append_products")
                .addParams("ct", "android")
                .addParams("session_id",token )
                .addParams("same_ebook_tip", "1")
                .addParams("product_ids",saleId+".1")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                    if (back!=null)back.onFail(e);
            }

            @Override
            public void onResponse(String response, int id) {
                APPLog.e("addCart",response);
                try{
                    BaseMeaasge msg= JSON.parseObject(response,BaseMeaasge.class);
                    if(msg.errorCode==0) {
                        if (back != null) back.onSucess("sucess");
                    }else {
                        if (back!=null)back.onFail(new Exception(msg.errorMsg));
                    }
                }catch ( Exception e){
                    if (back!=null)back.onFail(e);
                }
            }
        });
    }
    /**
     * 购物车列表
     */
    public static void cartList(String token, final BackMassage back){
        if (isStart)return;
        isStart=true;
        OkHttpUtils.get().url(url+"/shoppingcart/third/get_new_cart")
                .addParams("ct", "android")
                .addParams("session_id",token )
                .addParams("source", "topsir电子书")
                .addParams("resource","2")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isStart=false;
                    if (back!=null)back.onFail(e);
            }

            @Override
            public void onResponse(String response, int id) {
                isStart=false;
                APPLog.e("CartList",response);
                try{
                    MCartListResponseVO msg= JSON.parseObject(response,MCartListResponseVO.class);
                    if(msg.getErrorCode()==0) {
                        if (back != null) back.onSucess(msg);
                    }else {
                        if (back!=null)back.onFail(new Exception(msg.getErrorMsg()));
                    }
                }catch ( Exception e){
                    if (back!=null)back.onFail(e);
                }
            }
        });
    }
    /**
     * 取消勾选
     */
    public static void gouxuan(final String token, String item_ids, final BackMassage back){
        OkHttpUtils.get().url(url+"/shoppingcart/third/check_item")
                .addParams("ct", "android")
                .addParams("session_id",token )
                .addParams("item_ids",item_ids)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                APPLog.e("error",e.getMessage());
                    if (back!=null)back.onFail(e);
            }

            @Override
            public void onResponse(String response, int id) {
                APPLog.e("gouxuan",response);
                try{
                    BaseMeaasge msg= JSON.parseObject(response,BaseMeaasge.class);
                    if(msg.errorCode==0) {
//                        if (back != null) back.onSucess("sucess");
                        cartList(token,back);
                    }else {
                        if (back!=null)back.onFail(new Exception(msg.errorMsg));
                    }
                }catch ( Exception e){
                    if (back!=null)back.onFail(e);
                }
            }
        });
    }
    /**
     * 勾选
     */
    public static void quxiaoGouxuan(final String token, String item_ids, final BackMassage back){
        OkHttpUtils.get().url(url+"/shoppingcart/third/uncheck_item")
                .addParams("ct", "android")
                .addParams("session_id",token )
                .addParams("item_ids",item_ids)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                    if (back!=null)back.onFail(e);
            }

            @Override
            public void onResponse(String response, int id) {
                APPLog.e("quxiaoGouxuan",response);
                try{
                    BaseMeaasge msg= JSON.parseObject(response,BaseMeaasge.class);
                    if(msg.errorCode==0) {
//                        if (back != null) back.onSucess("sucess");
                        cartList(token,back);
                    }else {
                        if (back!=null)back.onFail(new Exception(msg.errorMsg));
                    }
                }catch ( Exception e){
                    if (back!=null)back.onFail(e);
                }
            }
        });
    }
    /**
     * 删除商品
     */
    public static void deleteData(final String token, String items , final BackMassage back){
        APPLog.e("deleteData-items",items);
        OkHttpUtils.get().url(url+"/shoppingcart/third/remove_items")
                .addParams("ct", "android")
                .addParams("session_id",token )
                .addParams("item_ids",items )
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                    if (back!=null)back.onFail(e);
            }

            @Override
            public void onResponse(String response, int id) {
                APPLog.e("deleteData",response);
                try{
                    BaseMeaasge msg= JSON.parseObject(response,BaseMeaasge.class);
                    if(msg.errorCode==0) {
//                        if (back != null) back.onSucess("sucess");
                        cartList(token,back);
                    }else {
                        if (back!=null)back.onFail(new Exception(msg.errorMsg));
                    }
                }catch ( Exception e){
                    if (back!=null)back.onFail(e);
                }
            }
        });
    }
}
