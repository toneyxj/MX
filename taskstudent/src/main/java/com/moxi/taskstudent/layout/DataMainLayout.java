package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.view.BottomLineTextview;
import com.mx.mxbase.constant.APPLog;

/**
 * Created by Administrator on 2016/12/30.
 */
public class DataMainLayout extends BaseLayout {
    public DataMainLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);
    }
    private BottomLineTextview have_download;
    private BottomLineTextview no_download;
    private FrameLayout task_data_main_fragment;

    private TaskDataLayout taskDataLayout;
    private TastDataHaveDownloadLayout tastDataHaveDownloadLayout;
    private int currentIndex=0;
    private boolean isrefuresh=true;
    @Override
    int getLayout() {
        return R.layout.data_mian_layout;
    }

    @Override
    void initLayout(View view) {
        have_download=(BottomLineTextview)view.findViewById(R.id.have_download);
        no_download=(BottomLineTextview)view.findViewById(R.id.no_download);
        task_data_main_fragment=(FrameLayout)view.findViewById(R.id.task_data_main_fragment);

        have_download.setOnClickListener(this);
        no_download.setOnClickListener(this);
        currentIndex=0;
    }

    @Override
    void click(View v) {
        super.click(v);
        switch (v.getId()){
            case R.id.have_download://已下载的界面
                have_download.setDrawLine(true);
                no_download.setDrawLine(false);
                addView(getIndexView(0));
                break;
            case R.id.no_download://未下载的界面
                have_download.setDrawLine(false);
                no_download.setDrawLine(true);
                addView(getIndexView(1));
                break;
            default:
                break;
        }
    }
    public void initView(){
        if (currentIndex==0){
            click(have_download);
        }else {
            click(no_download);
        }
    }

    private BaseLayout getIndexView(int index) {
        currentIndex = index;
        BaseLayout view = null;
        switch (index) {
            case 0://已下载
                if (tastDataHaveDownloadLayout == null) {
                    tastDataHaveDownloadLayout = new TastDataHaveDownloadLayout(getContext(),mainInterface);
                }
                view = tastDataHaveDownloadLayout;
                break;
            case 1://未下载
                if (taskDataLayout == null) {
                    taskDataLayout = new TaskDataLayout(getContext(),mainInterface);
                }else {
                    isrefuresh=false;
                }
                view = taskDataLayout;
                break;
            default:
                break;
        }
        return view;
    }
    private void addView(BaseLayout layout) {
        if (layout == null) return;
        task_data_main_fragment.removeAllViews();
        task_data_main_fragment.addView(layout);

        if (layout==tastDataHaveDownloadLayout){
            tastDataHaveDownloadLayout.refuresh();
        }else if(isrefuresh){
            taskDataLayout.refuresh();
        }
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        if (currentIndex==0){
            tastDataHaveDownloadLayout.moveLeft();
        }else if (currentIndex==1){
            taskDataLayout.moveLeft();
        }
    }

    @Override
    public void moveRight() {
        super.moveRight();
        if (currentIndex==0){
            tastDataHaveDownloadLayout.moveRight();
        }else if (currentIndex==1){
            taskDataLayout.moveRight();
        }
    }

    @Override
    public void onStop() {
        APPLog.e("这个里面结束了");
        if (tastDataHaveDownloadLayout!=null){
            tastDataHaveDownloadLayout.onStop();
        }else if(taskDataLayout!=null){
            taskDataLayout.onStop();
        }
        super.onStop();

    }
}
