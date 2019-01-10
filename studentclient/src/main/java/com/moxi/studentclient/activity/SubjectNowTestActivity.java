package com.moxi.studentclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.moxi.studentclient.Presenter.SubjectNewPersenter;
import com.moxi.studentclient.R;
import com.moxi.studentclient.SClientActivity;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.DrawToolView;
import com.moxi.studentclient.widget.NewSubjectView;
import com.moxi.studentclient.widget.NowSubjectDefautView;
import com.moxi.studentclient.widget.SubjectCompleteView;

/**
 * 现场出题
 */
public class SubjectNowTestActivity extends SClientActivity implements SubjectNewPersenter.OnSubjectPersenterCallBack, NowSubjectDefautView.SubjectDefautViewCallBack {
    private SubjectNewPersenter persenter;
    private NewSubjectView subjectNewView;
    private SubjectCompleteView completeView;
    private View titleView;
    private NowSubjectDefautView subjectNowDefautView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persenter = new SubjectNewPersenter(this);
        persenter.setSubjectCallBack(this);
        ininView("现场出题");
    }

    protected void ininView(String title) {
        super.ininView(title, persenter);
        setTitleHind(false);
        setTitle("现场出题");
        subjectNowDefautView = new NowSubjectDefautView(this);
        subjectNowDefautView.setCallBack(this);
        subjectNowDefautView.postData();
        contentView.removeAllViews();
        contentView.addView(subjectNowDefautView);
    }

    @Override
    public void onSubjectComplete() {
        setTitleHind(false);
        setTitle(persenter.getContentTitle());
        contentView.removeAllViews();
        contentView.addView(completeView);
        completeView.setTitleAndTime(persenter.getAnswer(), "用时：" + Utils.timeSecondfrom(endTime()));
        completeView.setSubmitButInVisible();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (subjectNowDefautView != null) {
            subjectNowDefautView.rmoePostData();
        }
        setResult(DrawToolView.NowTest);
        if (subjectNewView!=null)
        subjectNewView.onStop();
    }

    @Override
    public void onSubjectSubmit() {
        finish();
    }

    /**
     * 现场出题获取成功
     */
    @Override
    public void subjectDefautViewSuccess() {
        setTitleHind(true);
        contentView.removeAllViews();
        completeView = new SubjectCompleteView(this);
        completeView.setId(R.id.subjectCompleteView);
        completeView.setCallBack(persenter);

        subjectNewView = new NewSubjectView(this);
        subjectNewView.setId(R.id.subjectView);
        subjectNewView.setSubjectViewCallBack(persenter);


        titleView = LayoutInflater.from(this).inflate(R.layout.content_title_view, null);
        titleView.findViewById(R.id.submitBut).setOnClickListener(persenter);
        ((TextView) titleView.findViewById(R.id.submitBut)).setText("提交");
        contentView.addView(titleView);
        contentView.addView(subjectNewView);
        persenter.intSubjectText(subjectNewView);
        ((TextView) titleView.findViewById(R.id.title)).setText(persenter.getContentTitle());

        startTime(0);
    }

    @Override
    public void subjectDefautViewLoging() {
        Intent logInt = new Intent();
        logInt.setClass(this, LogingActivity.class);
        startActivity(logInt);
        finish();
    }

    @Override
    public void withPen() {
        if (subjectNewView != null)
            subjectNewView.paintInvalidateRectView.setPaint(0);
    }

    @Override
    public void withRubber() {
        if (subjectNewView != null)
            subjectNewView.paintInvalidateRectView.setPaint(-1);
    }

    @Override
    protected void onResume() {
        if (subjectNewView!=null)
        subjectNewView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (subjectNewView!=null)
        subjectNewView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (subjectNewView!=null)
        subjectNewView.onStop();
        super.onDestroy();

    }
}
