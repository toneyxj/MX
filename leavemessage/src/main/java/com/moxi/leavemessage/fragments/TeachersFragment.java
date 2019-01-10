package com.moxi.leavemessage.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.leavemessage.LeaveMessageApplication;
import com.moxi.leavemessage.R;
import com.moxi.leavemessage.URL;
import com.moxi.leavemessage.activity.ChatActivity;
import com.moxi.leavemessage.adapter.TeacherGvAdapter;
import com.moxi.leavemessage.bean.UserBean;
import com.moxi.leavemessage.interfaces.OnFlingListener;
import com.moxi.leavemessage.utils.JsonUtil;
import com.moxi.leavemessage.view.HSlidableGridView;
import com.mx.mxbase.base.baseFragment;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class TeachersFragment extends baseFragment implements AdapterView.OnItemClickListener, OnFlingListener {
    @Bind(R.id.gv)
    HSlidableGridView gv;
    TeacherGvAdapter adapter;
    JsonUtil mju;
    Context ctx;
    HttpGetRequest teachRequest;
    List<UserBean> list,tempList;
    int dataCount=0,currentpage=1,pageCount;
    @Override
    public void initFragment(View view) {
        gv.setOnItemClickListener(this);
        gv.setOnFlingListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_teachers;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTeacher();
        mju=LeaveMessageApplication.getInstence().getJutil();
        tempList=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    private void getTeacher() {
        ctx=getMyContext();
        teachRequest=new HttpGetRequest(getMyContext(),callback,null,"getTeach", URL.LEAVEMSG_CHOSE_TEACH,true);
        teachRequest.execute();
        dialogShowOrHide(true,"加载中...");
    }

    RequestCallBack callback=new RequestCallBack() {
        @Override
        public void onSuccess(String result, String code) {
            hidDiolag();
            APPLog.e(result);
            list=mju.getUserbeanList(result);
            //list=mju.getTestList(TestStr.teach);
            if (null!=list){
                iniTempList();
                adapter=new TeacherGvAdapter(tempList,ctx);
                gv.setAdapter(adapter);
            }
        }

        @Override
        public void onFail(String code, boolean showFail, int failCode, String msg) {
            hidDiolag();
            APPLog.e("get teachers fail");
            ToastUtils.getInstance().showToastShort("获取失败");

        }
    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: 2016/12/22 选择发送老师
//        ToastUtils.getInstance().showToastShort("发送给"+list.get(i).getName());

        String str1=String.valueOf(list.get(i).getId());
        Intent chatIntent = new Intent();
        chatIntent.putExtra("user", str1);
        chatIntent.setClass(getActivity(), ChatActivity.class);
        startActivity(chatIntent);
        getActivity().finish();
    }

    private void iniTempList() {
        dataCount=list.size();
        pageCount=dataCount/12;
        if (dataCount%12>0)
            pageCount++;
        for (int i=0;i<(dataCount>12? 12:dataCount);i++){
            tempList.add(list.get(i));
        }
    }
    @Override
    public void onLeftFling() {
        //下一页
        if (currentpage<pageCount){
            currentpage++;
            tempList.clear();
            for (int i=(currentpage-1)*12;i<(dataCount>currentpage*12?currentpage*12:dataCount);i++){
                tempList.add(list.get(i));
            }
            adapter.setData(tempList);
        }else
            ToastUtils.getInstance().showToastShort("最后一页");
    }

    @Override
    public void onRightFling() {
        //上一页
        if (currentpage>1){
            currentpage--;
            tempList.clear();
            for (int i=(currentpage-1)*12;i<currentpage*12;i++){
                tempList.add(list.get(i));
            }
            adapter.setData(tempList);
        }else
            ToastUtils.getInstance().showToastShort("首页");
    }
}
