package com.moxi.classRoom;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.dialog.HitnDialog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.AlertDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/28.
 */
public abstract class BaseFragment extends Fragment implements RequestCallBack, View.OnClickListener {
    public Context context;
    public MyHandler mHandler;
    private boolean isfinish=false;

    private static class MyHandler extends Handler {

        WeakReference<BaseFragment> mReference = null;

        MyHandler(BaseFragment activity) {
            this.mReference = new WeakReference<BaseFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragment outer = mReference.get();
            if (outer == null || outer.isRemoving()) {
                Log.e("outer is null");
                return;
            }

            outer.handleMessage(msg);
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void handleMessage(Message msg) {
    }

    private HitnDialog dialog;

    public HitnDialog dialogShowOrHide(boolean is,String  hitn) {
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
        }
        if (is) {
            dialog = new HitnDialog(getActivity(), R.style.AlertDialogStyle,hitn);
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);

            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            try {
                dialog.show();
            }catch (Exception e){

            }
        }
        return dialog;
    }
    public HitnDialog getHitnDialog(){
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getMainContentViewId(), null);
        ButterKnife.bind(this, view);
        mHandler = new MyHandler(this);

        onFragmentCreated(inflater, container, savedInstanceState);
        /**
         * 视图树 观察视图的变化
         */
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                onViewInitChange();
                return true;
            }
        });
        return view;
    }

    /**
     * 当视图出事啊改变调用
     */
    public void onViewInitChange(){

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isfinish=true;
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
        }
    }

    protected abstract int getMainContentViewId();

    protected abstract void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 请求提交成功
     *
     * @param result 返回json字符串
     * @param code   code标记值
     */
    @Override
    public void onSuccess(String result, String code) {
        if (isfinish&&isDetached()||!isVisible())return;
        dialogShowOrHide(false,"");
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (isfinish&&isDetached()||!isVisible())return;
        dialogShowOrHide(false,"");
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
        new HttpPostRequest(context, this, valuePairs, code, Url, show).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        new HttpGetRequest(context, this, valuePairs, code, Url, show).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void insureDialog(String content, Object code, InsureOrQuitListener listener) {
        insureDialog("提示", content, code, listener);
    }

    public void insureDialog(String hitn, String content, Object code, InsureOrQuitListener listener) {
        insureDialog(hitn, content, "确定", "取消", code, listener);
    }

    public void insureDialog(String hitn, String content, String insure, String quit, final Object code, final InsureOrQuitListener listener) {
        //没有问题可以进行移动
        new AlertDialog(context).builder().setTitle(hitn).setCancelable(false).setMsg(content).
                setNegativeButton(insure, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.isInsure(code, true);
                        }
                    }
                }).setPositiveButton(quit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.isInsure(code, false);
                }
            }
        }).show();
    }
}
