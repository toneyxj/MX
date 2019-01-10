package com.moxi.CPortTeacher.frament.voteDialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moxi.CPortTeacher.Cinterface.VoteInterface;
import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.VotePeopleAdapter;
import com.moxi.CPortTeacher.model.Student;
import com.moxi.classRoom.BaseFragment;
import com.mx.mxbase.view.NoGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 展示选择的人
 * Created by Administrator on 2016/11/3.
 */
@SuppressLint("ValidFragment")
public class VoteSelectPeople extends BaseFragment {
    private VoteInterface anInterface;
    public VoteSelectPeople(VoteInterface voteInterface){
        anInterface=voteInterface;

    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.addview_vote_show_select_people;
    }
    @Bind(R.id.people_item)
    NoGridView people_item;
    @Bind(R.id.send_vote)
    Button send_vote;
    @Bind(R.id.add_student)
    Button add_student;
    private VotePeopleAdapter adapter=null;
    private List<Student> students=new ArrayList<>();
    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        students=new ArrayList<>();
        students.add(new Student());
        students.add(new Student());
        students.add(new Student());
        students.add(new Student());
        adapter=new VotePeopleAdapter(getContext(),students);

        people_item.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
