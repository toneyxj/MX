package com.moxi.taskteacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.utils.GetTeacherUserId;
import com.moxi.taskteacher.view.BottomLineTextview;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 2016/12/30.
 */
public class DataFragment extends BaseFragment implements View.OnClickListener {

    private BottomLineTextview bltAllData;
    private BottomLineTextview bltDownData;

    private DownLoadDataFragment downLoadDataFragment;
    private ResFragment resFragment;
    private List<Fragment> listDataf = new ArrayList<>();
    private FragmentTransaction ft;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_data, container, false);
        //初始化view的各控件
        isPrepared = true;

        bltAllData = (BottomLineTextview) view.findViewById(R.id.blt_data_all);
        bltDownData = (BottomLineTextview) view.findViewById(R.id.blt_data_down);

        bltDownData.setOnClickListener(this);
        bltAllData.setOnClickListener(this);
        bltDownData.setDrawLine(true);
        bltAllData.setDrawLine(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
        loadData();
    }

    @Override
    protected void loadData() {
        if (!isPrepared) {
            return;
        }
        downLoadDataFragment = new DownLoadDataFragment();
        resFragment = new ResFragment();
        listDataf.clear();
        listDataf.add(downLoadDataFragment);
        listDataf.add(resFragment);
        showFragment(0);
    }

    private void showFragment(int i) {
        String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
        if (teaId.equals("")) {
            Toastor.showToast(getActivity(), "检测到未登录，请先登录");
            return;
        } else {
            ft = fm.beginTransaction();
            for (int a = 0; a < listDataf.size(); a++) {
                if (i == a) {
                    if (listDataf.get(i).isAdded()) {
                        ft.show(listDataf.get(i));
                    } else {
                        ft.add(R.id.data_fragment, listDataf.get(i)).show(listDataf.get(i));
                    }
                } else {
                    ft.hide(listDataf.get(a));
                }
            }
            ft.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blt_data_down:
                bltAllData.setDrawLine(false);
                bltDownData.setDrawLine(true);
                showFragment(0);
                break;
            case R.id.blt_data_all:
                bltDownData.setDrawLine(false);
                bltAllData.setDrawLine(true);
                showFragment(1);
                break;
        }
    }

    /**
     * 返回监听
     */
    public void onKeyPressDown() {
        if (resFragment != null && !resFragment.isHidden()) {
            resFragment.onKeyPressDown();
        } else if (downLoadDataFragment != null && !downLoadDataFragment.isHidden()) {
            downLoadDataFragment.onKeyPressDown();
        } else {
            System.exit(0);
        }
    }

    //翻页
    public void onPageUp() {
        if (downLoadDataFragment != null && !downLoadDataFragment.isHidden()) {
            downLoadDataFragment.onPageUp();
        } else if (resFragment != null && !resFragment.isHidden()) {
            resFragment.onPageUp();
        }
    }

    public void onPageDown() {
        if (resFragment != null && !resFragment.isHidden()) {
            resFragment.onPageDown();
        } else if (downLoadDataFragment != null && !downLoadDataFragment.isHidden()) {
            downLoadDataFragment.onPageDown();
        }
    }
}
