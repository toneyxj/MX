package com.moxi.studentclient.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moxi.studentclient.R;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.utils.GlideUtils;
import com.moxi.studentclient.utils.SubjectUtils;
import com.moxi.studentclient.view.NoRadioGroup;
import com.moxi.studentclient.view.PaintInvalidateRectView;
import com.mx.mxbase.utils.Log;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectInfoView extends LinearLayout implements View.OnClickListener {

    private ExamsDetailsModel edm;

    private TextView titleTv, answerTv, analysisTv;
    private ImageView anserImage_iv;

    private TextView sroceNameTv, sroceNumTv, sroceTimeTv;
    private NoRadioGroup radioGroup;

    private RadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4;

    private Context context;

    private SubjectInfoCallBack callBack;

    private int page;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    public interface SubjectInfoCallBack {
        // public void answerCallBack(int page, ExamsDetailsModel edmModel);
    }

    public void setSubjectViewCallBack(SubjectInfoCallBack callBack) {
        this.callBack = callBack;
    }

    public SubjectInfoView(Context context) {
        super(context);
        initView(context);
    }

    public SubjectInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SubjectInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_info,
                this);
        titleTv = (TextView) view.findViewById(R.id.title_tv);
        answerTv = (TextView) view.findViewById(R.id.answer_tv);
        analysisTv = (TextView) view.findViewById(R.id.analysis_tv);
        anserImage_iv = (ImageView) view.findViewById(R.id.anserImage_iv);
        radioGroup = (NoRadioGroup) view.findViewById(R.id.radio_rg);

        sroceNameTv = (TextView) view.findViewById(R.id.subject_info_name_title_tv);
        sroceNumTv = (TextView) view.findViewById(R.id.subject_info_name_score_tv);
        sroceTimeTv = (TextView) view.findViewById(R.id.subject_info_name_time_tv);

        radioBtn1 = (RadioButton) view.findViewById(R.id.radio_answer_1);
        radioBtn2 = (RadioButton) view.findViewById(R.id.radio_answer_2);
        radioBtn3 = (RadioButton) view.findViewById(R.id.radio_answer_3);
        radioBtn4 = (RadioButton) view.findViewById(R.id.radio_answer_4);
    }

    public void setSorceView(String studentName, int sroceNum, String time) {
        setSroceData(studentName, sroceNum, time);
        findViewById(R.id.score_ll).setVisibility(View.VISIBLE);
        findViewById(R.id.info_ll).setVisibility(View.GONE);
    }

    private void setSroceData(String studentName, int sroceNum, String time) {
        sroceNameTv.setText(studentName + "同学的成绩");
        sroceNumTv.setText(String.valueOf(sroceNum));
        sroceTimeTv.setText("用时" + time);
    }

    /**
     * 解析同步练习试题
     *
     * @param page     当前页码,从0开始
     * @param edmModel 试题数据
     */
    public void parseExamsDetails(int page, ExamsDetailsModel edmModel, String classWorkId) {
        findViewById(R.id.score_ll).setVisibility(View.GONE);
        findViewById(R.id.info_ll).setVisibility(View.VISIBLE);
        if (edmModel != null)
            this.edm = edmModel;
        this.page = page;
        if (edm == null)
            return;
        if (edm.getResult().get(page).getType() == 0) {
            radioGroup.setVisibility(View.VISIBLE);
            answerTv.setVisibility(View.VISIBLE);
            anserImage_iv.setVisibility(View.GONE);
            SubjectUtils.setTitle(context, (page + 1) + "、" + edm.getResult().get(page).getTitle(), titleTv);
            resetRadioGroup(page, edm);
//            SubjectUtils.setInfoTitle(context, (page + 1) + "、" + edm.getResult().get(page).getTitle(), titleTv);
            SubjectUtils.setAnswer(context, edm.getResult().get(page).getStudentAnswer(), edm.getResult().get(page).getAnswer(), answerTv);
            SubjectUtils.setAnalysis(context, edm.getResult().get(page).getAnalysis(), edm.getResult().get(page).getKnowledge(), analysisTv);
        } else {
            answerTv.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            anserImage_iv.setVisibility(View.VISIBLE);
            SubjectUtils.setInfoTitle(context, (page + 1) + "、" + edm.getResult().get(page).getTitle(), titleTv);
            resetImageView(anserImage_iv, classWorkId, edm.getResult().get(page).getStudentAnswer());
            SubjectUtils.setAnalysis(context, edm.getResult().get(page).getAnalysis(), edm.getResult().get(page).getKnowledge(), analysisTv);

        }
    }

    /**
     * 显示图片
     *
     * @param board_iv
     * @param
     */
    private void resetImageView(ImageView board_iv, String classWorkId, String studentAnswer) {
        if (TextUtils.isEmpty(studentAnswer)) {
            board_iv.setVisibility(View.GONE);
        }
        String url = Connector.getInstance().url + "classWorkImg/" + studentAnswer;
        Log.d("TAG", url);
        GlideUtils.getInstance().loadGreyImage(context, board_iv, url);
    }

    /**
     * 重新设置radioGroup
     *
     * @param page 题号
     * @param edm  数据对象
     */
    private void resetRadioGroup(int page, ExamsDetailsModel edm) {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_fail);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        SubjectUtils.setRadioGroup(context, edm.getResult().get(page).getOption(), radioBtn1, radioBtn2, radioBtn3, radioBtn4);
        if (TextUtils.isEmpty(edm.getResult().get(page).getStudentAnswer())) {
            radioGroup.clearCheck();
        } else {
            radioBtn1.setCompoundDrawables(null, null, null, null);
            radioBtn2.setCompoundDrawables(null, null, null, null);
            radioBtn3.setCompoundDrawables(null, null, null, null);
            radioBtn4.setCompoundDrawables(null, null, null, null);
            switch (edm.getResult().get(page).getStudentAnswer()) {
                case "A":
                    if (edm.getResult().get(page).getStudentAnswer().equals(edm.getResult().get(page).getAnswer())) {
                        radioBtn1.setCompoundDrawables(null, null, null, null);
                    } else {
                        radioBtn1.setCompoundDrawables(null, null, drawable, null);
                    }
                    radioBtn1.setChecked(true);
                    break;
                case "B":
                    if (edm.getResult().get(page).getStudentAnswer().equals(edm.getResult().get(page).getAnswer())) {
                        radioBtn2.setCompoundDrawables(null, null, null, null);
                    } else {
                        radioBtn2.setCompoundDrawables(null, null, drawable, null);
                    }
                    radioBtn2.setChecked(true);
                    break;
                case "C":
                    if (edm.getResult().get(page).getStudentAnswer().equals(edm.getResult().get(page).getAnswer())) {
                        radioBtn3.setCompoundDrawables(null, null, null, null);
                    } else {
                        radioBtn3.setCompoundDrawables(null, null, drawable, null);
                    }
                    radioBtn3.setChecked(true);
                    break;
                case "D":
                    if (edm.getResult().get(page).getStudentAnswer().equals(edm.getResult().get(page).getAnswer())) {
                        radioBtn4.setCompoundDrawables(null, null, null, null);
                    } else {
                        radioBtn4.setCompoundDrawables(null, null, drawable, null);
                    }
                    radioBtn4.setChecked(true);
                    break;
            }
        }
        radioBtn1.setEnabled(false);
        radioBtn2.setEnabled(false);
        radioBtn3.setEnabled(false);
        radioBtn4.setEnabled(false);

    }

    /**
     * 计算view的高度
     *
     * @param child
     * @return
     */
    private int measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int childMeasureHeight;
        if (lp.height > 0) {
            childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        } else {
            childMeasureHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//未指定
        }
        child.measure(childMeasureWidth, childMeasureHeight);
        int aaa = child.getMeasuredHeight();
        return aaa;
    }


}
