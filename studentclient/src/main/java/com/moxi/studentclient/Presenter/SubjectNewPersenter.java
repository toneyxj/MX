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
import com.moxi.studentclient.model.NowSubjectModel;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.widget.NewSubjectView;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.AlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectNewPersenter extends BasePersenter implements NewSubjectView.SubjectCallBack, SubjectCompleteView.SubjectCompleteCallBack, View.OnClickListener {

    private NewSubjectView newSubjectView;

    private NowSubjectModel edm;

    private OnSubjectPersenterCallBack callBack;
    private Context context;

    public SubjectNewPersenter() {

    }

    public SubjectNewPersenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAction(String action) {

    }

    @Override
    public void submitCallBack() {
//        new AlertDialog(context).builder().setTitle("提醒").setMsg("确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (nowSubject != null)
//                    sumit(nowSubject.getStudentAnser());
//            }
//        }).setPositiveButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        }).show();
    }


    @Override
    public void answerCallBack(NowSubjectModel.NowSubject edmModel) {
//        sumit( edmModel.getResult().get(page).getResult());
//        nowSubject = edmModel;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBut:
                Log.d("TAG", newSubjectView.getStuendtResult());
                new AlertDialog(context).builder().setTitle("提醒").setMsg("确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (newSubjectView != null)
                            sumit(newSubjectView.getStuendtResult());
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                break;
        }
    }

    /**
     * 提交试卷
     */
    private void commitClasswork() {
        if (UserInformation.getInstance().getID() == -1 || UserInformation.getInstance().getUserInformation() == null) {
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

        public void onSubjectSubmit();
    }

    public void setSubjectCallBack(OnSubjectPersenterCallBack callBack) {
        this.callBack = callBack;
    }

    public void intSubjectText(NewSubjectView nowSubjectView) {
        String url = Connector.getInstance().getQuestion;
        final String result = ACache.get(context).getAsString(url);
        edm = GsonTools.getPerson(result, NowSubjectModel.class);
        nowSubjectView.parseExamsDetails(edm);
        newSubjectView = nowSubjectView;
    }

    /**
     * 提交答案
     *
     * @param d
     */
    private void sumit(Map<String, String> d) {
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            String filePath = Environment.getExternalStorageDirectory().getPath();
            String fileName = "subject.jpg";
            if ("3".equals(d.get("type"))) {
                byte[] binary = ACache.get(context).getAsBinary(d.get("anwer"));
                if (binary != null) {
                    Utils.byte2File(binary, filePath, fileName);
                    d.put("anwer", "");
                }
            } else {
                Resources r = context.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(r, R.mipmap.ic_time);
                Utils.saveBitmapFile(bmp, filePath, fileName);
            }

            File imgFile = new File(filePath + File.separator + fileName);

            OkHttpUtils.post().url(Connector.getInstance().questionCommitAnswer).addParams("userId", UserInformation.getInstance().getID() + "")
                    .addParams("questionId", edm.getResult().getId() + "")
                    .addParams("answer", d.get("anwer"))
                    .addFile("answerFile", "subject", imgFile)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    if (callBack != null) {
                        callBack.onSubjectComplete();
                    }
                    Log.d("TAG", response);
                }
            });
        } else {
            Intent infoInt = new Intent();
            infoInt.setClass(context, PersonalInformationActivity.class);
            context.startActivity(infoInt);
        }
    }

    public String getAnswer() {
        if (newSubjectView != null && edm.getResult().getType() != 3) {
            if (edm.getResult().getType() == 4) {
                String okAnswer="1".equals(edm.getResult().getAnswer())?"正确":"错误";
                String subjectAnswer="1".equals(newSubjectView.getStuendtResult().get("anwer"))?"正确":"错误";
                return "正确答案：" + okAnswer + "\n" + "提交答案：" + subjectAnswer;
            } else {
                return "正确答案：" + edm.getResult().getAnswer() + "\n" + "提交答案：" + newSubjectView.getStuendtResult().get("anwer");
            }
        } else {
            return getContentTitle();
        }
    }

    public String getContentTitle() {
        String subjectType = "";
        if (edm != null && edm.getResult() != null) {
            switch (edm.getResult().getType()) {
                case 1:
                    subjectType = "----单选题";
                    break;
                case 2:
                    subjectType = "----多选题";
                    break;
                case 3:
                    subjectType = "----主观题";
                    break;
                case 4:
                    subjectType = "----判断题";
                    break;
            }
        }
        return "现场出题" + subjectType;
    }


}
