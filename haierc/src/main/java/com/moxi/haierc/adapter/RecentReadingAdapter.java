package com.moxi.haierc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.haierc.R;
import com.moxi.haierc.model.BookStoreFile;
import com.moxi.haierc.util.ReadManagerPicUtils;
import com.mx.mxbase.utils.GlideUtils;
import com.mx.mxbase.utils.StringUtils;

import java.util.List;

/**
 * Created by King on 2017/7/6.
 */

public class RecentReadingAdapter extends BaseAdapter {

    private Context context;
    private GridView gridView;
    private List<BookStoreFile> listRecent;

    public void dataChange(List<BookStoreFile> listRecent) {
        this.listRecent = listRecent;
        this.notifyDataSetChanged();
    }

    public RecentReadingAdapter(Context context, GridView gridView, List<BookStoreFile> listRecent) {
        this.context = context;
        this.gridView = gridView;
        this.listRecent = listRecent;
    }

    @Override
    public int getCount() {
        return listRecent == null ? 0 : listRecent.size();
    }

    @Override
    public BookStoreFile getItem(int position) {
        return listRecent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookStoreFile mode = listRecent.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.new_main_recent_reading_item, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_new_main_recent);
        TextView read_progress = (TextView) convertView.findViewById(R.id.read_progress);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_new_main_recent_name);

        int height = gridView.getHeight() - 10;
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(param);

        if (mode.getIsDdBook() == 1) {
            GlideUtils.getInstance().loadGreyImage(context, imageView, mode.bookImageUrl);
            StringUtils.setDDReadBookProgress(true,mode.progress,read_progress);
        } else {
            ReadManagerPicUtils.getInstance().setLocationBookPic(context, imageView, mode.getFilePath(),read_progress);
        }
        String name = mode.filePath;
        name = name.substring(name.lastIndexOf("/") + 1);
        textView.setText(name);
        return convertView;
    }
}
