package com.moxi.handwritten.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.moxi.handwritten.R;
import com.moxi.handwritten.adapter.LineTextAdapter;
import com.mx.mxbase.base.BaseApplication;

import java.util.List;

/**
 * 选择绘制方式，矢量手写和一般手写
 * Created by Administrator on 2016/8/23.
 */
public class SelectDrawStyle extends Dialog implements AdapterView.OnItemClickListener{
    private TextView title;// dialog显示文字控件
    private ListView lists;// dialog显示文字控件
    private List<String> contents;// 提示内容
    private   String titles;
    private AdapterView.OnItemClickListener listenr;

    public SelectDrawStyle(Context context, int theme, String title,List<String> contents,
                           AdapterView.OnItemClickListener listenr) {
        super(context, theme);
        this.contents = contents;
        this.listenr = listenr;
        this.titles=title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_draw_style);
        lists=(ListView)findViewById(R.id.lists);

        title=(TextView)findViewById(R.id.title);
        title.setText(titles);

        lists.setOnItemClickListener(this);

        LineTextAdapter adapter=new LineTextAdapter(getContext(),contents,false);
        lists.setAdapter(adapter);

    }

    /**
     * 显示dialog
     *
     * @param context  上下文
     * @param contents 提示内容
     * @param listenr  如果点击确认返回控件
     */
    public static void getdialog(Context context, String title, List<String> contents,
                                 AdapterView.OnItemClickListener listenr) {
        SelectDrawStyle dialog = new SelectDrawStyle(context, R.style.dialog,title,
                contents, listenr);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 6, 0, BaseApplication.ScreenWidth / 6, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.dismiss();
        if (listenr!=null)listenr.onItemClick(parent,view,position,id);
    }
}
