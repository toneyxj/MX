package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.Student;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class VotePeopleAdapter extends BAdapter<Student> {
    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public VotePeopleAdapter(Context context, List<Student> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_vote_people;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.index = (TextView) view.findViewById(R.id.index);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.delete_item = (ImageButton) view.findViewById(R.id.delete_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Student student=getItem(position);
        holder.index.setText(getIndex(position));
        holder.name.setText(student.name);

        if (listener!=null) {
            holder.delete_item.setTag(position);
            holder.delete_item.setOnClickListener(listener);
        }
    }
    private String getIndex(int position){
        String index="";
        switch (position){
            case 0:
                index="A";
                break;
            case 1:
                index="B";
                break;
            case 2:
                index="C";
                break;
            case 3:
                index="D";
                break;
            case 4:
                index="E";
                break;
            case 5:
                index="F";
                break;
            case 6:
                index="G";
                break;
            default:
                index="#";
                break;
        }
        return index+".";
    }

    public class ViewHolder {
        TextView index;
        TextView name;
        ImageButton delete_item;
    }
}
