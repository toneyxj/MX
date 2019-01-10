package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.ClassTests.ThesugestionResultFragment;
import com.mx.mxbase.utils.Log;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ThesugestionAnswerAdapter extends BaseAdapter{

    private Context context;
    private List<ThesugestionResultFragment.RadioSelectAnswer> radioSelectAnswerList;

    NumberFormat numberFormat = NumberFormat.getInstance();
    int max = 0;

    int type;

    public ThesugestionAnswerAdapter(Context context, int type,int max,List<ThesugestionResultFragment.RadioSelectAnswer> radioSelectAnswerList){
        numberFormat.setMaximumFractionDigits(2);
        this.context = context;
        this.radioSelectAnswerList = radioSelectAnswerList;
        this.max = max;
        this.type = type;
    }

    @Override
    public int getCount() {
        return radioSelectAnswerList.size();
    }

    @Override
    public Object getItem(int position) {
        return radioSelectAnswerList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.thesugestion_item, null);
            viewHolder.tempNameTextView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tempProgressAns = (com.moxi.classRoom.widget.ProgressView) convertView.findViewById(R.id.progress_ans);
            viewHolder.tempAnswerNumbersTextView = (TextView) convertView.findViewById(R.id.answer_numbers);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (type == 4){
            viewHolder.tempNameTextView.setText(radioSelectAnswerList.get(position).getAnswer());
        }else{
            viewHolder.tempNameTextView.setText(radioSelectAnswerList.get(position).getAnswer() + ".");
        }

        viewHolder.tempProgressAns.setCurNumber(radioSelectAnswerList.get(position).getSize());
        viewHolder.tempProgressAns.setMaxNumber(max);
        int current = radioSelectAnswerList.get(position).getSize();
        Log.d("class","current:" + current);
        Log.d("class","max:" + max);
        String result = numberFormat.format((float) current / (float) max * 100);
        viewHolder.tempAnswerNumbersTextView.setText(result + "%");
        return convertView;
    }

    static class ViewHolder{
        private TextView tempNameTextView;
        private com.moxi.classRoom.widget.ProgressView tempProgressAns;
        private TextView tempAnswerNumbersTextView;

    }

}
