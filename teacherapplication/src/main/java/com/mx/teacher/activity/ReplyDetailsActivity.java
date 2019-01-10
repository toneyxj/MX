package com.mx.teacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.DateUtil;
import com.mx.mxbase.utils.GsonTools;
import com.mx.teacher.R;
import com.mx.teacher.adapter.ReplyResultAdapter;
import com.mx.teacher.entity.Test;

import java.text.NumberFormat;

import butterknife.Bind;

/**
 * 批复详情界面
 * Created by Archer on 16/9/25.
 */
public class ReplyDetailsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_mid_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_reply_result)
    RecyclerView recyclerView;
    @Bind(R.id.tv_subjective_right)
    TextView tvSubRight;
    @Bind(R.id.tv_teacher_desc)
    TextView tvDesc;
    @Bind(R.id.tv_complement_time)
    TextView tvComplementTime;

    private String homework = "";
    private Test test;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_reply_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        homework = this.getIntent().getStringExtra("home_work_json");
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("批复详情");

        //设置点击事件
        llBack.setOnClickListener(this);

        //初始化recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        parseHomeWorkJson(homework);
    }

    /**
     * 解析数据
     *
     * @param homeWork
     */
    private void parseHomeWorkJson(String homeWork) {
        test = GsonTools.getPerson(homeWork, Test.class);
        int count = 0;
        for (int i = 0; i < test.getResult().getExamList().size(); i++) {
            if (test.getResult().getExamList().get(i).getResult().equals(test.getResult().getExamList().get(i).getAnswer())) {
                count++;
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) count / (float) 6 * 100);
        tvSubRight.setText(result + "%");
        try {
            tvDesc.setText(test.getResult().getResult().getComment());
            tvComplementTime.setText(test.getResult().getResult().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            tvComplementTime.setText(DateUtil.getCurDateStr("yyyy-MM-dd HH:mm:ss"));
        }
        recyclerView.setAdapter(new ReplyResultAdapter(this, test));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
