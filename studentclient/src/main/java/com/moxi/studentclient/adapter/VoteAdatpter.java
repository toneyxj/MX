package com.moxi.studentclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.model.VoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */

public class VoteAdatpter extends BaseAdapter {
    private Context ctx;
    private List<VoteBean> list;
    char item='A';
    public VoteAdatpter(Context ctx) {
        this.ctx = ctx;
        list=new ArrayList<>();
    }

    public void setData(List<VoteBean> data){
        list=data;
        notifyDataSetChanged();
    }
    public List<VoteBean> getData(){
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (null==v){
            holder=new ViewHolder();
            v=View.inflate(ctx, R.layout.item_vote,null);
            holder.item=(TextView)v.findViewById(R.id.item_tv);
            holder.name=(TextView)v.findViewById(R.id.name_tv);
            v.setTag(holder);
        }else
            holder=(ViewHolder)v.getTag();
        VoteBean bean=list.get(position);
        holder.item.setText((char)(item+position)+".");
        holder.name.setText(bean.option);
        if (bean.checked){
            holder.name.setBackgroundColor(Color.parseColor("#505050"));
            holder.name.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.name.setBackgroundResource(R.drawable.shape_question_bg);
            holder.name.setTextColor(Color.parseColor("#000000"));
        }
        return v;
    }

    class ViewHolder{
        TextView item,name;
    }
}
