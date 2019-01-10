package com.moxi.studentclient.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.R;
import com.moxi.studentclient.adapter.VoteAdatpter;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.moxi.studentclient.model.VoteBean;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.DrawToolView;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票
 * Created by Administrator on 2016/11/3.
 */

public class VoteActivity extends Activity implements View.OnClickListener, RequestCallBack {
    TextView title,no_vote;
    GridView gv;
    TextView submite;
    ImageView closer;
    LinearLayout body_ly;
    VoteAdatpter adapter;
    int selectIndex = -1;


    List<VoteBean> beans;

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vote_layout);
        title = (TextView) findViewById(R.id.title_tv);
        no_vote=(TextView) findViewById(R.id.no_vote);
        body_ly=(LinearLayout) findViewById(R.id.body_ly);
        gv = (GridView) findViewById(R.id.gv);
        submite = (TextView) findViewById(R.id.submite_tv);
        closer = (ImageView) findViewById(R.id.close_iv);
        title.setText("投票");
        closer.setOnClickListener(this);
        submite.setOnClickListener(this);
        adapter = new VoteAdatpter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position,((VoteAdatpter)parent.getAdapter()).getData());
            }
        });


        mHandler=new Handler();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Utils.isLogin(this))
            getUser();
    }

    long userId;
    UserBaseInformation user;

    private void getUser() {

        userId= UserInformation.getInstance().getID();
        user=UserInformation.getInstance().getUserInformation();
        getVoteData();
    }

    private void getVoteData() {
        List<ReuestKeyValues> pramers=new ArrayList<>();
        new HttpGetRequest(this,this,pramers,"votedata", Connector.getInstance().getVoteStudents,true).execute();
    }

    private void selectItem(int index, List<VoteBean> data) {
        for (int i = 0; i < data.size(); i++) {
            VoteBean bean = data.get(i);
            if (index == i) {
                bean.checked = !(bean.checked);
                if (bean.checked)
                    selectIndex = index;
                else
                    selectIndex = -1;
            } else
                data.get(i).checked = false;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.submite_tv:
                if (-1 == selectIndex) {
                    Toastor.getToast(this, "未选择").show();
                    return;
                } else {
                    // TODO: 2016/11/3 submite
                    subVote();
                }
                break;
        }
    }

    private void subVote() {
        List<ReuestKeyValues> parmers=new ArrayList<>();
        parmers.add(new ReuestKeyValues("userId",userId+""));
        parmers.add(new ReuestKeyValues("voteOptionId",beans.get(selectIndex).id+""));
        new HttpGetRequest(this,this,parmers,"subVote",
                Connector.getInstance().subVoteUrl,true).execute();
    }


    @Override
    public void onSuccess(String result, String code) {
        if (code.equals("votedata")){
            beans=JsonAnalysis.getInstance().getVoteList(result);
            if (null==beans||0==beans.size()){
                // TODO: 2016/11/12 没有投票记录
                finishAtivity();
            }else {
                adapter.setData(beans);
                no_vote.setVisibility(View.GONE);
                body_ly.setVisibility(View.VISIBLE);
            }
        }else if (code.equals("subVote")){
            // TODO: 2016/11/11 投票成功
            ToastUtils.getInstance().showToastShort("投票成功,3秒后自动关闭");
            finishAtivity();
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (code.equals("votedata")){//投票请求返回
            no_vote.setVisibility(View.VISIBLE);
            body_ly.setVisibility(View.GONE);
        }
        finishAtivity();
    }


    @Override
    protected void onStop() {
        setResult(DrawToolView.VOTE);
        super.onStop();
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
