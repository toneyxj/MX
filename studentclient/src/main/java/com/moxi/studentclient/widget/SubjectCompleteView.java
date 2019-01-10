package com.moxi.studentclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.mx.mxbase.utils.Log;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class SubjectCompleteView extends LinearLayout implements View.OnClickListener {

    private SubjectCompleteCallBack callBack;

    private SubjectNumView subjectNumView;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitSubjectBut:
                if (callBack != null)
                    callBack.submitCallBack();
                break;
        }
    }

    public interface SubjectCompleteCallBack {
        public void submitCallBack();
    }

    public void setCallBack(SubjectCompleteCallBack callBack) {
        this.callBack = callBack;
    }

    public SubjectCompleteView(Context context) {
        super(context);
        init(context);
    }

    public SubjectCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubjectCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_complete,
                this);
        findViewById(R.id.submitSubjectBut).setOnClickListener(this);
        subjectNumView = (SubjectNumView) view.findViewById(R.id.subjectNumView1);
        subjectNumView.setPageView(true);
    }

    public void setData(ExamsDetailsModel edmModel) {
        subjectNumView.setSubNumData(SubjectNumView.TypeView.RESULTTYPE, edmModel);
    }

    public void setSubjectNumViewCallBack(SubjectNumView.SubjectNumChooseCallBack callBack) {
        subjectNumView.setSubjectNumChooseCallBack(callBack);
    }

    public void setTitleAndTime(String title, String time) {
        ((TextView) findViewById(R.id.complete_title_tv)).setText(title);
        ((TextView) findViewById(R.id.complete_time_tv)).setText(time);
    }

    public void setSubmitButInVisible() {
        findViewById(R.id.submitSubjectBut).setVisibility(View.INVISIBLE);
    }

}
