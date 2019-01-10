package com.moxi.studentb;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moxi.studentb.view.DonutProgress;
import com.mx.mxbase.utils.StorageUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

        long total = 17179869184l;
        long avail = StorageUtil.getRomAvailableSize(this);
        String roAvail = StorageUtil.getPrintSize(avail);//StorageUtil.getRomAvailableSize(this)

        //控件赋初值
        donutProgress.setInnerBottomText(StorageUtil.getPrintSize(total - avail) + "B/16GB");
        donutProgress.setMax(total);
        donutProgress.setProgress(total - avail);
        tvEmpty.setText(roAvail + "B");
        tvUse.setText(StorageUtil.getPrintSize(total - avail) + "B");
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

    // 获得可用的内存
    public long getmem_UNUSED(Context mContext) {
        long MEM_UNUSED;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }

    // 获得总内存
    public long getmem_TOLAL() {
        long mTotal;
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int begin = content.indexOf(':');
        int end = content.indexOf('k');
        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }
}