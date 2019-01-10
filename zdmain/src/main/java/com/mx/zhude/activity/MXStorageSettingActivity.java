package com.mx.zhude.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mx.mxbase.utils.StorageUtil;
import com.mx.zhude.R;
import com.mx.zhude.view.DonutProgress;

/**
 * Created by Archer on 16/7/28.
 */
public class MXStorageSettingActivity extends Activity implements View.OnClickListener {

    private DonutProgress donutProgress;
    private TextView tvUse, tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mx_activity_storage_setting);
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //初始化控件绑定
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress_storage);
        tvUse = (TextView) findViewById(R.id.tv_storage_already_use);
        tvEmpty = (TextView) findViewById(R.id.tv_storage_already_empty);

        //绑定点击事件
        findViewById(R.id.img_storage_back).setOnClickListener(this);
        findViewById(R.id.tv_storage_back).setOnClickListener(this);

        //获取内部存储信息
        String roAvail = StorageUtil.getRomAvailableSize(this);
        String roTotal = (float) (16.0 - Float.parseFloat(roAvail.substring(0, roAvail.indexOf("GB")))) + "GB";

        //控件赋初值
        String temp = roTotal.substring(0, roTotal.indexOf(".") + 3);
        donutProgress.setInnerBottomText(temp + "GB/16GB");
        donutProgress.setMax(1600);
        donutProgress.setProgress((int) ((float) (16.0 - Float.parseFloat(roAvail.substring(0, roAvail.indexOf("GB")))) * 100));
        float empty = 16 - Float.parseFloat(temp);
        tvEmpty.setText(empty + "GB");
        tvUse.setText(temp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_storage_back:
            case R.id.tv_storage_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}