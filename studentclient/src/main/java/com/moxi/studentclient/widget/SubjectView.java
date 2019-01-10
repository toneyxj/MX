package com.moxi.studentclient.widget;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.moxi.studentclient.utils.MxgsaTagHandler;
import com.moxi.studentclient.utils.SubjectUtils;
import com.moxi.studentclient.view.NoRadioGroup;
import com.moxi.studentclient.view.PaintInvalidateRectView;
import com.moxi.studentclient.view.SlideLinerlayout;
import com.mx.mxbase.utils.GsonTools;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectView extends LinearLayout implements View.OnClickListener {

    private ExamsDetailsModel edm;

    private String aCachePath=Connector.getInstance().url;

    private TextView titleTv, pageTv;
    private NoRadioGroup radioGroup;
    public PaintInvalidateRectView paintInvalidateRectView;
    private RadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4;
    private SlideLinerlayout slideLinerlayout;

    private Context context;

    private SubjectCallBack callBack;

    private int page;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_but:
                updateAnswer(page, "E");
                break;
            case R.id.radio_answer_1:
                updateAnswer(page, "A");
                break;
            case R.id.radio_answer_2:
                updateAnswer(page, "B");
                break;
            case R.id.radio_answer_3:
                updateAnswer(page, "C");
                break;
            case R.id.radio_answer_4:
                updateAnswer(page, "D");
                break;
        }
    }

    public interface SubjectCallBack {
        public void answerCallBack(int page, ExamsDetailsModel edmModel);
    }

    public void setaCachePath(String aCachePath){
        this.aCachePath=aCachePath;
    }

    public void setSubjectViewCallBack(SubjectCallBack callBack) {
        this.callBack = callBack;
    }

    public SubjectView(Context context) {
        super(context);
        initView(context);
    }

    public SubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SubjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject,
                this);
        titleTv = (TextView) view.findViewById(R.id.title_tv);
        pageTv = (TextView) view.findViewById(R.id.page_tv);
        radioGroup = (NoRadioGroup) view.findViewById(R.id.radio_rg);
        paintInvalidateRectView = (PaintInvalidateRectView) view.findViewById(R.id.paintachace_prv);
        slideLinerlayout = (SlideLinerlayout) view.findViewById(R.id.paintachace_ll);

        view.findViewById(R.id.ok_but).setOnClickListener(this);

        radioBtn1 = (RadioButton) view.findViewById(R.id.radio_answer_1);
        radioBtn2 = (RadioButton) view.findViewById(R.id.radio_answer_2);
        radioBtn3 = (RadioButton) view.findViewById(R.id.radio_answer_3);
        radioBtn4 = (RadioButton) view.findViewById(R.id.radio_answer_4);

        radioBtn1.setOnClickListener(this);
        radioBtn2.setOnClickListener(this);
        radioBtn3.setOnClickListener(this);
        radioBtn4.setOnClickListener(this);

    }

    /**
     * 解析同步练习试题
     *
     * @param page     当前页码,从0开始
     * @param edmModel 试题数据
     */
    public void parseExamsDetails(int page, ExamsDetailsModel edmModel) {
        this.edm = edmModel;
        this.page = page;
        if (edm == null)
            return;
        if (edm.getResult().get(page).getType() == 0) {
            radioGroup.setVisibility(View.VISIBLE);
            slideLinerlayout.setVisibility(View.GONE);
            SubjectUtils.setTitle(context, (page + 1) + "、" + edm.getResult().get(page).getTitle(), titleTv);
            resetRadioGroup(page, edm);
        } else {
            radioGroup.setVisibility(View.GONE);
            slideLinerlayout.setVisibility(View.VISIBLE);
           // SubjectUtils.setTitle(context, (page + 1) + "、" + edm.getResult().get(page).getTitle(), titleTv);


            titleTv.setText(Html.fromHtml((page + 1) + "、" + edm.getResult().get(page).getTitle(), null, new MxgsaTagHandler(context)));
//            int height = measureView(titleTv);
//            RelativeLayout.LayoutParams da = (RelativeLayout.LayoutParams) paintInvalidateRectView.getLayoutParams();
//            if (height < DensityUtil.getScreenH(context)) {
//                height = DensityUtil.getScreenH(context);
//            }
//            da.height = height;
//            Log.e("当前绘制区域高度", "height" + height);
//            paintInvalidateRectView.setLayoutParams(da);
            if (!TextUtils.isEmpty(edm.getResult().get(page).getResult())){
                String bitUrlCache = aCachePath + "_" + page;
                paintInvalidateRectView.initBitmap(ACache.get(context).getAsBitmap(bitUrlCache));
            }
        }
        pageTv.setText("第" + (page + 1) + "题");
//        tvPage.setText((page + 1) + "/" + edm.getResult().size());
    }

    /**
     * 重新设置radioGroup
     *
     * @param page 题号
     * @param edm  数据对象
     */
    private void resetRadioGroup(int page, ExamsDetailsModel edm) {
        SubjectUtils.setRadioGroup(context, edm.getResult().get(page).getOption(), radioBtn1, radioBtn2, radioBtn3, radioBtn4);
        if (TextUtils.isEmpty(edm.getResult().get(page).getResult())) {
            radioGroup.clearCheck();
        } else {
            switch (edm.getResult().get(page).getResult()) {
                case "A":
                    radioBtn1.setChecked(true);
                    break;
                case "B":
                    radioBtn2.setChecked(true);
                    break;
                case "C":
                    radioBtn3.setChecked(true);
                    break;
                case "D":
                    radioBtn4.setChecked(true);
                    break;
            }
        }
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

    /**
     * 更新缓存结果
     *
     * @param page
     * @param d
     */
    private void updateAnswer(int page, String d) {
        if (edm.getResult().get(page).getType() != 0) {
            String daaa = aCachePath + "_" + page;
            ACache.get(context).put(daaa, paintInvalidateRectView.getBitmap());
        }
        edm.getResult().get(page).setResult(d);
        ACache.get(context).put(aCachePath, GsonTools.obj2json(edm));

        if (callBack != null) {
            callBack.answerCallBack(page, edm);
        }
    }

    public void onStop(){
        paintInvalidateRectView.onstop();
    }
    public void onResume(){
        paintInvalidateRectView.onResume();
        onPause();
    }
    public void onPause(){
        paintInvalidateRectView.onPause();
    }

}
