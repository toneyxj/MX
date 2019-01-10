package com.moxi.CPortTeacher.frament;

import android.content.Context;

import com.moxi.CPortTeacher.Cinterface.SettingTitleInterface;
import com.moxi.classRoom.BaseFragment;

/**
 * Created by Administrator on 2016/11/2.
 */
public abstract class CportBaseFragment extends BaseFragment {
    public boolean isfinish=false;
    /**
     * 设置事件监听
     */
    public SettingTitleInterface listener;
    private String title = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SettingTitleInterface) context;
        } catch (Exception e) {
        }
    }

    public void setTitle(String title) {
        this.title = title;
        onRefuresh();
    }

    public String getTitle() {
        return title;
    }

    public void onRefuresh() {
        if (listener!=null)
        listener.setTitle(title);
    }
    /**
     * 点击返回
     */
    public void onclickBack(){

    }
    public void moveLeft() {

    }

    public void moveRight() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isfinish=true;
    }
}
