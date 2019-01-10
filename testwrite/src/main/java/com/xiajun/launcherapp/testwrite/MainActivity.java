package com.xiajun.launcherapp.testwrite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.moxi.handwritinglibs.ScrollWritePadView;

public class MainActivity extends Activity implements View.OnClickListener {
    private ScrollWritePadView pad_view;
    private Button shang;
    private Button xia;
    private Button shang_ye;
    private Button xia_ye;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pad_view = (ScrollWritePadView) findViewById(R.id.pad_view);
        shang = (Button) findViewById(R.id.shang);
        xia = (Button) findViewById(R.id.xia);
        shang_ye = (Button) findViewById(R.id.shang_ye);
        xia_ye = (Button) findViewById(R.id.xia_ye);

        xia.setOnClickListener(this);
        shang.setOnClickListener(this);
        shang_ye.setOnClickListener(this);
        xia_ye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == shang) {
            pad_view.addScreenY(-300);
        } else if (v==xia){
            pad_view.addScreenY(300);
        }else if (v==shang_ye){
            if (index==0)return;
            pad_view.saveWritePad("test");
            index--;
            pad_view.setScreenHeight(pad_view.getWidth(),1300+index*100);
            pad_view.setSaveCode("savecode.test"+index);
        }else if (v==xia_ye){
            if (index>=6)return;
            pad_view.saveWritePad("test");
            index++;
            pad_view.setScreenHeight(pad_view.getWidth(),1300+index*100);
            pad_view.setSaveCode("savecode.test"+index);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowManager wm = (WindowManager) MainActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
                pad_view.setScreenHeight(wm.getDefaultDisplay().getWidth(),1300+index*100);
                pad_view.onResume();
                pad_view.setSaveCode("savecode.test"+index);
            }
        }, 300);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pad_view.onPause();
        pad_view.onstop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
