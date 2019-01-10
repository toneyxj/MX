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
import com.moxi.studentclient.activity.PersonalInformationActivity;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectView;
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
public class SubjectPersenter extends BasePersenter implements SubjectNumView.SubjectNumChooseCallBack, SubjectView.SubjectCallBack, SubjectCompleteView.SubjectCompleteCallBack, View.OnClickListener {

    private SubjectView subjectView;
    private SubjectNumView subjectNumView;

    private ExamsDetailsModel edm;

    private OnSubjectPersenterCallBack callBack;
    private Context context;

    public SubjectPersenter() {

    }

    public SubjectPersenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAction(String action) {
        Log.d("TAG", "SubjectPersenter" + action);
    }

    @Override
    public void submitCallBack() {
        new AlertDialog(context).builder().setTitle("提醒").setMsg("确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitClasswork();
            }
        }).setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();
    }

    @Override
    public void choolseCallBack(int page, String error) {
        if (error == null) {
            subjectView.parseExamsDetails(page, edm);
            if (callBack != null) {
                callBack.onSubjectStart();
            }
        } else {
            if (callBack != null) {
                callBack.onSubjectComplete();
            }
        }
    }

    @Override
    public void answerCallBack(int page, ExamsDetailsModel edmModel) {
        sumit(page, edmModel.getResult().get(page).getResult());
//        subjectNumView.setSubNumtPage(page + 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBut:
                if (callBack != null) {
                    callBack.onSubjectComplete();
                }
                break;
        }
    }

    /**
     * 提交试卷
     */
    private void commitClasswork() {
        if(UserInformation.getInstance().getID()==-1||UserInformation.getInstance().getUserInformation()==null){
            Intent infoInt = new Intent();
            infoInt.setClass(context, PersonalInformationActivity.class);
            context.startActivity(infoInt);
            return;
        }
        OkHttpUtils.post().url(Connector.getInstance().commitClasswork).addParams("userId", UserInformation.getInstance().getID() + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (callBack != null) {
                    callBack.onSubjectSubmit();
                }
                Log.d("TAG", response);
            }
        });
    }


    public interface OnSubjectPersenterCallBack {
        public void onSubjectComplete();

        public void onSubjectStart();

        public void onSubjectSubmit();

    }

    public void setSubjectCallBack(OnSubjectPersenterCallBack callBack) {
        this.callBack = callBack;
    }

    public void intSubjectText(SubjectView subjectView, SubjectNumView subjectNumView) {
        String url = Connector.getInstance().getStudentCurrentClassWork;
        final String result = ACache.get(context).getAsString(url);
        Log.d("TAG",result);
        edm = GsonTools.getPerson(result, ExamsDetailsModel.class);

        this.subjectView = subjectView;
        this.subjectNumView = subjectNumView;

        subjectNumView.setSubNumtView(0, SubjectNumView.TypeView.SELECTTYPE, edm, this);
    }

    /**
     * 提交答案
     *
     * @param page
     * @param d
     */
    private void sumit(int page, String d) {
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            String key = Connector.getInstance().getStudentCurrentClassWork + "_" + page;
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
            OkHttpUtils.post().url(Connector.getInstance().commitAnswer).addParams("userId", UserInformation.getInstance().getID() + "")
                    .addParams("exerciseId", edm.getResult().get(page).getId() + "")
                    .addParams("answer", d)
                    .addFile("answerFile", String.valueOf(page), imgFile).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("TAG", response);
                }
            });
        }else{
            Intent infoInt = new Intent();
            infoInt.setClass(context, PersonalInformationActivity.class);
            context.startActivity(infoInt);
        }
    }

    public void setSubjectCompleteView(SubjectCompleteView subjectCompleteView) {
        subjectCompleteView.setCallBack(this);
        subjectCompleteView.setData(edm);
        subjectCompleteView.setSubjectNumViewCallBack(new SubjectNumView.SubjectNumChooseCallBack() {
            @Override
            public void choolseCallBack(int page, String error) {
                subjectNumView.setSubNumtPage(page);
            }
        });
    }

    public String getContentTitle() {
        if (edm != null && edm.getResult() != null)
            return "练习题" + "（共" + edm.getResult().size() + "）题";
        return "练习题";
    }


}
