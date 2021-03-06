package com.moxi.yuyinhecheng.YuYinmanager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by xiajun on 2019/5/10.
 */

public class YuYinManager {
    private Context context;
    private YuYinCallBack callBack;

    public void setCallBack(YuYinCallBack callBack) {
        this.callBack = callBack;
    }

    public YuYinManager(Context context) {
        this.context = context;
        //初始化广播监听
       IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MsgConfig.speek_send_error);
        intentFilter.addAction(MsgConfig.speek_send_over);
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        context.registerReceiver(receiver,intentFilter);
    }

    public void SendYuYinMsg(String msg) {
        if (!mBond) return;
        Message clientMessage = Message.obtain();
        clientMessage.what = MsgConfig.sendMsg;
        Bundle bundle = new Bundle();
        bundle.putString("data", msg);
        clientMessage.setData(bundle);
        try {
            serverMessenger.send(clientMessage);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止语音播放
     */
    public boolean stopYuYinMsg(){
        if (!mBond)return false;
        Message clientMessage = Message.obtain();
        clientMessage.what = MsgConfig.speekStop;
        try {
            serverMessenger.send(clientMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 链接成功
     */
    private boolean mBond = false;

    private Messenger serverMessenger;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接成功
            serverMessenger = new Messenger(service);
            Log.i("Main", "服务连接成功");
            mBond = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serverMessenger = null;
            mBond = false;
        }
    };

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (callBack==null)return;
            String what=intent.getAction();
            if (what.equals(MsgConfig.speek_send_over)){
                callBack.onYuYinOver();
            }else if (what.equals(MsgConfig.speek_send_error)){
                callBack.onYuYinFail(intent.getStringExtra("error"));
            }else if (what.equals(MsgConfig.speek_send_start)){
                callBack.onYuYinStart();
            }

        }
    };

    public void bindServiceInvoked() {
        Intent intent = new Intent();
        intent.setAction("com.moxi.yuyinhecheng.YuYinService");
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void onDestroy() {
        if (mBond) {
            stopYuYinMsg();
            context.unbindService(conn);
        }
        context.unregisterReceiver(receiver);
    }
}
