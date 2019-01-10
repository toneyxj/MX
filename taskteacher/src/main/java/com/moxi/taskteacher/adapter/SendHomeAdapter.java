package com.moxi.taskteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.cache.ACache;
import com.moxi.taskteacher.httpconstans.TeaContsans;
import com.moxi.taskteacher.model.HomeWork;
import com.moxi.taskteacher.teainterface.ItemClickListener;

import java.util.List;

/**
 * Created by Archer on 2016/12/22.
 */
public class SendHomeAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<List<HomeWork.HomeWorkModel.WorkModel>> listWork;
    private ItemClickListener onItemClickListener;
    private int page;

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SendHomeAdapter(Context context, List<List<HomeWork.HomeWorkModel.WorkModel>> listWork, int page) {
        this.context = context;
        this.listWork = listWork;
        this.page = page;
    }

    public void setPage(int page) {
        this.page = page;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_send_item, parent, false);
        return new HomeWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String states = ACache.get(context).getAsString(TeaContsans.GET_ALL_WORK + listWork.get(page).get(position).getId());
        ((HomeWorkViewHolder) holder).tvType.setText(listWork.get(page).get(position).getType());
        ((HomeWorkViewHolder) holder).tvTitle.setText(listWork.get(page).get(position).getName());
        if (states != null) {
            if (states.equals("发送")) {
                ((HomeWorkViewHolder) holder).imgState.setText("未发送");
            } else {
                ((HomeWorkViewHolder) holder).imgState.setText(states);
            }
            ((HomeWorkViewHolder) holder).tvState.setText(states);
        } else {
            ((HomeWorkViewHolder) holder).imgState.setText("未下载");
            ((HomeWorkViewHolder) holder).tvState.setText("下载");
        }
        holder.itemView.setTag(states);
        if (onItemClickListener != null) {
            ((HomeWorkViewHolder) holder).llState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(holder.itemView, position);
                    String temp = (String) holder.itemView.getTag();
                    if (temp != null) {
//                        ACache.get(context).put(TeaContsans.GET_ALL_WORK + position, "已发送");
                    } else {
                        ACache.get(context).put(TeaContsans.GET_ALL_WORK + listWork.get(page).get(position).getId(), "发送");
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listWork == null ? 0 : listWork.get(page).size();
    }

    class HomeWorkViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvType;
        TextView tvState, imgState;
        LinearLayout llState;

        public HomeWorkViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_send_item_title);
            tvType = (TextView) itemView.findViewById(R.id.tv_send_item_type);
            tvState = (TextView) itemView.findViewById(R.id.tv_send_state);
            llState = (LinearLayout) itemView.findViewById(R.id.ll_send_state);
            imgState = (TextView) itemView.findViewById(R.id.img_send_state);
        }
    }
}
