package com.mx.homework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.homework.R;
import com.mx.homework.model.HomeWorkModel;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *
 */
public class HomeWorkActivity extends BaseActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    @Bind(R.id.custom_recycler_home_work)
    CustomRecyclerView customRecycler;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_back)
    TextView tvBaseBack;
    @Bind(R.id.ll_base_right)
    LinearLayout llBaseRight;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.tv_base_mid_title)
    TextView tvMidTitle;

    private GestureDetector gestureDetector = null;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_home_work;
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
        llBaseRight.setVisibility(View.VISIBLE);
        tvBaseBack.setText("首页");
        tvBaseRight.setText("作业状态");
        tvMidTitle.setText("课后作业");

        llBack.setOnClickListener(this);
        llBaseRight.setOnClickListener(this);

        //课后作业相关
        customRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        gestureDetector = new GestureDetector(this, this);
        customRecycler.setGestureDetector(gestureDetector);

        setHomeWorkAdapter();
    }

    private void setHomeWorkAdapter() {
        List<HomeWorkModel> listHW = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HomeWorkModel hwm = new HomeWorkModel();
            hwm.setChapter("第一章");
            hwm.setState(HomeWorkModel.DoneState.HADE_READ);
            hwm.setSubject("语文");
            hwm.setTitle("朱自清 春");
            hwm.setTeacher("大王");
            listHW.add(new HomeWorkModel());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.ll_base_right:
                Toastor.showToast(this, "作业状态");
                break;
        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
