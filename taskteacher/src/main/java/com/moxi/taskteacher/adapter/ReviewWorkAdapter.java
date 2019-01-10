package com.moxi.taskteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.model.ReviewWorkList;
import com.moxi.taskteacher.teainterface.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 2016/12/22.
 */
public class ReviewWorkAdapter extends RecyclerView.Adapter {

    private Context context;
    private ItemClickListener onItemClickListener;
    private int page;
    private List<List<ReviewWorkList.ReviewWork.Review>> listValue = new ArrayList<>();

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ReviewWorkAdapter(Context context, List<List<ReviewWorkList.ReviewWork.Review>> listWork, int page) {
        this.context = context;
        this.page = page;
        this.listValue = listWork;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_review_item, parent, false);
        return new HomeWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((HomeWorkViewHolder) holder).tvName.setText(listValue.get(page).get(position).getStudentName());
        ((HomeWorkViewHolder) holder).tvTime.setText(listValue.get(page).get(position).getTime());
        if (listValue.get(page).get(position).getReplyFile().equals("")) {
            ((HomeWorkViewHolder) holder).tvValue.setText(listValue.get(page).get(position).getName() + "(未批复)");
        } else {
            ((HomeWorkViewHolder) holder).tvValue.setText(listValue.get(page).get(position).getName() + "(已批复)");
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listValue == null ? 0 : listValue.get(page).size();
    }

    class HomeWorkViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvName;
        TextView tvValue;
        TextView tvTime;

        public HomeWorkViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_item_avatar);
            tvValue = (TextView) itemView.findViewById(R.id.tv_test_value);
            tvName = (TextView) itemView.findViewById(R.id.tv_student_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_write_time);
        }
    }
}
