package com.mx.reader.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.reader.R;
import com.onyx.android.sdk.data.cms.OnyxCmsCenter;
import com.onyx.android.sdk.data.cms.OnyxMetadata;
import com.onyx.android.sdk.data.cms.OnyxThumbnail;
import com.onyx.android.sdk.data.util.RefValue;

import java.io.File;
import java.util.List;

/**
 * Created by Archer on 16/8/4.
 */
public class LocalAllBookRecyclerAdapter extends RecyclerView.Adapter {
    private List<List<File>> listFiles;
    private int index;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public LocalAllBookRecyclerAdapter(List<List<File>> listFiles,
                                       Context context, int index) {
        this.listFiles = listFiles;
        this.context = context;
        this.index = index;
    }

    public void setOnItemClickLIstener(OnItemClickListener onItemClickLIstener) {
        this.onItemClickListener = onItemClickLIstener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_book_more_type_item, parent, false);
        return new BookTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((BookTypeViewHolder) holder).tvBookType.setText(listFiles.get(index).get(position).getName());
        try {
            OnyxMetadata onyxMetadata = OnyxCmsCenter.getMetadata(context, listFiles.get(index).get(position));
            RefValue<Bitmap> refValue = new RefValue<Bitmap>();
            if (OnyxCmsCenter.getThumbnail(context,
                    onyxMetadata, OnyxThumbnail.ThumbnailKind.Middle, refValue)) {
                ((BookTypeViewHolder) holder).imgBookType.setImageBitmap(refValue.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public int getItemCount() {
        if (listFiles != null) {
            return listFiles.size() > 0 ? listFiles.get(index).size() : 0;
        } else {
            return 0;
        }
    }

    public class BookTypeViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookType;
        ImageView imgBookType;

        public BookTypeViewHolder(View itemView) {
            super(itemView);
            tvBookType = (TextView) itemView.findViewById(R.id.tv_recycler_item_book_type);
            imgBookType = (ImageView) itemView.findViewById(R.id.img_recycler_item_book_type);
        }
    }
}
