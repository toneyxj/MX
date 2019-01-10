package com.moxi.CPortTeacher.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.classRoom.information.UserInformation;

import butterknife.Bind;

/**
 * 无内容显示Fragment
 * Created by Administrator on 2016/11/8.
 */
public class NoContentFragment extends CportBaseFragment {
    @Bind(R.id.hitn_txt)
    TextView hitn_txt;
    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHitn_txt();
    }

    public void setHitn_txt(){
        if (hitn_txt==null)return;
        if (!UserInformation.getInstance().isLoging()){
            hitn_txt.setText("您还未登录！！");
        }else if(UserInformation.getInstance().getUserInformation().lesson!=1){
            hitn_txt.setText("您还未开始上课！！");
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_no_content;
    }

    @Override
    public void onClick(View v) {

    }
}
