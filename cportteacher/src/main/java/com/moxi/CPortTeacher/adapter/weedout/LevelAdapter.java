package com.moxi.CPortTeacher.adapter.weedout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.mx.mxbase.interfaces.OnItemClickListener;

/**
 * 难度适配器
 * Created by Archer on 16/11/9.
 */
public class LevelAdapter extends RecyclerView.Adapter {

    private Context context;
    private int count;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public LevelAdapter(Context context, int count) {
        this.count = count;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_weed_out_level_item, null);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position % 4 == 3 || position == count - 1) {
            ((LevelViewHolder) holder).imgNext.setVisibility(View.GONE);
        } else {
            ((LevelViewHolder) holder).imgNext.setVisibility(View.VISIBLE);
        }
        ((LevelViewHolder) holder).tvLevel.setText((position + 1) + "");
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class LevelViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNext;
        TextView tvLevel;

        public LevelViewHolder(View itemView) {
            super(itemView);
            imgNext = (ImageView) itemView.findViewById(R.id.img_weed_out_arro_next);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_weed_out_level);
        }
    }
}
