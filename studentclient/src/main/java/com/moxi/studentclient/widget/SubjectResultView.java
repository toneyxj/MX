package com.moxi.studentclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.moxi.studentclient.R;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class SubjectResultView extends LinearLayout implements View.OnClickListener {

    private SubjectResultCallBack callBack;

    public void setCallBack(SubjectResultCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subjectInfoBut:
                if (callBack != null)
                    callBack.subjctInfoOnClick();
                break;
        }
    }

    public interface SubjectResultCallBack {
        public void subjctInfoOnClick();
    }

    public SubjectResultView(Context context) {
        super(context);
        init(context);
    }

    public SubjectResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubjectResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_result,
                this);
        findViewById(R.id.subjectInfoBut).setOnClickListener(this);

    }
}
