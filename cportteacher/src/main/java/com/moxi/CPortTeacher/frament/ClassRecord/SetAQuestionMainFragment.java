package com.moxi.CPortTeacher.frament.ClassRecord;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.ClassTests.ClassRoomTotalScoreFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.SetSubjectModel;

/**
 * Created by Administrator on 2016/11/15.
 */
public class SetAQuestionMainFragment extends CportBaseFragment implements SetAQuestionFragment.SubjectModelListener {
    private FragmentManager fm;
    private SetAQuestionFragment setAQuestionFragment;
    private ClassRoomTotalScoreFragment classRoomTotalScoreFragment;
    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        showFragment(1);
    }
    private void showFragment(int position) {
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
        switch (position) {
            case 1:// 随堂考试
                if (setAQuestionFragment == null) {
                    setAQuestionFragment = new SetAQuestionFragment(this);
                    mFragmentTransaction.add(R.id.class_record_set_a_question_fragment, setAQuestionFragment);
                } else {
                    mFragmentTransaction.show(setAQuestionFragment);
                }
                setAQuestionFragment.onRefuresh();
                break;
            case 2:// 随堂考试
                mFragmentTransaction.add(R.id.class_record_set_a_question_fragment, classRoomTotalScoreFragment);
                mFragmentTransaction.hide(setAQuestionFragment);
                break;
            default:
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public void onclickBack() {
        super.onclickBack();
        sendBackClick();
    }
    private void sendBackClick(){
        if (classRoomTotalScoreFragment!=null) {
            FragmentTransaction mFragmentTransaction = fm.beginTransaction();
            mFragmentTransaction.remove(classRoomTotalScoreFragment);
            mFragmentTransaction.commitAllowingStateLoss();
            classRoomTotalScoreFragment = null;
            showFragment(1);
            setTitle("");
        }
    }
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_set_a_question_main;
    }

    @Override
    public void onSubjectModel(SetSubjectModel model) {
         classRoomTotalScoreFragment = new ClassRoomTotalScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classWorkId", model.recordId);
        classRoomTotalScoreFragment.setArguments(bundle);
        showFragment(2);
        setTitle("记录详情");
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        if (classRoomTotalScoreFragment==null){
            setAQuestionFragment.moveLeft();
        }
    }

    @Override
    public void moveRight() {
        super.moveRight();
        if (classRoomTotalScoreFragment==null){
            setAQuestionFragment.moveRight();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
