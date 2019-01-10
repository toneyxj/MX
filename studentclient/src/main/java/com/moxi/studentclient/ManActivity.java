package com.moxi.studentclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.moxi.studentclient.activity.SubjectInfoActivity;
import com.moxi.studentclient.activity.SubjectTestActivity;

public class ManActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                Intent it=new Intent();
                it.setClass(this, SubjectTestActivity.class);
                startActivity(it);
                break;
            case R.id.button6:
                Intent it6=new Intent();
                it6.setClass(this, SubjectInfoActivity.class);
                startActivity(it6);
                break;
        }
    }
}
