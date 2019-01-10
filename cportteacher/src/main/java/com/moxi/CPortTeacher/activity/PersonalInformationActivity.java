package com.moxi.CPortTeacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.classRoom.CportNoInterfaceActivity;
import com.moxi.classRoom.dbUtils.UserInformationUtils;
import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;

/**
 * 个人信息展示
 */
public class PersonalInformationActivity extends CportNoInterfaceActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout ll_base_back;
    @Bind(R.id.tv_base_back)
    TextView tv_base_back;
    @Bind(R.id.tv_base_mid_title)
    TextView tv_base_mid_title;

    @Bind(R.id.photo_src)
    ImageView photo_src;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.edit_data)
    TextView edit_data;

    @Bind(R.id.input_phone)
    TextView input_phone;
    @Bind(R.id.input_password)
    TextView input_password;
    @Bind(R.id.input_email)
    TextView input_email;
    @Bind(R.id.input_school)
    TextView input_school;
    @Bind(R.id.input_grade)
    TextView input_grade;

    @Bind(R.id.quit)
    TextView quit;
    private HttpTimer httpTimer;

    @Override
    public void initActivity(Bundle savedInstanceState) {
        tv_base_back.setText("首页");
        tv_base_mid_title.setText("");
        ll_base_back.setOnClickListener(this);

        edit_data.setOnClickListener(this);
        quit.setOnClickListener(this);

        //初始化设置个人信息
        initInformation();
    }
    private void initInformation(){
        UserBaseInformation information= UserInformationUtils.getInstance().getInformation(UserInformation.getInstance().getID());
        ImageLoader.getInstance().displayImage(information.headimg,photo_src, ImageLoadUtils.getoptionssetResource(R.mipmap.mx_img_logo));
        user_name.setText(information.name);

        input_phone.setText(information.parentMobile);
        input_password.setText(information.password);
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
                startActivityForResult(new Intent(PersonalInformationActivity.this,AlterInformationActivity.class),10);
                break;
            case R.id.quit://退出登录
              if (UserInformation.getInstance().isLoging()&&UserInformation.getInstance().getUserInformation().lesson==1){
                  insureDialog("您正在上课无法退出登录","退出登录",null);
                  return;
              }
                UserInformation.getInstance().logoutAccount();
                CPortApplication.getBus().post(new OttoBeen("loging", LogingActivity.OTTO_LOGING));
                PersonalInformationActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK)return;
        //编辑资料改变返回
        if (requestCode==10){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (httpTimer!=null){
//            httpTimer.stopTimer();
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_information;
    }
}
