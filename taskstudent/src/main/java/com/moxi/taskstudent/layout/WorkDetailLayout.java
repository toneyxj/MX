package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.model.WorkListModel;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.utils.ToastUtils;
import com.moxi.taskstudent.view.ImageViewProgressArc;
import com.moxi.taskstudent.view.PaintInvalidateRectView;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.FileSaveASY;
import com.mx.mxbase.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * 作业详情页面
 * Created by Administrator on 2016/12/21.
 */
public class WorkDetailLayout extends BaseLayout {
    private WorkListModel model;
    public WorkDetailLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);
    }

    private ImageViewProgressArc work_image;
    private ImageViewProgressArc add_task;
    private PaintInvalidateRectView paint_view;
    private Button click_button;

    @Override
    int getLayout() {
        return R.layout.addview_work_detail;
    }

    @Override
    void initLayout(View view) {
        work_image=(ImageViewProgressArc)findViewById(R.id.work_image);
        add_task=(ImageViewProgressArc)findViewById(R.id.add_task);
        paint_view=(PaintInvalidateRectView)findViewById(R.id.paint_view);
        click_button=(Button) findViewById(R.id.click_button);
//        APPLog.e("里面的数据集合="+model.toString());


        click_button.setOnClickListener(this);
    }
    public void initData(WorkListModel model){
        paint_view.clearScreen();
        this.model=model;
        work_image.loadImage(Connector.getInstance().getDownloadFileURL(0,model.downloadFile));
        if (model.answerFile.equals("")){
            add_task.setVisibility(GONE);
            paint_view.setVisibility(VISIBLE);
            click_button.setVisibility(VISIBLE);
            click_button.setText("提交");
        }else if (!model.replyFile.equals("")){
            add_task.setVisibility(VISIBLE);
            paint_view.setVisibility(GONE);
            click_button.setVisibility(VISIBLE);
            click_button.setText("查看批复");
            add_task.loadImage(Connector.getInstance().getDownloadFileURL(1,model.answerFile));
        }else {
            add_task.setVisibility(VISIBLE);
            paint_view.setVisibility(GONE);
            click_button.setVisibility(GONE);
            add_task.loadImage(Connector.getInstance().getDownloadFileURL(1,model.answerFile));
        }

    }

    @Override
    void click(View v) {
        super.click(v);
        switch (v.getId()){
            case R.id.click_button:
                String buttonStr=click_button.getText().toString();
                if (buttonStr.equals("提交")){
                    dialogShowOrHide(true,"作业提交中");
                    final String SavePath=StringUtils.getSDPath() +  "save.png";
                    APPLog.d("保存路径="+SavePath);
                    new FileSaveASY(paint_view.CurrentBitmap(), SavePath, new FileSaveASY.saveSucess() {
                        @Override
                        public void onSaveSucess(boolean is) {
                            if (!is){
                                dialogShowOrHide(false,"");
                                ToastUtils.getInstance().showToastShort("保存失败");
                                return;
                            }
//                            List<RequestKeyValues> valuePairs = new ArrayList<>();
//                            valuePairs.add(new RequestKeyValues("homeWorkId", model.id));
//                            valuePairs.add(new RequestKeyValues("file", "提交文件"));
//                            valuePairs.add(new RequestKeyValues("userId", Connector.getInstance().userId));
//                            getData(valuePairs, "提交作业", Connector.getInstance().getHomeWork, true);
                            OkHttpUtils.post().url(Connector.getInstance().commitAnswer)
                                    .addParams("userId", Connector.getInstance().userId)
                                    .addParams("id",model.id)
                                    .addFile("file", "save.png", new File(SavePath)).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    StringUtils.deleteFile(SavePath);
                                    ToastUtils.getInstance().showToastShort("提交失败");
                                    dialogShowOrHide(false,"");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    APPLog.e("返回数据="+response);
                                    StringUtils.deleteFile(SavePath);

                                    dialogShowOrHide(false,"");
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        String result=object.getString("result");
                                        if (null!=result&&!result.equals("")&&!result.equals("null")){
                                            ToastUtils.getInstance().showToastShort("提交完成");
                                            model.answerFile=result;
                                            model.setTaskStatus(null);
                                            mainInterface.SubimitWork(model);
                                        }else {
                                            String msg=object.getString("msg");
                                            ToastUtils.getInstance().showToastShort(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).execute();

                }else if(buttonStr.equals("查看批复")){
                        mainInterface.CheckReplay(model);
                }
                break;
            default:
                break;
        }
    }
}
