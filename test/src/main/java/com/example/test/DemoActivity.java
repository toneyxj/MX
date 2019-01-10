package com.example.test;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.PageFling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        ,PageFling {
    GridView gv;
    MuPDFReaderView mv;
    LinearLayout mv_ll;
    List<String> list;
    String basePath;
    GvAdapter adpter;

    private MuPDFCore core;
    int currentpage;
    String mFilePath,TAG="DemoActivty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        gv=(GridView) findViewById(R.id.gv);
        mv=(MuPDFReaderView) findViewById(R.id.mupdf_rv);
        mv_ll=(LinearLayout) findViewById(R.id.mv_ll);
        basePath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        String pdf="sample.pdf";
        String xps="xps.xps";
        list.add(basePath+pdf);
        list.add(basePath+xps);
        adpter=new GvAdapter(list,this);
        gv.setAdapter(adpter);
        gv.setOnItemClickListener(this);
    }


    public void onDestroy() {
        if (core != null)
            core.onDestroy();
        core = null;
        super.onDestroy();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path=list.get(position);
        if (null!=parent){
            core = openFile(Uri.decode(path));

            if (core != null && core.countPages() == 0) {
                core = null;
            }
            if (core == null || core.countPages() == 0 || core.countPages() == -1) {
                Log.e(TAG, "Document Not Opening");
            }
            if (core != null) {

                mv_ll.setVisibility(View.VISIBLE);
                mv.setPageFlyListener(DemoActivity.this);
                mv.setAdapter(new MuPDFPageAdapter(this, core));
                currentpage=1;
            }
        }else
            Toast.makeText(this,"没找到文件",Toast.LENGTH_SHORT).show();
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(this, path);
            Log.e("path:",path);
            Log.e("filePath:",mFilePath);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }

    @Override
    public void doFly(int index,int flyterword) {
        if (0==flyterword) {
            if (currentpage>1)
                currentpage--;
        }else {
            if (currentpage<core.countPages())
                currentpage++;
        }
        Log.e("dofly:",currentpage+"/"+core.countPages());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (mv_ll.getVisibility()==View.VISIBLE) {
                mv_ll.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
