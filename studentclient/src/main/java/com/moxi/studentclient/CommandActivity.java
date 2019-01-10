package com.moxi.studentclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.moxi.classRoom.CportNoInterfaceActivity;

public abstract class CommandActivity extends CportNoInterfaceActivity implements ServiceConnection, CommandService.OnLinkCallBack {

    private Intent commandServiceIt;
    private CommandService commandService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commandServiceIt = new Intent();
        commandServiceIt.setClass(this, CommandService.class);
        startService(commandServiceIt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(commandServiceIt, this, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    protected abstract void onCommandAction(String action);


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        commandService = ((CommandService.MyBinder) iBinder).getService();
        commandService.setCallBack(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onLink(String link) {
        //        if (commandService != null)
//            commandService.setAction(action);
    }

    @Override
    public void onAction(String action) {
        onCommandAction(action);
    }
}
