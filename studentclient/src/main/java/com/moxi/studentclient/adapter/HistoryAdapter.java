package com.moxi.studentclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.model.AnswerHistorybean;
import com.moxi.studentclient.model.ClassingHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class HistoryAdapter extends BaseAdapter {
    Context context;
    List<AnswerHistorybean> ahlist = new ArrayList<>();

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AnswerHistorybean> data) {
        if (data == null)
            this.ahlist = new ArrayList<>();
        this.ahlist = data;
    }

    @Override
    public int getCount() {
        if (ahlist == null)
            return 0;
        return ahlist.size();
    }

    @Override
    public Object getItem(int position) {
        if (ahlist == null)
            return null;
        return ahlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder hold;
        if (null == v) {
            hold = new ViewHolder();
            v = View.inflate(context, R.layout.item_history_layout, null);
            hold.day = (TextView) v.findViewById(R.id.day_tv);
            hold.time = (TextView) v.findViewById(R.id.time_tv);
            hold.title = (TextView) v.findViewById(R.id.title_tv);
            hold.timeSecond = (TextView) v.findViewById(R.id.time_second_tv);
            v.setTag(hold);
        } else {
            hold = (ViewHolder) v.getTag();
        }
        AnswerHistorybean ahbean = ahlist.get(position);
        hold.day.setText(ahbean.getClassWorkDate());
        hold.time.setText(ahbean.getClassWorkTime());
        hold.title.setText(ahbean.getClassWorkTitle());
        hold.timeSecond.setText(ahbean.getTimeSpan());
        if(position>0&&ahlist.get(position).getClassWorkDate().equals(ahlist.get(position-1).getClassWorkDate())){
            hold.day.setVisibility(View.INVISIBLE);
        }else{
            hold.day.setVisibility(View.VISIBLE);
        }
        return v;
    }

    class ViewHolder {
        TextView day;
        TextView time;
        TextView title;
        TextView timeSecond;
    }
}
