package com.moxi.CPortTeacher.frament;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.WhiteBoard.CoursewareWhiteBoardFragment;
import com.moxi.CPortTeacher.frament.WhiteBoard.WhiteBoardFragment;
import com.moxi.CPortTeacher.weight.BottomLineTextview;

import butterknife.Bind;

/**白板区域主控制器
 * Created by Administrator on 2016/11/30.
 */
public class MainWhiteBoardFragment extends CportBaseFragment{
    private FragmentManager fm;
    private int flagPosition = -1;

    @Bind(R.id.draw_white_board)
    BottomLineTextview draw_white_board;
    @Bind(R.id.courseware_white_board)
    BottomLineTextview courseware_white_board;

    private WhiteBoardFragment whiteBoardFragment;
    private CoursewareWhiteBoardFragment coursewareWhiteBoardFragment;
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_main_white_board;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        draw_white_board.setOnClickListener(this);
        courseware_white_board.setOnClickListener(this);
        onClick(draw_white_board);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.white_area_add_page){
            whiteBoardFragment.onClick(v);
            return;
        }
        draw_white_board.setDrawLine(false);
        courseware_white_board.setDrawLine(false);
        switch (v.getId()) {
            case R.id.draw_white_board://手写白板
                if (whiteBoardFragment==null){
                    getHandler().sendEmptyMessageDelayed(1,200);
                }else {
                    showFragment(draw_white_board,1);
                }
                break;
            case R.id.courseware_white_board://课件白板
                showFragment(courseware_white_board,2);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                showFragment(draw_white_board,1);
                break;
            default:
                break;
        }
    }

    private void showFragment(BottomLineTextview textview, int position) {
        textview.setDrawLine(true);
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
        hideFragment(mFragmentTransaction);
        if (position==flagPosition)return;
        switch (position) {
            case 1:// 手写白板
                if (whiteBoardFragment == null) {
                    whiteBoardFragment = new WhiteBoardFragment();
                    mFragmentTransaction.add(R.id.main_white_board_fragment, whiteBoardFragment);
                } else {
                    mFragmentTransaction.show(whiteBoardFragment);
                }
                whiteBoardFragment.onRefuresh();
                break;
            case 2:// 课件白板
                if (coursewareWhiteBoardFragment == null) {
                    coursewareWhiteBoardFragment = new CoursewareWhiteBoardFragment();
                    mFragmentTransaction.add(R.id.main_white_board_fragment, coursewareWhiteBoardFragment);
                } else {
                    mFragmentTransaction.show(coursewareWhiteBoardFragment);
                }
                coursewareWhiteBoardFragment.onRefuresh();
                break;
            default:
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
        flagPosition = position;
        if (flagPosition==1){
            listener.ShowOrHideAdd(true);
        }else {
            listener.ShowOrHideAdd(false);
        }

    }

    @Override
    public void onRefuresh() {
        super.onRefuresh();
        if (listener!=null){
            if (flagPosition==1){
                listener.ShowOrHideAdd(true);
            }else {
                listener.ShowOrHideAdd(false);
            }
        }
    }

    /**
     * 隐藏fragment
     *
     * @param mFragmentTransaction
     */
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (whiteBoardFragment != null) {
            mFragmentTransaction.hide(whiteBoardFragment);
        }
        if (coursewareWhiteBoardFragment != null) {
            mFragmentTransaction.hide(coursewareWhiteBoardFragment);
        }

    }
    @Override
    public void onclickBack() {
        super.onclickBack();
        sendBackClick();
    }
    private void sendBackClick(){
        if (whiteBoardFragment != null&&!whiteBoardFragment.isHidden()) {
            whiteBoardFragment.onclickBack();
        }
        else if (coursewareWhiteBoardFragment != null&&!coursewareWhiteBoardFragment.isHidden()) {
            coursewareWhiteBoardFragment.onclickBack();
        }
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        CportBaseFragment fragment=getBaseFragment();
        if (null!=fragment){
            fragment.moveLeft();
        }
    }

    @Override
    public void moveRight() {
        super.moveRight();
        CportBaseFragment fragment=getBaseFragment();
        if (null!=fragment){
            fragment.moveRight();
        }
    }
    private CportBaseFragment getBaseFragment() {
        if (null != whiteBoardFragment && !whiteBoardFragment.isHidden()) {
            return whiteBoardFragment;
        } else if (null != coursewareWhiteBoardFragment && !coursewareWhiteBoardFragment.isHidden()) {
            return coursewareWhiteBoardFragment;
        }
        return null;
    }
    /**
     * 截屏
     */
    public Bitmap PrintScreen() {
       if (whiteBoardFragment!=null&&!whiteBoardFragment.isHidden()){
           return whiteBoardFragment.PrintScreen();
       }
        return null;
    }
}
