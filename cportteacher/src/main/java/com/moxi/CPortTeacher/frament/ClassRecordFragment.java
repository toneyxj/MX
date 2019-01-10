package com.moxi.CPortTeacher.frament;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.ClassRecord.ClassroomParticipationFragment;
import com.moxi.CPortTeacher.frament.ClassRecord.SetAQuestionMainFragment;
import com.moxi.CPortTeacher.weight.BottomLineTextview;

import butterknife.Bind;

/**
 * 课堂记录
 * Created by Administrator on 2016/10/28.
 */
public class ClassRecordFragment extends CportBaseFragment {
    private FragmentManager fm;
    private int flagPosition = -1;

    @Bind(R.id.set_question)
    BottomLineTextview set_question;
    @Bind(R.id.classroom_participation)
    BottomLineTextview classroom_participation;

    private SetAQuestionMainFragment setAQuestionMainFragment;
    private ClassroomParticipationFragment classroomParticipationFragment;
    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        set_question.setOnClickListener(this);
        classroom_participation.setOnClickListener(this);
        onClick(set_question);
    }

    @Override
    public void onClick(View v) {
        set_question.setDrawLine(false);
        classroom_participation.setDrawLine(false);
        switch (v.getId()) {
            case R.id.set_question://随堂测试
                showFragment(set_question,1);
                break;
            case R.id.classroom_participation://课堂考试
                showFragment(classroom_participation,2);
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
            case 1:// 随堂考试
                if (setAQuestionMainFragment == null) {
                    setAQuestionMainFragment = new SetAQuestionMainFragment();
                    mFragmentTransaction.add(R.id.class_record_main_fragment, setAQuestionMainFragment);
                } else {
                    mFragmentTransaction.show(setAQuestionMainFragment);
                }
                setAQuestionMainFragment.onRefuresh();
                break;
            case 2:// 课堂考试
                if (classroomParticipationFragment == null) {
                    classroomParticipationFragment = new ClassroomParticipationFragment();
                    mFragmentTransaction.add(R.id.class_record_main_fragment, classroomParticipationFragment);
                } else {
                    mFragmentTransaction.show(classroomParticipationFragment);
                }
                setAQuestionMainFragment.onRefuresh();
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
        if (setAQuestionMainFragment != null) {
            mFragmentTransaction.hide(setAQuestionMainFragment);
        }
        if (classroomParticipationFragment != null) {
            mFragmentTransaction.hide(classroomParticipationFragment);
        }

    }
    @Override
    public void onclickBack() {
        super.onclickBack();
        sendBackClick();
    }
    private void sendBackClick(){
        if (setAQuestionMainFragment != null&&!setAQuestionMainFragment.isHidden()) {
            setAQuestionMainFragment.onclickBack();
        }
        else if (classroomParticipationFragment != null&&!classroomParticipationFragment.isHidden()) {
            classroomParticipationFragment.onclickBack();
        }
    }
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_record;
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
        if (null != setAQuestionMainFragment && !setAQuestionMainFragment.isHidden()) {
            return setAQuestionMainFragment;
        } else if (null != classroomParticipationFragment && !classroomParticipationFragment.isHidden()) {
            return classroomParticipationFragment;
        }
        return null;
    }
}
