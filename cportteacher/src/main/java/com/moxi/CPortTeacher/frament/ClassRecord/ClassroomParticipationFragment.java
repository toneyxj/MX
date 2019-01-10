package com.moxi.CPortTeacher.frament.ClassRecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ParticipationRecordAdapter;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ParticipateRecordModel;
import com.mx.mxbase.view.LinerlayoutInter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *课堂参与记录
 * Created by Administrator on 2016/10/28.
 */
public class ClassroomParticipationFragment extends CportBaseFragment implements LinerlayoutInter.LinerLayoutInter{
    @Bind(R.id.main_layout_inter)
    LinerlayoutInter main_layout_inter;
    @Bind(R.id.record_list)
    ListView record_list;

    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;
    private List<ParticipateRecordModel> listData=new ArrayList<>();
    private List<ParticipateRecordModel> listMiddle=new ArrayList<>();
    private ParticipationRecordAdapter adapter;
    private int List_item_height=0;

    /**
     * 当前显示页数
     */
    private int CurrentIndex = 0;
    /**
     * 总页数
     */
    private int totalIndex = 1;
    private int pageSize = 11;
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_classroom_participation;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        main_layout_inter.setLayoutInter(this);
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
        listData.add(new ParticipateRecordModel());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last_page:
                moveLeft();
                break;
            case R.id.next_page:
                moveRight();
                break;
            default:
                break;
        }
    }
    @Override
    public void onViewInitChange() {
        if (record_list.getHeight()==0)return;
        List_item_height=(record_list.getHeight()-10)/(pageSize);
        initSonData();
    }

    /**
     * 分配下面数据以刷新
     */
    public void initSonData() {
        //计算页数
        totalIndex = listData.size() / pageSize;
        totalIndex += listData.size() % pageSize == 0 ? 0 : 1;

        //计算当前页数
        if (CurrentIndex > totalIndex - 1) {
            CurrentIndex = totalIndex - 1;
        }
        if (CurrentIndex<0)CurrentIndex=0;
        if (totalIndex==0)totalIndex=1;

        if (listData.size() == 0) {
            adapterItems(listData);
        } else if (totalIndex - 1 == CurrentIndex) {
            adapterItems(listData.subList(CurrentIndex * pageSize, listData.size()));
        } else {
            adapterItems(listData.subList(CurrentIndex * pageSize, (CurrentIndex + 1) * pageSize));
        }
        setShowText();
    }
    private void adapterItems(List<ParticipateRecordModel> List) {
        listMiddle.clear();
        listMiddle.addAll(List);
        if (adapter == null) {
            adapter = new ParticipationRecordAdapter(context,listMiddle,List_item_height);
            record_list.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 设置显示当前显示页数
     */
    public void setShowText() {
        show_index.setText(String.valueOf(CurrentIndex + 1) + "/" + totalIndex);
    }
    @Override
    public void moveLeft() {
        if (CurrentIndex>0){
            CurrentIndex--;
            initSonData();
        }
    }

    @Override
    public void moveRight() {
        if (CurrentIndex<(totalIndex-1)){
            CurrentIndex++;
            initSonData();
        }
    }
}
