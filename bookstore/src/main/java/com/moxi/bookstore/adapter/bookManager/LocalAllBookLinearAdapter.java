package com.moxi.bookstore.adapter.bookManager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.bookstore.R;
import com.moxi.bookstore.db.EbookDB;
import com.moxi.bookstore.db.TableConfig;
import com.moxi.bookstore.db.TableOperate;
import com.moxi.bookstore.modle.BookStoreFile;
import com.moxi.bookstore.utils.ReadManagerPicUtils;
import com.mx.mxbase.constant.PhotoConfig;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.GlideUtils;
import com.mx.mxbase.utils.SharePreferceUtil;
import com.mx.mxbase.utils.StorageUtil;
import com.mx.mxbase.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by King on 2017/7/25.
 */

public class LocalAllBookLinearAdapter extends RecyclerView.Adapter {
    private List<BookStoreFile> listFiles;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private RecyclerView recyclerView;
    SharePreferceUtil share;

    public List<BookStoreFile> getListFiles() {
        return listFiles;
    }

    public LocalAllBookLinearAdapter(List<BookStoreFile> listFiles, Context context, RecyclerView recyclerView) {
        this.listFiles = listFiles;
        this.context = context;
        this.recyclerView = recyclerView;
        share = SharePreferceUtil.getInstance(context);
    }

    public void setOnItemClickLIstener(OnItemClickListener onItemClickLIstener) {
        this.onItemClickListener = onItemClickLIstener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linearlayout_stacks, parent, false);
        int height = (1030 - 10) / 9;
//        int height = (recyclerView.getHeight() - 10) / 9;
//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.getLayoutParams().height = height;
        return new LocalAllBookLinearAdapter.BookTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String bookFullName = listFiles.get(position).getName();
        ((BookTypeViewHolder) holder).tvBookName.setText(bookFullName.substring(0, bookFullName.lastIndexOf(".")));
        File f = new File(listFiles.get(position).getFilePath());
        ((BookTypeViewHolder) holder).tvBookSize.setText(StorageUtil.getPrintSize(f.length()) + "B");
        ((BookTypeViewHolder) holder).tvBookTime.setText(getModifiedTime(listFiles.get(position).getFilePath()));
        String path = listFiles.get(position).filePath;
        EbookDB book = TableOperate.getInstance().queryByPath(TableConfig.TABLE_NAME, path);
        if (book == null) {
            ReadManagerPicUtils.getInstance().setLocationBookPic(context,((BookTypeViewHolder) holder).imgBookType, listFiles.get(position).filePath,((BookTypeViewHolder) holder).read_progress);
        } else {
            GlideUtils.getInstance().loadGreyImage(context, ((BookTypeViewHolder) holder).imgBookType, book.iconUrl);
            StringUtils.setDDReadBookProgress(true,book.progress,((BookTypeViewHolder) holder).read_progress);
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    private int getSources(File file) {
        return PhotoConfig.getSources(file.getName());
    }

    @Override
    public int getItemCount() {
        if (listFiles != null) {
            return listFiles.size();
        } else {
            return 0;
        }
    }

    public class BookTypeViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookName;
        TextView tvBookSize;
        TextView tvBookTime;
        TextView read_progress;
        ImageView imgBookType;

        public BookTypeViewHolder(View itemView) {
            super(itemView);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_linear_item_book_name);
            tvBookSize = (TextView) itemView.findViewById(R.id.tv_linear_item_book_size);
            tvBookTime = (TextView) itemView.findViewById(R.id.tv_linear_item_book_time);
            read_progress = (TextView) itemView.findViewById(R.id.read_progress);
            imgBookType = (ImageView) itemView.findViewById(R.id.img_linear_item_book_type);
        }
    }

    /**
     * 读取修改时间的方法2
     */
    public String getModifiedTime(String filePath) {
        File f = new File(filePath);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }
}
