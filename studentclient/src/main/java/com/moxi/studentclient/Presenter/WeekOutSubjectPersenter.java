package com.moxi.studentclient.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.studentclient.R;
import com.moxi.studentclient.activity.LogingActivity;
import com.moxi.studentclient.activity.PersonalInformationActivity;
import com.moxi.studentclient.bean.WeekOutResult;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectView;
import com.mx.mxbase.model.BaseModel;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.AlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class WeekOutSubjectPersenter extends BasePersenter implements SubjectView.SubjectCallBack, SubjectCompleteView.SubjectCompleteCallBack, View.OnClickListener {

    private SubjectView subjectView;

    private ExamsDetailsModel edm;

    private OnWeekOutSubjectPersenterCallBack callBack;
    private Context context;

    public WeekOutSubjectPersenter() {

    }

    public WeekOutSubjectPersenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAction(String action) {
        Log.d("TAG", "SubjectPersenter" + action);
    }

    @Override
    public void submitCallBack() {
        if (callBack != null) {
            callBack.onSubjectSubmit();
        }
    }


    @Override
    public void answerCallBack(int page, ExamsDetailsModel edmModel) {
        sumit(page, edmModel.getResult().get(page).getResult());
    }

    private void sumit(final int page, String d) {
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            String key = Connector.getInstance().getStudentCurrentFallWork + "_" + page;
            byte[] binary = ACache.get(context).getAsBinary(key);
            String filePath = Environment.getExternalStorageDirectory().getPath();
            String fileName = "subject.jpg";
            if (binary != null) {
                Utils.byte2File(binary, filePath, fileName);
                d = "";
            } else {
                Resources r = context.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(r, R.mipmap.ic_time);
                Utils.saveBitmapFile(bmp, filePath, fileName);
            }
            File imgFile = new File(filePath + File.separator + fileName);
            OkHttpUtils.post().url(Connector.getInstance().commitAnswerFall).addParams("userId", UserInformation.getInstance().getID() + "")
                    .addParams("exerciseId", edm.getResult().get(page).getId() + "")
                    .addParams("answer", d)
                    .addFile("answerFile", String.valueOf(page), imgFile).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    WeekOutResult result = GsonTools.getPerson(response, WeekOutResult.class);
                    if (result.getCode() == 0) {
                        if ("1".equals(result.getResult().getIsRight())) {
                            commitClasswork(page,page);
                            if (callBack != null) {
                                callBack.onWeekOutSubjectCompleteFail(page);
                            }
                        } else if ("0".equals(result.getResult().getIsRight())) {
                            commitClasswork(page,edm.getResult().size()-1);
                            if (callBack != null) {
                                callBack.onWeekOutSubjectCompleteSuccess(page, edm.getResult().size());
                            }
                        }
                    } else {
                        Toast.makeText(context.getApplicationContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Intent infoInt = new Intent();
            infoInt.setClass(context, LogingActivity.class);
            context.startActivity(infoInt);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBut:
//                new AlertDialog(context).builder().setTitle("提醒").setMsg("确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        commitClasswork();
//                    }
//                }).setPositiveButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                }).show();
                break;
        }
    }

    /**
     * 交卷
     * @param page
     * @param countPage
     */
    private void commitClasswork(int page, int countPage) {
        if (page < countPage)
            return;
        OkHttpUtils.post().url(Connector.getInstance().commitFallwork).addParams("userId", UserInformation.getInstance().getID() + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                BaseModel result = GsonTools.getPerson(response, BaseModel.class);
                Toast.makeText(context.getApplicationContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface OnWeekOutSubjectPersenterCallBack {
        public void onWeekOutSubjectCompleteSuccess(int page, int countPage);

        public void onWeekOutSubjectCompleteFail(int page);

        public void onSubjectSubmit();

    }

    public void setWeekOutSubjectCallBackCallBack(OnWeekOutSubjectPersenterCallBack callBack) {
        this.callBack = callBack;
    }

    public void intSubjectText(int page, SubjectView subjectView) {
        String url = Connector.getInstance().getStudentCurrentFallWork;
        final String result = ACache.get(context).getAsString(url);
        edm = GsonTools.getPerson(result, ExamsDetailsModel.class);
        this.subjectView = subjectView;
        subjectView.parseExamsDetails(page, edm);

    }

    /**
     * 设置下一题
     *
     * @param page
     */
    public void nextSubjectText(int page) {

        subjectView.parseExamsDetails(page, edm);
    }


}
