package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ThesugestionSelectAdapter extends BaseAdapter{

    private Context context;
    private List<ThesugestionAnswerListModel> thesugestionAnswerListModelList;

    public ThesugestionSelectAdapter(Context context, List<ThesugestionAnswerListModel> thesugestionAnswerListModelList){
        this.context = context;
        this.thesugestionAnswerListModelList = thesugestionAnswerListModelList;
    }

    @Override
    public int getCount() {
        return thesugestionAnswerListModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return thesugestionAnswerListModelList.get(position);
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
        viewHolder.name.setText(thesugestionAnswerListModelList.get(position).getStudentName());
        String state = thesugestionAnswerListModelList.get(position).getAnswer();
        if (state.equals("")){
            viewHolder.incn_sec.setVisibility(View.GONE);
        }else{
            viewHolder.incn_sec.setVisibility(View.VISIBLE);
        }
        String iconCode = thesugestionAnswerListModelList.get(position).getHeadimg();
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
