package com.moxi.leavemessage.activity;

import android.app.Activity;
import  android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.FrameLayout;

import com.moxi.leavemessage.R;
import com.moxi.leavemessage.URL;
import com.moxi.leavemessage.fragments.StudentsFragment;
import com.moxi.leavemessage.fragments.TeachersFragment;
import com.mx.mxbase.base.BaseActivity;


import butterknife.Bind;

public class ChosePersonActivity extends BaseActivity {
    @Bind(R.id.fragment_group)
    FrameLayout fragment_group;

    boolean isTeacher=true;
    FragmentManager fm;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_chose_person;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
         fm=getSupportFragmentManager();
        if(URL.getUser(getApplicationContext())==null){
            finish();
            return;
        }
        if(URL.type==1){
            isTeacher=true;
        }else{
            isTeacher=false;
        }

        if (isTeacher){
            StudentsFragment sf=new StudentsFragment();
            fm.beginTransaction().replace(R.id.fragment_group, sf).commit();
        }else {
            TeachersFragment tf=new TeachersFragment();
            fm.beginTransaction().replace(R.id.fragment_group, tf).commit();
        }

    }
    public void goBack(View v){
        finish();
    }
    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
