package com.mx.home.application;

import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;
import com.mx.mxbase.base.BaseApplication;

import org.litepal.LitePalApplication;

/**
 * Created by Archer on 16/8/29.
 */
public class HomeApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        LitePalApplication.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }
}
