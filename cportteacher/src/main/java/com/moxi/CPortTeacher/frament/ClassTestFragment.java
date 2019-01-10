package com.moxi.CPortTeacher.frament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.ClassTests.ClassRoomExamFragment;
import com.moxi.CPortTeacher.frament.ClassTests.ClassRoomTestFragment;
import com.moxi.CPortTeacher.frament.ClassTests.TestFragment;
import com.moxi.CPortTeacher.frament.ClassTests.TheSugestionFragment;
import com.moxi.CPortTeacher.frament.ClassTests.WeedOutAnswerFragment;
import com.moxi.CPortTeacher.weight.BottomLineTextview;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.Log;

import butterknife.Bind;

/**
 * 课堂测试
 * Created by Administrator on 2016/10/28.
 */
public class ClassTestFragment extends CportBaseFragment {
    public final static String code="ClassTestFragment";
    private FragmentManager fm;
    private int flagPosition = -1;

    @Bind(R.id.classroom_test)
    BottomLineTextview classroom_test;
    @Bind(R.id.classroom_exam)
    BottomLineTextview classroom_exam;
    @Bind(R.id.weed_out_answer)
    BottomLineTextview weed_out_answer;
    @Bind(R.id.the_sugestion)
    BottomLineTextview the_sugestion;

    private ClassRoomTestFragment classRoomTestFragment;
    private ClassRoomExamFragment classRoomExamFragment;
    private WeedOutAnswerFragment weedOutAnswerFragment;
    private TheSugestionFragment theSugestionFragment;
    private TestFragment testFragment;

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        classroom_test.setOnClickListener(this);
        classroom_exam.setOnClickListener(this);
        the_sugestion.setOnClickListener(this);
        weed_out_answer.setOnClickListener(this);

        //设置默认第一页
        if (classRoomTestFragment == null){
            classRoomTestFragment = new ClassRoomTestFragment(showFragmentCallBack);
        }
        showFragment(classRoomTestFragment,true,"classRoomTestFragment");
    }

    @Override
    public void onClick(View v) {
        classroom_test.setDrawLine(false);
        classroom_exam.setDrawLine(false);
        the_sugestion.setDrawLine(false);
        weed_out_answer.setDrawLine(false);
        switch (v.getId()) {
            case R.id.classroom_test://随堂测试
                showFragment(classroom_test,1);
                break;
            case R.id.classroom_exam://课堂考试
                showFragment(classroom_exam,2);
                break;
            case R.id.the_sugestion://现场出题
                showFragment(the_sugestion,3);
                break;
            case R.id.weed_out_answer://淘汰答题
                showFragment(weed_out_answer,4);
                break;
            default:
                break;
        }
    }
    private void showFragment(BottomLineTextview textview, int position) {
        textview.setDrawLine(true);
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
//        hideFragment(mFragmentTransaction);
        if (position==flagPosition)return;
        switch (position) {
            case 1:// 随堂考试
                if (classRoomTestFragment == null){
                    classRoomTestFragment = new ClassRoomTestFragment(showFragmentCallBack);
                }
                showFragment(classRoomTestFragment,false,"classRoomTestFragment");
                classRoomTestFragment.onRefuresh();
                break;
            case 2:// 课堂考试
                if (classRoomExamFragment == null){
                    classRoomExamFragment = new ClassRoomExamFragment();
                }
                showFragment(classRoomExamFragment,false,"classRoomExamFragment");
                classRoomExamFragment.onRefuresh();
                break;
            case 3:// 现场出题
                if (theSugestionFragment == null){
                    theSugestionFragment = new TheSugestionFragment(showFragmentCallBack);
                }
                showFragment(theSugestionFragment,false,"theSugestionFragment");
                theSugestionFragment.onRefuresh();
                break;
            case 4://  淘汰答题
                if (weedOutAnswerFragment == null){
                    weedOutAnswerFragment = new WeedOutAnswerFragment();
                }
                showFragment(weedOutAnswerFragment,false,"classRoomExamFragment");
                weedOutAnswerFragment.onRefuresh();
                break;
            default:
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
        flagPosition = position;

    }
    /**
     * 隐藏fragment
     *
     * @param mFragmentTransaction
     */
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (classRoomTestFragment != null) {
            mFragmentTransaction.hide(classRoomTestFragment);
        }
        if (classRoomExamFragment != null) {
            mFragmentTransaction.hide(classRoomExamFragment);
        }
        if (weedOutAnswerFragment != null) {
            mFragmentTransaction.hide(weedOutAnswerFragment);
        }
        if (theSugestionFragment != null) {
            mFragmentTransaction.hide(theSugestionFragment);
        }
    }

//    @Override
//    public void onDestroy() {
//        removeAllfragment();
//        super.onDestroy();
//    }
//    private void removeAllfragment(){
//        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
//        if (null!=classRoomTestFragment){
//            mFragmentTransaction.remove(classRoomTestFragment);
//        }
//        if (null!=classRoomExamFragment){
//            mFragmentTransaction.remove(classRoomExamFragment);
//        }
//        if (null!=weedOutAnswerFragment){
//            mFragmentTransaction.remove(weedOutAnswerFragment);
//        }
//        if (null!=theSugestionFragment){
//            mFragmentTransaction.remove(theSugestionFragment);
//        }
//        mFragmentTransaction.commitAllowingStateLoss();
//        classRoomTestFragment=null;
//        weedOutAnswerFragment=null;
//        classRoomExamFragment=null;
//        theSugestionFragment=null;
//    }
    @Override
    public void onclickBack() {
        super.onclickBack();
        sendBackClick();
    }
    //classRoomExamFragment.isVisible()
    private void sendBackClick(){
        APPLog.e("flagPosition"+flagPosition);
        if (classRoomTestFragment != null&&(flagPosition==1||flagPosition==-1)) {
            classRoomTestFragment.onclickBack();
        }
        else if (classRoomExamFragment != null&&flagPosition==2) {
            classRoomExamFragment.onclickBack();
        }
        else if (theSugestionFragment != null&&(flagPosition==3||flagPosition==-1)) {
            theSugestionFragment.onclickBack();
        }
        else if (weedOutAnswerFragment != null&&flagPosition==4) {
            weedOutAnswerFragment.onclickBack();
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_test;
    }

    ShowFragmentCallBack showFragmentCallBack = new ShowFragmentCallBack() {
        @Override
        public void show(Fragment fragment, String tag) {
            Log.d("class","called...");
//            ((CportBaseFragment)fragment).onclickBack();
            showFragment(fragment,false,tag);
        }
    };
    /**
     * 当前显示fragment
     */
    private void showFragment(Fragment fragment,boolean isFirstFragment,String tag){
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (isFirstFragment){
            classroom_test.setDrawLine(true);
            ft.add(R.id.class_room_main_fragment,fragment,tag);
        }else{
            ft.replace(R.id.class_room_main_fragment, fragment,tag);
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public interface ShowFragmentCallBack{
        public void show(Fragment fragment,String tag);
    }

    @Override
    public void onDestroyView() {
        Log.d("class","ClassTestFragment onDestroyView...");
        super.onDestroyView();
    }
}
