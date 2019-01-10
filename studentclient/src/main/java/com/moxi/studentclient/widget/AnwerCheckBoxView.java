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
import com.moxi.studentclient.adapter.ClassAnwerAdapter;
import com.moxi.studentclient.adapter.ClassExaminationAdapter;
import com.moxi.studentclient.model.AnwerBean;
import com.moxi.studentclient.model.ExamsDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class AnwerCheckBoxView extends LinearLayout {
    private GridView sec_grid;

    private ClassAnwerAdapter classAnwerAdapter;
    private List<AnwerBean> data;

    private int type;//1,单选，2，多选

    private Context context;


    public AnwerCheckBoxView(Context context) {
        super(context);
        initView(context);
    }

    public AnwerCheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnwerCheckBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_anwer_checkbox,
                this);

        sec_grid = (GridView) view.findViewById(R.id.sec_grid);

        classAnwerAdapter = new ClassAnwerAdapter(context);
        sec_grid.setAdapter(classAnwerAdapter);
    }

    /**
     * t //1,单选，2，多选
     */
    public void setSubNumtView(int t,int countItem) {
        data = new ArrayList<>();
        AnwerBean bean;
        for (int i = 0; i < countItem; i++) {
            bean = new AnwerBean();
            bean.setId(i);
            bean.setValue((char)('A' + i));
            bean.setSelect(false);
            data.add(bean);
        }
        this.type = t;
        classAnwerAdapter.setData(t, data);
        classAnwerAdapter.notifyDataSetChanged();
    }

    /**
     * 获取选择结果
     * @return
     */
    public String getSelectValue() {
        List<AnwerBean> selectData = classAnwerAdapter.getData();
        if(selectData==null)
            return null;
        String studnetAnwser = "";
        for (AnwerBean bean : selectData) {
            if (bean.isSelect()) {
                studnetAnwser += ";" + bean.getValue();
            }
        }
        if (studnetAnwser != null&&studnetAnwser.length()>1)
            return studnetAnwser.substring(1);
        return null;
    }


}
