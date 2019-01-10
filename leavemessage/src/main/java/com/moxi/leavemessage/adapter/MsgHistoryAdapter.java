package com.moxi.leavemessage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.leavemessage.R;
import com.moxi.leavemessage.URL;
import com.moxi.leavemessage.bean.MsgHistoryBean;
import com.moxi.leavemessage.config.LoaclData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MsgHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<MsgHistoryBean.ResultBean> data;

    public MsgHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MsgHistoryBean.ResultBean> data) {
        if (data == null)
            this.data = new ArrayList<>();
        this.data = data;
/*
        if (this.data == null)
            this.data = new ArrayList<>();
        for (MsgHistoryBean.ResultBean bean : data) {
            this.data.add(bean);
        }
        */
    }

    public List<MsgHistoryBean.ResultBean> getData() {
        if(data==null)
            return new ArrayList<>();
        return data;
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if (data == null)
            return null;
        return data.get(position);
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
            hold.nameTv = (TextView) v.findViewById(R.id.nameTv);
            hold.timeTv = (TextView) v.findViewById(R.id.timeTv);
            hold.contextTv = (TextView) v.findViewById(R.id.contentTv);
            hold.headIv = (ImageView) v.findViewById(R.id.headIv);
            hold.checkBox = (CheckBox) v.findViewById(R.id.editCheckBox);
            hold.checkBox.setOnCheckedChangeListener(changeListener);
            v.setTag(hold);
        } else {
            hold = (ViewHolder) v.getTag();
        }
        MsgHistoryBean.ResultBean bean = data.get(position);
        hold.checkBox.setTag(bean);
        if (bean.isEdit()) {
            hold.checkBox.setVisibility(View.VISIBLE);
            hold.checkBox.setChecked(bean.isDelEdit());
        } else {
            hold.checkBox.setVisibility(View.GONE);
        }

        hold.contextTv.setText(bean.getLastSendContent());
        hold.timeTv.setText(bean.getLastSendTime());
        if (URL.userId == bean.getUser1()) {
            hold.nameTv.setText(bean.getUName2());
            if (URL.type == 1) {
                hold.headIv.setImageResource(LoaclData.getInstatnce().getStudentImg(Integer.valueOf(bean.getU2Headimg())));
            } else {
                hold.headIv.setImageResource(LoaclData.getInstatnce().getTeachHeadImg(Integer.valueOf(bean.getU2Headimg())));
            }
        } else {
            hold.nameTv.setText(bean.getUName1());
            if (URL.type == 1) {
                hold.headIv.setImageResource(LoaclData.getInstatnce().getStudentImg(Integer.valueOf(bean.getU1Headimg())));
            } else {
                hold.headIv.setImageResource(LoaclData.getInstatnce().getTeachHeadImg(Integer.valueOf(bean.getU1Headimg())));
            }

        }
        return v;
    }

    CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (compoundButton.getTag() == null)
                return;
            MsgHistoryBean.ResultBean bean = (MsgHistoryBean.ResultBean) compoundButton.getTag();
            bean.setDelEdit(b);
        }
    };

    class ViewHolder {
        TextView nameTv;
        TextView timeTv;
        TextView contextTv;
        ImageView headIv;
        CheckBox checkBox;
    }
}
