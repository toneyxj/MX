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
 * Created by Archer on 2016/12/21.
 */
public class HomeWorkFragment extends BaseFragment implements View.OnClickListener {

    private BottomLineTextview bltReview;
    private BottomLineTextview bltSend;

    private SendWorkFragment sendWorkFragment;
    private ReviewFragment reviewFragment;
    private List<Fragment> listFragment = new ArrayList<>();
    private FragmentTransaction ft;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_home, container, false);
        //初始化view的各控件
        isPrepared = true;
        bltReview = (BottomLineTextview) view.findViewById(R.id.blt_review);
        bltSend = (BottomLineTextview) view.findViewById(R.id.blt_send);
        //设置监听
        bltReview.setOnClickListener(this);
        bltSend.setOnClickListener(this);
        bltSend.setDrawLine(true);
        bltReview.setDrawLine(false);
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
        if (!isPrepared || !isVisible()) {
            return;
        }
        reviewFragment = new ReviewFragment();
        sendWorkFragment = new SendWorkFragment();
        listFragment.clear();
        listFragment.add(sendWorkFragment);
        listFragment.add(reviewFragment);
        showFragmentIndex(0);
    }

    /**
     * 显示fragment
     *
     * @param i 索引
     */
    private void showFragmentIndex(int i) {
        String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
        if (teaId.equals("")) {
            Toastor.showToast(getActivity(), "检测到未登录，请先登录");
            return;
        } else {
            ft = fm.beginTransaction();
            for (int a = 0; a < listFragment.size(); a++) {
                if (i == a) {
                    if (listFragment.get(i).isAdded()) {
                        ft.show(listFragment.get(i));
                    } else {
                        ft.add(R.id.work_fragment, listFragment.get(i)).show(listFragment.get(i));
                    }
                } else {
                    ft.hide(listFragment.get(a));
                }
            }
            ft.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blt_review:
                bltSend.setDrawLine(false);
                bltReview.setDrawLine(true);
                showFragmentIndex(1);
                break;
            case R.id.blt_send:
                bltSend.setDrawLine(true);
                bltReview.setDrawLine(false);
                showFragmentIndex(0);
                break;
            default:
                break;
        }
    }

    /**
     * 返回监听
     */
    public void onKeyPressDown() {
        if (sendWorkFragment != null && !sendWorkFragment.isHidden()) {
            sendWorkFragment.onKeyPressDown();
        } else if (reviewFragment != null && !reviewFragment.isHidden()) {
            reviewFragment.onKeyPressDown();
        }
    }

    //翻页
    public void onPageUp() {
        if (sendWorkFragment != null && !sendWorkFragment.isHidden()) {
            sendWorkFragment.onPageUp();
        } else if (reviewFragment != null && !reviewFragment.isHidden()) {
            reviewFragment.onPageUp();
        }
    }

    public void onPageDown() {
        if (sendWorkFragment != null && !sendWorkFragment.isHidden()) {
            sendWorkFragment.onPageDown();
        } else if (reviewFragment != null && !reviewFragment.isHidden()) {
            reviewFragment.onPageDown();
        }
    }
}
