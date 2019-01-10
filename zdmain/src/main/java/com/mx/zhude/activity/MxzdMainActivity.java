package com.mx.zhude.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.mx.plugin.aidl.ILoadPlugin;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.DateUtil;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.zhude.R;
import com.mx.zhude.utils.PrepareCMS;
import com.onyx.android.sdk.device.EpdController;

import java.io.File;
import java.util.Date;

import butterknife.Bind;

public class MxzdMainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.img_zdmain_reader)
    ImageView imgReader;
    @Bind(R.id.img_zdmain_rili)
    ImageView imgRili;
    @Bind(R.id.img_zdmain_filemanager)
    ImageView imgFile;
    @Bind(R.id.img_zdmain_setting)
    ImageView imgSetting;
    @Bind(R.id.tv_date_week)
    TextView tvDateWeek;
    @Bind(R.id.mx_zd_img_continu_reader)
    ImageView imgContinuReader;
    @Bind(R.id.mx_zd_img_reader_zhude)
    ImageView imgZhudeRead;
    private String loadPugin = "";
    private boolean NEED_REFEESH = false;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_zdmain;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        imgFile.setOnClickListener(this);
        imgReader.setOnClickListener(this);
        imgRili.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        imgContinuReader.setOnClickListener(this);
        imgZhudeRead.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        tvDateWeek.setText(DateUtil.getCurDateStr("yyyy年MM月dd日") + "  " + DateUtil.getWeekOfDate(new Date()));
        if (NEED_REFEESH) {
            EpdController.invalidate(this.getWindow().getDecorView(), EpdController.UpdateMode.GC);
            NEED_REFEESH = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            dialogShowOrHide(false, "请稍后...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        NEED_REFEESH = true;
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private static final String ACTION_BIND_SERVICE = "android.mx.aidl.LOAD_PLUGIN";
    private ILoadPlugin iLoadPlugin;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iLoadPlugin = ILoadPlugin.Stub.asInterface(iBinder);
            try {
                iLoadPlugin.loadPluginFile(loadPugin);
                if (iLoadPlugin != null) {
                    unbindService(mServiceConnection);
                    iLoadPlugin = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iLoadPlugin = null;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mx_zd_img_continu_reader:
                String file = share.getString("mx_last_reader_file");
                if (file.equals("")) {
                    Toastor.showToast(this, "未检测到最近阅读书籍！");
                } else {
                    FileUtils.getInstance().openFile(this, new File(file));
                }
                break;
            case R.id.img_zdmain_reader:
//                try {
//                    Intent input = new Intent();
//                    ComponentName reader = new ComponentName("com.onyx", "com.onyx.content.browser.activity.LibraryActivity");
//                    input.setComponent(reader);
//                    startActivity(input);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                loadPugin = "reader.apk";
                startPlugin();
                break;
            case R.id.img_zdmain_rili:
                // TODO: 16/9/5 打开帮助文档
                new CopyAssetsAsnyc("help.pdf").execute();
                break;
            case R.id.mx_zd_img_reader_zhude:
                new CopyAssetsAsnyc("zhude.pdf").execute();
                break;
            case R.id.img_zdmain_filemanager:
                loadPugin = "filemanager.apk";
                startPlugin();
                break;
            case R.id.img_zdmain_setting:
                startActivity(new Intent(this, MXSettingActivity.class));
                break;
        }
    }

    /**
     * 启动插件
     */
    private void startPlugin() {
        dialogShowOrHide(true, "请稍后...");
        Intent intentService = new Intent(ACTION_BIND_SERVICE);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MxzdMainActivity.this.bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
    }

    class CopyAssetsAsnyc extends AsyncTask<Void, Void, Void> {

        private String fileName;

        public CopyAssetsAsnyc(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialogShowOrHide(true, "请稍后正在读取...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            File zhude = new File(Environment.getExternalStorageDirectory()
//                    + Constant.FILE_PATH + fileName);
//            if (!zhude.exists()) {
//                FileUtils.copyAssets(MxzdMainActivity.this, fileName, Environment.getExternalStorageDirectory()
//                        + Constant.FILE_PATH);
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            dialogShowOrHide(false, "请稍后...");
            try {
                File zhude = new File(Environment.getExternalStorageDirectory()
                        + Constant.FILE_PATH + fileName);
                new PrepareCMS(MxzdMainActivity.this).insertCMS(zhude.getAbsolutePath());
                FileUtils.getInstance().openFile(MxzdMainActivity.this, zhude);
            } catch (Exception e) {
                Toastor.showToast(MxzdMainActivity.this, "目标文件不存在！");
            }
        }
    }
}