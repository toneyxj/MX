package com.moxi.CPortTeacher.adapter.weedout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 16/11/9.
 */
public class TitleAdapter extends RecyclerView.Adapter {

    private Context context;
    private int page;
    private int currentIndex = -99;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        this.notifyDataSetChanged();
    }

    private List<List<Integer>> listData = new ArrayList<>();
    private List<Integer> listTemp = new ArrayList<>();

    public TitleAdapter(Context context, int page, int size) {
        this.context = context;
        this.page = page;
        listTemp.clear();
        for (int i = 0; i < size; i++) {
            listTemp.add(i);
        }
        listData = ListUtils.splitList(listTemp, 12);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_margin_examination_layout, null);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((TitleViewHolder) holder).tvIndex.setText((position + 1) + "");
        if (currentIndex == position) {
            ((TitleViewHolder) holder).tvIndex.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
            ((TitleViewHolder) holder).tvIndex.setTextColor(context.getResources().getColor(R.color.colorWihte));
        } else {
            ((TitleViewHolder) holder).tvIndex.setBackgroundResource(R.drawable.qimu_backgroundshape);
            ((TitleViewHolder) holder).tvIndex.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
        ((TitleViewHolder) holder).rlPage.setVisibility(View.GONE);

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
        return listData.get(page).size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView tvIndex;
        RelativeLayout rlPage;
        ImageView imgPage;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.inf);
            rlPage = (RelativeLayout) itemView.findViewById(R.id.rl_img_page);
            imgPage = (ImageView) itemView.findViewById(R.id.img_menu_page);
        }
    }
}
