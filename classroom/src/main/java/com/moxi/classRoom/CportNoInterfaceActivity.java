package com.moxi.classRoom;

import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.base.lifecycle.BaseNoInterfaceActivity;
import com.mx.mxbase.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public abstract class CportNoInterfaceActivity extends BaseNoInterfaceActivity implements RequestCallBack {


    /**
     * 请求提交成功
     *
     * @param result 返回json字符串
     * @param code   code标记值
     */
    @Override
    public void onSuccess(String result, String code) {
        if (isfinish) return;
        dialogShowOrHide(false, "");
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (isfinish) return;
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
        new HttpPostRequest(this, this, valuePairs, code, Url, show).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        new HttpGetRequest(this, this, valuePairs, code, Url, show).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    }

    /**
     * 是否显示全屏
     * @param is
     */
    public void isFullScreen(boolean is) {
//        if (is) {
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            getWindow().setAttributes(lp);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        } else {
//            WindowManager.LayoutParams attr = getWindow().getAttributes();
//            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(attr);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
    }
}
