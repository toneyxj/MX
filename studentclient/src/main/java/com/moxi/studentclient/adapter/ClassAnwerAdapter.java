package com.moxi.studentclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.moxi.studentclient.R;
import com.moxi.studentclient.model.AnwerBean;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassAnwerAdapter extends BaseAdapter {

    private Context context;
    private int type;


    private List<AnwerBean> secPostion;

    public ClassAnwerAdapter(Context context) {
        this.context = context;

    }

    /**
     * @param t    1,单选，2多选
     * @param data
     */
    public void setData(int t, List<AnwerBean> data) {
        this.type = t;
        this.secPostion = data;
    }

    public List<AnwerBean> getData() {
        return secPostion;
    }

    @Override
    public int getCount() {
        if(secPostion==null)
            return 0;
        return secPostion.size();
    }

    @Override
    public Object getItem(int position) {
        if(secPostion==null)
            return null;
        return secPostion.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_anwer_layout, null);
            viewHolder.id_tx = (CheckBox) convertView.findViewById(R.id.checkbox_answer);
            viewHolder.id_tx.setOnClickListener(listener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.id_tx.setText(String.valueOf(secPostion.get(position).getValue()));
        viewHolder.id_tx.setTag(secPostion.get(position).getId());
        viewHolder.id_tx.setChecked(secPostion.get(position).isSelect());

//        if (secPostion.get(position).isSelect()) {
//            viewHolder.id_tx.setChecked(true);
//            viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
//            viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
//        } else {
//            viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
//            viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorWithe));
//        }

        return convertView;
    }

    static class ViewHolder {
        private CheckBox id_tx;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selectPostion = Integer.valueOf(view.getTag().toString());
            for (AnwerBean bean : secPostion) {
                if (type == 1 && bean.getId() == selectPostion) {
                    boolean isSelect = !secPostion.get(selectPostion).isSelect();
                    bean.setSelect(isSelect);
                } else if (type == 1 && bean.getId() != selectPostion) {
                    bean.setSelect(false);
                }else if(type == 2&&bean.getId() == selectPostion){
                    boolean isSelect = !secPostion.get(selectPostion).isSelect();
                    bean.setSelect(isSelect);
                }
            }
            ClassAnwerAdapter.this.notifyDataSetChanged();
        }
    };

}