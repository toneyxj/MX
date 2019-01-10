package com.mx.teacher.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.teacher.R;
import com.mx.teacher.constant.TestConstant;

import java.util.HashMap;

public class MainActivity extends Activity {

    private LinearLayout ll_base_back;
    private TextView homeBackTv;
    private TextView titleTv;

    private RelativeLayout stu_rel;
    private RelativeLayout tea_rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        stu_rel = (RelativeLayout) findViewById(R.id.stu_rel);
        tea_rel = (RelativeLayout) findViewById(R.id.tea_rel);
        stu_rel.setOnClickListener(stu_rel_Click);
        tea_rel.setOnClickListener(tea_rel_Click);
        ll_base_back = (LinearLayout) findViewById(R.id.ll_base_back);
        ll_base_back.setVisibility(View.VISIBLE);
        homeBackTv = (TextView) findViewById(R.id.tv_base_back);
        titleTv = (TextView) findViewById(R.id.tv_base_mid_title);
        homeBackTv.setText(getResources().getString(R.string.home_back_tx));
        titleTv.setText(getResources().getString(R.string.home_teacher_title));
        ll_base_back.setOnClickListener(backClick);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            backClick();
        }
    };

    View.OnClickListener stu_rel_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TestConstant.IS_TEACHER = false;
            Intent intent = new Intent(MainActivity.this, HomeWorkActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener tea_rel_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TestConstant.IS_TEACHER = true;
            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
            startActivity(intent);
        }
    };


    private void backClick() {
        this.finish();
    }

}
