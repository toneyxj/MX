package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.ConfigData;
import com.moxi.CPortTeacher.model.Student;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class StutentAdapter extends BAdapter<Student> {
    public StutentAdapter(Context context, List<Student> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_student;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.select_icon = (ImageView) view.findViewById(R.id.select_icon);
            holder.back_photo = (ImageView) view.findViewById(R.id.back_photo);
            holder.name = (TextView) view.findViewById(R.id.name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.back_photo.setImageResource(ConfigData.getInstance().photos[Integer.parseInt(getList().get(position).photo)]);
        holder.name.setText(getList().get(position).name);
        holder.select_icon.setVisibility(getList().get(position).isSelect ? View.VISIBLE : View.INVISIBLE);
    }
    public class ViewHolder {
        ImageView back_photo;
        ImageView select_icon;
        TextView name;
    }

    /**
     * 更改显示状态
     * @param position
     * @param listView
     */
    public void updateSelect(int position, GridView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {

            View view = listView.getChildAt(position - visibleFirstPosi);
            ImageView select_icon = (ImageView) view.findViewById(R.id.select_icon);

            boolean isTrue=getList().get(position).isSelect;
            select_icon.setVisibility(isTrue?View.VISIBLE:View.INVISIBLE);
        }
    }
}
