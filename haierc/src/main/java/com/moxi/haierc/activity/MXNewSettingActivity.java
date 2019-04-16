package com.moxi.haierc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.haierc.R;
import com.moxi.haierc.util.CheckVersionCode;
import com.moxi.updateapp.UpdateUtil;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.interfaces.YesOrNo;
import com.mx.mxbase.utils.AppUtil;
import com.mx.mxbase.utils.DeviceUtil;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.MXVersionManager;
import com.mx.mxbase.utils.StorageUtil;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import okhttp3.Call;

import static com.moxi.haierc.R.id.img_setting_logo;

/**
 * Created by Archer on 16/7/27.
 */
public class MXNewSettingActivity extends BaseActivity implements View.OnClickListener {
    private WifiManager mWifi;
    private TextView tvWifiName;
    @Bind(R.id.tv_setting_online_update)
    TextView tvUpdate;
    @Bind(R.id.tv_version_name)
    TextView tvVersionName;
    @Bind(R.id.tv_setting_no)
    TextView tvDeviceNo;
    @Bind(img_setting_logo)
    ImageView imgSettingLogo;
    @Bind(R.id.tv_cun_chu_info)
    TextView tvCunChuInfo;
    @Bind(R.id.update_tv)
    TextView tvneed;

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
    @SuppressLint("WifiManagerLeak")
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
        imgSettingLogo.setOnClickListener(this);

        //获取wifi信息
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String value= DeviceUtil.getDeviceSerial();
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (isfinish)return;
                        tvDeviceNo.setText("产品序列号:" +value);
                    }
                });
            }
        }).start();
        String saveName = MXVersionManager.queryReadFile(this);
        String versionLocal = AppUtil.getPackageInfo(this).versionName;
        String versionSave = saveName.substring(saveName.lastIndexOf("_") + 1);
        if (versionSave.equals("")) {
            versionSave = "0";
        }
        String localInt = versionLocal.substring(versionLocal.lastIndexOf("_") + 1);
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher mLocal = p.matcher(localInt);
        Matcher mSave = p.matcher(versionSave);
        int local = Integer.parseInt(mLocal.replaceAll("").trim());
        int save = Integer.parseInt(mSave.replaceAll("").trim());
        if (local >= save) {
            tvVersionName.setText("版本号:" + localInt);
        } else {
            tvVersionName.setText("版本号:" + versionSave);
        }
        CheckVersionCode.getInstance(this).checkframework(new YesOrNo() {
            @Override
            public void onYesOrNo(boolean is) {
                if (isfinish)return;
                tvneed.setVisibility(is?View.VISIBLE:View.GONE);
            }
        });
        if (Constant.isMoXi){
            imgSettingLogo.setImageResource(R.mipmap.mx_img_logo_1);
        }else {
            imgSettingLogo.setImageResource(R.mipmap.mx_img_logo_2);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    private void readSDCard(TextView textView) {
        if (isfinish)return;
        String[] paths = getSdPath();
        if (paths != null && paths.length >1) {
            long total = 17179869184l;
            long avail = StorageUtil.getRomAvailableSize(this);

            StatFs sf = new StatFs(paths[1]);
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();

            long extStorage = blockSize * blockCount;
            long extTotalTemp = (long) getInitGB(extStorage);
            textView.setText(StorageUtil.getPrintSize(total + extTotalTemp - (avail + availCount * blockSize)) + "/" + StorageUtil.getPrintSize(total + extTotalTemp));
        }
    }

    /**
     * 获取2的n次方GB数
     * @param size
     * @return
     */
    private double getInitGB(long size){
        double temp;
        if (size < 1024){
            return size;
        } else {
            size = size / 1024;
        }

        if (size < 1024) {
            return size * Math.pow(1024, 1);
        } else {
            size = size / 1024;
        }

        if (size < 1024) {
            return size * Math.pow(1024, 2);
        } else {
            size = size / 1024;
        }
        for (int i = 0; i < 10; i++) {
            temp = Math.pow(2, i);
            if (temp >= size) {
                size = (long) temp;
                break;
            }
        }
        return size * Math.pow(1024, 3);
    }

    /**
     * 获取所有外置存储器的目录
     * @return
     */
    private String[] getSdPath(){
        String[] paths;
        StorageManager manager = (StorageManager) this.getSystemService(STORAGE_SERVICE);
        try {
            Method methodGetPaths = manager.getClass().getMethod("getVolumePaths");
            paths = (String[]) methodGetPaths.invoke(manager);
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        wifiInfo.getSSID();
        if (mWifi.isWifiEnabled()) {
            if (wifiInfo.getSSID() == null||wifiInfo.getSSID().equals("0x")) {
                tvWifiName.setText("未连接");
            } else {
                tvWifiName.setText(wifiInfo.getSSID());
            }
        } else {
            tvWifiName.setText("未打开");
        }
        //组件赋初值
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isfinish)return;
                readSDCard(tvCunChuInfo);
            }
        }, 2000);
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

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    int times = 0;

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
                try{
                    Intent sound = new Intent();
                    ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.ebag.DeviceSoundSettingActivity");
                    sound.setComponent(cnSound);
                    startActivity(sound);
                }catch (Exception e){
                }
                break;
            //存储
            case R.id.ll_setting_save:
                Intent memory = new Intent(this, MXStorageSettingActivity.class);
                startActivity(memory);
                break;
            //输入法设置
            case R.id.ll_setting_input:
                try{
                    Intent input = new Intent();
                    ComponentName cnInput = new ComponentName("com.onyx", "com.onyx.ebag.InputMethodAndLanguageSettings");
                    input.setComponent(cnInput);
                    startActivity(input);
                }catch (Exception e){
                }

                break;
            //时间日期设置
            case R.id.ll_setting_date:
                Intent date = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(date);
                break;
            //其它
            case R.id.ll_setting_feedback:
