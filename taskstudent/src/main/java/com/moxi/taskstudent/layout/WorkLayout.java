package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.adapter.WorkAdapter;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.config.JsonData;
import com.moxi.taskstudent.model.DbDownloadModel;
import com.moxi.taskstudent.model.WorkListModel;
import com.moxi.taskstudent.request.RequestKeyValues;
import com.moxi.taskstudent.taskInterface.ClickItem;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.taskInterface.UploadImageStatus;
import com.moxi.taskstudent.utils.DbDownlaodUtils;
import com.moxi.taskstudent.view.ImageViewProgressArc;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.view.LinerlayoutInter;
import com.mx.mxbase.view.RelativeLayoutInter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业lauyout
 * Created by Administrator on 2016/12/21.
 */
public class WorkLayout extends BaseLayout implements UploadImageStatus, LinerlayoutInter.LinerLayoutInter,ClickItem {
    public WorkLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);
    }

    private GridView all_files;
    private ImageViewProgressArc download_image;
    private RelativeLayoutInter mian_page_layout;
    private ImageButton last_page;
    private TextView show_index;
    private TextView fail_hitn;
    private ImageButton next_page;
    private List<WorkListModel> listData = new ArrayList<>();
    private List<WorkListModel> middleData = new ArrayList<>();
    private WorkAdapter adapter;
    private int page = 1;
    private int rows = 12;
    //总页数
    private int totalPage = 0;
    //当前页数
    private int currentPage = 0;

    @Override
    int getLayout() {
        return R.layout.addview_task_work;
    }

    @Override
    void initLayout(View view) {
        all_files = (GridView) findViewById(R.id.all_files);
        download_image = (ImageViewProgressArc) findViewById(R.id.download_image);
        mian_page_layout = (RelativeLayoutInter) findViewById(R.id.mian_page_layout);
        last_page = (ImageButton) findViewById(R.id.last_page);
        show_index = (TextView) findViewById(R.id.show_index);
        fail_hitn = (TextView) findViewById(R.id.fail_hitn);
        next_page = (ImageButton) findViewById(R.id.next_page);

        download_image.setUploadSucess(this);

        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        fail_hitn.setOnClickListener(this);
        mian_page_layout.setLayoutInter(this);
        mian_page_layout.setLayoutInter(this);
        page = 1;
        rows = 12;
    }

    public void refuresh() {
        dialogShowOrHide(true,"作业加载中...");
        List<RequestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new RequestKeyValues("rows", String.valueOf(rows)));
        valuePairs.add(new RequestKeyValues("page", String.valueOf(page)));
        valuePairs.add(new RequestKeyValues("userId", Connector.getInstance().userId));
        getData(valuePairs, "作业列表", Connector.getInstance().getHomeWork, true);
    }

    @Override
    public void onUploadImage(View view, boolean isSucess, String path, int status) {
        if (isStop)return;
        String value = "";
        boolean is = false;
        switch (status) {
            case 0:
                value = "开始下载...";
                is = true;
                break;
            case 1:
                value = "下载中...";
                is = true;
                break;
            case 2:
                value = "下载失败...";
                downloadIndex=-1;
                is = false;
                break;
            case 3:
                value = "下载成功...";
                is = false;
                DbDownlaodUtils.getInstance().save(new DbDownloadModel(path));
                middleData.get(downloadIndex).setTaskStatus(null);
//                adapter.updateButton(downloadIndex,all_files);
                setAdapter();
                downloadIndex=-1;
                break;
            default:
                break;
        }
        dialogShowOrHide(is, value);
    }
    public void ChangeModel(WorkListModel model){
        for (int i = 0; i < middleData.size(); i++) {
            if (model.downloadFile.equals(middleData.get(i).downloadFile)){
                middleData.get(i).answerFile=model.answerFile;
                middleData.get(i).setTaskStatus(null);
                setAdapter();
                return;
            }
        }
    }

    @Override
    void click(View v) {
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
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("作业列表")) {
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
                currentPage=page-1;
                listData.addAll(JsonData.getInstance().getWorkListModels(array.toString()));
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
                adapter = new WorkAdapter(getContext(), middleData,this);
                all_files.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }else {
            fail_hitn.setVisibility(VISIBLE);
            fail_hitn.setText("您还未有新的作业");
        }
        show_index.setText(String.valueOf(currentPage + 1) + "/" + totalPage);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        if (code.equals("作业列表")) {
            if (page>1){
                page--;
            }
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
    private int downloadIndex=-1;

    @Override
    public void onClickItem(int index) {
        WorkListModel model=middleData.get(index);
        if (model.getButtonStr().equals("下载")){
            downloadIndex=index;
            String path=Connector.getInstance().getDownloadFileURL(0,model.downloadFile);
            download_image.loadImage(path);
        }else if (model.getButtonStr().equals("继续")){
            mainInterface.clickWorkContinue(model);
        }else if (model.getButtonStr().equals("打开")){
            mainInterface.clickWorkOpen(model);
        }else {
            mainInterface.clickWorkCheck(model);
        }
    }
}
