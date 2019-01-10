package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.ParticipateRecordModel;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * 课堂参与记录
 * Created by Administrator on 2016/11/10.
 */
public class ParticipationRecordAdapter extends BAdapter<ParticipateRecordModel> {
    private int height;

    public ParticipationRecordAdapter(Context context, List<ParticipateRecordModel> list, int height) {
        super(context, list);
        this.height = height;
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_participation_record;
    }

    @Override
    public void setItemHeight(View view) {
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(param);
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();

            holder.participate_hitn_time = (TextView) view.findViewById(R.id.participate_hitn_time);//提示时间
            holder.participate_title = (TextView) view.findViewById(R.id.participate_title);//标题
            holder.participate_date = (TextView) view.findViewById(R.id.participate_date);//具体日期
            holder.participate_time = (TextView) view.findViewById(R.id.participate_time);//具体时间

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ParticipateRecordModel model = getList().get(position);

    }

    public class ViewHolder {
        TextView participate_hitn_time;//提示时间
        TextView participate_title;//标题
        TextView participate_date;//具体日期
        TextView participate_time;//具体时间
    }
}
