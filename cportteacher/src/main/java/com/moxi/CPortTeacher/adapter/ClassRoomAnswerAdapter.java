package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.ClassAnswerModel;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.classRoom.widget.ProgressView;
import com.mx.mxbase.utils.Log;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassRoomAnswerAdapter extends BaseAdapter{

    private Context context;
    private List<ClassAnswerModel> classAnswerModelList;

    public ClassRoomAnswerAdapter(Context context, List<ClassAnswerModel> classAnswerModelList){
        this.context = context;
        this.classAnswerModelList = classAnswerModelList;
    }

    @Override
    public int getCount() {
        return classAnswerModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return classAnswerModelList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_answer_fragment, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.progress_ans = (ProgressView) convertView.findViewById(R.id.progress_ans);
            viewHolder.answer_numbers = (TextView) convertView.findViewById(R.id.answer_numbers);
            viewHolder.complete_img = (ImageView) convertView.findViewById(R.id.complete_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(classAnswerModelList.get(position).getName());

        String iconCode = classAnswerModelList.get(position).getIcon();
        if (iconCode.equals("1")){
            viewHolder.icon.setImageResource(R.mipmap.icon_one);
        }else if(iconCode.equals("2")){
            viewHolder.icon.setImageResource(R.mipmap.icon_two);
        }else if(iconCode.equals("3")){
            viewHolder.icon.setImageResource(R.mipmap.icon_three);
        }else if(iconCode.equals("4")){
            viewHolder.icon.setImageResource(R.mipmap.icon_four);
        }
        int currentTopic = classAnswerModelList.get(position).getCurrentTopic();
        int totleTopic = classAnswerModelList.get(position).getTotleTopic();
        if (classAnswerModelList.get(position).getFinish() == 1){
            //已完成
            viewHolder.progress_ans.setMaxNumber(totleTopic);
            viewHolder.progress_ans.setCurNumber(totleTopic);
            viewHolder.complete_img.setVisibility(View.VISIBLE);
            viewHolder.answer_numbers.setVisibility(View.GONE);
        }else{
            //未完成
            viewHolder.progress_ans.setMaxNumber(totleTopic);
            int sc = currentTopic/totleTopic;
            Log.d("class","sc===>" + sc);
            viewHolder.progress_ans.setCurNumber(currentTopic);
            viewHolder.complete_img.setVisibility(View.GONE);
            viewHolder.answer_numbers.setVisibility(View.VISIBLE);
            viewHolder.answer_numbers.setText(currentTopic+"/"+totleTopic);
        }

        return convertView;
    }

    static class ViewHolder{
        private ImageView icon;
        private TextView name;
        private ProgressView progress_ans;
        private TextView answer_numbers;
        private ImageView complete_img;
    }

}
