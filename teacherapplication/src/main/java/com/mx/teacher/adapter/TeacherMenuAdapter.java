package com.mx.teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mx.teacher.R;
import com.mx.teacher.entity.TeacherEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class TeacherMenuAdapter extends BaseAdapter {

    private List<TeacherEntry> data = new ArrayList<TeacherEntry>();
    private Context context;

    public TeacherMenuAdapter(Context context,List<TeacherEntry> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_teacher_menu,null);
            viewHolder.textView = (TextView) view.findViewById(R.id.menu_tx);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.textView.setText(data.get(position).getMenuStr());
        return view;
    }

    static class ViewHolder{
        TextView textView;
    }

}
