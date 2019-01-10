package com.moxi.copybook.adapter;

import android.content.Context;
import android.view.View;

import com.moxi.copybook.R;
import com.moxi.copybook.view.PrictiseTextView;
import com.mx.mxbase.adapter.BAdapter;
import com.mx.mxbase.model.PrictiseTextBeen;

import java.util.List;


/**
 * Created by Administrator on 2016/8/10.
 */
public class PrictiseTextAdapter extends BAdapter<PrictiseTextBeen> {
    private int width;
    public PrictiseTextAdapter(Context context, List<PrictiseTextBeen> list, int width) {
        super(context, list);
        this.width=width;
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_prictise_text;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.prictise_text = (PrictiseTextView) view.findViewById(R.id.prictise_text);
            holder.prictise_text.setWidthAndHeight(width);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.prictise_text.setInitValue(getList().get(position));
    }
    public class ViewHolder {
        PrictiseTextView prictise_text;
    }
}
