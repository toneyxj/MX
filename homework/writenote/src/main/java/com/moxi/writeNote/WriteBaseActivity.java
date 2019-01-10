package com.moxi.writeNote;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.mx.mxbase.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public abstract class WriteBaseActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        ActivityUtils.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ActivityUtils.getInstance().ClearActivity(this);
    }
}
