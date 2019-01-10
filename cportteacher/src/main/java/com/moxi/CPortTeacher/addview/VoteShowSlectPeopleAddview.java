package com.moxi.CPortTeacher.addview;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.moxi.CPortTeacher.Cinterface.VoteInterface;
import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.VotePeopleAdapter;
import com.moxi.CPortTeacher.model.Student;
import com.mx.mxbase.view.NoGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 投票显示选择被投票的学院
 * Created by Administrator on 2016/11/3.
 */
public class VoteShowSlectPeopleAddview extends BaseLinearLayout implements OnClickListener{
    @Bind(R.id.people_item)
    NoGridView people_item;
    @Bind(R.id.send_vote)
    Button send_vote;
    @Bind(R.id.add_student)
    Button add_student;
    private  VotePeopleAdapter adapter=null;
    private List<Student> students;
    private VoteInterface voteInterface;

    public VoteShowSlectPeopleAddview(Context context,ViewGroup viewGroup,VoteInterface voteInterface) {
        super(context,viewGroup);
        this.voteInterface=voteInterface;
    }

    @Override
    public int getLayout() {
        return R.layout.addview_vote_show_select_people;
    }

    @Override
    public void initView(View view) {
        students=new ArrayList<>();
        List<Student> students=new ArrayList<>();
        students.add(   new Student(23l,"小红"));
        students.add(new Student(32l,"小名"));
        students.add(new Student(23l,"小张"));
        students.add(new Student(28l,"小哈"));

        send_vote.setOnClickListener(this);
        add_student.setOnClickListener(this);
        setItem(students);

    }

    /**
     * 获得选择的学生
     * @return
     */
    public List<Student> getStudents() {
        return students;
    }

    public void setItem(List<Student> students){
        this.students.clear();
        this.students.addAll(students);
        initAdapter();
    }
    private void initAdapter(){
        if (adapter==null){
            adapter=new VotePeopleAdapter(getContext(),students);
            adapter.setListener(this);
            people_item.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (voteInterface==null)return;
      switch (v.getId()){
          case R.id.send_vote:
              voteInterface.sendVote();
              break;
          case R.id.add_student:
              voteInterface.studentAdd();
              break;
          case R.id.delete_item:
              int index= (int) v.getTag();
              students.remove(index);
              initAdapter();
              break;
          default:
              break;
      }
    }
}
