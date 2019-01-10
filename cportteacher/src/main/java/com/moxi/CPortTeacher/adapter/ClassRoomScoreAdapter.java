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
import com.moxi.CPortTeacher.model.ClassScoreModel;
import com.moxi.classRoom.widget.ProgressView;
import com.mx.mxbase.utils.Log;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassRoomScoreAdapter extends BaseAdapter {

    private Context context;
    private List<ClassScoreModel> classAnswerModelList;

    public ClassRoomScoreAdapter(Context context, List<ClassScoreModel> classAnswerModelList) {
        this.context = context;
        this.classAnswerModelList = classAnswerModelList;
    }

    private View.OnClickListener onClickListener;

    public void setStudentOnclick(View.OnClickListener onclick) {
        onClickListener = onclick;
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
            viewHolder.numss = (TextView) convertView.findViewById(R.id.numss);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.progress_ans = (ProgressView) convertView.findViewById(R.id.progress_ans);
            viewHolder.answer_numbers = (TextView) convertView.findViewById(R.id.answer_numbers);
            viewHolder.complete_img = (ImageView) convertView.findViewById(R.id.complete_img);
            viewHolder.icon.setOnClickListener(onClickListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(classAnswerModelList.get(position).getName());

        String iconCode = classAnswerModelList.get(position).getIcon();
        if (iconCode.equals("1")) {
            viewHolder.icon.setImageResource(R.mipmap.icon_one);
        } else if (iconCode.equals("2")) {
            viewHolder.icon.setImageResource(R.mipmap.icon_two);
        } else if (iconCode.equals("3")) {
            viewHolder.icon.setImageResource(R.mipmap.icon_three);
        } else if (iconCode.equals("4")) {
            viewHolder.icon.setImageResource(R.mipmap.icon_four);
        }
        viewHolder.icon.setTag(classAnswerModelList.get(position));
        int currentTopic = classAnswerModelList.get(position).getCurrentTopic();
        int totleTopic = classAnswerModelList.get(position).getTotleTopic();
        viewHolder.numss.setVisibility(View.VISIBLE);
        viewHolder.numss.setText((classAnswerModelList.get(position).getRanking()) + ".");
        viewHolder.progress_ans.setMaxNumber(totleTopic);
        viewHolder.progress_ans.setCurNumber(currentTopic);
        viewHolder.complete_img.setVisibility(View.GONE);
        viewHolder.answer_numbers.setVisibility(View.VISIBLE);
        viewHolder.answer_numbers.setText(currentTopic + "");

        return convertView;
    }

    static class ViewHolder {
        private TextView numss;
        private ImageView icon;
        private TextView name;
        private ProgressView progress_ans;
        private TextView answer_numbers;
        private ImageView complete_img;
    }

}
