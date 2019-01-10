package com.mx.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.teacher.R;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.constant.TestConstant;
import com.mx.teacher.entity.AllExamsModel;

import java.util.List;

/**
 * Created by Archer on 16/9/9.
 */
public class HomeWorkAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<AllExamsModel.AllExams> list;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeWorkAdapter(Context context, List<AllExamsModel.AllExams> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_home_work_item, parent, false);
        return new HomeWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String url = TestConstant.GET_HOME_WORK_BY_ID + list.get(position).getId();
        ((HomeWorkViewHolder) holder).tvExamsChapter.setText(list.get(position).getChapter());
        ((HomeWorkViewHolder) holder).tvExamsTime.setText(list.get(position).getArrTime());
        ((HomeWorkViewHolder) holder).tvExamsTitle.setText(list.get(position).getTitle());
        ((HomeWorkViewHolder) holder).tvExamsTeacher.setText(list.get(position).getTeacher());
        ((HomeWorkViewHolder) holder).tvExamsSubject.setText(list.get(position).getSubject());
        if (ACache.get(context).getAsString(url) == null) {
            ((HomeWorkViewHolder) holder).tvExamsStates.setText("未下载");
        } else if (list.get(position).getReplyStatus() == 0) {
            ((HomeWorkViewHolder) holder).tvExamsStates.setText("已完成");
        } else if (list.get(position).getReplyStatus() == 1) {
            ((HomeWorkViewHolder) holder).tvExamsStates.setText("已批复");
        } else {
            ((HomeWorkViewHolder) holder).tvExamsStates.setText("未完成");
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class HomeWorkViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamsChapter;
        TextView tvExamsTitle;
        TextView tvExamsStates;
        TextView tvExamsSubject;
        TextView tvExamsTeacher;
        TextView tvExamsTime;

        public HomeWorkViewHolder(View itemView) {
            super(itemView);
            tvExamsChapter = (TextView) itemView.findViewById(R.id.tv_all_exams_chapter);
            tvExamsStates = (TextView) itemView.findViewById(R.id.tv_all_exams_states);
            tvExamsSubject = (TextView) itemView.findViewById(R.id.tv_all_exams_subject);
            tvExamsTeacher = (TextView) itemView.findViewById(R.id.tv_all_exams_teacher);
            tvExamsTitle = (TextView) itemView.findViewById(R.id.tv_all_exams_title);
            tvExamsTime = (TextView) itemView.findViewById(R.id.tv_all_exams_arrTime);
        }
    }
}
