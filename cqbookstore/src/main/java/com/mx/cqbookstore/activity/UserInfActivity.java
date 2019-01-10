package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.MyApplication;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.http.bean.Userbean;
import com.mx.mxbase.utils.SharePreferceUtil;

import butterknife.Bind;

public class UserInfActivity extends MyBaseActivity {
    @Bind(R.id.input_phone)
    TextView phone;
    @Bind(R.id.input_password)
    TextView pwd;
    @Bind(R.id.input_email)
    TextView input_email;
    @Bind(R.id.regest_time)
    TextView regest_time;
    @Bind(R.id.quit)
    TextView quit;
    Userbean user;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_user_inf;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        user= MyApplication.getMyApplication().getUser();
        initView();
    }

    private void initView() {
        phone.setText(user.getMobilehone());
        pwd.setText(user.getPassword());
        input_email.setText(user.getEmail());
        regest_time.setText(user.getCreateDate());
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil("退出登录");
                MyApplication.getMyApplication().setUser(null);
                SharePreferceUtil.getInstance(UserInfActivity.this).setCache("userid","");
                startActivity(new Intent(UserInfActivity.this,LoginActivity.class));
                finish();
            }
        });
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
