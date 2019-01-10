package com.moxi.studentclient.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moxi.studentclient.R;
import com.moxi.studentclient.cache.ACache;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.model.NowSubjectModel;
import com.moxi.studentclient.utils.GlideUtils;
import com.moxi.studentclient.view.PaintInvalidateRectView;
import com.moxi.studentclient.view.SlideLinerlayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 现场出题
 * Created by Administrator on 2016/10/28 0028.
 */
public class NewSubjectView extends LinearLayout implements View.OnClickListener {

    private NowSubjectModel.NowSubject edm;
    private ImageView titleIv;
    private LinearLayout panin_ll;
    public PaintInvalidateRectView paintInvalidateRectView;
    private SlideLinerlayout truefalse_ll;
    private Button trueBut, falseBut;

    private  String url;


    private AnwerCheckBoxView anwerCheckBoxView;

    private Context context;

    private SubjectCallBack callBack;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.true_but:
                edm.setStudentAnser("1");
                setBgColor(trueBut,falseBut);
                break;
            case R.id.false_but:
                edm.setStudentAnser("0");
                setBgColor(falseBut,trueBut);
                break;
        }
    }


    public interface SubjectCallBack {
        public void answerCallBack(NowSubjectModel.NowSubject edmModel);
    }

    public void setSubjectViewCallBack(SubjectCallBack callBack) {
        this.callBack = callBack;
    }

    public NewSubjectView(Context context) {
        super(context);
        initView(context);
    }

    public NewSubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NewSubjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_now,
                this);
        titleIv = (ImageView) view.findViewById(R.id.title_iv);
        panin_ll = (LinearLayout) view.findViewById(R.id.panin_ll);
        paintInvalidateRectView = (PaintInvalidateRectView) view.findViewById(R.id.paintachace_prv);
        truefalse_ll = (SlideLinerlayout) view.findViewById(R.id.truefalse_ll);


        trueBut = (Button) view.findViewById(R.id.true_but);
        falseBut = (Button) view.findViewById(R.id.false_but);
        trueBut.setOnClickListener(this);
        falseBut.setOnClickListener(this);

        anwerCheckBoxView = (AnwerCheckBoxView) view.findViewById(R.id.anwerCheckBoxView);
    }

    public void parseExamsDetails(NowSubjectModel edmModel) {
        this.edm = edmModel.getResult();
        if (edm == null)
            return;
         url = Connector.getInstance().url + "questionImg/" + edm.getTitle();
        GlideUtils.getInstance().loadGreyImage(context.getApplicationContext(), titleIv, url);

        if (edm.getType() == 1 || edm.getType() == 2) {
            anwerCheckBoxView.setSubNumtView(edm.getType(), edm.getOption());
        }


        // 1-单选，2-多选，3-主观，4-判断
        if (edm.getType() == 1) {
            anwerCheckBoxView.setVisibility(View.VISIBLE);
            anwerCheckBoxView.setSubNumtView(edm.getType(), edm.getOption());

            panin_ll.setVisibility(View.GONE);
            truefalse_ll.setVisibility(View.GONE);
        } else if (edm.getType() == 2) {
            anwerCheckBoxView.setVisibility(View.VISIBLE);
            anwerCheckBoxView.setSubNumtView(edm.getType(), edm.getOption());
            panin_ll.setVisibility(View.GONE);
            truefalse_ll.setVisibility(View.GONE);
        } else if (edm.getType() == 3) {
            anwerCheckBoxView.setVisibility(View.GONE);
            panin_ll.setVisibility(View.VISIBLE);
            truefalse_ll.setVisibility(View.GONE);
        } else if (edm.getType() == 4) {
            anwerCheckBoxView.setVisibility(View.GONE);
            panin_ll.setVisibility(View.GONE);
            truefalse_ll.setVisibility(View.VISIBLE);
        }
    }

    private void setBgColor(Button t1, Button t2) {
        t1.setBackgroundResource(R.drawable.shape_topbtn_select_bg);
        t2.setBackgroundResource(R.drawable.shape_topbtn_normal_bg);
        t1.setTextColor(Color.parseColor("#FFFFFF"));
        t2.setTextColor(Color.parseColor("#000000"));
    }

    /**
     * 获取做题结果
     * @return
     */
    public Map<String,String> getStuendtResult() {
        Map<String,String> map=new HashMap<>();
        map.put("type",String.valueOf(edm.getType()));
        map.put("anwer",getSelectResult());
        return map;
    }

    public String getSelectResult(){
        if (edm.getType() == 1 || edm.getType() == 2) {
            if (anwerCheckBoxView.getSelectValue() == null)
                return "";
            return anwerCheckBoxView.getSelectValue();
        } else if(edm.getType()==4){
            return edm.getStudentAnser();
        }else if(edm.getType()==3){
            return saveImage();
        }
        return "";
    }

    private String saveImage(){
        ACache.get(context).put(url, paintInvalidateRectView.getBitmap(false));
        return url;
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
