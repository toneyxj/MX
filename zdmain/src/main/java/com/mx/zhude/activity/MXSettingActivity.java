package com.mx.zhude.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.mx.plugin.aidl.ILoadPlugin;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.AppUtil;
import com.mx.mxbase.utils.DeviceUtil;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Toastor;
import com.mx.zhude.R;
import com.mx.zhude.model.MXUpdateMsg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by Archer on 16/7/27.
 */
public class MXSettingActivity extends BaseActivity implements View.OnClickListener {

    private WifiManager mWifi;
    private TextView tvWifiName;
    @Bind(R.id.tv_setting_online_update)
    TextView tvUpdate;
    @Bind(R.id.tv_version_name)
    TextView tvVersionName;
    @Bind(R.id.tv_setting_no)
    TextView tvDeviceNo;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_setting;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //获取组件
        tvWifiName = (TextView) findViewById(R.id.tv_setting_wifi_name);

        //设置点击事件监听
        findViewById(R.id.ll_setting_wifi).setOnClickListener(this);
        findViewById(R.id.ll_setting_date).setOnClickListener(this);
        findViewById(R.id.ll_setting_feedback).setOnClickListener(this);
        findViewById(R.id.ll_setting_help).setOnClickListener(this);
        findViewById(R.id.ll_setting_input).setOnClickListener(this);
        findViewById(R.id.ll_setting_save).setOnClickListener(this);
        findViewById(R.id.ll_setting_sound).setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        //获取wifi信息
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        wifiInfo.getSSID();
        tvVersionName.setText("版本号:" + AppUtil.getPackageInfo(this).versionName);
        tvDeviceNo.setText("产品序列号:" + DeviceUtil.getDeviceSerial());

        //组件赋初值
        if (mWifi.isWifiEnabled()) {
            if (wifiInfo.getSSID() == null) {
                tvWifiName.setText("未连接");
            } else {
                tvWifiName.setText(wifiInfo.getSSID());
            }
        } else {
            tvWifiName.setText("未打开");
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

    }

    String test = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //wifi设置
            case R.id.ll_setting_wifi:
                Intent wifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(wifi);
                break;
            //声音设置
            case R.id.ll_setting_sound:
                Intent sound = new Intent();
                ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.ebag.DeviceSoundSettingActivity");
                sound.setComponent(cnSound);
                startActivity(sound);
                break;
            //存储
            case R.id.ll_setting_save:
                Intent memory = new Intent(this, MXStorageSettingActivity.class);
                startActivity(memory);
                break;
            //输入法设置
            case R.id.ll_setting_input:
                Intent input = new Intent();
                ComponentName cnInput = new ComponentName("com.onyx", "com.onyx.ebag.InputMethodAndLanguageSettings");
                input.setComponent(cnInput);
                startActivity(input);
                break;
            //时间日期设置
            case R.id.ll_setting_date:
                Intent date = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(date);
                break;
            //其它
            case R.id.ll_setting_feedback:
                Intent other = new Intent();
                ComponentName cnOther = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SettingsActivity");
                other.setComponent(cnOther);
                startActivity(other);
                break;
            //应用
            case R.id.ll_setting_help:
                //com.onyx/.content.browser.activity.ApplicationsActivity
                Intent app = new Intent();
                ComponentName cnApp = new ComponentName("com.onyx", "com.onyx.content.browser.activity.ApplicationsActivity");
                app.setComponent(cnApp);
                startActivity(app);
                break;
            case R.id.tv_setting_online_update:
                // TODO: 16/9/1

                HashMap<String, String> check = new HashMap<>();
                check.put("version", AppUtil.getPackageInfo(this).versionName);
                OkHttpUtils.getInstance().post().url(Constant.CHECK_VERSION).addParams("version", AppUtil.getPackageInfo(this).versionName).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        test = e.toString();
                        Toastor.showToast(MXSettingActivity.this, "请求数据失败,请检查相关配置");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        test = response;
                        try {
                            final MXUpdateMsg msg = GsonTools.getPerson(response, MXUpdateMsg.class);
                            if (msg.getResult().getVersion().equals(AppUtil.getPackageInfo(MXSettingActivity.this).versionName)) {
                                Intent sound = new Intent();//com.onyx.content.browser.activity.OnyxOTAActivity
                                ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SimpleOTAActivity");
                                sound.setComponent(cnSound);
                                startActivity(sound);
                            } else {
                                Intent sound = new Intent();
                                ComponentName cnSound = new ComponentName("com.mx.home", "com.mx.home.activity.MXUpdateActivity");
                                sound.setComponent(cnSound);
                                startActivity(sound);
//                                Intent update = new Intent(MXSettingActivity.this, MXUpdateActivity.class);
//                                update.putExtra("update_info", response);
//                                startActivity(update);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent sound = new Intent();
                            ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SimpleOTAActivity");
                            sound.setComponent(cnSound);
                            startActivity(sound);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private static final String ACTION_BIND_SERVICE = "android.mx.aidl.LOAD_PLUGIN";
    private ILoadPlugin iLoadPlugin;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iLoadPlugin = ILoadPlugin.Stub.asInterface(iBinder);
            try {
                if (iLoadPlugin.checkVersion(0)) {

                }
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

    private void startCheckVersion() {
        Intent intentService = new Intent(ACTION_BIND_SERVICE);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MXSettingActivity.this.bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
    }
}