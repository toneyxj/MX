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
import com.moxi.leavemessage.bean.ChatBean;
import com.moxi.leavemessage.bean.MsgHistoryBean;
import com.moxi.leavemessage.config.LoaclData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<ChatBean.ResultBean> data;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ChatBean.ResultBean> data) {
        if (data == null)
            this.data = new ArrayList<>();
        this.data = data;
    }
    public void addData(ChatBean.ResultBean bean){
        this.data.add(bean);
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getSendUser() == URL.userId)
            return 0;
        return 1;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder hold;
        int type = getItemViewType(position);
        if (type == 1) {
            if (null == v) {
                hold = new ViewHolder();
                v = View.inflate(context, R.layout.row_received_message, null);
                hold.nameTv = (TextView) v.findViewById(R.id.tv_userid);
                hold.timeTv = (TextView) v.findViewById(R.id.timestamp);
                hold.contextTv = (TextView) v.findViewById(R.id.tv_chatcontent);
                hold.headIv = (ImageView) v.findViewById(R.id.iv_userhead);
                v.setTag(hold);
            } else {
                hold = (ViewHolder) v.getTag();
            }
            ChatBean.ResultBean bean = data.get(position);
            hold.nameTv.setText(bean.getSendUname());
            hold.contextTv.setText(bean.getContent());
            hold.timeTv.setText(bean.getCreatetime());
            if(URL.type==1){
                hold.headIv.setImageResource(LoaclData.getInstatnce().getStudentImg(Integer.valueOf(bean.getSendHeadimg())));
            }else{
                hold.headIv.setImageResource(LoaclData.getInstatnce().getTeachHeadImg(Integer.valueOf(bean.getSendHeadimg())));
            }

        } else {
            if (null == v) {
                hold = new ViewHolder();
                v = View.inflate(context, R.layout.row_sent_message, null);
                hold.timeTv = (TextView) v.findViewById(R.id.timestamp);
                hold.contextTv = (TextView) v.findViewById(R.id.tv_chatcontent);
                hold.headIv = (ImageView) v.findViewById(R.id.iv_userhead);
                v.setTag(hold);
            } else {
                hold = (ViewHolder) v.getTag();
            }

            ChatBean.ResultBean bean = data.get(position);
            hold.contextTv.setText(bean.getContent());
            hold.timeTv.setText(bean.getCreatetime());
            if(URL.type==1){
                hold.headIv.setImageResource(LoaclData.getInstatnce().getStudentImg(Integer.valueOf(bean.getSendHeadimg())));
            }else{
                hold.headIv.setImageResource(LoaclData.getInstatnce().getTeachHeadImg(Integer.valueOf(bean.getSendHeadimg())));
            }
        }

        return v;
    }


    class ViewHolder {
        TextView nameTv;
        TextView timeTv;
        TextView contextTv;
        ImageView headIv;
    }
}
