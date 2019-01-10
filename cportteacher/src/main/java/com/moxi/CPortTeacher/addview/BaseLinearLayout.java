package com.moxi.CPortTeacher.addview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/3.
 */
public abstract class BaseLinearLayout extends LinearLayout{
    public BaseLinearLayout(Context context,ViewGroup view) {
        super(context);
        initView(context,view);
    }
    private void initView(Context context,ViewGroup view){
        inflate(context, getLayout(), view);
        ButterKnife.bind(this, view);
        initView(view);
    }

    public abstract int getLayout();
    public abstract void initView(View view);

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
