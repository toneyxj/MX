package com.moxi.studentclient.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.widget.ProgressArcView;
import com.moxi.studentclient.R;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * 课件白板显示adapter
 * Created by Administrator on 2016/11/30.
 */
public class CoursewareWhiteBoardFileAdapter extends BAdapter<CoursewareWhiteBoardModel> {

    public CoursewareWhiteBoardFileAdapter(Context context, List<CoursewareWhiteBoardModel> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_courseware_white_board;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.file_name = (TextView) view.findViewById(R.id.file_name);
            holder.progress = (ProgressArcView) view.findViewById(R.id.progress);
            view.setTag(holder);
            holder.progress.setVisibility(View.INVISIBLE);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.file_name.setText(getList().get(position).getFileName());
    }

    public class ViewHolder {
        TextView file_name;
        ProgressArcView progress;
    }

    /**
     * 更新文件进度
     *
     * @param FileId 下载路径
     * @param currentSize 当前下载值
     * @param gridView        显示列表数据view
     */
    public void updataProgress(long FileId, int currentSize, GridView gridView) {
        int index=getindex(FileId);
        if (index==-1)return;
            View view = gridView.getChildAt(index);
            ProgressArcView progress= (ProgressArcView) view.findViewById(R.id.progress);
            progress.initsetNumber(100,currentSize);
    }
    public int getindex(long fileId){
        int i=0;
        for (CoursewareWhiteBoardModel model:getList()){
            if (model.FileId==fileId){
                return i;
            }
            i++;
        }
        return -1;
    }

}
