package com.moxi.studentclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.dbUtils.CoursewareWhiteBoardUtils;
import com.moxi.studentclient.R;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.utils.Utils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.StringUtils;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class WeedOutSubjectCompleteView extends LinearLayout implements View.OnClickListener {

    private WeedOutSubjectCompleteViewCallBackCallBack callBack;

    private TextView compleTitleTv, compleTimeTv;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitSubjectBut:
                int tag = Integer.parseInt(view.getTag().toString());
                if (tag == -1) {
                    if (callBack != null)
                        callBack.reStartCallBack();
                } else {
                    if (callBack != null)
                        callBack.nextCallBack(tag);
                }
                break;
        }
    }

    public interface WeedOutSubjectCompleteViewCallBackCallBack {
        public void nextCallBack(int page);

        public void reStartCallBack();
    }

    public void setCallBack(WeedOutSubjectCompleteViewCallBackCallBack callBack) {
        this.callBack = callBack;
    }

    public WeedOutSubjectCompleteView(Context context) {
        super(context);
        init(context);
    }

    public WeedOutSubjectCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeedOutSubjectCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_weedout_subject_complete,
                this);
        findViewById(R.id.submitSubjectBut).setOnClickListener(this);
        compleTitleTv= (TextView) view.findViewById(R.id.weekout_title_tv);
        compleTimeTv= (TextView) view.findViewById(R.id.weekout_time_tv);

    }

    public void setView(boolean isSuccess, int page, int countPage, String time) {
        if (isSuccess) {
            if (page == countPage - 1) {
                ((Button) findViewById(R.id.submitSubjectBut)).setText("");
                ((Button) findViewById(R.id.submitSubjectBut)).setVisibility(View.GONE);
            } else {
                ((Button) findViewById(R.id.submitSubjectBut)).setText("进入下一题");
                findViewById(R.id.submitSubjectBut).setVisibility(View.VISIBLE);
                findViewById(R.id.submitSubjectBut).setTag(page);
            }

            compleTitleTv.setText("恭喜你！闯关成功");
            compleTimeTv.setText("用时：" + time);

            ((ImageView)findViewById(R.id.weekout_iv)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_weekout_success));

        } else {
            ((Button) findViewById(R.id.submitSubjectBut)).setText("重新挑战");
            findViewById(R.id.submitSubjectBut).setVisibility(View.VISIBLE);
            findViewById(R.id.submitSubjectBut).setTag(-1);
            compleTitleTv.setText("很遗憾，闯关失败");
            compleTimeTv.setText("用时：" + time);
            ((ImageView)findViewById(R.id.weekout_iv)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_weekout_fail));
        }
    }

}
