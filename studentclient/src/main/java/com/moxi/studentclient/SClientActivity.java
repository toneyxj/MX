package com.moxi.studentclient;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.studentclient.Presenter.BasePersenter;
import com.moxi.studentclient.activity.HomeActivity;
import com.moxi.studentclient.interfacess.DrawToolListener;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.ConnectStateView;
import com.moxi.studentclient.view.DrawToolView;
import com.moxi.studentclient.view.TopLoginViewGroup;

import butterknife.Bind;

public abstract class SClientActivity extends CommandActivity implements DrawToolListener {
    public LinearLayout contentView;
    @Bind(R.id.titleTv)
    TextView titleTv;
    @Bind(R.id.contentTitleTv)
    TextView contentTitleTv;
    @Bind(R.id.timeTv)
    TextView timeTv;
    @Bind(R.id.tool_dv)
    DrawToolView toolDv;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;
    @Bind(R.id.userNameTv)
    TopLoginViewGroup userNameTv;
    @Bind(R.id.linkNameTv)
    ConnectStateView linkNameTv;


    private BasePersenter persenter;

    private Handler mHandler;

    private int second = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sclient);
//        ButterKnife.bind(this);
//        contentView = (LinearLayout) findViewById(R.id.content_ll);
//        mHandler = new Handler();
    }

    @Override
    public void initActivity(Bundle savedInstanceState) {
        contentView = (LinearLayout) findViewById(R.id.content_ll);
        mHandler = new Handler();
        toolDv.setOnDrawToolListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sclient;
    }

    protected void startTime(int startSecond) {
        timeTv.setVisibility(View.VISIBLE);
        second = startSecond;
        mHandler.postDelayed(runnable, 1000);
    }
    protected void inVisibleTime(){
        timeTv.setVisibility(contentView.INVISIBLE);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            second++;
            if (timeTv != null) {
                timeTv.setText(Utils.timeSecondfrom(second));
                startTime(second);
            }
        }
    };

    protected int endTime() {
        mHandler.removeCallbacks(runnable);
        return second;
    }


    @Override
    protected void onResume() {
        super.onResume();
        String action=CommandService.getLinkClassName();
        ((ConnectStateView)findViewById(R.id.linkNameTv)).setUpdate(action);
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            userNameTv.islogin = true;
            String name = UserInformation.getInstance().getUserInformation().name;
            userNameTv.updataUser(R.mipmap.boy_ico, name);
        } else {
            userNameTv.updataUser(R.mipmap.user_unlogin_ico, "未登录");
            finish();
        }

    }

    public void goBack(View v) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCommandAction(String action) {
        ((ConnectStateView)findViewById(R.id.linkNameTv)).setUpdate(action);
        persenter.onAction(action);
    }

    protected void ininView(String topTitle, BasePersenter persenter) {
        userNameTv.setTopTile(topTitle);
        contentTitleTv.setText(topTitle);
        this.persenter = persenter;
    }

    protected void setTitleHind(Boolean hind) {
        if (hind) {
            findViewById(R.id.relativeLayout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        }
    }

    protected void setTitle(String title) {
        if (contentTitleTv != null) {
            contentTitleTv.setText(title);
        }
    }


    @Override
    public abstract void withPen();

    @Override
    public abstract void withRubber();

    @Override
    public void doClassExam() {
        finish();
    }

    @Override
    public void doelimination() {
        finish();
    }

    @Override
    public void doResponder() {

    }

    @Override
    public void doVote() {

    }
}
