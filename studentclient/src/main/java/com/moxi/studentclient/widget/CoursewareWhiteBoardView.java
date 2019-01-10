package com.moxi.studentclient.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooler.download.DownloadListener;
import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.dbUtils.CoursewareWhiteBoardUtils;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpDowladFiles;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.CommandService;
import com.moxi.studentclient.R;
import com.moxi.studentclient.activity.LogingActivity;
import com.moxi.studentclient.adapter.ClassExaminationAdapter;
import com.moxi.studentclient.adapter.CoursewareWhiteBoardFileAdapter;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.dialog.HitnDialog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.view.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class CoursewareWhiteBoardView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener, HttpDowladFiles.DowloadCallBack, RequestCallBack, AdapterView.OnItemLongClickListener {

    private ProgressDialog dialog;

    private Long downFileId;//记录当前下载的FildId

    GridView all_files;

    ImageButton last_page;
    TextView show_index;
    ImageButton next_page;

    TextView net_data_hitn;

    BottomLineTextview location_files;
    BottomLineTextview net_files;

    private PdfView pdfView;

    private CommandService commandService;


    private Context context;

    private CoursewareWhiteBoardFileAdapter adapter;
    /**
     * 网路文件
     */
    private List<CoursewareWhiteBoardModel> allNetDatas = new ArrayList<>();
    /**
     * 本地一下载文件
     */
    private List<CoursewareWhiteBoardModel> locationDatas = new ArrayList<>();
    /**
     * 界面显示数据集合
     */
    private List<CoursewareWhiteBoardModel> middleDatas = new ArrayList<>();
    private List<HttpDowladFiles> downloadList = new ArrayList<>();
    private int netCurrentPage = 0;
    private int locationCurrentPage = 0;
    private int pageType = 0;//0代表本地下载书展示，1代表网络图书展示
    private boolean isDowload = false;

    public ProgressDialog dialogShowOrHide(boolean is, String hitn) {
        if (is) {

            dialog = new ProgressDialog(context);
            dialog.setMessage(hitn);
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            if (dialog != null)
                dialog.dismiss();
            dialog = null;
        }
        return dialog;
    }

    /**
     * 下载提示框
     */
    private HitnDialog hitnDialog;

    public HitnDialog hitnDialogShowOrHide(boolean is, String hitn) {
        if (hitnDialog != null) {
            hitnDialog.dismiss();
            hitnDialog = null;
        }
        if (is) {
            hitnDialog = new HitnDialog(getContext(), com.mx.mxbase.R.style.AlertDialogStyle, hitn);
//            dialog.setMessage(hitn);
            hitnDialog.setCancelable(false);// 是否可以关闭dialog
            hitnDialog.setCanceledOnTouchOutside(false);
            try {
                hitnDialog.show();
            } catch (Exception e) {

            }
        }
        return hitnDialog;
    }


    public interface SubjectNumChooseCallBack {
        public void choolseCallBack(int page, String error);
    }

    public CoursewareWhiteBoardView(Context context) {
        super(context);
        initView(context);
    }

    public CoursewareWhiteBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CoursewareWhiteBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_courseware_white_board,
                this);
        all_files = (GridView) view.findViewById(R.id.all_files);
        last_page = (ImageButton) view.findViewById(R.id.last_page);

        show_index = (TextView) view.findViewById(R.id.show_index);

        next_page = (ImageButton) view.findViewById(R.id.next_page);

        net_data_hitn = (TextView) view.findViewById(R.id.net_data_hitn);

        location_files = (BottomLineTextview) view.findViewById(R.id.location_files);

        net_files = (BottomLineTextview) view.findViewById(R.id.net_files);


        pdfView = (PdfView) view.findViewById(R.id.pdfView);


        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        net_data_hitn.setOnClickListener(this);

        location_files.setOnClickListener(this);
        net_files.setOnClickListener(this);

        all_files.setOnItemClickListener(this);
        all_files.setOnItemLongClickListener(this);

        onClick(location_files);


    }

    /**
     * 设置下载文件Service
     *
     * @param commandService
     */
    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
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
            case R.id.net_data_hitn://点击重新加载
                if (pageType == 1)
                    getAllNetFile();
                break;
            case R.id.location_files://本地文件
                pageType = 0;
                setShowClickItem(location_files);
                if (locationDatas.size() == 0) {
                    setRecentlyDatas();
                } else {
                    setAdapter();
                }
                break;
            case R.id.net_files://网络文件
                pageType = 1;
                setShowClickItem(net_files);
                if (allNetDatas.size() == 0) {
                    getAllNetFile();
                } else {
                    setAdapter();
                }

                break;
            default:
                break;
        }
    }

    private void setShowClickItem(BottomLineTextview view) {
        location_files.setDrawLine(false);
        net_files.setDrawLine(false);
        view.setDrawLine(true);

    }

    /**
     * 获取最近的数据
     */
    private void setRecentlyDatas() {
        locationDatas.clear();
        locationDatas.addAll(CoursewareWhiteBoardUtils.getInstance().getAllReadRecorder(UserInformation.getInstance().getID()));
        APPLog.e("locationDatas=" + locationDatas.toString());
        setAdapter();
    }

    private void getAllNetFile() {
        if (UserInformation.getInstance().getUserInformation() != null) {
            dialogShowOrHide(true, "网络文件加载中...");
            postData(null, "303", Connector.getInstance().getList, false);
        } else {
            Intent loingInt = new Intent();
            loingInt.setClass(context, LogingActivity.class);
            context.startActivity(loingInt);
        }

    }

    /**
     * post请求
     *
     * @param valuePairs 请求参数值
     * @param code       本次请求标示
     * @param Url        请求路径
     * @param show       是否显示提示
     */
    public void postData(
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
        new HttpPostRequest(context, this, valuePairs, code, Url, show).execute();
    }

    private void setAdapter() {
        middleDatas.clear();
        //数据适配
        int size = 0;
        if (pageType == 0) {
            if (locationCurrentPage == -1) locationCurrentPage = 0;
            size = locationDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);
            if (totalpage != 0) {
                if (locationCurrentPage > (totalpage - 1)) locationCurrentPage = totalpage - 1;
            }
            if (locationDatas.size() != 0) {
                if (locationCurrentPage == (totalpage - 1)) {
                    middleDatas.addAll(locationDatas.subList(locationCurrentPage * 12, locationDatas.size()));
                } else {
                    middleDatas.addAll(locationDatas.subList(locationCurrentPage * 12, (locationCurrentPage + 1) * 12));
                }
            }
            setBottomTxt(totalpage, locationCurrentPage);
        } else {
            if (netCurrentPage == -1) netCurrentPage = 0;
            size = allNetDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);

            if (netCurrentPage > (totalpage - 1)) netCurrentPage = totalpage - 1;
            if (allNetDatas.size() != 0) {
                if (netCurrentPage == (totalpage - 1)) {
                    middleDatas.addAll(allNetDatas.subList(netCurrentPage * 12, allNetDatas.size()));
                } else {
                    middleDatas.addAll(allNetDatas.subList(netCurrentPage * 12, (netCurrentPage + 1) * 12));
                }
            }
            setBottomTxt(totalpage, netCurrentPage);
        }
        if (adapter == null) {
            adapter = new CoursewareWhiteBoardFileAdapter(context, middleDatas);
            all_files.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (middleDatas.size() == 0) {
            all_files.setVisibility(View.GONE);
            net_data_hitn.setVisibility(View.VISIBLE);
            if (pageType == 0) {
                net_data_hitn.setText("您还未有下载文件");
            } else {
                net_data_hitn.setText("未获取到最新上传网络文件");
            }
        } else {
            all_files.setVisibility(View.VISIBLE);
            net_data_hitn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(String result, String code) {
        dialogShowOrHide(false, "网络文件加载中...");
        if (code.equals("303")) {
            CacheDbUtils.getInstance().saveHourData(code, result);
            allNetDatas.clear();
            allNetDatas.addAll(JsonAnalysis.getInstance().getCoursewareWhiteBoardModels(result));
            setAdapter();
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        dialogShowOrHide(false, "网络文件加载中...");
        if (code.equals("303")) {
            all_files.setVisibility(View.GONE);
            net_data_hitn.setVisibility(View.VISIBLE);
            net_data_hitn.setText("获取网路文件失败，点击重试");
        }

    }

    public void moveLeft() {
        int size = 0;
        if (pageType == 0) {
            size = locationDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);
            if (locationCurrentPage > 0) {
                locationCurrentPage--;
                setAdapter();
            }
        } else {
            size = allNetDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);
            if (netCurrentPage > 0) {
                netCurrentPage++;
                setAdapter();
            }
        }
    }

    private void setBottomTxt(int totalpage, int currentPage) {
        show_index.setText(String.valueOf(currentPage + 1) + "/" + totalpage);
    }

    public void moveRight() {
        int size = 0;
        if (pageType == 0) {
            size = locationDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);
            if (locationCurrentPage < (totalpage - 1)) {
                locationCurrentPage++;
                setAdapter();
            }
        } else {
            size = allNetDatas.size();
            int totalpage = size / 12 + (size % 12 == 0 ? 0 : 1);
            if (netCurrentPage < (totalpage - 1)) {
                netCurrentPage++;
                setAdapter();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final CoursewareWhiteBoardModel model = middleDatas.get(position);
        if (pageType == 0) {
//            FileUtils.getInstance().openFile(context, new File(model.getSavePath()));
            File data = new File(model.getSavePath());
            if (data.exists()) {
                pdfView.openPdf(model.getSavePath());
            } else {
                ToastUtils.getInstance().showToastShort("文件不存在，请重新下载");
            }
        } else {
            if (isDowloading(model.getSavePath())) {
                return;
            }
            //下载文件
            insureDialog("请确认下载文件:" + model.Filename, "文件确认下载", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is) {
                        hitnDialogShowOrHide(true, "文件下载中...");
                        HttpDowladFiles dowload = new HttpDowladFiles(context, String.valueOf(middleDatas.get(position).FileId), CoursewareWhiteBoardView.this, Connector.getInstance().courseWareDownload + model.FileId, model.getSavePath(), false);
                        dowload.execute();
                        downloadList.add(dowload);

                        /*
                        //加入下载队列
                        if (isDowload) {
                            ToastUtils.getInstance().showToastShort("文件等待下载中");
                        }else{
                           /*
                            if(commandService!=null){
                                downFileId= middleDatas.get(position).FileId;
                                commandService.myDownloadManager(Connector.getInstance().courseWareDownload + model.FileId,model.getSavePath(),downloadListener);
                                Log.d("TAG","downFileId is "+downFileId,"---url is"+Connector.getInstance().courseWareDownload + model.FileId+"----localPath is"+model.getSavePath());
                            }else{
                                Log.d("TAG","commandservice is null");
                            }
                        }

                        HttpDowladFiles dowload = new HttpDowladFiles(context, String.valueOf(middleDatas.get(position).FileId), CoursewareWhiteBoardView.this, Connector.getInstance().courseWareDownload + model.FileId, model.getSavePath(), false);
                        dowload.execute();
                        downloadList.add(dowload);
                        */
                    }
                }
            });
        }
    }

    public boolean isDowloading(String savePasth) {
        for (int i = 0; i < downloadList.size(); i++) {
            if (downloadList.get(i).getBackUrl().equals(savePasth)) {
                return true;
            }
        }
        return false;
    }

    private int getposition(long fileId) {
        int index = -1;
        for (int i = 0; i < allNetDatas.size(); i++) {
            if (allNetDatas.get(i).FileId == fileId) {
                index = i;
                break;
            }
        }
        return index;
    }


    @Override
    public void dowladSucess(String code, boolean is, String resultPath) {
        hitnDialogShowOrHide(false, "");
        if (is) {
            Long fileId = Long.parseLong(code);
            int position = getposition(fileId);
            if (position == -1) return;
            if(allNetDatas==null)
                return;
            CoursewareWhiteBoardModel model = allNetDatas.get(position);
            model.recentlyOpenTime = System.currentTimeMillis();
            //保存数据
            CoursewareWhiteBoardUtils.getInstance().saveData(model);
            if (locationDatas != null) {
                //重新获取本地文件集合
                locationDatas.clear();
                locationDatas.addAll(CoursewareWhiteBoardUtils.getInstance().getAllReadRecorder(UserInformation.getInstance().getID()));
            }
            //删除网络数据集合index
            if (allNetDatas != null) {
                allNetDatas.remove(position);
                //重新适配
                setAdapter();
                if (downloadList.size() >= 1)
                    downloadList.remove(0);
                goLoadfile();
            }
            if(context==null)
                return;
            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "下载失败，请重试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到本地文件库中
     */
    private void goLoadfile() {
        pageType = 0;
        setShowClickItem(location_files);
        if (locationDatas.size() == 0) {
            setRecentlyDatas();
        } else {
            setAdapter();
        }
    }

    @Override
    public void downloadUnderway(String code, int progress, HttpDowladFiles dowloadFiles) {
        Long fileId = Long.parseLong(code);
        int position = getposition(fileId);
        if (position == -1) return;
        if (pageType == 1) {
            adapter.updataProgress(fileId, progress, all_files);
        }
        hitnDialog.setContnet("文件已下载:" + progress + "%");
    }

    public void insureDialog(String content, Object code, InsureOrQuitListener listener) {
        insureDialog("提示", content, code, listener);
    }

    public void insureDialog(String hitn, String content, Object code, InsureOrQuitListener listener) {
        insureDialog(hitn, content, "确定", "取消", code, listener);
    }

    public void insureDialog(String hitn, String content, String insure, String quit, final Object code, final InsureOrQuitListener listener) {
        //没有问题可以进行移动
        new AlertDialog(context).builder().setTitle(hitn).setCancelable(false).setMsg(content).
                setNegativeButton(insure, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.isInsure(code, true);
                        }
                    }
                }).setPositiveButton(quit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.isInsure(code, false);
                }
            }
        }).show();
    }

    /**
     * 关闭View
     *
     * @return
     */
    public boolean finshView() {
        return pdfView.finshView();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        APPLog.e("长按" + position);
        final CoursewareWhiteBoardModel model = middleDatas.get(position);
        if (pageType == 0) {
            insureDialog("请确认删除：" + model.Filename, "删除", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is) {
                        StringUtils.deleteFile(model.savePath);
                        CoursewareWhiteBoardUtils.getInstance().deleteData(model.id);
                        setRecentlyDatas();
                        allNetDatas.clear();
                    }
                }
            });
        }
        return false;
    }
}
