package com.moxi.taskteacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.adapter.TaskDataAdapter;
import com.moxi.taskteacher.model.TaskDataModel;
import com.moxi.taskteacher.teainterface.ClickItem;
import com.moxi.taskteacher.utils.TaskDataModelUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.view.LinerlayoutInter;
import com.mx.mxbase.view.RelativeLayoutInter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 已下载资料
 * Created by Archer on 2016/12/30.
 */
public class DownLoadDataFragment extends BaseFragment implements LinerlayoutInter.LinerLayoutInter, ClickItem, AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView all_files;
    private ImageButton last_page;
    private RelativeLayoutInter mian_page_layout;
    private TextView show_index;
    private TextView fail_hitn;
    private ImageButton next_page;

    private List<TaskDataModel> listData = new ArrayList<>();
    private List<TaskDataModel> middleData = new ArrayList<>();
    private TaskDataAdapter adapter;
    private int rows = 12;
    //总页数
    private int totalPage = 0;
    //当前页数
    private int currentPage = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_download, container, false);
        //初始化view的各控件
        isPrepared = true;

        all_files = (GridView) view.findViewById(R.id.download_files);
        last_page = (ImageButton) view.findViewById(R.id.down_last_page);
        mian_page_layout = (RelativeLayoutInter) view.findViewById(R.id.data_download_layout);
        show_index = (TextView) view.findViewById(R.id.down_show_index);
        fail_hitn = (TextView) view.findViewById(R.id.fail_message);
        next_page = (ImageButton) view.findViewById(R.id.down_next_page);

        mian_page_layout.setLayoutInter(this);
        all_files.setOnItemClickListener(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        fail_hitn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            init();
        }
    }

    @Override
    protected void loadData() {
        if (!isPrepared) {
            return;
        }
        init();
    }

    private void init() {
        listData.clear();
        listData.addAll(TaskDataModelUtils.getInstance().getAllReadRecorder());
        setAdapter();
    }

    private void setAdapter() {
        middleData.clear();
        totalPage = (listData.size() / rows) + ((listData.size() % rows == 0) ? 0 : 1);
        if (currentPage < 0) currentPage = 0;

        if (currentPage >= totalPage) {
            currentPage = totalPage - 1;
        }
        if (listData.size() != 0) {
            fail_hitn.setVisibility(View.GONE);
            if (currentPage == (totalPage - 1)) {
                middleData.addAll(listData.subList(currentPage * rows, listData.size()));
            } else {
                middleData.addAll(listData.subList(currentPage * rows, (currentPage + 1) * rows));
            }
            if (adapter == null) {
                adapter = new TaskDataAdapter(getContext(), middleData, this);
                all_files.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            fail_hitn.setVisibility(View.VISIBLE);
            fail_hitn.setText("暂无资料");
        }
        show_index.setText(String.valueOf(currentPage + 1) + "/" + totalPage);
    }

    /**
     * 返回监听
     */
    public void onKeyPressDown() {
        getActivity().finish();
    }

    public void onPageUp() {
    }

    public void onPageDown() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.down_last_page:
                moveRight();
                break;
            case R.id.down_next_page:
                moveLeft();
                break;
        }
    }

    @Override
    public void moveRight() {
        APPLog.e("moveRight");
        if (currentPage < (totalPage - 1)) {
            currentPage++;
            setAdapter();
        }
    }

    @Override
    public void moveLeft() {
        APPLog.e("moveLeft");
        if (currentPage > 0) {
            currentPage--;
            setAdapter();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FileUtils.getInstance().openFile(getActivity(), new File(listData.get(i).getSavePath()));
    }

    @Override
    public void onClickItem(int index) {
    }
}
