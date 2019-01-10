package com.moxi.classRoom;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生C端基本activity
 * Created by Administrator on 2016/10/26.
 */
public abstract class CPortActivity extends BaseActivity implements RequestCallBack{

    /**
     * 请求提交成功
     * @param result 返回json字符串
     * @param code code标记值
     */
    @Override
    public void onSuccess(String result, String code) {
        if (isfinish)return;
        dialogShowOrHide(false,"");
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (isfinish)return;
        dialogShowOrHide(false,"");
    }

    /**
     * post请求
     * @param valuePairs 请求参数值
     * @param code 本次请求标示
     * @param Url 请求路径
     * @param show 是否显示提示
     */
    public void postData(
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs==null)valuePairs=new ArrayList<>();
         new HttpPostRequest(this, this, valuePairs, code, Url,show).execute();
    }

    /**
     * get请求
     * @param valuePairs 请求参数值
     * @param code 本次请求标示
     * @param Url 请求路径
     * @param show 是否显示提示
     */
    public void getData(
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs==null)valuePairs=new ArrayList<>();
         new HttpGetRequest(this, this, valuePairs, code, Url,show).execute();
    }

}
