package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.moxi.taskstudent.request.HttpGetRequest;
import com.moxi.taskstudent.request.HttpPostRequest;
import com.moxi.taskstudent.request.RequestCallBack;
import com.moxi.taskstudent.request.RequestKeyValues;
import com.moxi.taskstudent.taskInterface.MainInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */
public abstract class BaseLayout extends FrameLayout implements View.OnClickListener,RequestCallBack{
    public boolean isStop = false;
    public MainInterface mainInterface;
    public BaseLayout(Context context, MainInterface mainInterface) {
        super(context);
        initView();
        this.mainInterface=mainInterface;
    }
    private void initView() {
        View myView = LayoutInflater.from(getContext()).inflate(getLayout(), null);
        addView(myView);
        initLayout(myView);
    }
    abstract int getLayout();
    abstract void initLayout(View view);
   public  void moveLeft(){};
    public   void moveRight(){};
    /**
     * 请求提交成功
     *
     * @param result 返回json字符串
     * @param code   code标记值
     */
    @Override
    public void onSuccess(String result, String code) {
        if (isStop) return;
        dialogShowOrHide(false, "");
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (isStop) return;
        dialogShowOrHide(false, "");
    }

    /**
     * post请求
     *
     * @param valuePairs 请求参数值
     * @param code       本次请求标示
     * @param Url        请求路径
     * @param show       是否显示提示
     */
    public void postData(
            List<RequestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
        new HttpPostRequest(getContext(), this, valuePairs, code, Url, show).execute();
    }

    /**
     * get请求
     *
     * @param valuePairs 请求参数值
     * @param code       本次请求标示
     * @param Url        请求路径
     * @param show       是否显示提示
     */
    public void getData(
            List<RequestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
        new HttpGetRequest(getContext(), this, valuePairs, code, Url, show).execute();
    }

    public void dialogShowOrHide(boolean is,String  hitn) {
        mainInterface.onDialogShowOrHide(is,hitn);
    }

    @Override
    public void onClick(View v) {
       click(v);
    }
    void click(View v){}

    public void onStop(){
        isStop=true;
    }
}
