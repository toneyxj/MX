package com.moxi.studentclient.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.model.ExamsDetails;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.widget.SubjectNumView;

import java.util.List;

/**
 * Created by zhengdelong on 2016/11/1.
 */

public class ClassExaminationAdapter extends BaseAdapter {

    private Context context;
    private SubjectNumView.TypeView type;
    private List<ExamsDetails> classExaminationModel;

    private int secPostion = -1;

    public ClassExaminationAdapter(Context context) {
        this.context = context;

    }

    public void setData(SubjectNumView.TypeView t, ExamsDetailsModel data) {
        this.classExaminationModel = data.getResult();
        this.type = t;
    }

    public void setSecPostion(int page) {
        this.secPostion = page;
    }

    @Override
    public int getCount() {
        if (type == SubjectNumView.TypeView.INFOTYPE)
            return classExaminationModel.size() + 1;
        if (classExaminationModel != null)
            return classExaminationModel.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (type == SubjectNumView.TypeView.INFOTYPE)
            return null;
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
        if (type == SubjectNumView.TypeView.INFOTYPE) {
            //查看解释
            viewHolder.id_tx.setTag(position-1);
            if (position == 0) {
                viewHolder.id_tx.setText("成绩单");
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorWithe));
            } else {
                viewHolder.id_tx.setText(String.valueOf(position));
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }
        } else if (type == SubjectNumView.TypeView.RESULTTYPE) {
            //是否做完
            viewHolder.id_tx.setText(String.valueOf(position + 1));
            viewHolder.id_tx.setTag(position);
            if (TextUtils.isEmpty(classExaminationModel.get(position).getResult())) {
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorBlack));

            } else {
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorWithe));
            }

        } else if (type == SubjectNumView.TypeView.SELECTTYPE) {
            //选择做题
            viewHolder.id_tx.setText(String.valueOf(position + 1));
            viewHolder.id_tx.setTag(position);
            if (secPostion == position) {
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorWithe));
            } else {
                viewHolder.id_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
                viewHolder.id_tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView id_tx;
    }

}
