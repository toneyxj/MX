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
public class WeedOutSubjectView extends LinearLayout implements View.OnClickListener {

    private WeedOutSubjectViewCallBack callBack;

    public void setCallBack(WeedOutSubjectViewCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_subjct_but:
                if (callBack != null)
                    callBack.startSubjectOnClick();
                break;
        }
    }

    public interface WeedOutSubjectViewCallBack {
        public void startSubjectOnClick();
    }

    public WeedOutSubjectView(Context context) {
        super(context);
        init(context);
    }

    public WeedOutSubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeedOutSubjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_week_out_subject,
                this);
        findViewById(R.id.start_subjct_but).setOnClickListener(this);

    }
}
