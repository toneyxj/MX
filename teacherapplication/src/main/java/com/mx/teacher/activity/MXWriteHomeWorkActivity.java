package com.mx.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.GsonTools;
import com.mx.teacher.R;
import com.mx.teacher.adapter.WriteHomeWorkAdapter;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.entity.Test;

import butterknife.Bind;

/**
 * Created by Archer on 16/9/12.
 */
public class MXWriteHomeWorkActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.custom_recycler_write_home_work)
    RecyclerView customRecyclerView;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_back)
    TextView tvBaseBack;
    @Bind(R.id.tv_base_mid_title)
    TextView tvMidTitle;
    @Bind(R.id.img_home_work_left)
    ImageView imgLeft;
    @Bind(R.id.img_home_work_right)
    ImageView imgRight;
    @Bind(R.id.tv_home_work_page_count)
    TextView tvPageCount;

    private WriteHomeWorkAdapter adapter;
    private String homeWork = "", url;
    private int page = 0, homeWorkState;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_write_home_work;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //初始化view
        llBack.setVisibility(View.VISIBLE);
        tvBaseBack.setText("课后作业");
        tvMidTitle.setText("重庆市初三下学期其中测试");

        //设置点击事件监听
        llBack.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        url = this.getIntent().getStringExtra("home_work_url");
        homeWorkState = this.getIntent().getIntExtra("home_work_state", -3);

        parseHomeWorkJson(url, page);

        customRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 解析课后作业json数据
     */
    private void parseHomeWorkJson(String url, int page) {
        homeWork = ACache.get(this).getAsString(url);
        if (homeWork.equals("")) {
            return;
        }
        Test test = GsonTools.getPerson(homeWork, Test.class);
        if (homeWorkState == -3) {
        }
        try {
            adapter = new WriteHomeWorkAdapter(this, url, test, homeWorkState, test.getResult().getExamList(), page);
            tvPageCount.setText((page + 1) + "/" + (test.getResult().getExamList().size() / 3 + 1));
            customRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == -1) {
                this.finish();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            if (page > 0) {
                page--;
                parseHomeWorkJson(url, page);
            }
            return true;
        } else if ( keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            if (page < 1) {
                page++;
                parseHomeWorkJson(url, page);
            } else {
                Intent intent = new Intent(this, MXSubjectiveActivity.class);
                intent.putExtra("home_work_url", url);
                intent.putExtra("home_work_json", homeWork);
                intent.putExtra("home_work_state", homeWorkState);
                startActivityForResult(intent, 1000);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.img_home_work_right:
                if (page < 1) {
                    page++;
                    parseHomeWorkJson(url, page);
                } else {
                    Intent intent = new Intent(this, MXSubjectiveActivity.class);
                    intent.putExtra("home_work_url", url);
                    intent.putExtra("home_work_json", homeWork);
                    intent.putExtra("home_work_state", homeWorkState);
                    startActivityForResult(intent, 1000);
                }
                break;
            case R.id.img_home_work_left:
                if (page > 0) {
                    page--;
                    parseHomeWorkJson(url, page);
                }
                break;
            default:
                break;
        }
    }
}
