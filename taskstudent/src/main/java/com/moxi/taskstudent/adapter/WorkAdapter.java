package com.moxi.taskstudent.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.model.WorkListModel;
import com.moxi.taskstudent.taskInterface.ClickItem;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */
public class WorkAdapter extends BAdapter<WorkListModel> {
    private ClickItem clickItem;
    public WorkAdapter(Context context, List<WorkListModel> list, ClickItem clickItem) {
        super(context, list);
        this.clickItem=clickItem;
    }

    @Override
    public int getContentView() {
        return R.layout.adater_work;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.work_title = (TextView) view.findViewById(R.id.work_title);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.onclick_button = (Button) view.findViewById(R.id.onclick_button);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.work_title.setText(getList().get(position).title);
        holder.status.setText(getList().get(position).getTaskStatus());
        holder.onclick_button.setText(getList().get(position).getButtonStr());
        holder.onclick_button.setTag(position);
        holder.onclick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItem!=null)clickItem.onClickItem((Integer) v.getTag());
            }
        });
    }
    public class ViewHolder {
        TextView work_title;
        TextView status;
        Button onclick_button;
    }

    public void updateButton(int position,GridView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
            if(position>visibleFirstPosi&&position<=visibleLastPosi){
                View view = listView.getChildAt(position - visibleFirstPosi);
                Button onclick_button=(Button)view.findViewById(R.id.onclick_button);
                onclick_button.setText(getList().get(position).getButtonStr());
            }
    }

}
