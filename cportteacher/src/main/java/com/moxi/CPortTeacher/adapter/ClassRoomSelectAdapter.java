package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.ClassExaminationModel;
import com.moxi.CPortTeacher.model.ClassSelectModel;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassRoomSelectAdapter extends BaseAdapter{

    private Context context;
    private List<ClassSelectModel> classSelectModels;

    public ClassRoomSelectAdapter(Context context, List<ClassSelectModel> classSelectModels){
        this.context = context;
        this.classSelectModels = classSelectModels;
    }

    @Override
    public int getCount() {
        return classSelectModels.size();
    }

    @Override
    public Object getItem(int position) {
        return classSelectModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_seclect_layout, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.incn);
            viewHolder.incn_sec = (ImageView) convertView.findViewById(R.id.incn_sec);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(classSelectModels.get(position).getName());
        int state = classSelectModels.get(position).getState();
        if (state == 0){
            viewHolder.incn_sec.setVisibility(View.GONE);
        }else{
            viewHolder.incn_sec.setVisibility(View.VISIBLE);
        }
        String iconCode = classSelectModels.get(position).getHeadimg();
        if (iconCode.equals("1")){
            viewHolder.icon.setImageResource(R.mipmap.icon_one);
        }else if(iconCode.equals("2")){
            viewHolder.icon.setImageResource(R.mipmap.icon_two);
        }else if(iconCode.equals("3")){
            viewHolder.icon.setImageResource(R.mipmap.icon_three);
        }else if(iconCode.equals("4")){
            viewHolder.icon.setImageResource(R.mipmap.icon_four);
        }
        return convertView;
    }

    static class ViewHolder{
        private ImageView icon;
        private ImageView incn_sec;
        private TextView name;

    }

}
