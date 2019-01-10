package com.mx.cqbookstore.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.MyApplication;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.Userbean;
import com.mx.cqbookstore.view.XEditText;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.SharePreferceUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MyBaseActivity implements View.OnClickListener {
    @Bind(R.id.xet_username)
    XEditText username_et;
    @Bind(R.id.xet_password)
    XEditText password_et;
    @Bind(R.id.tv_login)
    TextView login_tv;
    @Bind(R.id.clear_inputname)
    ImageView clear_inputname;
    String localname,localpwd;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        login_tv.setOnClickListener(this);
        clear_inputname.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        localname=SharePreferceUtil.getInstance(this).getString("userphone");
        localpwd=SharePreferceUtil.getInstance(this).getString("userpwd");
        username_et.setText(localname);
        password_et.setText(localpwd);
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
    String uname,pwd;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.clear_inputname:
                username_et.setText(null);
                break;
        }
    }

    private void doLogin() {
        if (TextUtils.isEmpty(username_et.getText())){
            ToastUtil("未填写账户");
            return;
        }
        if (TextUtils.isEmpty(password_et.getText())){
            ToastUtil("未填写密码");
            return;
        }
        BaseMap pramers=new BaseMap();
        String ecoduser= getBase64Code(username_et.getText().toString());
        String ecodpwd=md5(password_et.getText().toString());
        APPLog.e("Base64:"+ecoduser);
        pramers.put("phone",ecoduser);
        pramers.put("password",ecodpwd);

        SoapRequest request=new SoapRequest(handler,sut,this,1, Config.ACTION_LOGIN);
        request.execute(pramers);
        showDialog("正在登陆...");
    }
    Userbean user;
    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS){
                APPLog.e("msg:"+decodBase64((String)msg.obj));
                user=jut.PreseLoginUser(decodBase64((String)msg.obj));
                if (null!=user){
                    // TODO: 2016/12/26 用户详情，sp保存用户
                    SharePreferceUtil.getInstance(LoginActivity.this).setCache("userphone",user.getMobilehone());
                    SharePreferceUtil.getInstance(LoginActivity.this).setCache("userpwd",password_et.getText().toString());
                    SharePreferceUtil.getInstance(LoginActivity.this).setCache("userid",user.getID());
                    ToastUtil("登录成功");
                    MyApplication.getMyApplication().setUser(user);
                    startActivity(new Intent(LoginActivity.this,UserInfActivity.class));
                    finish();
                }
            }else
                APPLog.e("登陆失败");
        }
    };

    private String getBase64Code(String str){
        return Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
    }
    private String decodBase64(String str){
        return new String(Base64.decode(str.getBytes(),Base64.DEFAULT));
    }
    public  String md5(String string) {

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

