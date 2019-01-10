package com.moxi.studentclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.Presenter.SubjectPersenter;
import com.moxi.studentclient.Presenter.WeekOutSubjectPersenter;
import com.moxi.studentclient.R;
import com.moxi.studentclient.SClientActivity;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.DrawToolView;
import com.moxi.studentclient.widget.SubjectCompleteView;
import com.moxi.studentclient.widget.SubjectDefautView;
import com.moxi.studentclient.widget.SubjectNumView;
import com.moxi.studentclient.widget.SubjectView;
import com.moxi.studentclient.widget.WeedOutSubjectCompleteView;
import com.moxi.studentclient.widget.WeedOutSubjectView;

import java.util.ArrayList;
import java.util.List;

public class WeekOutSubjectActivity extends SClientActivity implements WeekOutSubjectPersenter.OnWeekOutSubjectPersenterCallBack, WeedOutSubjectView.WeedOutSubjectViewCallBack ,WeedOutSubjectCompleteView.WeedOutSubjectCompleteViewCallBackCallBack{
    private WeekOutSubjectPersenter persenter;
    private SubjectView subjectView;
    private WeedOutSubjectCompleteView weedOutSubjectCompleteView;
    private WeedOutSubjectView weedOutSubjectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persenter = new WeekOutSubjectPersenter(this);
        persenter.setWeekOutSubjectCallBackCallBack(this);
        ininView("淘汰答题");
    }

    @Override
    public void withPen() {

    }

    @Override
    public void withRubber() {

    }

    protected void ininView(String title) {
        super.ininView(title, persenter);
        setTitleHind(false);
        setTitle("淘汰答题");
        weedOutSubjectView = new WeedOutSubjectView(this);
        weedOutSubjectView.setCallBack(this);
        contentView.removeAllViews();
        contentView.addView(weedOutSubjectView);
    }

    @Override
    public void onWeekOutSubjectCompleteSuccess(int page, int countPage) {
        setTitleHind(false);
        setTitle(Utils.titleWithPage(page));
        contentView.removeAllViews();
        if(weedOutSubjectCompleteView==null){
            weedOutSubjectCompleteView=new WeedOutSubjectCompleteView(this);
            weedOutSubjectCompleteView.setCallBack(this);
        }
        weedOutSubjectCompleteView.setView(true,page,countPage,Utils.timeSecondfrom(endTime()));
        contentView.addView(weedOutSubjectCompleteView);
    }

    @Override
    public void onWeekOutSubjectCompleteFail(int page) {
        setTitleHind(false);
        setTitle(Utils.titleWithPage(page));
        contentView.removeAllViews();
        if(weedOutSubjectCompleteView==null){
            weedOutSubjectCompleteView=new WeedOutSubjectCompleteView(this);
            weedOutSubjectCompleteView.setCallBack(this);
        }
        weedOutSubjectCompleteView.setView(false,page,0,Utils.timeSecondfrom(endTime()));
        contentView.addView(weedOutSubjectCompleteView);
    }

    @Override
    public void onSubjectSubmit() {
//        Intent it6 = new Intent();
//        it6.setClass(this, SubjectInfoActivity.class);
//        startActivity(it6);
        finish();
    }

    @Override
    public void startSubjectOnClick() {
        if(UserInformation.getInstance().getUserInformation()==null){
            Intent logInt = new Intent();
            logInt.setClass(this, LogingActivity.class);
            this.startActivity(logInt);
            return;
        }
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", UserInformation.getInstance().getID() + ""));
        postData(values, "2011", Connector.getInstance().getStudentCurrentFallWork, false);
    }

    private void startSubjectView(){
        setTitleHind(false);
        setTitle(Utils.titleWithPage(0));
        contentView.removeAllViews();
        if (subjectView == null) {
            subjectView = new SubjectView(this);
            subjectView.setId(R.id.subjectView);
            subjectView.setSubjectViewCallBack(persenter);
            subjectView.setaCachePath(Connector.getInstance().getStudentCurrentFallWork);
        }
        contentView.addView(subjectView);
        persenter.intSubjectText(0, subjectView);
        startTime(0);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        ACache.get(this).put(Connector.getInstance().getStudentCurrentFallWork, result);
        startSubjectView();
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        ToastUtils.getInstance().showToastShort(msg);
    }

    @Override
    public void nextCallBack(int page) {
        setTitle(Utils.titleWithPage(page+1));
        contentView.removeAllViews();
        if (subjectView == null) {
            subjectView = new SubjectView(this);
            subjectView.setId(R.id.subjectView);
            subjectView.setSubjectViewCallBack(persenter);
        }
        contentView.addView(subjectView);
        persenter.nextSubjectText(page+1);
        startTime(0);
    }

    @Override
    public void reStartCallBack() {
        weedOutSubjectView.setCallBack(this);
        contentView.removeAllViews();
        contentView.addView(weedOutSubjectView);
        inVisibleTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(DrawToolView.WEEKOUT);
    }
}
