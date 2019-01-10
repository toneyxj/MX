package com.moxi.taskteacher.activity;

import android.view.MotionEvent;
import android.view.View;


import com.moxi.taskteacher.request.HttpGetRequest;
import com.moxi.taskteacher.request.HttpPostRequest;
import com.moxi.taskteacher.request.RequestCallBack;
import com.moxi.taskteacher.request.ReuestKeyValues;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */
public abstract class TaskBaseActivity extends BaseActivity implements RequestCallBack {
    private boolean isStop = false;
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
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
        new HttpPostRequest(this, this, valuePairs, code, Url, show).execute();
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
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
        new HttpGetRequest(this, this, valuePairs, code, Url, show).execute();
    }

    /**
     * 点击其它地方关闭软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (StringUtils.isShouldHideInput(v, ev)) {
                StringUtils.closeIMM(this, v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
    }
}
