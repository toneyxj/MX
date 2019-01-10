package com.example.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class GvAdapter extends BaseAdapter {
    List<String> data;
    Context ctx;

    public GvAdapter(List<String> data, Context ctx) {
        this.data = data;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ImageView iv;
        TextView tv;
        v=View.inflate(ctx,R.layout.item_gv,null);
        iv=(ImageView) v.findViewById(R.id.iv);
        tv=(TextView)v.findViewById(R.id.name);
        String filepath=data.get(position);
        tv.setText(filepath.substring(filepath.lastIndexOf(File.separator)));
        return v;
    }
}
