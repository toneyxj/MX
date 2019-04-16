package com.moxi.studentclient.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.CportNoInterfaceActivity;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.R;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.moxi.studentclient.widget.XEditText;
import com.mx.mxbase.base.MyApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LogingActivity extends CportNoInterfaceActivity implements View.OnClickListener {
    public static String OTTO_LOGING = "ottoLoging";
    @Bind(R.id.ll_base_back)
    LinearLayout ll_base_back;
    @Bind(R.id.tv_base_back)
    TextView tv_base_back;
    @Bind(R.id.tv_base_mid_title)
    TextView tv_base_mid_title;

    @Bind(R.id.photo_src)
    ImageView photo_src;
    @Bind(R.id.user_name)
    TextView user_name;

    @Bind(R.id.input_ip)
    XEditText input_ip;
    @Bind(R.id.xet_username)
    XEditText xet_username;
    @Bind(R.id.clear_inputname)
    ImageView clear_inputname;
    @Bind(R.id.xet_password)
    XEditText xet_password;

    @Bind(R.id.ll_remember_password)
    LinearLayout ll_remember_password;
    @Bind(R.id.change_save_password)
    ImageView change_save_password;
    private boolean isChange = true;
    @Bind(R.id.tv_find_back_password)
    TextView tv_find_back_password;

    @Bind(R.id.tv_login)
    TextView tv_login;
    @Bind(R.id.tv_register)
    TextView tv_register;

    @Override
    public void initActivity(Bundle savedInstanceState) {
        tv_base_back.setText("登录");
        tv_base_mid_title.setText("");
        ll_base_back.setOnClickListener(this);

        ll_remember_password.setOnClickListener(this);
        tv_find_back_password.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        clear_inputname.setOnClickListener(this);

        String ip = UserInformation.getInstance().getDstName();
        if (!TextUtils.isEmpty(ip)) {
            input_ip.setText(ip);
        }
        xet_username.setText(getUserName());

    }


    private String getUserName() {
        return MyApplication.preferences.getString("userName", "13900000001");
    }


    private void setUserName(String userName) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putString("userName", userName);
        editor.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                onBackPressed();
                break;
            case R.id.ll_remember_password:
                isChange = !isChange;
                change_save_password.setImageResource(isChange ? R.mipmap.mx_img_check_box_chosed : R.mipmap.mx_img_check_box_normal);
                break;
            case R.id.tv_find_back_password:
                break;
            case R.id.clear_inputname://清空输入用户名
                xet_username.setText("");
                break;
            case R.id.tv_login://登录
                String name = xet_username.getText().toString().trim();
                String address_IP = input_ip.getText().toString().trim();
                String password = xet_password.getText().toString().trim();
                if (address_IP.equals("")) {
                    ToastUtils.getInstance().showToastShort("请输入本地IP地址！");
                    return;
                }
                UserInformation.getInstance().setDstName(address_IP);
                if (name.equals("") || password.equals("")) {
                    ToastUtils.getInstance().showToastShort("输入不能为空！");
                    return;
                }
                setUserName(name);
                loging(name, address_IP, password);
                break;
            case R.id.tv_register://显示协议
                ToastUtils.getInstance().showToastShort("显示协议！");
                break;
            default:
                break;
        }
    }

    private void loging(String name, String ip, String pawword) {
        UserInformation.getInstance().setDstName(ip);
        Connector.ClearData();
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("mobile", name));
        values.add(new ReuestKeyValues("password", pawword));
        values.add(new ReuestKeyValues("type", "0"));
        //  dialogShowOrHide(true, "登录中...");
        getData(values, "登录", Connector.getInstance().loging, true);
    }

    @Override
    public void onSuccess(String result, String code) {
        //super.onSuccess(result, code);
        String name = "";
        String password = "";
        String address_IP = "";
        if (xet_username != null && xet_password != null && input_ip != null) {
            name = xet_username.getText().toString();
            password = xet_password.getText().toString();
            address_IP = input_ip.getText().toString();
        }
        try {
            JsonAnalysis.getInstance().setLogingInformation(name, password, address_IP, result);
            LogingActivity.this.finish();
        } catch (JSONException e) {
            ToastUtils.getInstance().showToastShort("登录数据解析失败");
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_loging;
    }

}
