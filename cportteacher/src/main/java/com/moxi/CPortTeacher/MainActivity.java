package com.moxi.CPortTeacher;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxi.CPortTeacher.Cinterface.SettingTitleInterface;
import com.moxi.CPortTeacher.activity.LogingActivity;
import com.moxi.CPortTeacher.activity.PersonalInformationActivity;
import com.moxi.CPortTeacher.config.ConfigData;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.config.OttoCode;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.dialog.FirstAnswerQuestionDialog;
import com.moxi.CPortTeacher.dialog.FirstAnswerQuestionPeopleRankDialog;
import com.moxi.CPortTeacher.dialog.SelectFromALotDialog;
import com.moxi.CPortTeacher.dialog.VoteDialog;
import com.moxi.CPortTeacher.dialog.VoteProgressDialog;
import com.moxi.CPortTeacher.frament.ClassRecordFragment;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.frament.MainWhiteBoardFragment;
import com.moxi.CPortTeacher.frament.NoContentFragment;
import com.moxi.CPortTeacher.frament.WhiteBoard.WhiteBoardFragment;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.utils.DownloadAsy;
import com.moxi.CPortTeacher.utils.TeacherCloseBoardCast;
import com.moxi.CPortTeacher.view.PaintInvalidateRectView;
import com.moxi.classRoom.CportNoInterfaceActivity;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.dbUtils.UserInformationUtils;
import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.serve.TimeReceiver;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.squareup.otto.Subscribe;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends CportNoInterfaceActivity implements View.OnClickListener, SettingTitleInterface {
    private TimeReceiver timeReceiver = null;
    @Bind(R.id.back_click)
    TextView back_click;
    @Bind(R.id.click_loging)
    LinearLayout click_loging;
    @Bind(R.id.photo)
    ImageView photo;
    @Bind(R.id.loging_status)
    TextView loging_status;
    @Bind(R.id.white_area_add_page)
    ImageButton white_area_add_page;

    //上课状态竖栏
    @Bind(R.id.class_status)
    TextView class_status;

    //主要分类
    @Bind(R.id.classify_group)
    RadioGroup classify_group;

    //工具栏
    @Bind(R.id.penil)
    RadioButton penil;
    @Bind(R.id.rubber)
    RadioButton rubber;
    @Bind(R.id.screenshort)
    RadioButton screenshort;
    @Bind(R.id.clean)
    RadioButton clean;
    @Bind(R.id.answerfirst)
    RadioButton answerfirst;
    @Bind(R.id.select)
    RadioButton select;
    @Bind(R.id.vote)
    RadioButton vote;
    @Bind(R.id.interact)
    RadioButton interact;


    //主要显示的frament
    private FragmentManager fm;
    private int flagPosition = -1;
    private MainWhiteBoardFragment mainWhiteBoardFragment;
    private ClassTestFragment classTestFragment;
    private ClassRecordFragment classRecordFragment;
    private NoContentFragment noContentFragment;
    private TeacherCloseBoardCast homeKeyEventBrodcast=new TeacherCloseBoardCast();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //注册保存右边主菜单栏item
            List<Point> points = new ArrayList<>();
            points.add(getViewPoint(penil));
            points.add(getViewPoint(rubber));
            points.add(getViewPoint(screenshort));
            points.add(getViewPoint(clean));
            points.add(getViewPoint(answerfirst));
            points.add(getViewPoint(select));
            points.add(getViewPoint(vote));
            ConfigData.getInstance().setPoints(points, penil.getMeasuredWidth(), penil.getMeasuredHeight());
        }
    }

    private Point getViewPoint(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }


    @Override
    public void firstAbove() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initActivity(Bundle savedInstanceState) {
        registerReceiver(homeKeyEventBrodcast, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        CPortApplication.getBus().register(this);
        UserInformation.getInstance().setLoging(false);

        fm = getSupportFragmentManager();

        back_click.setOnClickListener(this);
        click_loging.setOnClickListener(this);
        class_status.setOnClickListener(this);

        //初始化工具栏
        penil.setOnClickListener(this);
        rubber.setOnClickListener(this);
        screenshort.setOnClickListener(this);
        clean.setOnClickListener(this);
        answerfirst.setOnClickListener(this);
        select.setOnClickListener(this);
        vote.setOnClickListener(this);
        interact.setOnClickListener(this);

        white_area_add_page.setOnClickListener(this);

        //主要分类设置
        classify_group.setOnCheckedChangeListener(classfiyChangeListener);

        //发起自动登录
        automationLoging();

//        //注册后台下载服务
//        Intent intent = new Intent(MainActivity.this, DownloadService.class);
//        startService(intent);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1://登录延缓处理

                break;
            default:
                break;
        }
    }

    @Subscribe
    public void backLoging(OttoBeen been) {
        if (been.code.equals(LogingActivity.OTTO_LOGING)) {
            //更新登录数据
            if (UserInformation.getInstance().isLoging()) {
                UserBaseInformation information = UserInformationUtils.getInstance().getInformation();
                photo.setImageResource(R.mipmap.default_photo);
                loging_status.setText(information.name);
                changeLessionStatus(information.lesson, information.className);
            } else {
                photo.setImageResource(R.mipmap.default_photo);
                loging_status.setText("未登录");
                clearFragment();
            }
        }
        showFragment(flagPosition, false);
    }

    private void changeLessionStatus(int lesson, String className) {
        boolean isLesson = lesson == 1;
        CPortApplication.setTextAllImage(class_status, isLesson, R.mipmap.attend_class_have, R.mipmap.attend_class_no, 1);
        if (isLesson) {
            class_status.setText(className);
        } else {
            class_status.setText("上课");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click://返回
                onBackPressed();
                break;
            case R.id.click_loging://登录
                if (UserInformation.getInstance().isLoging()) {
                    //已登录进入个人资料
                    startActivity(new Intent(MainActivity.this, PersonalInformationActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LogingActivity.class));
                }
                break;
            case R.id.class_status://改变上课状态
                if (UserInformation.getInstance().isLoging()) {
                    if (UserInformation.getInstance().getUserInformation().lesson == 1) {
                        //正在上课
                        insureQuitClass(new InsureOrQuitListener() {
                            @Override
                            public void isInsure(Object code, boolean is) {
                                if (is) {
                                    endLesson("结束上课");
                                }
                            }
                        });
                    } else {
                        //开始上课
                        startLesson();
                    }
                } else {
                    startActivity(new Intent(MainActivity.this, LogingActivity.class));
                }
                break;
            case R.id.white_area_add_page://添加白板
                if (judgeHitn()) return;
                if (null != mainWhiteBoardFragment) {
                    mainWhiteBoardFragment.onClick(v);
                }
                break;
            //工具栏点击项
            case R.id.penil://铅笔
                if (judgeHitn()) return;
                CPortApplication.getBus().post(new OttoBeen(0, PaintInvalidateRectView.pakegeName));
                break;
            case R.id.rubber://橡皮
                if (judgeHitn()) return;
                CPortApplication.getBus().post(new OttoBeen(-1, PaintInvalidateRectView.pakegeName));
                break;
            case R.id.screenshort://截图
                if (judgeHitn()) return;
                if (null != mainWhiteBoardFragment && !mainWhiteBoardFragment.isHidden()) {
                    Bitmap bitmap=mainWhiteBoardFragment.PrintScreen();
                    if (bitmap!=null){
                        //不等于null表示截图成功获取
                    }
                }
                break;
            case R.id.clean://清理
                if (judgeHitn()) return;
                CPortApplication.getBus().post(new OttoBeen(null, PaintInvalidateRectView.pakegeName + "clear"));
                setPen();
                break;
            case R.id.answerfirst://抢答
                if (judgeHitn()) return;
                FirstAnswerStatus();
                break;
            case R.id.select://抽选
                if (judgeHitn()) return;
                SelectFromALotDialog.getdialog(MainActivity.this, this);
                break;
            case R.id.vote://投票
                if (judgeHitn()) return;
                VoteProgress();
                break;
            case R.id.interact://投影
                try {
                    PackageManager manager = this.getPackageManager();
                    Intent Calligraphy = manager.getLaunchIntentForPackage("com.awindinc.sphone2tv");
                    this.startActivity(Calligraphy);
                }catch (Exception e){
                    ToastUtils.getInstance().showToastShort("未安装投影应用");
                }

                break;
            default:
                break;
        }
    }
    private void setPen(){
        penil.setChecked(true);
        onClick(penil);
    }

    /**
     * 抢答初始化
     */
    private void FirstAnswerStatus(){
        //开启获取抢答结果
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "抢答初始化...");
        getData(values, "抢答初始化", Connector.getInstance().getRobAnswerResult, false);
    }
    /**
     * 投票进度初始话
     */
    private void VoteProgress(){
        //开启获取抢答结果
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "投票初始化...");
        getData( values, "投票进度", Connector.getInstance().getVoteProgress,false);
    }
    /**
     * 判断是否需要提示，true代表有提示
     *
     * @return
     */
    private boolean judgeHitn() {
        if (!UserInformation.getInstance().isLoging()) {
            insureDialog("您还未登录！！", "登录", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is) {
                        startActivity(new Intent(MainActivity.this, LogingActivity.class));
                    }
                }
            });
            return true;
        } else if (UserInformation.getInstance().getUserInformation().lesson != 1) {
            insureDialog("您还未开始上课，是否开始上课", "上课", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is) {
                        startLesson();
                    }
                }
            });
            return true;
        }
        return false;
    }

