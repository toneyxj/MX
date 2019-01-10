package com.moxi.settinglib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.Calendar;

/**
 * Created by zhengdelong on 2016/11/15.
 */

public class SettingActivity extends Activity implements View.OnClickListener {

    private long lastClickTime = 0;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final String SETTINGS_PACKAGE_NAME = "com.android.settings";
    private static final String VCOM_INFO_FILE_PATH = "/sys/class/hwmon/hwmon0/device/vcom_value";

    LinearLayout ll_base_back;
    int times = 0;

    TextView tv_base_mid_title;
    TextView tv_base_back;

    //电源管理
    RelativeLayout yinsi_rel;
    //连续点击5次
    RelativeLayout kuozhan_rel;
    //隐私
    TextView yingsi_tv;
    //应用设置
    RelativeLayout kuozhan2_rel;
    //校准
    TextView jiaozhun;

    //VCOM
    RelativeLayout about_img_rel;
    //关于设备
    TextView about_shebei;
    RelativeLayout about_aouther;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        kuozhan_rel = (RelativeLayout) findViewById(R.id.kuozhan_rel);
        tv_base_back = (TextView) findViewById(R.id.tv_base_back);
        tv_base_back.setText("其它");
        ll_base_back = (LinearLayout) findViewById(R.id.ll_base_back);
        ll_base_back.setVisibility(View.VISIBLE);
        ll_base_back.setOnClickListener(this);
        tv_base_mid_title = (TextView) findViewById(R.id.tv_base_mid_title);
        tv_base_mid_title.setVisibility(View.GONE);
        yinsi_rel = (RelativeLayout) findViewById(R.id.yinsi_rel);
        kuozhan2_rel = (RelativeLayout) findViewById(R.id.kuozhan2_rel);
        about_img_rel = (RelativeLayout) findViewById(R.id.about_img_rel);
        yingsi_tv = (TextView) findViewById(R.id.yingsi_tv);
        jiaozhun = (TextView) findViewById(R.id.jiaozhun);
        about_shebei = (TextView) findViewById(R.id.about_shebei);
        about_aouther = (RelativeLayout) findViewById(R.id.about_renzheng);
        yinsi_rel.setOnClickListener(this);
        kuozhan2_rel.setOnClickListener(this);
        about_img_rel.setOnClickListener(this);
        yingsi_tv.setOnClickListener(this);
        jiaozhun.setOnClickListener(this);
        about_shebei.setOnClickListener(this);
        kuozhan_rel.setOnClickListener(this);
        about_aouther.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yinsi_rel) {
            //电源管理
            Intent intent = new Intent();
            intent.setClassName(SETTINGS_PACKAGE_NAME, "com.android.settings.DisplaySettings");
            startActivity(intent);
        } else if (v.getId() == R.id.yingsi_tv) {
            Intent intent = new Intent();
            intent.setClassName(SETTINGS_PACKAGE_NAME, "com.android.settings.PrivacySettings");
            startActivity(intent);
        } else if (v.getId() == R.id.kuozhan2_rel) {
            Intent intent = new Intent();
            intent.setClassName(SETTINGS_PACKAGE_NAME, "com.android.settings.ApplicationSettings");
            startActivity(intent);
        } else if (v.getId() == R.id.jiaozhun) {
            Intent intent = new Intent();
            intent.setClassName("com.onyx.android.tscalibration", "com.onyx.android.tscalibration.MainActivity");
            startActivity(intent);
        } else if (v.getId() == R.id.about_img_rel) {
            Toast.makeText(SettingActivity.this, getVcomInfo(), Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.about_shebei) {
            Intent intent = new Intent();
            intent.setClassName(SETTINGS_PACKAGE_NAME, "com.android.settings.DeviceInfoSettings");
            startActivity(intent);
        } else if (v.getId() == R.id.ll_base_back) {
            SettingActivity.this.finish();
        } else if (v.getId() == R.id.kuozhan_rel) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME || lastClickTime == 0) {
                times++;
                if (times == 5) {
                    kuozhan2_rel.setVisibility(View.VISIBLE);
                }
            } else {
                times = 0;
            }
            lastClickTime = currentTime;
        } else if (v.getId() == R.id.about_renzheng) {
            Intent intentAuthen = new Intent();
            intentAuthen.setClass(this, null);
            startActivity(intentAuthen);
        }
    }

    private String getVcomInfo() {
//        String vcom = FileUtil.readContentOfFile(new File(VCOM_INFO_FILE_PATH));
        String vcom = readFile(VCOM_INFO_FILE_PATH);
        return "VCOM:" + Double.valueOf(vcom) / 100 + " V";
    }


    private String readFile(String filepath) {
        String path = filepath;
        if (null == path) {
            return null;
        }

        String filecontent = null;
        File f = new File(path);
        if (f != null && f.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }

            CharBuffer cb;
            try {
                cb = CharBuffer.allocate(fis.available());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }

            InputStreamReader isr;
            try {
                isr = new InputStreamReader(fis, "utf-8");
                try {
                    if (cb != null) {
                        isr.read(cb);
                    }
                    filecontent = new String(cb.array());
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.d("content", "readFile filecontent = " + filecontent);
        return filecontent;
    }

}
