package com.moxi.taskteacher.fragment;

import android.os.AsyncTask;
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
import com.moxi.taskteacher.cache.JsonData;
import com.moxi.taskteacher.httpconstans.TeaContsans;
import com.moxi.taskteacher.model.TaskDataModel;
import com.moxi.taskteacher.request.HttpDowladFiles;
import com.moxi.taskteacher.request.HttpPostRequest;
import com.moxi.taskteacher.request.RequestCallBack;
import com.moxi.taskteacher.request.ReuestKeyValues;
import com.moxi.taskteacher.teainterface.ClickItem;
import com.moxi.taskteacher.utils.DownloadAsy;
import com.moxi.taskteacher.utils.GetTeacherUserId;
import com.moxi.taskteacher.utils.TaskDataModelUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.mx.mxbase.view.LinerlayoutInter;
import com.mx.mxbase.view.RelativeLayoutInter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源fragment
 * Created by Archer on 2016/12/21.
 */
public class ResFragment extends BaseFragment implements RequestCallBack, LinerlayoutInter.LinerLayoutInter, View.OnClickListener, ClickItem, HttpDowladFiles.DowloadCallBack, AdapterView.OnItemClickListener {

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
    private int rows = 16;
    //总页数
    private int totalPage = 0;
    //当前页数
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_res, container, false);
        //初始化view的各控件
        isPrepared = true;

        all_files = (GridView) view.findViewById(R.id.all_files);
        last_page = (ImageButton) view.findViewById(R.id.last_page);
        mian_page_layout = (RelativeLayoutInter) view.findViewById(R.id.mian_page_layout);
        show_index = (TextView) view.findViewById(R.id.show_index);
        fail_hitn = (TextView) view.findViewById(R.id.fail_hitn);
        next_page = (ImageButton) view.findViewById(R.id.next_page);

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
    protected void loadData() {
        if (!isPrepared && !isVisible()) {
            return;
        }
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
        if (teaId.equals("")) {
            Toastor.showToast(getActivity(), "检测到未登录，请先登录");
            return;
        }
        valuePairs.add(new ReuestKeyValues("userId", teaId));
        valuePairs.add(new ReuestKeyValues("rows", String.valueOf(rows)));
        valuePairs.add(new ReuestKeyValues("page", String.valueOf(page)));
        new HttpPostRequest(getActivity(), this, valuePairs, "资料列表", TeaContsans.GET_ALL_COURSEWARE, true).execute();
    }

    @Override
    public void onSuccess(String result, String code) {
        if (code.equals("资料列表")) {
            fail_hitn.setVisibility(View.GONE);
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
                currentPage++;
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
        if (listData.size() != 0) {
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

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        if (code.equals("资料列表")) {
            if (page > 1) page--;
            if (listData.size() == 0) {
                fail_hitn.setVisibility(View.VISIBLE);
                fail_hitn.setText(msg + "\n点击重试");
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
            if (page <= (currentPage + 1)) {
                currentPage++;
                setAdapter();
            } else {
                page++;
                loadData();
            }
        }
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
            case R.id.fail_hitn:
                loadData();
                break;
            default:
                break;
        }
    }

    @Override
    public void dowladSucess(String code, boolean is, String resultPath) {
        if (is) {
            for (int i = 0; i < listData.size(); i++) {
                if (resultPath.equals(listData.get(i).getSavePath())) {
                    TaskDataModelUtils.getInstance().saveData(listData.get(i));
                    break;
                }
            }
        }
        dialogShowOrHide(false, "");
        setAdapter();
    }

    @Override
    public void downloadUnderway(String code, int progress, HttpDowladFiles dowladFiles) {

    }

    @Override
    public void onClickItem(int position) {
        //没有问题可以进行移动

    }

    public void onKeyPressDown() {
        System.exit(0);
    }

    public void onPageUp() {

    }

    public void onPageDown() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final TaskDataModel model = middleData.get(position);
        if (model == null) return;
        if (!TaskDataModelUtils.getInstance().isHaveDownload(model.FileId)) {
            String msg = "请确认下载：" + model.Filename;
            new AlertDialog(getContext()).builder().setTitle("下载提示").setCancelable(false).setMsg(msg).
                    setNegativeButton("下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogShowOrHide(true, "下载：" + model.Filename + " 中...");
                            HttpDowladFiles hhh = new HttpDowladFiles(getContext(), "文件下载", ResFragment.this, TeaContsans.download + model.FileId, model.getSavePath(), true);
                            hhh.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            DownloadAsy.getInstance().addAsy(hhh);
                        }
                    }).setPositiveButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else {
            //打开
            FileUtils.getInstance().openFile(getActivity(), new File(model.getSavePath()));
        }
    }
}
