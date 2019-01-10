package com.moxi.CPortTeacher.adapter.weedout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.weedout.FallWorkProgressModel;
import com.moxi.classRoom.widget.ProgressView;
import com.mx.mxbase.utils.ListUtils;

import java.util.List;

/**
 * Created by Archer on 16/11/8.
 */
public class DoProgressAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<List<FallWorkProgressModel.FallWorkProgress.ProgressList>> list;
    private int page;

    public DoProgressAdapter(Context context, List<FallWorkProgressModel.FallWorkProgress.ProgressList> listData, int page) {
        this.context = context;
        this.page = page;
        this.list = ListUtils.splitList(listData, 7);
    }

    public void setPage(int page) {
        this.page = page;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_item_do_progress, null);
        return new DoProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FallWorkProgressModel.FallWorkProgress.ProgressList p = list.get(page).get(position);
        ((DoProgressViewHolder) holder).tvName.setText(p.getUsr_name());
        ((DoProgressViewHolder) holder).tvProgress.setText(p.getCount() + "/" + p.getTotal());
        ((DoProgressViewHolder) holder).progressView.setMaxNumber(p.getTotal());
        ((DoProgressViewHolder) holder).progressView.setCurNumber(p.getCount());
        if (p.getFinish() == p.getTotal()) {
            ((DoProgressViewHolder) holder).imgFinish.setVisibility(View.VISIBLE);
            ((DoProgressViewHolder) holder).tvProgress.setVisibility(View.GONE);
        } else {
            ((DoProgressViewHolder) holder).tvProgress.setVisibility(View.VISIBLE);
            ((DoProgressViewHolder) holder).imgFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.get(page).size();
    }

    class DoProgressViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvProgress;
        ProgressView progressView;
        ImageView imgFinish;

        public DoProgressViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_student_name);
            tvProgress = (TextView) itemView.findViewById(R.id.tv_weed_out_progress);
            progressView = (ProgressView) itemView.findViewById(R.id.pv_weed_out);
            imgFinish = (ImageView) itemView.findViewById(R.id.img_weed_out_finish);
        }
    }
}
