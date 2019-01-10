package com.moxi.CPortTeacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.SetSubjectModel;
import com.mx.mxbase.adapter.BAdapter;

import java.util.List;

/**
 * 出题记录adapter
 * Created by Administrator on 2016/11/9.
 */
public class SetSubjectAdapter extends BAdapter<SetSubjectModel> {
    private int height;

    public SetSubjectAdapter(Context context, List<SetSubjectModel> list, int height) {
        super(context, list);
        this.height = height;
    }

    @Override
    public int getContentView() {
        return R.layout.adapter_set_subject;
    }

    @Override
    public void setItemHeight(View view) {
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(param);
    }

    @Override
    public void onInitView(View view, int position, boolean firstAdd) {
        ViewHolder holder;
        if (firstAdd) {
            holder = new ViewHolder();

//            holder.onclick_item = (LinearLayout) view.findViewById(R.id.onclick_item);//提示时间
            holder.operate_time = (TextView) view.findViewById(R.id.operate_time);//提示时间
            holder.title = (TextView) view.findViewById(R.id.title);//标题
            holder.status_img = (ImageView) view.findViewById(R.id.status_img);//显示做题状态
            holder.reissue = (TextView) view.findViewById(R.id.reissue);//显示是否收到题
            holder.date = (TextView) view.findViewById(R.id.date);//具体日期
            holder.time = (TextView) view.findViewById(R.id.time);//具体时间

            holder.reissue.setVisibility(View.INVISIBLE);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SetSubjectModel model = getList().get(position);

        holder.operate_time.setText(model.time);
        holder.title.setText(model.content);
        holder.date.setText(model.timeData);
        holder.time.setText(model.timeHours);

//        holder.onclick_item.setTag(position);
//        holder.onclick_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.getInstance().showToastShort("点击");
//                clickItem.onClickItem((Integer) v.getTag());
//            }
//        });
    }

    public class ViewHolder {
//        LinearLayout onclick_item;//提示时间
        TextView operate_time;//提示时间
        TextView title;//标题
        ImageView status_img;//显示做题状态
        TextView reissue;//显示是否收到题
        TextView date;//具体日期
        TextView time;//具体时间
    }
//
//    /**
//     * 更改显示状态
//     * @param position
//     * @param listView
//     */
//    public void updateSelect(int position, ListView listView) {
//        int visibleFirstPosi = listView.getFirstVisiblePosition();
//        int visibleLastPosi = listView.getLastVisiblePosition();
//        if (position >= visibleFirstPosi && position <= visibleLastPosi) {
//
//            View view = listView.getChildAt(position - visibleFirstPosi);
//            ImageView select_icon = (ImageView) view.findViewById(R.id.select_icon);
//
//            boolean isTrue=getList().get(position).isSelect;
//            select_icon.setVisibility(isTrue?View.VISIBLE:View.INVISIBLE);
//        }
//    }
}
