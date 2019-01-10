package com.moxi.studentclient.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.studentclient.R;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.VoteBean;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.DrawToolView;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.APPLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 抢答
 * Created by Administrator on 2016/11/2.
 */

public class ResponderActivity extends Activity implements RequestCallBack{

    ImageView closer,result_iv,load_iv;
    boolean closeAniam=false;
    LinearLayout body;
    TextView infor_tv,no_RobAnswer;
    AnimationDrawable animation;

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_responding_layout);
        closer = (ImageView) findViewById(R.id.close_iv);
        body=(LinearLayout) findViewById(R.id.body_ly);
        result_iv=(ImageView) findViewById(R.id.result_iv);
        infor_tv=(TextView) findViewById(R.id.infor_tv);
        load_iv=(ImageView) findViewById(R.id.iv);
        no_RobAnswer=(TextView) findViewById(R.id.no_RobAnswer);
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHandler=new Handler();
    }


    @Override
    protected void onStart() {
        super.onStart();
        animation = (AnimationDrawable) load_iv.getBackground();
        animation.start();
        APPLog.e("load:"+load_iv.getVisibility());
        if (Utils.isLogin(this))
            getUser();
        doResponding();
    }
    UserBaseInformation user;
    long userId;
    private void getUser() {
        userId= UserInformation.getInstance().getID();
        user=UserInformation.getInstance().getUserInformation();
    }
    private void doResponding() {
        List<ReuestKeyValues> pramers=new ArrayList<>();
        pramers.add(new ReuestKeyValues("userId",userId+""));
        new HttpGetRequest(this,ResponderActivity.this,pramers,"doresp",
                Connector.getInstance().RobAnswerUrl,true).execute();
    }


    @Override
    protected void onStop() {
        setResult(DrawToolView.RESPON);
        super.onStop();
    }

    @Override
    public void onSuccess(String result, String code) {
        APPLog.e("success");
        boolean isSuccess=false;
        try {
            JSONObject msgobj = new JSONObject(result);
            JSONObject resultobj=msgobj.getJSONObject("result");
            int n=resultobj.getInt("isSuccess");
            isSuccess=(n==0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        animation.stop();
        load_iv.setVisibility(View.GONE);
        no_RobAnswer.setVisibility(View.GONE);
        if (isSuccess){
            result_iv.setImageResource(R.mipmap.respond_success);
            infor_tv.setText("恭喜，抢答成功!");
        }else {
            result_iv.setImageResource(R.mipmap.respond_fail);
            infor_tv.setText("抢答失败，再接再厉!");
        }
        body.setVisibility(View.VISIBLE);

        finishAtivity();
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        animation.stop();
        load_iv.setVisibility(View.GONE);
        no_RobAnswer.setText(msg);
        no_RobAnswer.setVisibility(View.VISIBLE);
        body.setVisibility(View.GONE);
        finishAtivity();
    }

    /**
     * 延迟3秒关闭
     */
    private void finishAtivity(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        },3*1000);
    }
}
