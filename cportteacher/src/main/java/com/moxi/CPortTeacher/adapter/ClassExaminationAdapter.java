package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.ClassExaminationModel;
import com.mx.mxbase.utils.Log;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassExaminationAdapter extends BaseAdapter{

    private Context context;
    private List<ClassExaminationModel> classExaminationModel;

    public ClassExaminationAdapter(Context context,List<ClassExaminationModel> classExaminationModel){
        this.context = context;
        this.classExaminationModel = classExaminationModel;
    }

    @Override
    public int getCount() {
        return classExaminationModel.size();
    }

    @Override
    public Object getItem(int position) {
        return classExaminationModel.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_examination_layout, null);
            viewHolder.id_tx = (TextView) convertView.findViewById(R.id.inf);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("class","ids==>" + classExaminationModel.get(position).getId_nums() + "");
        viewHolder.id_tx.setText(classExaminationModel.get(position).getId_nums() + "");
        if (position == 0){
            viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
            viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorWithe));
        }
        return convertView;
    }

    static class ViewHolder{
        private TextView id_tx;
    }

}
