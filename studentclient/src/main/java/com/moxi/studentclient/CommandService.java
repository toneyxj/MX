package com.moxi.studentclient;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.cooler.download.DownloadListener;
import com.cooler.download.DownloadManager;
import com.cooler.download.DownloadRequest;
import com.hp.hpl.sparta.Text;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.studentclient.bean.RequsetMsg.ConnClassMsg;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;


public class CommandService extends Service {
    private com.cooler.download.DownloadManager downloadManager;

    private OnLinkCallBack callBack;

    public interface OnLinkCallBack {
        public void onLink(String link);

        public void onAction(String action);
    }

    public void setCallBack(OnLinkCallBack callBack) {
        this.callBack = callBack;
    }

    public class MyBinder extends Binder {
        public CommandService getService() {
            return CommandService.this;
        }
    }

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        simulationSendMsg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void simulationSendMsg() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startCheckState();
            }
        }, 3 * 1000);
    }

    /**
     * 设置连接状态
     *
     * @param state
     */
    private void setLinkState(int state,String className) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putInt("Linkstate", state);
        if(!TextUtils.isEmpty(className)){
            editor.putString("LinkClassName", className);
        }
        editor.commit();
    }

    /**
     * 获取连接状态 0,已连接，－1未连接
     * @return
     */
    public static int getLinkState() {
        return MyApplication.preferences.getInt("Linkstate", -1);
    }

    /**
     * 获取当前上课科目
     * @return
     */
    public static String getLinkClassName() {
        return MyApplication.preferences.getString("LinkClassName", "");
    }

    private void startCheckState() {
        if (UserInformation.getInstance().getID() == -1 || UserInformation.getInstance().getUserInformation() == null) {
            if (callBack != null) {
                callBack.onAction("未连接");
            }
            setLinkState(-1,"未连接");
            simulationSendMsg();
            return;
        }
        if (-1 == NetWorkUtil.getConnectedType(this)) {
            if (callBack != null) {
                callBack.onAction("断网了");
            }
            setLinkState(-1,"断网了");
            simulationSendMsg();
            return;
        }

        List<ReuestKeyValues> values = new ArrayList<>();
        String url = Connector.getInstance().get_courrentClass;
        new HttpGetRequest(this, new RequestCallBack() {
            @Override
            public void onSuccess(String result, String code) {
                ConnClassMsg ccmsg = JsonAnalysis.getInstance().getConnState(result);
                String className = ccmsg.getResult().getSubject().getName();
                if (callBack != null) {
                    callBack.onAction(className);
                }
                setLinkState(0,className);
                simulationSendMsg();
            }

            @Override
            public void onFail(String code, boolean showFail, int failCode, String msg) {
                if (callBack != null) {
                    callBack.onAction("未连接");
                }
                setLinkState(-1,"未连接");
                simulationSendMsg();
            }
        }, values, "code", url, false).execute();

    }


    public CommandService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void setAction(String action) {
        Log.d("TAG", "CommandService" + action);
    }

    /**
     * @deprecated
     * 下载文件
     * @param url
     * @param loadPath
     * @param downloadListener
     */
    public void myDownloadManager(String url,String loadPath,DownloadListener downloadListener) {
        DownloadManager downloadManager = getDownloadManager();
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setRetryTime(5);
        downloadRequest.setDownloadListener(downloadListener);
        downloadRequest.setUrl(url);
        downloadRequest.setDestinationPath(loadPath);
        downloadManager.add(downloadRequest);
    }

    public void okHttpFile(){

    }

    private DownloadManager getDownloadManager() {
        if (downloadManager == null) {
            synchronized (CommandService.class) {
                downloadManager = new DownloadManager();
            }
        }
        return downloadManager;
    }


}
