package com.mx.cqbookstore.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.cqbookstore.MyApplication;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.imageloader.GlideUtils;
import com.mx.mxbase.constant.APPLog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class HGridstoreapter extends BaseAdapter{
    List<DBebookbean> list;
    Context ctx;
    public HGridstoreapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setData(List<DBebookbean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null==list)
            return 0;
        else
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
            v=View.inflate(ctx, R.layout.mx_recycler_book_type_item,null);
            holder.ico=(ImageView) v.findViewById(R.id.img_recycler_item_book_type);
            holder.title=(TextView) v.findViewById(R.id.tv_recycler_item_book_type);

            v.setTag(holder);
        }else
            holder=(ViewHolder)v.getTag();
        DBebookbean item=list.get(position);
        GlideUtils glideUtils=MyApplication.getMyApplication().getMglide();
        APPLog.e("cover:"+item.bookCover);
        glideUtils.loadGreyImage(ctx,holder.ico,item.bookCover);
        holder.title.setText(item.bookName);


        return v;
    }

    class ViewHolder{
        ImageView ico;
        TextView title;
    }
}
