package com.moxi.bookstore.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.bookstore.R;
import com.moxi.bookstore.interfacess.ClickPosition;
import com.moxi.bookstore.modle.vo.MProductVO;
import com.mx.mxbase.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 * Chanel页面下的bookAdapter
 */
public class CartAdapter extends BaseAdapter {
    private Context cxt;
    private List<MProductVO> data;
    ClickPosition listener;
    public CartAdapter(Context context,ClickPosition listener) {
        this.cxt = context;
        this.listener=listener;
    }

    public void setData(List<MProductVO> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (null==data||data.size()==0)
            return 0;
        else if (data.size()>4)
            return 4;
        else
            return data.size();
    }

    public List<MProductVO> getChickItems() {
        List<MProductVO> items = new ArrayList<>();
        if (null != data && data.size() > 0) {
            for (MProductVO bean : data) {
                if (bean.getIs_checked()==1) {
                    items.add(bean);
                }
            }
        }
        return items;
    }
    public List<MProductVO> getNoChickItems() {
        List<MProductVO> items = new ArrayList<>();
        if (null != data && data.size() > 0) {
            for (MProductVO bean : data) {
                if (bean.getIs_checked()!=1) {
                    items.add(bean);
                }
            }
        }
        return items;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(cxt, R.layout.item_cart_salebook, null);
            holder.bookico = (ImageView) convertView.findViewById(R.id.book_ico);
            holder.title = (TextView) convertView.findViewById(R.id.book_title_tv);
            holder.author = (TextView) convertView.findViewById(R.id.author_tv);
            holder.proprice = (TextView) convertView.findViewById(R.id.pro_price_tv);
            holder.orgprice = (TextView) convertView.findViewById(R.id.org_price_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox2);
            holder.checkBox.setOnClickListener(checkBoxClick);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.orgprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        MProductVO item = data.get(position);
        //加载图片
        GlideUtils.getInstance().loadGreyImage(cxt, holder.bookico, item.getProduct_image());
        String title;
        if (TextUtils.isEmpty(item.getProduct_name())) {
            title = "不详";
        } else
            title = item.getProduct_name();
        holder.title.setText(title);
        holder.author.setText("");
      //  holder.descs.setText("   " + item.getCategorys());
//        double price= Double.parseDouble(item.getPrice())/100;
        holder.proprice.setText("￥" +item.getPrice().getDangdang_price());
        holder.checkBox.setChecked(item.getIs_checked()==1);
        holder.checkBox.setTag(position);
         holder.orgprice.setText("￥" +item.getPrice().getOriginal_price());
        return convertView;
    }

    View.OnClickListener checkBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position= (int) view.getTag();
//            MProductVO item = (MProductVO) view.getTag();
//            CheckBox checkBox = (CheckBox) view;
//            item.setIs_checked(checkBox.isChecked()?1:0);
            listener.click(position);
        }
    };

    class ViewHolder {
        public ImageView bookico;
        public TextView title;
        public TextView author;
       // public TextView descs;
        public TextView proprice;
        public TextView orgprice;
        CheckBox checkBox;
    }


}
