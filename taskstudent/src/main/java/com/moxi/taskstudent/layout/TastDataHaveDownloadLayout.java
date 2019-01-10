package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.adapter.TaskDataAdapter;
import com.moxi.taskstudent.model.TaskDataModel;
import com.moxi.taskstudent.taskInterface.ClickItem;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.utils.TaskDataModelUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.view.LinerlayoutInter;
import com.mx.mxbase.view.RelativeLayoutInter;

import java.util.ArrayList;
import java.util.List;

/**
 * 已下载资料展示布局
 * Created by Administrator on 2016/12/30.
 */
public class TastDataHaveDownloadLayout extends BaseLayout implements LinerlayoutInter.LinerLayoutInter, ClickItem {
    public TastDataHaveDownloadLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);
    }

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

    @Override
    int getLayout() {
        return R.layout.addview_task_data;
    }

    @Override
    void initLayout(View view) {
        all_files = (GridView) findViewById(R.id.all_files);
        last_page = (ImageButton) findViewById(R.id.last_page);
        mian_page_layout = (RelativeLayoutInter) findViewById(R.id.mian_page_layout);
        show_index = (TextView) findViewById(R.id.show_index);
        fail_hitn = (TextView) findViewById(R.id.fail_hitn);
        next_page = (ImageButton) findViewById(R.id.next_page);

        mian_page_layout.setLayoutInter(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        fail_hitn.setOnClickListener(this);
        rows = 12;
    }

    public void refuresh() {
        listData.clear();
        listData.addAll(TaskDataModelUtils.getInstance().getAllReadRecorder());
        setAdapter();
    }

    public void setAdapter() {
        middleData.clear();
        totalPage = (listData.size() / rows) + ((listData.size() % rows == 0) ? 0 : 1);
        if (currentPage<0)currentPage=0;

        if (currentPage >= totalPage) {
            currentPage = totalPage - 1;
        }
        if (listData.size() != 0) {
            fail_hitn.setVisibility(GONE);
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
            fail_hitn.setVisibility(VISIBLE);
            fail_hitn.setText("暂无资料");
        }
        show_index.setText(String.valueOf(currentPage + 1) + "/" + totalPage);
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
    public void moveRight() {
        APPLog.e("moveRight");
        if (currentPage < (totalPage - 1)) {
            currentPage++;
            setAdapter();
        }
    }

    @Override
    void click(View v) {
        super.click(v);
        switch (v.getId()) {
            case R.id.last_page:
                moveLeft();
                break;
            case R.id.next_page:
                moveRight();
                break;
            case R.id.fail_hitn:
                refuresh();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickItem(int position) {
        //没有问题可以进行移动
        final TaskDataModel model = middleData.get(position);
        //打开
        mainInterface.openPdf(model.getSavePath());
    }
}
