package com.moxi.handwritten.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.moxi.handwritten.R;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class LineTextAdapter extends BAdapter<String> {
    /**
     * 是否画线
     */
    private  boolean isline;
    public LineTextAdapter(Context context, List<String> list, boolean isline) {
        super(context, list);
        this.isline=isline;
    }

    @Override
    public int getContentView() {
        return R.layout.addview_move_write;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.show_line = (View) view.findViewById(R.id.show_line);
            holder.fodler_path = (TextView) view.findViewById(R.id.fodler_path);

            if (!isline) {
                holder.show_line.setVisibility(View.GONE);
            }

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.fodler_path.setText(getList().get(position));
    }
    public class ViewHolder {
        View show_line;
        TextView fodler_path;
    }
}
