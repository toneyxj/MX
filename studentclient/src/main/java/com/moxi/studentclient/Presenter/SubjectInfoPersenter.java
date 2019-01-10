package com.moxi.studentclient.Presenter;

import android.content.Context;

import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsHistoryModel;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectInfoView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectResultView;
import com.moxi.studentclient.widget.SubjectView;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectInfoPersenter extends BasePersenter implements SubjectNumView.SubjectNumChooseCallBack {


    private ExamsDetailsModel edm;
    private ExamsDetailsHistoryModel model;

    private SubjectInfoView subjectInfoView;
    private String classWorkId;

    private Context context;

//    public SubjectInfoPersenter() {
//
//    }

    public SubjectInfoPersenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAction(String action) {

    }

    public void setEdm(ExamsDetailsHistoryModel model, String classWorkId) {
        this.model = model;
        this.classWorkId = classWorkId;
    }

    public void intSubjectText(SubjectInfoView subjectInfoView, SubjectNumView subjectNumView) {
//        new MXWriteHomeWork().getExamsDetails(context);
//        String url = Connector.getInstance().getStudentCurrentClassWork;
//        final String result = ACache.get(context).getAsString(url);
//        edm = GsonTools.getPerson(result, ExamsDetailsModel.class);
        edm = new ExamsDetailsModel();
        edm.setResult(model.getResult().getExerciseList());

        this.subjectInfoView = subjectInfoView;

        subjectNumView.setSubNumtView(0, SubjectNumView.TypeView.SELECTTYPE, edm, this);

    }


    @Override
    public void choolseCallBack(int page, String error) {
        if (page > -1) {
            subjectInfoView.parseExamsDetails(page, edm,classWorkId);
        } else {
//            subjectInfoView.setSorceView("张三", model.getResult().getTotalScore(), "32:00");
        }
    }
}
