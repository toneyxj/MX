package com.moxi.studentclient.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserInformation;
import com.moxi.studentclient.R;
import com.moxi.studentclient.activity.LogingActivity;
import com.moxi.studentclient.activity.PersonalInformationActivity;

/**
 * Created by Administrator on 2016/11/7.
 */

public class TopLoginViewGroup extends RelativeLayout {
    Context context;
    ImageView user_ico_iv;
    TextView userName;
    private TextView title;

    public boolean islogin = false;

    public TopLoginViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View v = View.inflate(context, R.layout.top_logingroup_layout, this);
        user_ico_iv = (ImageView) v.findViewById(R.id.user_ico_iv);
        userName = (TextView) v.findViewById(R.id.user_state_tv);
        title = (TextView) v.findViewById(R.id.top_title_tv);
        user_ico_iv.setOnClickListener(clickListener);
        userName.setOnClickListener(clickListener);
    }

    public void updataUser(int imagId, String name) {
        user_ico_iv.setBackgroundResource(imagId);
        userName.setText(name);
    }
    OnClickListener clickListener=new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (UserInformation.getInstance().getID()==-1) {
                Intent logInt = new Intent();
                logInt.setClass(context, LogingActivity.class);
                context.startActivity(logInt);
            }else{
                Intent infoInt = new Intent();
                infoInt.setClass(context, PersonalInformationActivity.class);
                context.startActivity(infoInt);
            }
        }
    };

    public void setTopTile(String topTile) {
        Drawable d=getResources().getDrawable(R.mipmap.mx_img_back);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        title.setCompoundDrawables(d,null,null,null);
        title.setText(topTile);
    }
}
