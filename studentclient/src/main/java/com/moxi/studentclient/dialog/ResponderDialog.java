package com.moxi.studentclient.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.moxi.studentclient.R;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ResponderDialog extends Dialog {

    static ResponderDialog intence;

    public ResponderDialog(Context context) {
        super(context);
    }

    public ResponderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ResponderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static ResponderDialog creat(Context ctx){
        if (null==intence){
            intence=new ResponderDialog(ctx,R.style.NoTitleDialog);
        }
            intence.setContentView(R.layout.dialog_responding_layout);
            intence.setCancelable(true);
            intence.setCanceledOnTouchOutside(false);
            // 监听返回键处理
            //intence.setOnCancelListener(cancelListener);
            // 设置居中
            intence.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = intence.getWindow().getAttributes();
            intence.getWindow().setAttributes(lp);


        return intence;
    }

    ImageView closer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        closer=(ImageView) findViewById(R.id.close_iv);
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intence.dismiss();
            }
        });
        super.onCreate(savedInstanceState);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.iv);
        // 获取ImageView上的动画背景
        AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        animation.start();
    }
}
