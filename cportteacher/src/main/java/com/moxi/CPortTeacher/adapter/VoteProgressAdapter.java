package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.VoteProgressModel;
import com.moxi.classRoom.widget.ProgressView;
import com.mx.mxbase.adapter.BAdapter;
import com.mx.mxbase.view.NoListView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class VoteProgressAdapter  extends BAdapter<VoteProgressModel> {
    public VoteProgressAdapter(Context context, List<VoteProgressModel> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_vote_progress;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.name_and_index = (TextView) view.findViewById(R.id.name_and_index);
            holder.progress = (ProgressView) view.findViewById(R.id.progress);
            holder.txt_progress = (TextView) view.findViewById(R.id.txt_progress);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name_and_index.setText(getIndex(position)+getList().get(position).name);
        holder.progress.setMaxNumber(getList().get(position).maxNumber);
        holder.progress.setCurNumber(getList().get(position).curNumber);
        holder.txt_progress.setText(getList().get(position).getProgress());
    }
    public class ViewHolder {
        TextView name_and_index;
        ProgressView progress;
        TextView txt_progress;
    }
    private String getIndex(int position){
        String index="";
        switch (position){
            case 0:
                index="A";
                break;
            case 1:
                index="B";
                break;
            case 2:
                index="C";
                break;
            case 3:
                index="D";
                break;
            case 4:
                index="E";
                break;
            case 5:
                index="F";
                break;
            case 6:
                index="G";
                break;
            default:
                index="#";
                break;
        }
        return index+".";
    }

    /**
     * 更改显示view状态
     * @param position
     * @param listView
     */
    public void updateProgress(int position, NoListView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {

            View view = listView.getChildAt(position - visibleFirstPosi);
            ProgressView progressView = (ProgressView) view.findViewById(R.id.progress);
            TextView txt_progress = (TextView) view.findViewById(R.id.txt_progress);

            progressView.setCurNumber(getList().get(position).curNumber);
            txt_progress.setText(getList().get(position).getProgress());
        }
    }
}
