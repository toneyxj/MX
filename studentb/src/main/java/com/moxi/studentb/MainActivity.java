package com.moxi.studentb;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.studentb.model.UserModel;
import com.moxi.studentb.util.CheckVersionCode;
import com.moxi.updateapp.UpdateUtil;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.http.MXHttpHelper;
import com.mx.mxbase.utils.MXUamManager;
import com.mx.mxbase.utils.Toastor;
import com.onyx.android.sdk.device.EpdController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    //true为老师端，false为学生端
    private boolean isTeacher = false;

    private TextView date_tx;
    private TextView week;
    private TextView day;
    private TextView read;
    private MXHttpHelper httpHelper;
    @Bind(R.id.rl_day_day_practice)
    RelativeLayout rlDayPractice;
    @Bind(R.id.rl_file_manager)
    RelativeLayout rlFileManager;
    @Bind(R.id.rl_hudongketang)
    RelativeLayout rl_hudongketang;
    @Bind(R.id.rl_hand_write)
    RelativeLayout rlHandWrite;
    @Bind(R.id.rl_settings)
    RelativeLayout rlSettings;
    @Bind(R.id.kechengbiao_rel)
    RelativeLayout rlTimeTable;
    @Bind(R.id.rl_user_logo)
    RelativeLayout rlUserLogo;
    @Bind(R.id.rl_reader)
    ImageView rlReader;
    @Bind(R.id.tit_tx)
    TextView tvReaderTitle;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.is_teacher)
    TextView is_teacher;
    @Bind(R.id.update_tv)
    TextView tvNeed;
    @Bind(R.id.liuyanban)
    RelativeLayout liuyan;
    @Bind(R.id.yunxizuoye)
    RelativeLayout zuoye;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.arg1 == 1001) {
            if (msg.what == Integer.parseInt(Constant.SUCCESS)) {
                UserModel userModel = (UserModel) msg.obj;
                if (userModel != null) {
                    tvUserName.setText(userModel.getResult().getName());
                }
            }
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read:
                //TODO 继续阅读
                continueReader(getrecentlyRead());
                break;
            case R.id.rl_reader:
                startAppByPackage("com.moxi.bookstore", "com.moxi.bookstore.activity.bookManager.MXBookStoreActivity");
                break;
            case R.id.rl_settings:
                //设置
                startActivity(new Intent(this, MXSettingActivity.class));
                break;
            case R.id.rl_day_day_practice:
                //好题天天练
                startAppByPackage("com.moxi.exams", "com.moxi.haierexams.activity.MXPracticeActivity");
                break;
            case R.id.rl_file_manager:
                //文件管理
                startAppByPackage("com.moxi.filemanager", "com.moxi.filemanager.FileManagerActivity");
                break;
            case R.id.kechengbiao_rel:
                //课程表
                startAppByPackage("com.moxi.timetable", "com.mx.timetable.activity.MXTimeTablesActivity");
                break;
            case R.id.rl_hand_write:
                //手写
                startAppByPackage("com.moxi.mxwriter", "com.moxi.mxwriter.MainActivity");
                break;
            case R.id.rl_user_logo:
                //用户登录
                if (isTeacher){
                    startAppByPackage("com.moxi.tuser", "com.mx.user.activity.MXLoginActivity");
                }else{
                    startAppByPackage("com.moxi.suser", "com.mx.user.activity.MXLoginActivity");
                }
                break;
            case R.id.rl_hudongketang:
                //互动课堂
                if (isTeacher){
                    //教师端
                    startAppByPackage("com.moxi.CPortTeacher","com.moxi.CPortTeacher.MainActivity");
                }else{
                    //学生端
                    startAppByPackage("com.moxi.studentclient","com.moxi.studentclient.activity.HomeActivity");
                }
                break;
            case R.id.liuyanban:
                startAppByPackage("com.moxi.leavemessage","com.moxi.leavemessage.activity.MainActivity");
                break;
            case R.id.yunxizuoye:
                if(isTeacher){
                    //教师端
                    startAppByPackage("com.moxi.taskteacher","com.moxi.taskteacher.MainActivity");
                }else {
                    //学生端
                    startAppByPackage("com.moxi.taskstudent","com.moxi.taskstudent.MainActivity");
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        read = (TextView) findViewById(R.id.read);
        date_tx = (TextView) findViewById(R.id.date_tx);
        week = (TextView) findViewById(R.id.week);
        day = (TextView) findViewById(R.id.day);

        //设置点击监听
        rl_hudongketang.setOnClickListener(this);
        read.setOnClickListener(this);
        rlDayPractice.setOnClickListener(this);
        rlFileManager.setOnClickListener(this);
        rlHandWrite.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
        rlTimeTable.setOnClickListener(this);
        rlUserLogo.setOnClickListener(this);
        rlReader.setOnClickListener(this);
        liuyan.setOnClickListener(this);
        zuoye.setOnClickListener(this);
    }

    private void setDate() {

        if (isTeacher){
            is_teacher.setText("互动课堂\n(教师端)");
        }else{
            is_teacher.setText("互动课堂\n(学生端)");

        }

        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat simday = new SimpleDateFormat("dd");
        day.setText(sim.format(date));
        date_tx.setText(simday.format(date));


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        week.setText(dayNames[dayOfWeek]);
        CheckVersionCode.getInstance(this).checkframework(tvNeed);
    }

    /**
     * 获取用户信息
     *
     * @param appSession 用户session
     */
    private void getUserInfo(String appSession) {
        if (appSession.equals("")) {
            tvUserName.setText("未登录");
        } else {
            HashMap<String, String> info = new HashMap<>();
            info.put("appSession", appSession);
            httpHelper.postStringBack(1001, Constant.GET_USER_INFO, info, getHandler(), UserModel.class);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        httpHelper = MXHttpHelper.getInstance(this);
        initView();
        setDate();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        String title = getrecentlyRead();
        tvReaderTitle.setText(title.substring(title.lastIndexOf("/") + 1));
        getUserInfo(MXUamManager.queryUser(this));
        triggerFullRefresh(this, 1000);
        setDate();
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

    /**
     * GC当前屏幕
     *
     * @param mainActivity 当前activity
     * @param i            时间间隔
     */
    private void triggerFullRefresh(final MainActivity mainActivity, int i) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EpdController.invalidate(mainActivity.getWindow().getDecorView(), EpdController.UpdateMode.GC);
            }
        }, i);
    }

    /**
     * 通过包名 类名启动app
     *
     * @param packName  包名 com.moxi.xxxx
     * @param className 类名
     */
    private void startAppByPackage(String packName, String className) {
        try {
            Intent sound = new Intent();
            ComponentName cnSound = new ComponentName(packName, className);
            sound.setComponent(cnSound);
            startActivity(sound);
        } catch (Exception e) {
            e.printStackTrace();
            Toastor.showToast(this, "启动失败，请检测是否正常安装");
            new UpdateUtil(this, this).checkInstall(packName);
        }
    }

    /**
     * 继续阅读
     *
     * @param path 最近阅读书籍文件路径
     */
    private void continueReader(String path) {
        if (!path.equals("")) {
            File file = new File(path);
            if (file.exists()) {
                Intent input = new Intent();
                ComponentName cnInput = new ComponentName("com.moxi.bookstore", "com.moxi.bookstore.activity.RecentlyActivity");
                input.setComponent(cnInput);
                startActivity(input);
            } else {
                showToast("阅读文件已异常，请确认文件是否存在！！");
            }
        } else {
            showToast("还没有阅读记录哟！");
        }
    }

    /**
     * 获得最近阅读书籍路径
     */
    public String getrecentlyRead() {
        ContentResolver contentResolver = getContentResolver();
        Uri selecturi = Uri.parse("content://com.moxi.bookstore.provider.RecentlyProvider/Recently");
        Cursor cursor = contentResolver.query(selecturi, null, null, null, null);
        if (cursor != null)
            if (cursor.moveToNext()) {
                String recently = cursor.getString(0);
                return recently;
            }
        if (cursor != null) cursor.close();
        return "";
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

}
