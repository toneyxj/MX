package com.moxi.taskteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.model.StudentModel;
import com.mx.mxbase.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 16/11/7.
 */
public class StudentAdapter extends RecyclerView.Adapter {

    private Context context;
    private int page;
    private List<List<StudentModel.StudentWeed>> lists = new ArrayList<>();

    private List<String> listChose;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, List<List<StudentModel.StudentWeed>> listData, int page) {
        this.context = context;
        this.page = page;
        this.lists = listData;
    }

    public void setListChose(List<String> listChose) {
        this.listChose = listChose;
        this.notifyDataSetChanged();
    }

    public void setPage(int page) {
        this.page = page;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_item_student, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((StudentViewHolder) holder).tvName.setText(lists.get(page).get(position).getName());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        if (listChose != null && listChose.contains(position + "")) {
            ((StudentViewHolder) holder).imgChecked.setVisibility(View.VISIBLE);
        } else {
            ((StudentViewHolder) holder).imgChecked.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lists.get(page).size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imgAvatar;
        ImageView imgChecked;

        public StudentViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_student_name);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_student_avatar);
            imgChecked = (ImageView) itemView.findViewById(R.id.img_student_is_checked);
        }
    }
}
