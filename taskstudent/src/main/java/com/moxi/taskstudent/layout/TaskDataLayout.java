package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.adapter.TaskDataAdapter;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.config.JsonData;
import com.moxi.taskstudent.model.TaskDataModel;
import com.moxi.taskstudent.request.HttpDowladFiles;
import com.moxi.taskstudent.request.RequestKeyValues;
import com.moxi.taskstudent.taskInterface.ClickItem;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.utils.DownloadAsy;
import com.moxi.taskstudent.utils.TaskDataModelUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.view.AlertDialog;
import com.mx.mxbase.view.LinerlayoutInter;
import com.mx.mxbase.view.RelativeLayoutInter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业
 * Created by Administrator on 2016/12/21.
 */
public class TaskDataLayout extends BaseLayout implements LinerlayoutInter.LinerLayoutInter,ClickItem,HttpDowladFiles.DowloadCallBack {
    public TaskDataLayout(Context context, MainInterface mainInterface) {
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
    private int page = 1;
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
        page = 1;
        rows = 12;

    }
    public void refuresh() {
        dialogShowOrHide(true,"资料加载中...");
        List<RequestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new RequestKeyValues("rows", String.valueOf(rows)));
        valuePairs.add(new RequestKeyValues("page", String.valueOf(page)));
        valuePairs.add(new RequestKeyValues("userId", Connector.getInstance().userId));
        getData(valuePairs, "资料列表", Connector.getInstance().getAllSendList, true);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("资料列表")) {
            fail_hitn.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(result);
                JSONObject resultJson = object.getJSONObject("result");
                totalPage = resultJson.getInt("pageCount");
                page = resultJson.getInt("page");

                JSONArray array = resultJson.getJSONArray("list");
                if (page == 1) {
                    listData.clear();
                }
                listData.addAll(JsonData.getInstance().getTaskDataModels(array.toString()));
                currentPage=page-1;
                setAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapter() {
        middleData.clear();
        if (currentPage >= totalPage) {
            currentPage = totalPage - 1;
        }
        if (listData.size()!=0) {
            if (currentPage == (totalPage - 1)) {
                middleData.addAll(listData.subList(currentPage * rows, listData.size()));
            } else {
                middleData.addAll(listData.subList(currentPage * rows, (currentPage + 1) * rows));
            }
            if (adapter == null) {
                adapter = new TaskDataAdapter(getContext(), middleData,this);
                all_files.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }else {
            fail_hitn.setVisibility(VISIBLE);
            fail_hitn.setText("暂无资料");
        }
        show_index.setText(String.valueOf(currentPage + 1) + "/" + totalPage);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        if (code.equals("资料列表")) {
            if (page>1)page--;
            if (listData.size()==0){
                fail_hitn.setVisibility(VISIBLE);
                fail_hitn.setText(msg+"\n点击重试");
            }
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
    public void moveRight() {
        APPLog.e("moveRight");
        if (currentPage < (totalPage - 1)) {
            page++;
            if ((page*rows>listData.size())&&(listData.size()%rows==0)&&(listData.size()/rows!=totalPage)){
                refuresh();
                return;
            }else {
                currentPage++;
                setAdapter();
            }
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
    public void dowladSucess(String code, boolean is, String resultPath) {
        if (isStop)return;
        if (is) {
            for (int i = 0; i < listData.size(); i++) {
                if (resultPath.equals(listData.get(i).getSavePath())) {
                    TaskDataModelUtils.getInstance().saveData(listData.get(i));
                    break;
                }
            }
        }
        dialogShowOrHide(false,"");
        setAdapter();
    }

    @Override
    public void downloadUnderway(String code, int progress, HttpDowladFiles dowladFiles) {
    }
private HttpDowladFiles httpDowladFiles;
    @Override
    public void onClickItem(int position) {
        //没有问题可以进行移动
        final TaskDataModel model = middleData.get(position);
        if (model == null) return;
        if (!TaskDataModelUtils.getInstance().isHaveDownload(model.FileId)) {
            String msg = "请确认下载：" + model.Filename;
            new AlertDialog(getContext()).builder().setTitle("下载提示").setCancelable(false).setMsg(msg).
                    setNegativeButton("下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogShowOrHide(true,"下载："+model.Filename+" 中...");
                            httpDowladFiles=  new HttpDowladFiles(getContext(),"文件下载",TaskDataLayout.this,Connector.getInstance().download+model.FileId,model.getSavePath(),true);
                            httpDowladFiles.execute();
                            DownloadAsy.getInstance().addAsy(httpDowladFiles);
                        }
                    }).setPositiveButton("取消", new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }else {
            //打开
            mainInterface.openPdf(model.getSavePath());
        }
    }

}