//    @Subscribe
//    public void onChangeIndex(OttoBeen been) {
//        String code = been.code;
//        if (null == code || !code.equals("MainActivity")) return;
//        int index = (int) been.style;
//        RadioButton radioButton = null;
//        switch (index) {
//            case 0:
//                radioButton = penil;
//                break;
//            case 1:
//                radioButton = rubber;
//                break;
//            case 2:
//                radioButton = screenshort;
//                break;
//            case 3:
//                radioButton = clean;
//                break;
//            case 4:
//                radioButton = answerfirst;
//                break;
//            case 5:
//                radioButton = select;
//                break;
//            case 6:
//                radioButton = vote;
//                break;
//            default:
//                break;
//        }
//        if (radioButton != null) {
//            radioButton.setChecked(true);
//        }
//    }


    /**
     * 项目主分类的changgeListener
     */
    RadioGroup.OnCheckedChangeListener classfiyChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.white_area://白板区域
                    showFragment(1, true);
                    if (judgeHitn())return;
                    sendShowOrHiden(WhiteBoardFragment.code);
                    setPen();
                    break;
                case R.id.class_test://课堂测试
                    showFragment(2, true);
                    if (judgeHitn())return;
                    sendShowOrHiden(ClassTestFragment.code);
                    setPen();
                    break;
                case R.id.class_record://课堂记录
                    showFragment(3, true);
                    break;
                default:
                    break;
            }
        }
    };
    private void sendShowOrHiden(String code){
        CPortApplication.getBus().post(new OttoBeen(null, WhiteBoardFragment.code,  code.equals(WhiteBoardFragment.code)));
        CPortApplication.getBus().post(new OttoBeen(null, ClassTestFragment.code,  code.equals(ClassTestFragment.code)));
    }

    /**
     * 显示fragment
     *
     * @param position    显示位置
     * @param isIntercept 是否拦截
     */
    private void showFragment(int position, boolean isIntercept) {
        whiteShowOrHide(false);
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
        hideFragment(mFragmentTransaction);
        if (position == flagPosition && isIntercept)
            return;
        switch (position) {
            case 1:// 白板区域
                if (showNoContentFragment(mFragmentTransaction, false)) {
                    if (mainWhiteBoardFragment == null) {
                        mainWhiteBoardFragment = new MainWhiteBoardFragment();
                        mFragmentTransaction.add(R.id.main_classfiy_fragment, mainWhiteBoardFragment);
                    } else {
                        mFragmentTransaction.show(mainWhiteBoardFragment);
//                        sendShowOrHiden(new OttoBeen(true, WhiteBoardFragment.code ));
                    }
                    mainWhiteBoardFragment.onRefuresh();
                }
                break;
            case 2:// 模拟测试
                if (showNoContentFragment(mFragmentTransaction, false)) {
                    if (classTestFragment == null) {
                        classTestFragment = new ClassTestFragment();
                        mFragmentTransaction.add(R.id.main_classfiy_fragment, classTestFragment);
                    } else {
                        mFragmentTransaction.show(classTestFragment);
//                        sendShowOrHiden(new OttoBeen(true, ClassTestFragment.code ));
                    }
                    classTestFragment.onRefuresh();
                }
                break;
            case 3:// 测试记录
                if (showNoContentFragment(mFragmentTransaction, false)) {
                    if (classRecordFragment == null) {
                        classRecordFragment = new ClassRecordFragment();
                        mFragmentTransaction.add(R.id.main_classfiy_fragment, classRecordFragment);
                    } else {
                        mFragmentTransaction.show(classRecordFragment);
                    }
                    classRecordFragment.onRefuresh();
                }
                break;
            default:
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
        flagPosition = position;
    }

    private boolean showNoContentFragment(FragmentTransaction mFragmentTransaction, boolean isLoging) {
        if (isLoging && UserInformation.getInstance().isLoging()) {
            return true;
        } else if (!UserInformation.getInstance().isLoging() || UserInformation.getInstance().getUserInformation().lesson != 1) {
            if (noContentFragment == null) {
                noContentFragment = new NoContentFragment();
                mFragmentTransaction.add(R.id.main_classfiy_fragment, noContentFragment);
            } else {
                mFragmentTransaction.show(noContentFragment);
                noContentFragment.setHitn_txt();
            }
            return false;
        }
        return true;
    }

    /**
     * 隐藏fragment
     *
     * @param mFragmentTransaction
     */
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (mainWhiteBoardFragment != null) {
            mFragmentTransaction.hide(mainWhiteBoardFragment);
//            sendShowOrHiden(new OttoBeen(false, WhiteBoardFragment.code ));
        }
        if (classTestFragment != null) {
            mFragmentTransaction.hide(classTestFragment);
//            sendShowOrHiden(new OttoBeen(false, ClassTestFragment.code ));
        }
        if (classRecordFragment != null) {
            mFragmentTransaction.hide(classRecordFragment);
        }
        if (noContentFragment != null) {
            mFragmentTransaction.hide(noContentFragment);
        }
    }

    private void setClassfiyCheck(int index) {
        if (isfinish)return;
        ((RadioButton) classify_group.getChildAt(index)).setChecked(true);
    }

    private void whiteShowOrHide(boolean is) {
        white_area_add_page.setVisibility(is ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (timeReceiver != null)
            unregisterReceiver(timeReceiver);

        CPortApplication.getBus().unregister(this);

        //设置未登录信息
        UserInformation.getInstance().setLoging(false);

       removeAllfragment();
        unregisterReceiver(homeKeyEventBrodcast);
        DownloadAsy.getInstance().clearAllAsy();
        super.onDestroy();
    }
    private void removeAllfragment(){
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
        if (null!=mainWhiteBoardFragment){
            mFragmentTransaction.remove(mainWhiteBoardFragment);
        }
        if (null!=classRecordFragment){
            mFragmentTransaction.remove(classRecordFragment);
        }
        if (null!=classTestFragment){
            mFragmentTransaction.remove(classTestFragment);
        }
        if (null!=noContentFragment){
            mFragmentTransaction.remove(noContentFragment);
        }
        mFragmentTransaction.commitAllowingStateLoss();
        mainWhiteBoardFragment=null;
        classRecordFragment=null;
        classTestFragment=null;
        noContentFragment=null;
    }

    private void startReciver() {
        timeReceiver = new TimeReceiver();
        //注册时间广播
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, intentFilter);
        //启动广播
        timeReceiver.onReceive(MainActivity.this, (new Intent()).setAction(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onBackPressed() {
        CPortApplication.getBus().post(new OttoBeen(null, "onBackPressed"));
            if (!back_click.getText().toString().equals("互动课堂(教师端)")) {
                //分发事件
                sendBackClick();
                return;
            }
            //判断老师是否正在上课
            if (UserInformation.getInstance().isLoging()) {
                if (UserInformation.getInstance().getUserInformation().lesson == 1) {
                    //正在上课
                    insureQuitClass(new InsureOrQuitListener() {
                        @Override
                        public void isInsure(Object code, boolean is) {
                            if (is) {
                                endLesson("结束退出");
                            }
                        }
                    });
                    return;
                }
            }
            this.finish();
    }

    private void sendBackClick() {
        if (mainWhiteBoardFragment != null && !mainWhiteBoardFragment.isHidden()) {
            mainWhiteBoardFragment.onclickBack();
        } else if (classTestFragment != null && !classTestFragment.isHidden()) {
            classTestFragment.onclickBack();
        } else if (classRecordFragment != null && !classRecordFragment.isHidden()) {
            classRecordFragment.onclickBack();
        }
    }

    /**
     * 设置显示标题
     */
    @Override
    public void setTitle(String Title) {
        //设置标题
        if (Title.equals("")||Title.equals("互动课堂")) {
            back_click.setText("互动课堂(教师端)");
        } else {
            back_click.setText(Title);
        }
        CPortApplication.setTextAllImage(back_click, Title.equals(""), R.mipmap.class_icon, R.mipmap.back, 0);
    }

    @Override
    public void ShowOrHideAdd(boolean is) {
        whiteShowOrHide(is);
    }

    /**
     * 自动登录
     */
    private void automationLoging() {
        if (UserInformation.getInstance().getAccount() == null) {
            onFail("登录", false, 1, "您还未登录！");
            return;
        }
        //发起自动登录
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("mobile", UserInformation.getInstance().getAccount()));
        values.add(new ReuestKeyValues("password", UserInformation.getInstance().getPassword()));
        values.add(new ReuestKeyValues("type", "1"));
        dialogShowOrHide(true, "数据初始化中...");
        getData(values, "登录", Connector.getInstance().loging, true);
    }

    /**
     * 开始上课
     */
    private void startLesson() {
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "上课准备中...");
        getData(values, "上课", Connector.getInstance().startLesson, true);
    }

    /**
     * 结束上课
     */
    private void endLesson(String code) {
        CPortApplication.getBus().post(new OttoBeen("","CoursewareWhiteBoardFragment"));
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "上课结束中...");
        getData(values, code, Connector.getInstance().endLesson, true);
    }


    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("登录")) {
            String name = UserInformation.getInstance().getAccount();
            String password = UserInformation.getInstance().getPassword();
            String address_IP = UserInformation.getInstance().getDstName();
            try {
                JsonAnalysis.getInstance().setLogingInformation(name, password, address_IP, result);
                setClassfiyCheck(0);
            } catch (JSONException e) {
                ToastUtils.getInstance().showToastShort("登录数据解析失败");
            }
        } else if (code.equals("上课")) {
            isFullScreen(true);
            UserInformationUtils.getInstance().setLesson(1);
            UserBaseInformation information = UserInformationUtils.getInstance().getInformation();
            changeLessionStatus(information.lesson, information.className);

            showFragment(flagPosition, false);
        } else if (code.equals("结束上课")) {
            isFullScreen(false);
            UserInformationUtils.getInstance().setLesson(0);
            UserBaseInformation information = UserInformationUtils.getInstance().getInformation();
            changeLessionStatus(information.lesson, information.className);

            showFragment(flagPosition, false);
            CacheDbUtils.getInstance().deleteFinishClassData();
            clearFragment();
        } else if (code.equals("结束退出")) {
            UserInformationUtils.getInstance().setLesson(0);
            CacheDbUtils.getInstance().deleteFinishClassData();
            MainActivity.this.finish();
//            showFragment(flagPosition);
        }else if(code.equals("抢答初始化")){
            FirstAnswerQuestionPeopleRankDialog.getdialog(this, this);
        }else if(code.equals("投票进度")){
            VoteProgressDialog.getdialog(this);
        }
    }

    /**
     * 清空fragment缓存内容
     */
    private void clearFragment() {
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();

        if (mainWhiteBoardFragment != null) {
            mFragmentTransaction.remove(mainWhiteBoardFragment);
        }

        if (classTestFragment != null) {
            mFragmentTransaction.remove(classTestFragment);
        }
        mFragmentTransaction.commitAllowingStateLoss();

        mainWhiteBoardFragment = null;
        classTestFragment = null;
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        if (code.equals("登录")) {
            UserInformation.getInstance().setLoging(false);
            setClassfiyCheck(0);
        }else if(code.equals("抢答初始化")){
            FirstAnswerQuestionDialog.getdialog(MainActivity.this, this);
        }else if(code.equals("投票进度")){
            VoteDialog.getdialog(MainActivity.this,null);
        }else if (code.equals("结束上课")&&failCode==0) {
            isFullScreen(false);
            UserInformationUtils.getInstance().setLesson(0);
            UserBaseInformation information = UserInformationUtils.getInstance().getInformation();
            changeLessionStatus(information.lesson, information.className);

            showFragment(flagPosition, false);
            CacheDbUtils.getInstance().deleteFinishClassData();
            clearFragment();
        } else if (code.equals("结束退出")&&failCode==0) {
            UserInformationUtils.getInstance().setLesson(0);
            CacheDbUtils.getInstance().deleteFinishClassData();
            MainActivity.this.finish();
//            showFragment(flagPosition);
        }
    }

    /**
     * 确认退出上课
     *
     * @param listener
     */
    private void insureQuitClass(InsureOrQuitListener listener) {
        insureDialog("请您确认下课！", "下课", listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        APPLog.e("点击事件="+keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CPortApplication.getBus().post(new OttoBeen(null, OttoCode.RESUME));
    }

    @Override
    protected void onStop() {
        super.onStop();
        CPortApplication.getBus().post(new OttoBeen(null, OttoCode.STOP));
    }

    @Override
    protected void onPause() {
        super.onPause();
        CPortApplication.getBus().post(new OttoBeen(null, OttoCode.STOP));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        CportBaseFragment fragment = getBaseFragment();
        if (null != fragment) {
            if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
                //上一页
                fragment.moveLeft();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
                //下一页
                fragment.moveRight();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private CportBaseFragment getBaseFragment() {
        if (null != mainWhiteBoardFragment && !mainWhiteBoardFragment.isHidden()) {
            return mainWhiteBoardFragment;
        } else if (null != classTestFragment && !classTestFragment.isHidden()) {
            return classTestFragment;
        } else if (null != classRecordFragment && !classRecordFragment.isHidden()) {
            return classRecordFragment;
        }
        return null;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }
}
