package com.moxi.studentclient.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.studentclient.R;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ResponderResultDialog extends Dialog {
    public static final int Fail=1;
    public static final int SUCCESS=0;
   static ResponderResultDialog intence;

    public ResponderResultDialog(Context context) {
        super(context);
    }

    private ResponderResultDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ResponderResultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static ResponderResultDialog creat(Context ctx){

            intence=new ResponderResultDialog(ctx,R.style.NoTitleDialog);
            v=View.inflate(ctx, R.layout.dialog_respondresult_layout,null);
            intence.setContentView(v);
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

    ImageView closer,iv;
    TextView infor;
    static View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        closer=(ImageView)v.findViewById(R.id.close_iv);
        iv=(ImageView)v.findViewById(R.id.iv);
        infor=(TextView)v.findViewById(R.id.infor_tv);

        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intence.dismiss();
            }
        });

        super.onCreate(savedInstanceState);
    }

    public void show(int flag){
        show();
        if (SUCCESS==flag) {
            iv.setBackgroundResource(R.mipmap.respond_success);
            infor.setText("恭喜您,抢答成功!");
        }else {
            iv.setBackgroundResource(R.mipmap.respond_fail);
            infor.setText("抢答失败,再接再厉!");
        }
    }
}
