package com.moxi.studentclient.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.studentclient.Presenter.SubjectInfoPersenter;
import com.moxi.studentclient.Presenter.SubjectPersenter;
import com.moxi.studentclient.R;
import com.moxi.studentclient.SClientActivity;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.AnswerHistorybean;
import com.moxi.studentclient.model.ExamsDetailsHistoryModel;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectInfoView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectResultView;
import com.moxi.studentclient.widget.SubjectView;
import com.mx.mxbase.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class SubjectInfoActivity extends SClientActivity {
    private SubjectInfoPersenter persenter;
    private View titleView;
    private SubjectNumView subjectNumView;
    private SubjectInfoView subjectInfoView;
    private AnswerHistorybean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persenter = new SubjectInfoPersenter(this);
        bean = (AnswerHistorybean) getIntent().getExtras().getSerializable("bean");
        if (bean == null)
            return;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", UserInformation.getInstance().getID() + ""));
        valuePairs.add(new ReuestKeyValues("classWorkId", bean.getClassWorkId() + ""));
        getData(valuePairs, "1015", Connector.getInstance().studentClassWork, true);
        ininView("查看试题");
    }

    @Override
    public void withPen() {

    }

    @Override
    public void withRubber() {

    }

    @Override
    public void onSuccess(String result, String code) {
        ExamsDetailsHistoryModel edmHistory = GsonTools.getPerson(result, ExamsDetailsHistoryModel.class);
        persenter.setEdm(edmHistory,bean.getClassWorkId()+"");
        persenter.intSubjectText(subjectInfoView, subjectNumView);
        ((TextView) titleView.findViewById(R.id.scoreTv)).setText(edmHistory.getResult().getTotalScore()+"分");
        ((TextView) titleView.findViewById(R.id.scoreTv)).setVisibility(View.VISIBLE);


    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    protected void ininView(String title) {
        super.ininView(title, persenter);
        setTitleHind(true);
        contentView.removeAllViews();
        subjectNumView = new SubjectNumView(this);
        LinearLayout.LayoutParams layoutParamsSubjectNum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsSubjectNum.setMargins(0, 20, 0, 0);
        subjectNumView.setLayoutParams(layoutParamsSubjectNum);


        subjectInfoView = new SubjectInfoView(this);
//        LinearLayout.LayoutParams layoutParamsSubjectNum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParamsSubjectNum.setMargins(0,20,0,0);
//        subjectInfoView.setLayoutParams(layoutParamsSubjectNum);
        titleView = LayoutInflater.from(this).inflate(R.layout.content_title_view, null);
        titleView.findViewById(R.id.submitBut).setVisibility(View.GONE);
        ((TextView) titleView.findViewById(R.id.title)).setText(bean.getClassWorkTitle());
        contentView.addView(titleView);
        contentView.addView(subjectNumView);
        contentView.addView(subjectInfoView);

    }


}
