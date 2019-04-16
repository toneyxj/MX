package com.moxi.studentclient.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.studentclient.R;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.mx.mxbase.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class SubjectDefautView extends LinearLayout implements RequestCallBack {
    private Context context;
    private SubjectDefautViewCallBack callBack;

    private boolean isShow = true;

    public void setCallBack(SubjectDefautViewCallBack callBack) {
        this.callBack = callBack;
    }

    public interface SubjectDefautViewCallBack {
        public void subjectDefautViewSuccess();

        public void subjectDefautViewLoging();
    }

    public SubjectDefautView(Context context) {
        super(context);
        init(context);
    }

    public SubjectDefautView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubjectDefautView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_default,
                this);

    }

    public void rmoePostData() {
        isShow = false;
    }

    /**

     */
    public void postData() {
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            isShow = true;
            List<ReuestKeyValues> values = new ArrayList<>();
            values.add(new ReuestKeyValues("userId", UserInformation.getInstance().getID() + ""));
            new HttpPostRequest(context, this, values, "1011", Connector.getInstance().getStudentCurrentClassWork, false).execute();
        } else {
            if (callBack != null) {
                callBack.subjectDefautViewLoging();
            }
        }
    }

    @Override
    public void onSuccess(String result, String code) {
        ExamsDetailsModel edm = GsonTools.getPerson(result, ExamsDetailsModel.class);
        if (edm != null && edm.getCode() == 0) {
            ACache.get(context).put(Connector.getInstance().getStudentCurrentClassWork, result);
            if (callBack != null) {
                callBack.subjectDefautViewSuccess();
            }
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShow)
                    postData();
            }
        }, 5 * 1000);
    }
}

