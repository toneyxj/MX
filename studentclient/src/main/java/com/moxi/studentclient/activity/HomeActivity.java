package com.moxi.studentclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.HttpPostRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.CommandService;
import com.moxi.studentclient.R;
import com.moxi.studentclient.adapter.HistoryAdapter;
import com.moxi.studentclient.bean.BoardBean;
import com.moxi.studentclient.bean.WorkListResult;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.moxi.studentclient.interfacess.DrawToolListener;
import com.moxi.studentclient.model.AnswerHistorybean;
import com.moxi.studentclient.utils.GlideUtils;
import com.moxi.studentclient.utils.Utils;
import com.moxi.studentclient.view.ConnectStateView;
import com.moxi.studentclient.view.DrawToolView;
import com.moxi.studentclient.view.HSlidableListView;
import com.moxi.studentclient.view.TopLoginViewGroup;
import com.moxi.studentclient.widget.CoursewareWhiteBoardView;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DrawToolListener, ServiceConnection, CommandService.OnLinkCallBack, RequestCallBack {

    @Bind(R.id.history_lv)
    HSlidableListView hisory_lv;

    @Bind(R.id.connect_state_iv)
    ImageView connect_state_iv;
    @Bind(R.id.connect_state_tv)
    TextView connect_state_tv;

    @Bind(R.id.doDraw_tv)
    TextView doDraw_tv;
    @Bind(R.id.doCourse_tv)
    TextView doCourse_tv;
    @Bind(R.id.doanswer_tv)
    TextView doanswer_tv;
    @Bind(R.id.doanswer_bottom)
    ImageView doanswer_bottom;
    @Bind(R.id.dohistory_tv)
    TextView dohistory_tv;
    @Bind(R.id.dohistory_bottom)
    ImageView dohistory_bottom;
    @Bind(R.id.drawbord_bottom)
    ImageView drawbord_bottom;
    @Bind(R.id.cwarebord_bottom)
    ImageView cwarebord_bottom;
    @Bind(R.id.cwarebord_tv)
    TextView cwarebord_tv;
    @Bind(R.id.drawbord_tv)
    TextView drawbord_tv;

    @Bind(R.id.index_tv)
    TextView index_tv;
    @Bind(R.id.body_ly)
    LinearLayout body_ly;
    @Bind(R.id.history_rl)
    RelativeLayout history_rl;
    @Bind(R.id.bord_rl)
    RelativeLayout bord_rl;


    @Bind(R.id.back_iv)
    ImageButton back_iv;
    @Bind(R.id.more_iv)
    ImageButton more_iv;
    @Bind(R.id.board_iv)
    ImageView board_iv;

    @Bind(R.id.tool_dv)
    DrawToolView dtv;//工具组合控件
    @Bind(R.id.loginGroup)
    TopLoginViewGroup logingroup;
    @Bind(R.id.connectstateview)
    ConnectStateView connectStateView;

    HistoryAdapter AHadapter;
    @Bind(R.id.bottom_ry)
    RelativeLayout bottomRy;

    private CoursewareWhiteBoardView coursewareWhiteBoardView;


    private Intent commandServiceIt;
    private CommandService commandService;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        hisory_lv.setDividerHeight(0);
        hisory_lv.setDivider(null);
        initListener();

        coursewareWhiteBoardView = (CoursewareWhiteBoardView) findViewById(R.id.coursewareWhiteBoardView);

        AHadapter = new HistoryAdapter(this);
        hisory_lv.setAdapter(AHadapter);
        commandServiceIt = new Intent();
        commandServiceIt.setClass(this, CommandService.class);
        startService(commandServiceIt);
    }

    public void goBack(View v) {
        if(!coursewareWhiteBoardView.finshView()){
            showToast("首页");
        }
        /*
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);
        */
    }

    @Override
    public void onActivityStarted(Activity activity) {

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

    private void initListener() {
        doDraw_tv.setOnClickListener(this);
        doCourse_tv.setOnClickListener(this);
        doanswer_tv.setOnClickListener(this);
        dohistory_tv.setOnClickListener(this);
        cwarebord_tv.setOnClickListener(this);
        drawbord_tv.setOnClickListener(this);
//        hisory_lv.setOnFlingListener(this);
        back_iv.setOnClickListener(this);
        more_iv.setOnClickListener(this);
        connectStateView.setOnClickListener(this);
        hisory_lv.setOnItemClickListener(this);
        dtv.setOnDrawToolListener(this);
    }

    private void getHistoryData(int page) {
        if (UserInformation.getInstance().getUserInformation() != null) {
            List<ReuestKeyValues> valuePairs = new ArrayList<>();
            valuePairs.add(new ReuestKeyValues("userId", UserInformation.getInstance().getID() + ""));
            valuePairs.add(new ReuestKeyValues("page", page + ""));
            postData(valuePairs, "1010", Connector.getInstance().studentClassWorkList, true);
        } else {
            Intent loingInt = new Intent();
            loingInt.setClass(this, LogingActivity.class);
            startActivity(loingInt);
        }
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        connectStateView.cancelPoll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cwarebord_tv://课件白板
                board_iv.setVisibility(View.GONE);
                cwarebord_bottom.setVisibility(View.VISIBLE);
                drawbord_bottom.setVisibility(View.INVISIBLE);
                coursewareWhiteBoardView.setVisibility(View.VISIBLE);
                body_ly.setVisibility(View.GONE);
                bottomRy.setVisibility(View.GONE);
                break;
            case R.id.drawbord_tv://手写白板
                cwarebord_bottom.setVisibility(View.INVISIBLE);
                drawbord_bottom.setVisibility(View.VISIBLE);
                body_ly.setVisibility(View.VISIBLE);
                hisory_lv.setVisibility(View.GONE);
                bottomRy.setVisibility(View.GONE);
                getBoard();
                coursewareWhiteBoardView.setVisibility(View.GONE);
                break;
            case R.id.doDraw_tv://老师的白板，墨认显示手写白板
                setBgColor(doDraw_tv, doCourse_tv);
                history_rl.setVisibility(View.GONE);
                bottomRy.setVisibility(View.GONE);
                bord_rl.setVisibility(View.VISIBLE);
                cwarebord_bottom.setVisibility(View.INVISIBLE);
                drawbord_bottom.setVisibility(View.VISIBLE);
                body_ly.setVisibility(View.VISIBLE);
                hisory_lv.setVisibility(View.GONE);
                bottomRy.setVisibility(View.GONE);
                getBoard();
                coursewareWhiteBoardView.setVisibility(View.GONE);

                break;
            case R.id.doCourse_tv:
                setBgColor(doCourse_tv, doDraw_tv);
                history_rl.setVisibility(View.VISIBLE);
                bord_rl.setVisibility(View.GONE);
                board_iv.setVisibility(View.GONE);
                bottomRy.setVisibility(View.GONE);
                body_ly.setVisibility(View.VISIBLE);
                coursewareWhiteBoardView.setVisibility(View.GONE);

                board_iv.setVisibility(View.GONE);
                doanswer_bottom.setVisibility(View.VISIBLE);
                dohistory_bottom.setVisibility(View.INVISIBLE);
                findViewById(R.id.back_iv).setTag(1);
                findViewById(R.id.more_iv).setTag(1);
                body_ly.setVisibility(View.VISIBLE);
                hisory_lv.setVisibility(View.VISIBLE);
                bottomRy.setVisibility(View.VISIBLE);
                coursewareWhiteBoardView.setVisibility(View.GONE);
                getHistoryData(1);


                break;
            case R.id.doanswer_tv: //点击答题记录
                board_iv.setVisibility(View.GONE);
                doanswer_bottom.setVisibility(View.VISIBLE);
                dohistory_bottom.setVisibility(View.INVISIBLE);
                findViewById(R.id.back_iv).setTag(1);
                findViewById(R.id.more_iv).setTag(1);
                body_ly.setVisibility(View.VISIBLE);
                hisory_lv.setVisibility(View.VISIBLE);

                bottomRy.setVisibility(View.VISIBLE);
                coursewareWhiteBoardView.setVisibility(View.GONE);
                getHistoryData(1);
                break;
            case R.id.dohistory_tv: //点击课堂参与记录
                board_iv.setVisibility(View.GONE);
                findViewById(R.id.back_iv).setTag(2);
                findViewById(R.id.more_iv).setTag(2);
                dohistory_bottom.setVisibility(View.VISIBLE);
                doanswer_bottom.setVisibility(View.INVISIBLE);
                bottomRy.setVisibility(View.GONE);
                coursewareWhiteBoardView.setVisibility(View.GONE);
                getDoHistoryData();
                break;
            case R.id.back_iv:
                if(v.getTag()==null)
                    return;
                int type = Integer.parseInt(v.getTag().toString());
                if (type == 1) {
                    int leftPage = Integer.parseInt(index_tv.getTag(R.id.tagFirst).toString());
                    if (leftPage > 1) {
                        getHistoryData(leftPage - 1);
                    } else {
                        ToastUtils.getInstance().showToastShort("已经是第一页");
                    }
                } else {

                }
                break;
            case R.id.more_iv:
                if(v.getTag()==null)
                    return;
                int typeT = Integer.parseInt(v.getTag().toString());
                if (typeT == 1) {
                    int rightPage = Integer.parseInt(index_tv.getTag(R.id.tagFirst).toString());
                    if (rightPage < Integer.parseInt(index_tv.getTag(R.id.tagSecond).toString())) {
                        getHistoryData(rightPage + 1);
                    } else {
                        ToastUtils.getInstance().showToastShort("已经是最后页");
                    }
                } else {
                }
                break;
        }
    }

    private void getDoHistoryData() {
//        WorkListResult workListResult = GsonTools.getPerson(result, WorkListResult.class);
        List<AnswerHistorybean> data = new ArrayList<>();
        AnswerHistorybean anserBean = new AnswerHistorybean();
        anserBean.setItemType(1);
        anserBean.setClassWorkDate("11月15日");
        anserBean.setClassWorkTime("11:36");
        anserBean.setTimeSpan("");
        anserBean.setClassWorkTitle("参与\"抢答\"");
        data.add(anserBean);
        anserBean = new AnswerHistorybean();
        anserBean.setItemType(1);
        anserBean.setClassWorkDate("11月15日");
        anserBean.setClassWorkTime("11:40");
        anserBean.setTimeSpan("");
        anserBean.setClassWorkTitle("选择人员抽中一次");
        data.add(anserBean);
        anserBean = new AnswerHistorybean();
        anserBean.setItemType(1);
        anserBean.setClassWorkDate("11月14日");
        anserBean.setClassWorkTime("11:40");
        anserBean.setTimeSpan("");
        anserBean.setClassWorkTitle("你抢到了题目");
        data.add(anserBean);
        AHadapter.setData(data);
        AHadapter.notifyDataSetChanged();
        index_tv.setTag(R.id.tagFirst, 0);
        index_tv.setTag(R.id.tagSecond, 0);
        index_tv.setText(0 + "/" + 0);
    }

    ProgressDialog pd;

    private void getBoard() {
        if (Utils.isLogin(this) && 0 == CommandService.getLinkState()) {
            pd = dialogShowOrHide(true, "加载老师白板...","");
            List<ReuestKeyValues> parmers = new ArrayList<>();
            parmers.add(new ReuestKeyValues("userId", userId + ""));
            new HttpGetRequest(this, getBoardCall, parmers, "getBoard",
                    Connector.getInstance().getBoardUrl, true).execute();
        } else
            showToast("未连接课堂");
    }

    BoardBean board;
    RequestCallBack getBoardCall = new RequestCallBack() {
        @Override
        public void onSuccess(String result, String code) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            board = JsonAnalysis.getInstance().getBoardformJson(result);
            if (null != board) {
                //String path=Connector.getInstance().BoardImagUrl + board.file;

                GlideUtils.getInstance().loadGreyImage(HomeActivity.this, board_iv,
                        Connector.getInstance().BoardImagUrl + board.file);
                board_iv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFail(String code, boolean showFail, int failCode, String msg) {
            pd.dismiss();
        }
    };


    private void setBgColor(TextView t1, TextView t2) {
        t1.setBackgroundResource(R.drawable.shape_topbtn_select_bg);
        t2.setBackgroundResource(R.drawable.shape_topbtn_normal_bg);
        t1.setTextColor(Color.parseColor("#FFFFFF"));
        t2.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AnswerHistorybean bean = (AnswerHistorybean) parent.getAdapter().getItem(position);
        if (bean != null && bean.getItemType() == 0) {
            Intent it6 = new Intent();
            it6.setClass(this, SubjectInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            it6.putExtras(bundle);
            startActivity(it6);
        }
    }

    private long firstClick;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!coursewareWhiteBoardView.finshView()) {
                if (System.currentTimeMillis() - firstClick > 2000) {
                    firstClick = System.currentTimeMillis();
                    showToast("再按一次退出");
                } else {
                    stopService(commandServiceIt);
                    System.exit(0);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void withPen() {
        showToast("start pentool");
    }

    @Override
    public void withRubber() {
        showToast("start rubber");
    }

    @Override
    public void doClassExam() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    long userId;
    UserBaseInformation user;

    @Override
    protected void onResume() {
        super.onResume();
        bindService(commandServiceIt, this, Service.BIND_AUTO_CREATE);
        userId = UserInformation.getInstance().getID();
        user = UserInformation.getInstance().getUserInformation();
        if (UserInformation.getInstance().getID() != -1 && UserInformation.getInstance().getUserInformation() != null) {
            logingroup.islogin = true;
            String name = UserInformation.getInstance().getUserInformation().name;
            logingroup.updataUser(R.mipmap.boy_ico, name);
        } else {
            logingroup.updataUser(R.mipmap.user_unlogin_ico, "未登录");
        }
    }

    @Override
    public void doelimination() {
    }

    @Override
    public void doResponder() {
    }

    @Override
    public void doVote() {
    }


    @Override
    public void onLink(String link) {

    }

    @Override
    public void onAction(String action) {
        connectStateView.setUpdate(action);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        commandService = ((CommandService.MyBinder) iBinder).getService();
        commandService.setCallBack(this);
        if (coursewareWhiteBoardView != null)
            coursewareWhiteBoardView.setCommandService(commandService);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dtv.finishDeal();
    }

    private void postData(
            List<ReuestKeyValues> valuePairs, String code, String Url, boolean show) {
        if (valuePairs == null) valuePairs = new ArrayList<>();
//        dialogShowOrHide(true,"加载中...");
        new HttpPostRequest(this, this, valuePairs, code, Url, show).execute();
    }

    @Override
    public void onSuccess(String result, String code) {
//        dialogShowOrHide(false,"加载中...");
        WorkListResult workListResult = GsonTools.getPerson(result, WorkListResult.class);
        AHadapter.setData(workListResult.getResult().getList());
        AHadapter.notifyDataSetChanged();
        index_tv.setTag(R.id.tagFirst, workListResult.getResult().getPage());
        index_tv.setTag(R.id.tagSecond, workListResult.getResult().getPageCount());
        index_tv.setText(workListResult.getResult().getPage() + "/" + workListResult.getResult().getPageCount());
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
//        dialogShowOrHide(false,"加载中...");
        ToastUtils.getInstance().showToastShort(msg);
    }

    private ProgressDialog dialog;

    public ProgressDialog dialogShowOrHide(boolean is, String hitn,String loacl) {
        if (is) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(hitn);
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            if (dialog != null)
                dialog.dismiss();
            dialog = null;
        }
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
