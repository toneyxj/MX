package com.moxi.taskstudent.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.model.TaskDataModel;
import com.moxi.taskstudent.taskInterface.ClickItem;
import com.moxi.taskstudent.utils.TaskDataModelUtils;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * 课件白板显示adapter
 * Created by Administrator on 2016/11/30.
 */
public class TaskDataAdapter extends BAdapter<TaskDataModel> {
private ClickItem clickItem;
    public TaskDataAdapter(Context context, List<TaskDataModel> list, ClickItem clickItem) {
        super(context, list);
        this.clickItem=clickItem;
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_task_data;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.file_name = (TextView) view.findViewById(R.id.file_name);
            holder.download_status = (TextView) view.findViewById(R.id.download_status);
            holder.click_layout = (LinearLayout) view.findViewById(R.id.click_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String end="未下载";
        if (TaskDataModelUtils.getInstance().isHaveDownload(getList().get(position).FileId)){
            end="已下载";
        }
        holder.download_status.setText(end);
        holder.file_name.setText(getList().get(position).getFileName());

        holder.click_layout.setTag(position);
        holder.click_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.onClickItem((Integer) v.getTag());
            }
        });
    }

    public class ViewHolder {
        TextView file_name;
        TextView download_status;
        LinearLayout click_layout;
    }
}
