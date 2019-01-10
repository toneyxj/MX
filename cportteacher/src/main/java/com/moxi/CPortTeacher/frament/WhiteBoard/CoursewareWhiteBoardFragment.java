package com.moxi.CPortTeacher.frament.WhiteBoard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.CoursewareWhiteBoardFileAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.utils.DownloadAsy;
import com.moxi.CPortTeacher.weight.BottomLineTextview;
import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.dbUtils.CoursewareWhiteBoardUtils;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpDowladFiles;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.dialog.HitnDialog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.StringUtils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 课件白板
 * Created by Administrator on 2016/11/30.
 */
public class CoursewareWhiteBoardFragment extends CportBaseFragment implements AdapterView.OnItemClickListener, HttpDowladFiles.DowloadCallBack,AdapterView.OnItemLongClickListener {
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_courseware_white_board;
    }

    @Bind(R.id.all_files)
    GridView all_files;

    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;

    @Bind(R.id.net_data_hitn)
    TextView net_data_hitn;

    @Bind(R.id.location_files)
    BottomLineTextview location_files;
    @Bind(R.id.net_files)
    BottomLineTextview net_files;
//    @Bind(R.id.pdf_show)
//    PdfView pdf_show;

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
    private CoursewareWhiteBoardModel pdfModel=null;

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CPortApplication.getBus().register(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        net_data_hitn.setOnClickListener(this);

        location_files.setOnClickListener(this);
        net_files.setOnClickListener(this);

        all_files.setOnItemClickListener(this);
        all_files.setOnItemLongClickListener(this);

        onClick(location_files);
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
//                openPdf();
                break;
            case R.id.net_files://网络文件
//                if (pdf_show.getVisibility()==View.VISIBLE){
//                    setTitle("");
//                    pdf_show.setVisibility(View.GONE);
//                }
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
//        dialogShowOrHide(true, "网络文件加载中...");
        initDilaog(true,"网络文件加载中...");
        postData(null, "获取所有的文件" + UserInformation.getInstance().getID(), Connector.getInstance().getList, false);
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
        super.onSuccess(result, code);
        initDilaog(false,"");
        if (code.equals("获取所有的文件" + UserInformation.getInstance().getID())) {
            CacheDbUtils.getInstance().saveHourData(code, result);
            allNetDatas.clear();
            allNetDatas.addAll(JsonAnalysis.getInstance().getCoursewareWhiteBoardModels(result));
            setAdapter();
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        initDilaog(false,"");
        if (code.equals("获取所有的文件")) {
            all_files.setVisibility(View.GONE);
            net_data_hitn.setVisibility(View.VISIBLE);
            net_data_hitn.setText("获取网路文件失败，点击重试");
        }
    }

    @Override
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

    @Override
    public void onclickBack() {
        super.onclickBack();
//        //点击返回
//        if (pdf_show.getVisibility()==View.VISIBLE){
//            pdf_show.setVisibility(View.GONE);
//            setTitle("");
//            pdfModel=null;
//        }
    }

    @Override
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
//    private void openPdf(){
//        if (pdfModel!=null) {
//            pdf_show.openPdf(pdfModel.getSavePath());
//            setTitle(pdfModel.Filename);
//            pdf_show.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final CoursewareWhiteBoardModel model = middleDatas.get(position);
        if (pageType == 0) {
//            FileUtils.getInstance().openFile(context, new File(model.getSavePath()));
            File file=new File(model.savePath);
            if (!file.exists()){
                allNetDatas.clear();
                CoursewareWhiteBoardUtils.getInstance().deleteData(model.id);
                return;
            }
//            pdfModel=model;
//           openPdf();
            FileUtils.getInstance().openFile(context,file);
        } else {
//            if (isDowloading(model.getSavePath())){
//                return;
//            }
            //下载文件
            insureDialog("请确认下载文件:" + model.Filename, "文件确认下载", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is) {
                        //加入下载队列
//                        if (isDowload) {
//                            ToastUtils.getInstance().showToastShort("文件等待下载中");
//                        }
//                        dialogShowOrHide(true,"文件下载中...");
                        initDilaog(true,"文件下载中...");
                        HttpDowladFiles dowload = new HttpDowladFiles(context, String.valueOf(middleDatas.get(position).FileId), CoursewareWhiteBoardFragment.this, Connector.getInstance().courseWareDownload + model.FileId, model.getSavePath(), false);
                        dowload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        downloadList.add(dowload);
                        DownloadAsy.getInstance().addAsy(dowload);
//                        DownloadUtils.AddDownloadFiles(context,Connector.getInstance().courseWareDownload + model.FileId,model.getSavePath());
                    }
                }
            });
        }
    }
public boolean isDowloading(String savePasth){
    for (int i = 0; i < downloadList.size(); i++) {
        if (downloadList.get(i).getBackUrl().equals(savePasth)){
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
        initDilaog(false,"");
        Long fileId = Long.parseLong(code);
        int position = getposition(fileId);
        if (is) {
            if (position == -1) return;
            CoursewareWhiteBoardModel model = allNetDatas.get(position);
            model.recentlyOpenTime = System.currentTimeMillis();
            //保存数据
            CoursewareWhiteBoardUtils.getInstance().saveData(model);

            //重新获取本地文件集合
            locationDatas.clear();
            locationDatas.addAll(CoursewareWhiteBoardUtils.getInstance().getAllReadRecorder(UserInformation.getInstance().getID()));
            //删除网络数据集合index
            allNetDatas.remove(position);
            //重新适配
            setAdapter();
            if (downloadList.size() >= 1)
                downloadList.remove(0);
        }else {
            ToastUtils.getInstance().showToastShort("文件下载失败！");
            adapter.updataProgress(fileId, -1, all_files);
        }
    }

    @Subscribe
    public void endLessen(OttoBeen been) {
        if (been.code.equals("CoursewareWhiteBoardFragment")) {
            for (int i = 0; i < downloadList.size(); i++) {
                downloadList.get(i).cancel(true);
            }
        }
    }

    @Override
    public void downloadUnderway(String code, int progress, HttpDowladFiles dowloadFiles) {
        Long fileId = Long.parseLong(code);
        int position = getposition(fileId);
        if (position == -1) {
            dialogShowOrHide(false,"");
            return;
        }
        if (pageType == 1) {
            adapter.updataProgress(fileId, progress, all_files);
        }
        HitnDialog hitnDialog=getHitnDialog();
        String descibe="文件已下载:"+progress+"%";
        APPLog.e(descibe);
        if (null!=hitnDialog&&hitnDialog.isShowing()){
            hitnDialog.setContnet(descibe);
        }else {
            initDilaog(false,"");
            initDilaog(true,descibe);
        }

    }
    private void initDilaog(boolean is,String content){
        dialogShowOrHide(is,content);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CPortApplication.getBus().unregister(this);
        endLessen(new OttoBeen("", "CoursewareWhiteBoardFragment"));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        APPLog.e("长按"+position);
        final CoursewareWhiteBoardModel model = middleDatas.get(position);
        if (pageType == 0) {
            insureDialog("请确认删除：" + model.Filename, "删除", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is){
                        StringUtils.deleteFile(model.savePath);
                        CoursewareWhiteBoardUtils.getInstance().deleteData(model.id);
                        setRecentlyDatas();
                        allNetDatas.clear();
                    }
                }
            });
        }
        return true;
    }
}
