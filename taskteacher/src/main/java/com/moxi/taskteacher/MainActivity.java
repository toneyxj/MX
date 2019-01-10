package com.moxi.taskteacher;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.moxi.taskteacher.activity.TaskBaseActivity;
import com.moxi.taskteacher.fragment.DataFragment;
import com.moxi.taskteacher.fragment.DataIndexFragment;
import com.moxi.taskteacher.fragment.HomeWorkFragment;
import com.moxi.taskteacher.fragment.ResFragment;
import com.moxi.taskteacher.utils.GetTeacherUserId;
import com.moxi.taskteacher.utils.TaskStudentBoardcast;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends TaskBaseActivity implements View.OnClickListener {
    @Bind(R.id.data)
    RadioButton data;
    @Bind(R.id.task_work)
    RadioButton task_work;

    //主要显示的frament
    private FragmentManager fm;
    private DataFragment dataFragment;
    private HomeWorkFragment homeWorkFragment;
    private List<Fragment> listF = new ArrayList<>();
    private FragmentTransaction ft;
    private boolean firstStart = true;
    private TaskStudentBoardcast homeKeyEventBrodcast=new TaskStudentBoardcast();

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        registerReceiver(homeKeyEventBrodcast, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        init();
    }

    /**
     * 初始化view
     */
    private void init() {
        fm = getSupportFragmentManager();
        homeWorkFragment = new HomeWorkFragment();
        dataFragment = new DataFragment();
        listF.clear();
        listF.add(dataFragment);
        listF.add(homeWorkFragment);
        data.setChecked(true);
        task_work.setOnClickListener(this);
        data.setOnClickListener(this);
        showFragmentIndex(0);
    }

    /**
     * 显示哪个fragment
     *
     * @param i index
     */
    private void showFragmentIndex(int i) {
        String teaId = GetTeacherUserId.getTeaUserId(this, firstStart);
        if (teaId.equals("")) {
            firstStart = false;
            Toastor.showToast(this, "检测到未登录，请先登录");
            return;
        } else {
            ft = fm.beginTransaction();
            for (int a = 0; a < 2; a++) {
                if (i == a) {
                    if (listF.get(i).isAdded()) {
                        ft.show(listF.get(i));
                    } else {
                        ft.add(R.id.main_fragment, listF.get(i)).show(listF.get(i));
                    }
                } else {
                    ft.hide(listF.get(a));
                }
            }
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        showFragmentIndex(0);
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
        unregisterReceiver(homeKeyEventBrodcast);
    }

    @Override
    public void onBackPressed() {
        if (dataFragment != null && !dataFragment.isHidden()) {
            dataFragment.onKeyPressDown();
        } else if (homeWorkFragment != null && !homeWorkFragment.isHidden()) {
            homeWorkFragment.onKeyPressDown();
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            if (dataFragment != null && !dataFragment.isHidden()) {
                dataFragment.onPageUp();
            } else if (homeWorkFragment != null && !homeWorkFragment.isHidden()) {
                homeWorkFragment.onPageUp();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            if (dataFragment != null && !dataFragment.isHidden()) {
                dataFragment.onPageDown();
            } else if (homeWorkFragment != null && !homeWorkFragment.isHidden()) {
                homeWorkFragment.onPageDown();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data:
                data.setChecked(true);
                showFragmentIndex(0);
                break;
            case R.id.task_work:
                task_work.setChecked(true);
                showFragmentIndex(1);
                break;
            default:
                break;
        }
    }
}