//                Intent other = new Intent();
//                ComponentName cnOther = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SettingsActivity");
//                other.setComponent(cnOther);
//                startActivity(other);
                Intent other = new Intent(this, SettingActivity.class);
                startActivity(other);
                break;
            //应用
            case R.id.ll_setting_help:
                //电源管理
                try{
                    Intent intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.DisplaySettings");
                    startActivity(intent);
                }catch (Exception e){
                }
                break;
            case R.id.tv_setting_online_update:
                // TODO: 16/9/1
                OkHttpUtils.post().url(Constant.CHECK_LOWER_UPDATE).addParams("versionName", AppUtil.getPackageInfo(this).versionName).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isfinish)return;
                        Toastor.showToast(MXNewSettingActivity.this, "网络请求失败，请检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isfinish)return;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject aa = new JSONObject(jsonObject.getString("result"));
                            int lowerVersion = aa.getInt("baseCode");
                            if (lowerVersion > Constant.LOCAL_VIR_VERSION) {
                                Intent sound = new Intent();//com.onyx.content.browser.activity.OnyxOTAActivity
                                ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SimpleOTAActivity");
                                sound.setComponent(cnSound);
                                MXNewSettingActivity.this.startActivity(sound);
                            } else {
                                new UpdateUtil(MXNewSettingActivity.this, MXNewSettingActivity.this).startUpdate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new UpdateUtil(MXNewSettingActivity.this, MXNewSettingActivity.this).startUpdate();
                        }
                    }
                });
                break;
            //隐藏功能查看各个子模块版本号
            case img_setting_logo:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME || lastClickTime == 0) {
                    times++;
                    Log.e("点击了", "aaaaa" + times);
                    if (times == 5) {
                        queryListModelVersion();
                    }
                } else {
                    times = 0;
                }
                lastClickTime = currentTime;
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏功能查看各个子模块版本号
     */
    private void queryListModelVersion() {
        List<PackageInfo> packages = this.getPackageManager().getInstalledPackages(0);
        String tempVersion = "";
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packageName = packageInfo.packageName;
            if (packageName.startsWith("com.moxi")) {
                String temp = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString() + "\t" + packageInfo.versionName;
                tempVersion += temp + "\n";
            }
        }
        Log.e("所有自模块版本号:", tempVersion);
        new AlertDialog(this).builder().setCancelable(false).setTitle("当前子模块版本号").setMsg(tempVersion).
                setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }
}