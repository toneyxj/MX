package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class ProbtionActivity extends MyBaseActivity implements View.OnClickListener {
    @Bind(R.id.back_iv)
    ImageButton last_page;
    @Bind(R.id.next_iv)
    ImageButton next_page;
    @Bind(R.id.prob_tv)
    TextView content_tv;
    @Bind(R.id.index_tv)
    TextView index_tv;
    String content,temp;

    List<String> clist;
    int pageIndex=1,totalCount=1,pageCount=1;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_probtion;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        content=getIntent().getStringExtra("html");
        totalCount=content.length();
        pageCount=totalCount/800;
        if (pageCount%800>0)
            pageCount++;
        clist=new ArrayList<>();
        initListener();
    }

    private void initListener() {
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        updataTv();

    }

    private void updataTv(){
        int start=0;
        if (pageIndex>1)
            start=(pageIndex-1)*810;
        int end=start+810>totalCount? totalCount:start+810;

        String str=content.substring(start,end);
        //APPLog.e("content:"+str);
        content_tv.setText(str);
        index_tv.setText(pageIndex+"/"+pageCount);
        APPLog.e("tvcount:"+content_tv.getText().length());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                goPrivouce();
                break;
            case R.id.next_iv:
                goNext();
                break;

        }
    }

    private void goPrivouce(){
        if (pageIndex==1){
            ToastUtil("首页");
        }else{
            pageIndex--;
            updataTv();
        }

    }
    private void goNext() {
        if (pageIndex>pageCount-1){
            ToastUtil("尾页");
        }else {
            pageIndex++;
            updataTv();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            goPrivouce();
            return true;
        } else if ( keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            goNext();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
