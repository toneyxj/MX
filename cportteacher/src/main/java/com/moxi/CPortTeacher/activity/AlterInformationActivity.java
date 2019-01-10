package com.moxi.CPortTeacher.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.classRoom.CportNoInterfaceActivity;
import com.moxi.classRoom.dbUtils.UserInformationUtils;
import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.mx.mxbase.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;

public class AlterInformationActivity extends CportNoInterfaceActivity implements View.OnClickListener{

    @Bind(R.id.ll_base_back)
    LinearLayout ll_base_back;
    @Bind(R.id.tv_base_back)
    TextView tv_base_back;
    @Bind(R.id.tv_base_mid_title)
    TextView tv_base_mid_title;

    @Bind(R.id.photo_src)
    ImageView photo_src;
    @Bind(R.id.edit_data)
    TextView edit_data;

    @Bind(R.id.input_name)
    EditText input_name;
    @Bind(R.id.input_sex)
    EditText input_sex;
    @Bind(R.id.input_phone)
    EditText input_phone;
    @Bind(R.id.input_email)
    EditText input_email;
    @Bind(R.id.input_school)
    EditText input_school;
    @Bind(R.id.input_grade)
    EditText input_grade;

    @Bind(R.id.save_edit)
    TextView save_edit;
    @Override
    public void initActivity(Bundle savedInstanceState) {
        tv_base_back.setText("首页");
        tv_base_mid_title.setText("");
        ll_base_back.setOnClickListener(this);

        edit_data.setOnClickListener(this);
        save_edit.setOnClickListener(this);

        //初始化设置个人信息
        initInformation();
    }
    private void initInformation(){
        UserBaseInformation information= UserInformationUtils.getInstance().getInformation(UserInformation.getInstance().getID());
        ImageLoader.getInstance().displayImage(information.headimg,photo_src, ImageLoadUtils.getoptionssetResource(R.mipmap.mx_img_logo));
        input_name.setText(information.name);
        input_phone.setText(information.parentMobile);
        input_email.setText(information.email);
        input_school.setText(information.school);
        input_grade.setText(information.grade);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                onBackPressed();
                break;
            case R.id.edit_data://编辑资料
                break;
            case R.id.save_edit://保存编辑
                break;
            default:
                break;
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_alter_information;
    }

}
