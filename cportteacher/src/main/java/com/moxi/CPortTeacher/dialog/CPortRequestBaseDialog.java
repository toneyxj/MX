package com.moxi.CPortTeacher.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.view.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public abstract class CPortRequestBaseDialog extends CPortBaseDialog implements RequestCallBack{
    public CPortRequestBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    private ProgressDialog dialog;

    public ProgressDialog dialogShowOrHide(boolean is,String  hitn) {
        if (is) {

            dialog = new ProgressDialog(getContext());
            dialog.setMessage(hitn);
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            if(dialog!=null)
                dialog.dismiss();
            dialog=null;
        }
        return dialog;
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
        new HttpPostRequest(getContext(), this, valuePairs, code, Url,show).execute();
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
        new HttpGetRequest(getContext(), this, valuePairs, code, Url,show).execute();
    }

    public void insureDialog(String content,Object code,InsureOrQuitListener listener){
        insureDialog("提示",content,code,listener);
    }
    public void insureDialog(String hitn,String content,Object code,InsureOrQuitListener listener){
        insureDialog(hitn, content, "确定", "取消", code, listener);
    }
    public void insureDialog(String hitn,String content,String insure,String quit, final Object code, final InsureOrQuitListener listener){
        //没有问题可以进行移动
        new AlertDialog(getContext()).builder().setTitle(hitn).setCancelable(false).setMsg(content).
                setNegativeButton(insure, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.isInsure(code,true);
                        }
                    }
                }).setPositiveButton(quit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.isInsure(code,false);
                }
            }
        }).show();
    }
    @Override
    public void onSuccess(String result, String code) {
        if (!this.isShowing())return;
        dialogShowOrHide(false,"");
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (!this.isShowing())return;
        dialogShowOrHide(false,"");
    }
}
