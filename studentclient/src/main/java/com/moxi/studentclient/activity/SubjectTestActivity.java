package com.moxi.studentclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.studentclient.Presenter.SubjectPersenter;
import com.moxi.studentclient.R;
import com.moxi.studentclient.SClientActivity;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.DrawToolView;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectDefautView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectView;
import com.mx.mxbase.utils.Log;

/**
 * 课堂考试
 */
public class SubjectTestActivity extends SClientActivity implements SubjectPersenter.OnSubjectPersenterCallBack, SubjectDefautView.SubjectDefautViewCallBack {
    private SubjectPersenter persenter;
    private SubjectView subjectView;
    private SubjectCompleteView completeView;
    private View titleView;
    private SubjectNumView subjectNumView;
    private SubjectDefautView subjectDefautView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persenter = new SubjectPersenter(this);
        persenter.setSubjectCallBack(this);
        ininView("课堂考试");
    }

    protected void ininView(String title) {
        super.ininView(title, persenter);
        setTitleHind(false);
        setTitle("课堂考试");
        subjectDefautView = new SubjectDefautView(this);
        subjectDefautView.setCallBack(this);
        subjectDefautView.postData();
        contentView.removeAllViews();
        contentView.addView(subjectDefautView);
    }

    @Override
    public void onSubjectComplete() {
        setTitleHind(false);
        setTitle("测试完成");
        contentView.removeAllViews();
        contentView.addView(completeView);
        persenter.setSubjectCompleteView(completeView);
        completeView.setTitleAndTime(persenter.getContentTitle(), "用时：" + Utils.timeSecondfrom(endTime()));
    }

    @Override
    public void onSubjectStart() {
        View subjectV = contentView.findViewById(R.id.subjectView);
        if (subjectV == null) {
            View completeV = contentView.findViewById(R.id.subjectCompleteView);
            if (completeV != null) {
                contentView.removeAllViews();
            }
            setTitleHind(true);
            contentView.addView(titleView);
            contentView.addView(subjectNumView);
            contentView.addView(subjectView);
            startTime(endTime());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subjectDefautView != null) {
            subjectDefautView.rmoePostData();
        }
        setResult(DrawToolView.Test);
        if (subjectView!=null)
        subjectView.onStop();
    }

    @Override
    public void onSubjectSubmit() {
        finish();
    }

    @Override
    public void subjectDefautViewSuccess() {
        setTitleHind(true);
        contentView.removeAllViews();
        completeView = new SubjectCompleteView(this);
        completeView.setId(R.id.subjectCompleteView);
        completeView.setCallBack(persenter);

        subjectView = new SubjectView(this);
        subjectView.setId(R.id.subjectView);
        subjectView.setSubjectViewCallBack(persenter);
        subjectView.setaCachePath(Connector.getInstance().getStudentCurrentClassWork);

        subjectNumView = new SubjectNumView(this);
        LinearLayout.LayoutParams layoutParamsSubjectNum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsSubjectNum.setMargins(0, 20, 0, 0);
        subjectNumView.setLayoutParams(layoutParamsSubjectNum);

        titleView = LayoutInflater.from(this).inflate(R.layout.content_title_view, null);
        titleView.findViewById(R.id.submitBut).setOnClickListener(persenter);
        contentView.addView(titleView);
        contentView.addView(subjectNumView);
        contentView.addView(subjectView);
        persenter.intSubjectText(subjectView, subjectNumView);
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
        if (subjectView != null)
            subjectView.paintInvalidateRectView.setPaint(0);
        Log.d("TAG", "withPen");
    }

    @Override
    public void withRubber() {
        if (subjectView != null)
            subjectView.paintInvalidateRectView.setPaint(1);
        Log.d("TAG", "withRubber");
    }

    @Override
    protected void onResume() {
        if (subjectView != null)
            subjectView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (subjectView != null)
            subjectView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (subjectView != null)
            subjectView.onStop();
        super.onDestroy();
    }
}
