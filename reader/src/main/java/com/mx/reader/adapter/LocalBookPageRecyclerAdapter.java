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
public class LocalBookPageRecyclerAdapter extends RecyclerView.Adapter {
    private List<File> listFile;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public LocalBookPageRecyclerAdapter(List<File> listPage, Context context) {
        this.listFile = listPage;
        this.context = context;
    }

    public void setOnItemClickLIstener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_book_type_item, parent, false);
        return new BookTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((BookTypeViewHolder) holder).tvBookType.setText(listFile.get(position).getName());
        try {
            OnyxMetadata onyxMetadata = OnyxCmsCenter.getMetadata(context, listFile.get(position));
            RefValue<Bitmap> refValue = new RefValue<Bitmap>();
            if (OnyxCmsCenter.getThumbnail(context,
                    onyxMetadata, OnyxThumbnail.ThumbnailKind.Middle, refValue)) {
                ((BookTypeViewHolder) holder).imgBookType.setImageBitmap(refValue.getValue());
            } else {
                ((BookTypeViewHolder) holder).imgBookType.setImageResource(R.mipmap.mx_img_defalt_background);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ((BookTypeViewHolder) holder).imgBookType.setImageResource(R.mipmap.mx_img_defalt_background);
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
        if (listFile == null) {
            return 0;
        } else if (listFile.size() > 8) {
            return 8;
        } else {
            return listFile.size();
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
