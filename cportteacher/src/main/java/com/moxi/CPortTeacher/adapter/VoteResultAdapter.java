package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.VoteProgressModel;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class VoteResultAdapter  extends BAdapter<VoteProgressModel> {
    public VoteResultAdapter(Context context, List<VoteProgressModel> list) {
        super(context, list);
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_vote_result;
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();
            holder.name_and_index = (TextView) view.findViewById(R.id.name_and_index);
            holder.vote_number = (TextView) view.findViewById(R.id.vote_number);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name_and_index.setText(getList().get(position).name);
        holder.vote_number.setText(String.valueOf(getList().get(position).curNumber));
    }
    public class ViewHolder {
        TextView name_and_index;
        TextView vote_number;
    }
    private String getIndex(int position){
        String index="";
        switch (position){
            case 0:
                index="A";
                break;
            case 1:
                index="B";
                break;
            case 2:
                index="C";
                break;
            case 3:
                index="D";
                break;
            case 4:
                index="E";
                break;
            case 5:
                index="F";
                break;
            case 6:
                index="G";
                break;
            default:
                index="#";
                break;
        }
        return index+".";
    }

}
