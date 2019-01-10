package com.mx.home.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.RemoteException;

import com.morgoo.droidplugin.pm.PluginManager;
import com.mx.home.R;
import com.mx.home.utils.UnzipFromAssets;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.Log;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends BaseActivity implements ServiceConnection {

    @Override
    protected int getMainContentViewId() {
        if (Constant.IS_LOAD_HOME_IMAGER) {
            return R.layout.mx_activity_home;
        } else {
            return R.layout.mx_activity_dont_show_home;
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    public static PowerManager.WakeLock wakeLock = null;

    private void init() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, getClass()
                .getCanonicalName());
        if (null != wakeLock) {
            wakeLock.acquire();
        }
        if (PluginManager.getInstance().isConnected()) {
            Log.e("isConnected()", "init插件服务器连接成功");
            new SSSAsync().execute();
        } else {
            PluginManager.getInstance().addServiceConnection(this);
        }
    }

    class SSSAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            startLoadInner("");
            return null;
        }
    }

    class StartAsyncApp extends AsyncTask<Void, Void, Void> {

        private File apk;
        private PackageManager pm;

        public StartAsyncApp(File apk) {
            pm = HomeActivity.this.getPackageManager();
            this.apk = apk;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), 0);
                Log.e("doInstall", "doInstall");
                doInstall(apk, info);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    /**
     * 记载插件
     *
     * @param packageName
     */
    private void startLoadInner(String packageName) {
        Log.e("startLoadInner", "开始启动");
        while (true) {//等待sd卡准备好再开始加在插件
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                break;
            }
        }
        String dataPath = Environment.getExternalStorageDirectory() + Constant.UPDATE_PATH;
        File updateFile = new File(dataPath);
        String outPutPath = Environment.getExternalStorageDirectory() + Constant.UPDATE_ZIP_PATH;
        if (!updateFile.exists() || !share.getBoolean(Constant.FIRST_START_APP)) {
            Log.e("exists", "exists");
            try {
                UnzipFromAssets.unZip(this, "update.zip", outPutPath, true);
            } catch (IOException e) {
                e.printStackTrace();
                HomeActivity.this.finish();
                Log.e("IOException", "exists");
            }
        }
        File file = new File(outPutPath);
        File[] files = file.listFiles();
        if (files != null) {
            Log.e("files", "files");
            for (final File apk : files) {
                new StartAsyncApp(apk).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        PackageManager pm = HomeActivity.this.getPackageManager();
        Intent intent;
        while (true) {
            try {
                PluginManager.getInstance().installPackage(
                        Environment.getExternalStorageDirectory() + Constant.UPDATE_ZIP_PATH + "/mxmain.apk", 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            intent = pm.getLaunchIntentForPackage(Constant.MAIN_PACKAGE);
            if (intent != null) {
                break;
            }
        }
        Log.e("onPostExecute", "finish");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setCache(Constant.FIRST_START_APP, true);
        startActivity(intent);
        wakeLock.release();
        HomeActivity.this.finish();
        Constant.IS_LOAD_HOME_IMAGER = false;
    }

    /**
     * 安装插件
     *
     * @param apk
     */
    private synchronized void doInstall(File apk, PackageInfo info) {
        try {
            int re = PluginManager.getInstance().installPackage(apk.getAbsolutePath(), 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        PluginManager.getInstance().removeServiceConnection(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.e("onServiceConnected", "链接service成功");
        new SSSAsync().execute();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e("onServiceDisconnected", "链接service失败");
    }
}
