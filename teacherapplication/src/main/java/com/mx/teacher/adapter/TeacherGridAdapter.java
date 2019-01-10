package com.mx.teacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.teacher.R;
import com.mx.teacher.constant.Constant;
import com.mx.teacher.entity.TeacherEntry;
import com.mx.teacher.entity.TeacherGridEntry;
import com.mx.teacher.http.HttpVolleyCallback;
import com.mx.teacher.http.VolleyHttpUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class TeacherGridAdapter extends BaseAdapter {

    private List<TeacherGridEntry> data = new ArrayList<TeacherGridEntry>();
    private Context context;

    public TeacherGridAdapter(Context context, List<TeacherGridEntry> data){
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_teacher_grid,null);
            viewHolder.home_rel = (RelativeLayout) view.findViewById(R.id.home_rel);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.time = (TextView) view.findViewById(R.id.time);
            viewHolder.distribute = (TextView) view.findViewById(R.id.distribute);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if (data.get(position).getArrStatus() == 0){
            //未分发
            viewHolder.distribute.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.distribute.setText("分发");
            viewHolder.status.setText("未分发");
            viewHolder.home_rel.setBackgroundResource(R.drawable.back_grid_f2);
            viewHolder.distribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //分发
                    int id = data.get(position).getId();
                    disWork(context,id,position);
                }
            });

        }else{
            //已分发
            viewHolder.home_rel.setBackgroundResource(R.drawable.back_grid);
            viewHolder.distribute.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.status.setText("已分发");
            viewHolder.time.setText(data.get(position).getArrTime());
        }
        viewHolder.title.setText(data.get(position).getTitle());
        return view;
    }

    static class ViewHolder{
        RelativeLayout home_rel;
        TextView title;
        TextView status;
        TextView time;
        TextView distribute;
    }

    private void disWork(Context context, int id, final int position){
        VolleyHttpUtil.get(context, Constant.HOMEDISCRBUTEURL + id + "/assign", new HttpVolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("book","dis==>" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code",-1);
                    if (code == 0){
                        //分发成功
                        data.get(position).setArrStatus(1);
                        Date date = new Date();
                        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sp.format(date);
                        data.get(position).setArrTime(time);
                        TeacherGridAdapter.this.notifyDataSetChanged();
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onFilad(String msg) {

            }
        });
    }

}
