package com.moxi.studentclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moxi.studentclient.R;
import com.moxi.studentclient.adapter.ClassExaminationAdapter;
import com.moxi.studentclient.model.ExamsDetailsModel;
import com.mx.mxbase.interfaces.OnItemClickListener;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectNumView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    private GridView sec_grid;

    private ClassExaminationAdapter classExaminationAdapter;
    private SubjectNumChooseCallBack callBack;
    private ExamsDetailsModel edm;
    private TypeView type;

    private Context context;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int page = Integer.parseInt(view.findViewById(R.id.inf).getTag().toString());
        if (page > -1) {
            setSubNumtPage(page);
        } else {
            if(callBack!=null){
                callBack.choolseCallBack(page,"成绩单");
            }
        }
    }


    public interface SubjectNumChooseCallBack {
        public void choolseCallBack(int page, String error);
    }

    public SubjectNumView(Context context) {
        super(context);
        initView(context);
    }

    public SubjectNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SubjectNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_subject_num,
                this);

        sec_grid = (GridView) view.findViewById(R.id.sec_grid);
        findViewById(R.id.page_left_but).setOnClickListener(this);
        findViewById(R.id.page_right_but).setOnClickListener(this);

        classExaminationAdapter = new ClassExaminationAdapter(context);
        sec_grid.setAdapter(classExaminationAdapter);
        sec_grid.setOnItemClickListener(this);
    }

    public enum TypeView {
        SELECTTYPE, RESULTTYPE, INFOTYPE;
    }

    /**
     * @param page     从0开始
     * @param edmModel
     */
    public void setSubNumtView(int page, TypeView t, ExamsDetailsModel edmModel, SubjectNumChooseCallBack callBack) {
        this.callBack = callBack;
        this.edm = edmModel;
        this.type = t;
        classExaminationAdapter.setData(t, edmModel);
        classExaminationAdapter.notifyDataSetChanged();
        ini(page);
    }

    public void setSubNumtPage(int page) {
        ini(page);
    }

    public void setSubNumData(TypeView t, ExamsDetailsModel edmModel) {
        this.type = t;
        this.edm = edmModel;
        classExaminationAdapter.setData(t, edmModel);
        classExaminationAdapter.notifyDataSetChanged();
    }

    public void setSubjectNumChooseCallBack(SubjectNumChooseCallBack callBack) {
        this.callBack = callBack;
    }

    public void setPageView(boolean hind) {
        if (hind) {
            findViewById(R.id.page_ll).setVisibility(View.GONE);
        } else {
            findViewById(R.id.page_ll).setVisibility(View.VISIBLE);
        }
    }

    private void ini(int page) {
        if (edm == null || edm.getResult() == null)
            return;

        if (page >= edm.getResult().size()) {
            if (callBack != null) {
                callBack.choolseCallBack(-1, "没有试题");
            }
            return;
        }else if(page<0){
            if (callBack != null) {
                callBack.choolseCallBack(-1, "没有试题");
            }
        }

        classExaminationAdapter.setSecPostion(page);
        classExaminationAdapter.notifyDataSetChanged();

        findViewById(R.id.page_left_but).setTag(page);
        findViewById(R.id.page_right_but).setTag(page);

        if (callBack != null) {
            callBack.choolseCallBack(page, null);
        }

    }

    @Override
    public void onClick(View view) {
        int page = Integer.parseInt(view.getTag().toString());
        switch (view.getId()) {
            case R.id.page_left_but:
                if (page > 0) {
                    setSubNumtPage(page - 1);
                } else {
                    Toast.makeText(context, "已经是第一题", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.page_right_but:
                if (page + 1 < edm.getResult().size()) {
                    setSubNumtPage(page + 1);
                } else {
                    Toast.makeText(context, "已经是最后一题", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }
}
