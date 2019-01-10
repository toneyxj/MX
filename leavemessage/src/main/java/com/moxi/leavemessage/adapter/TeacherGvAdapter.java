package com.moxi.leavemessage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.leavemessage.R;
import com.moxi.leavemessage.bean.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class TeacherGvAdapter extends BaseAdapter{
    List<UserBean> list;
    Context ctx;
    static final int STUDENT=0;
    static final int TEACHER=1;
    int flag;
    public TeacherGvAdapter(List<UserBean> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    public void setData(List<UserBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        ViewHolder holder;
        UserBean bean=list.get(i);
        if (null==v){
            holder=new ViewHolder();
            if (null!=bean.getSubject()){
                flag=TEACHER;
                v=View.inflate(ctx, R.layout.item_teacher_gv,null);
                holder.ico=(ImageView) v.findViewById(R.id.icon_iv);
                holder.name=(TextView) v.findViewById(R.id.name_tv);
                holder.subject=(TextView) v.findViewById(R.id.subject_tv);

            }else {
                flag=STUDENT;
                v=View.inflate(ctx, R.layout.item_student_gv,null);
                holder.ico=(ImageView) v.findViewById(R.id.icon_iv);
                holder.name=(TextView) v.findViewById(R.id.name_tv);
                holder.check=(ImageView) v.findViewById(R.id.check_iv);
            }
            v.setTag(holder);
        }else
            holder=(ViewHolder)v.getTag();

            holder.ico.setImageResource(getImagId(bean.getHeadimg()));
            holder.name.setText(bean.getName());
            if (flag==TEACHER){
                holder.subject.setText("科目: "+bean.getSubject().getName());
            }else {
                if (bean.isChecked)
                    holder.check.setVisibility(View.VISIBLE);
                else
                    holder.check.setVisibility(View.INVISIBLE);
            }


        return v;
    }
    private int getImagId(String type){
        int id;
        if (flag==TEACHER){
            switch (type){
                case "1":
                    id=R.mipmap.teacher_ico_001;
                    break;
                case "2":
                    id=R.mipmap.teacher_ico_002;
                    break;
                case "3":
                    id=R.mipmap.teacher_ico_003;
                    break;
                default:
                    id=R.mipmap.teacher_ico_001;
            }
        }else {
            switch (type){
                case "1":
                    id=R.mipmap.student_ico_001;
                    break;
                case "2":
                    id=R.mipmap.student_ico_002;
                    break;
                case "3":
                    id=R.mipmap.student_ico_003;
                    break;
                case "4":
                    id=R.mipmap.student_ico_004;
                    break;
                default:
                    id=R.mipmap.student_ico_001;
            }
        }
        return id;
    }
    class ViewHolder{
        ImageView ico,check;
        TextView name,subject;
    }
}
