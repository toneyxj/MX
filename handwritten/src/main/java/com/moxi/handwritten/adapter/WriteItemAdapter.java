package com.moxi.handwritten.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.handwritten.R;
import com.moxi.handwritten.WritPadActivity;
import com.moxi.handwritten.WriteDrawActivity;
import com.mx.mxbase.adapter.BAdapter;
import com.moxi.handwritten.model.WriteFileBeen;
import com.mx.mxbase.utils.LocationImageLoader;
import com.mx.mxbase.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class WriteItemAdapter extends BAdapter<WriteFileBeen> {
private boolean showAelect=false;

    public void setShowAelect(boolean showAelect) {
        this.showAelect = showAelect;
    }

    public WriteItemAdapter(Context context, List<WriteFileBeen> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_write_item;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.all_layout = (RelativeLayout) view.findViewById(R.id.all_layout);
            holder.show_select = (ImageView) view.findViewById(R.id.show_select);
            holder.show_image = (ImageView) view.findViewById(R.id.show_image);
            holder.file_name = (TextView) view.findViewById(R.id.file_name);
            holder.file_create_date = (TextView) view.findViewById(R.id.file_create_date);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.all_layout.getLayoutParams();
            params.width = (int) WritPadActivity.width;
            params.height = (int) WritPadActivity.high;
            holder.all_layout.setLayoutParams(params);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        WriteFileBeen been = getItem(position);
        if (been.type.equals("-1")) {
            holder.show_image.setScaleType(ImageView.ScaleType.CENTER);
            holder.show_image.setBackgroundResource(R.drawable.dotted_line);
            holder.show_image.setImageResource(R.mipmap.write_add);
        } else if (been.type.equals("1")) {
            holder.show_image.setBackgroundResource(R.drawable.di_white_bian_font_line1);
            holder.show_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LocationImageLoader.getInstance().loadImage(been.image, holder.show_image, null);
        } else if (been.type.equals("0")) {
            holder.show_image.setBackgroundResource(R.color.colorWihte);
            holder.show_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.show_image.setImageResource(R.mipmap.write_folder);
        }
        if (showAelect&&!been.type.equals("-1")){
            holder.show_select.setVisibility(View.VISIBLE);
            if (been.isSelect){
                holder.show_select.setImageResource(R.mipmap.have_select);
            }else{
                holder.show_select.setImageResource(R.mipmap.non_select);
            }
        }else{
            holder.show_select.setVisibility(View.INVISIBLE);
        }
        if (been.type.equals("1")){
            holder.file_name.setText(been.fileName.replace(WriteDrawActivity.FileLast,""));
        }else{
            holder.file_name.setText(been.fileName);
        }

        if (been.createDate.equals("")){
            holder.file_create_date.setText(been.createDate);
        }else{
            holder.file_create_date.setText(StringUtils.getStringDate(been.createDate));
        }

    }

    public class ViewHolder {
        RelativeLayout all_layout;
        ImageView show_select;
        ImageView show_image;
        TextView file_name;
        TextView file_create_date;
    }
    public void updateFileName(int position, GridView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {

            View view = listView.getChildAt(position - visibleFirstPosi);
            TextView file_name = (TextView) view.findViewById(R.id.file_name);
            file_name.setText(getList().get(position).fileName);
        }
    }

    public void updateSelect(int position, GridView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {

            View view = listView.getChildAt(position - visibleFirstPosi);
            ImageView select_image = (ImageView) view.findViewById(R.id.show_select);

            boolean isTrue=getList().get(position).isSelect;
            if (isTrue) {
                select_image.setImageResource(R.mipmap.have_select);
            } else {
                select_image.setImageResource(R.mipmap.non_select);
            }
        }
    }
}
