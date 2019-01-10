package com.moxi.copybook;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.FileUtils;

import butterknife.Bind;

/**
 * 练字启动页
 */
public class PractiseCalligraphyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.show_title)
    TextView show_title;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.primary_text)
    LinearLayout primary_text;
    @Bind(R.id.middle_text)
    LinearLayout middle_text;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_practise_calligraphy;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        show_title.setOnClickListener(this);
        primary_text.setOnClickListener(this);
        middle_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_title:
                onBackPressed();
                break;
            case R.id.primary_text:
//testProdiver();
                PractiseCalligraphyDetailActivity.Start(this,0,"小学字帖",title.getText().toString());
//                initData("0");
                break;
            case R.id.middle_text:
                PractiseCalligraphyDetailActivity.Start(this,1,"中学字帖",title.getText().toString());
                break;
            default:
                break;
        }
    }
    private void testProdiver(){
        ContentResolver contentResolver=getContentResolver();
        Uri selecturi=Uri.parse("content://com.moxi.bookstore.provider/BookRack");
        Cursor cursor=contentResolver.query(selecturi, null, null, null, "publishtime DESC");
        if (cursor!=null)
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String searchContent = cursor.getString(1);
            long time = cursor.getLong(7);
            APPLog.e("id",id);
            APPLog.e("searchContent",searchContent);
            APPLog.e("time",time);

        }
    }
    private void initData(final String index){
        String fontPath = "font/kai_ti.TTF";
        String sdFilePath= FileUtils.getInstance().createMkdirs("font");
        sdFilePath+="kai_ti"+index+".ttf";
        new FileCopy(this, fontPath, sdFilePath,index , new FileCopy.CopyListener() {
            @Override
            public void CopyListener(boolean results, String path, String name) {
                APPLog.e("复制" + results + ",路径=" + path + ",名称=" + name);
                int indexs=Integer.parseInt(name);
                if (indexs>=1000)return;
                String names=String.valueOf(++indexs);
                initData(names);
            }
        }).execute();
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

}
