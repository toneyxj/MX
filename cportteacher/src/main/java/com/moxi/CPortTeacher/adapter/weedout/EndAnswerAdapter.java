package com.moxi.CPortTeacher.adapter.weedout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.weedout.FallWorkScoreModel;
import com.mx.mxbase.utils.ListUtils;

import java.util.List;

/**
 * Created by Archer on 16/11/8.
 */
public class EndAnswerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<List<FallWorkScoreModel.FallWorkScore.ScoreList>> list;
    private int page;

    public EndAnswerAdapter(Context context, List<FallWorkScoreModel.FallWorkScore.ScoreList> list, int page) {
        this.context = context;
        this.list = ListUtils.splitList(list, 7);
        this.page = page;
    }

    public void setPage(int page) {
        this.page = page;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_item_end_answer, null);
        return new DoProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FallWorkScoreModel.FallWorkScore.ScoreList sl = list.get(page).get(position);
        ((DoProgressViewHolder) holder).tvState.setText(sl.getSuccess());
        ((DoProgressViewHolder) holder).tvTime.setText(sl.getUseTime());
        ((DoProgressViewHolder) holder).tvStuName.setText(sl.getUsr_name());
        ((DoProgressViewHolder) holder).tvPosition.setText((position + page * 7 + 1) + "");
        if (sl.getSuccess().equals("成功")) {
            ((DoProgressViewHolder) holder).imgComplete.setVisibility(View.VISIBLE);
        } else {
            ((DoProgressViewHolder) holder).imgComplete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.get(page).size();
    }

    class DoProgressViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition;
        TextView tvStuName;
        ImageView imgComplete;
        TextView tvState;
        TextView tvTime;

        public DoProgressViewHolder(View itemView) {
            super(itemView);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_end_position);
            tvState = (TextView) itemView.findViewById(R.id.tv_end_state);
            tvStuName = (TextView) itemView.findViewById(R.id.tv_student_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_complete_time);
            imgComplete = (ImageView) itemView.findViewById(R.id.img_complete);
        }
    }
}
