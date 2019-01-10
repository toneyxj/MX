package com.mx.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.teacher.R;
import com.mx.teacher.entity.Test;

/**
 * Created by Archer on 16/9/9.
 */
public class ReplyResultAdapter extends RecyclerView.Adapter {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private Test test;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ReplyResultAdapter(Context context, Test test) {
        this.context = context;
        this.test = test;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_reply_result_item, parent, false);
        return new HomeWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (test.getResult().getExamList().get(position).getAnswer().equals(test.getResult().getExamList().get(position).getResult())) {
            ((HomeWorkViewHolder) holder).imgIndex.setBackgroundResource(R.drawable.moxi_shape_black_corner_35);
            ((HomeWorkViewHolder) holder).tvIndex.setTextColor(context.getResources().getColor(R.color.colorWihte));
        } else {
            ((HomeWorkViewHolder) holder).imgIndex.setBackgroundResource(R.drawable.moxi_shape_white_corner_35);
            ((HomeWorkViewHolder) holder).tvIndex.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
        ((HomeWorkViewHolder) holder).tvIndex.setText((position + 1) + "");
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class HomeWorkViewHolder extends RecyclerView.ViewHolder {

        TextView tvIndex;
        ImageView imgIndex;

        public HomeWorkViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_reply_result_index);
            imgIndex = (ImageView) itemView.findViewById(R.id.img_reply_result);
        }
    }
}
