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
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.http.imageloader.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class HGridViewAdapter extends BaseAdapter{
    List<EbookResouce> list;
    Context ctx;
    public HGridViewAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setData(List<EbookResouce> list){
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
            v=View.inflate(ctx, R.layout.item_ebook_gv,null);
            holder.ico=(ImageView) v.findViewById(R.id.ico_iv);
            holder.title=(TextView) v.findViewById(R.id.title_tv);
            holder.author=(TextView) v.findViewById(R.id.author_tv);
            holder.saleprice=(TextView) v.findViewById(R.id.lowest_price_tv);
            holder.price=(TextView) v.findViewById(R.id.org_price_tv);
            holder.price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            v.setTag(holder);
        }else
            holder=(ViewHolder)v.getTag();
        EbookResouce item=list.get(position);
        GlideUtils glideUtils=MyApplication.getMyApplication().getMglide();
        String path=item.getCoverPath();
        if (!path.contains(Config.IMAGPATH))
            path=Config.IMAGPATH+path;
        glideUtils.loadGreyImage(ctx,holder.ico,path);
        holder.title.setText(item.getName());
        String author=item.getAuthor();
        if (null==author)
            author="不详";
        holder.author.setText(author);
        holder.saleprice.setText("￥:"+item.getPrice());

        return v;
    }

    class ViewHolder{
        ImageView ico;
        TextView title,author,price,saleprice;
    }
}
