package com.moxi.leavemessage.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by Administrator on 2016/12/21.
 */

public class StudentsFragment extends baseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnFlingListener {
    @Bind(R.id.gv)
    HSlidableGridView gv;
    @Bind(R.id.count_tv)
    TextView count_tv;
    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    TeacherGvAdapter adapter;
    JsonUtil mju;
    Context ctx;
    HttpGetRequest studentRequest;
    List<UserBean> list,tempList;
    boolean hasChecked=false;
    int dataCount=0,currentpage=1,pageCount;
    @Override
    public void initFragment(View view) {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        gv.setOnItemClickListener(this);
        gv.setOnFlingListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_students;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTeacher();
        mju= LeaveMessageApplication.getInstence().getJutil();
        tempList=new ArrayList<>();
        super.onCreate(savedInstanceState);

    }

    private void getTeacher() {
        ctx=getMyContext();
        studentRequest=new HttpGetRequest(getMyContext(),callback,null,"getTeach", URL.LEAVEMSG_CHOSE_STUDENT,true);
        studentRequest.execute();
        dialogShowOrHide(true,"加载中...");
    }

    RequestCallBack callback=new RequestCallBack() {
        @Override
        public void onSuccess(String result, String code) {
            hidDiolag();
            APPLog.e(result);
            list=mju.getUserbeanList(result);
            //list=mju.getTestList(TestStr.str);
            if (null!=list){
                iniTempList();
                count_tv.setText("学生名单（"+list.size()+"人）");
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

    private void iniTempList() {
        dataCount=list.size();
        pageCount=dataCount/56;
        if (dataCount%56>0)
            pageCount++;
        for (int i=0;i<(dataCount>56? 56:dataCount);i++){
            tempList.add(list.get(i));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                String str=getSelectUser();
                // TODO: 2016/12/22 选择发送
                if (hasChecked) {
//                    ToastUtils.getInstance().showToastShort("发送给："+str);
                    String str1=getSelectUser();
                    Intent chatIntent = new Intent();
                    chatIntent.putExtra("user", str1.substring(1));
                    chatIntent.setClass(getActivity(), ChatActivity.class);
                    startActivity(chatIntent);
                }else
                    ToastUtils.getInstance().showToastShort("请选择学生");
                getActivity().finish();
                break;
            case R.id.btn2:
                // TODO: 2016/12/22 全部发送
//                ToastUtils.getInstance().showToastShort("全部发送");
                String str1=getSelectAllUser();
                Intent chatIntent = new Intent();
                chatIntent.putExtra("user", str1.substring(1));
                chatIntent.setClass(getActivity(), ChatActivity.class);
                startActivity(chatIntent);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        UserBean item=list.get(i);
        item.isChecked=!item.isChecked;
        adapter.notifyDataSetChanged();
    }


    private String getSelectUser(){
        StringBuilder sb=new StringBuilder();
        for (UserBean item:list) {
            if (item.isChecked) {
                sb.append(","+item.getId());
                hasChecked=true;
            }
        }
        return sb.toString();
    }
    private String getSelectAllUser(){
        StringBuilder sb=new StringBuilder();
        for (UserBean item:list) {
                sb.append(","+item.getId());
        }
        return sb.toString();
    }

    @Override
    public void onLeftFling() {
        //下一页
        if (currentpage<pageCount){
            currentpage++;
            tempList.clear();
            for (int i=(currentpage-1)*56;i<(dataCount>currentpage*56?currentpage*56:dataCount);i++){
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
            for (int i=(currentpage-1)*56;i<currentpage*56;i++){
                tempList.add(list.get(i));
            }
            adapter.setData(tempList);
        }else
            ToastUtils.getInstance().showToastShort("首页");
    }
}
