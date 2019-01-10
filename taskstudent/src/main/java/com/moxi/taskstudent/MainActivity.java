package com.moxi.taskstudent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxi.taskstudent.activity.TaskBaseActivity;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.layout.BaseLayout;
import com.moxi.taskstudent.layout.DataMainLayout;
import com.moxi.taskstudent.layout.GiveAnOfficalLayout;
import com.moxi.taskstudent.layout.PDFLayout;
import com.moxi.taskstudent.layout.WorkDetailLayout;
import com.moxi.taskstudent.layout.WorkLayout;
import com.moxi.taskstudent.model.WorkListModel;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.utils.DownloadAsy;
import com.moxi.taskstudent.utils.TaskDataModelUtils;
import com.moxi.taskstudent.utils.TaskStudentBoardcast;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.MXUamManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;

public class MainActivity extends TaskBaseActivity implements View.OnClickListener ,MainInterface{
    @Bind(R.id.click_loging)
    LinearLayout click_loging;
    @Bind(R.id.photo)
    ImageView photo;
    @Bind(R.id.loging_status)
    TextView loging_status;
    @Bind(R.id.task_group)
    RadioGroup task_group;
    @Bind(R.id.data)
    RadioButton data;
    @Bind(R.id.task_work)
    RadioButton task_work;
    @Bind(R.id.addview_task)
    FrameLayout addview_task;

    private DataMainLayout dataMainLayout;
    private WorkLayout workLayout;
    private WorkDetailLayout workDetailLayout;
    private GiveAnOfficalLayout giveAnOfficalLayout;
    private PDFLayout pdfLayout;
    private WorkListModel listModel;
    private String pdfPath;
    /**
     * 当前索引值
     */
    private int currentIndex = 0;
    private int workIndex=1;
    private int taskIndex=0;
    private boolean isaddwork=true;
    private TaskStudentBoardcast homeKeyEventBrodcast=new TaskStudentBoardcast();
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 10:
//                if (currentIndex == 1 && workLayout != null) {
//                    //刷新作业列表
//                    workLayout.refuresh();
//                }
//                getHandler().sendEmptyMessageDelayed(10, 15000);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        registerReceiver(homeKeyEventBrodcast, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        task_group.setOnCheckedChangeListener(classfiyChangeListener);

        click_loging.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String value=MXUamManager.querUserBId(this);
        if (value.equals("")){
            try {
                Intent input = new Intent();
                ComponentName cnInput = new ComponentName("com.moxi.suser", "com.mx.user.activity.MXLoginActivity");
                input.setComponent(cnInput);
                startActivity(input);
            }catch (Exception e){
                showToast("未安装用户系统");
                this.finish();
            }

        }else {
            try {
                JSONObject object=new JSONObject(value);
                JSONObject result=object.getJSONObject("result");
                Connector.getInstance().userId=result.getString("id");

                if (!data.isChecked()&&!task_work.isChecked()){
                    data.setChecked(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 项目主分类的changgeListener
     */
    RadioGroup.OnCheckedChangeListener classfiyChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.data://资料
                    addView(getIndexView(taskIndex));
                    break;
                case R.id.task_work://作业
                    addView(getIndexView(workIndex));
                    break;
                default:
                    break;
            }
        }
    };

    private void clearLayout(BaseLayout layout){
        if (workDetailLayout!=null&&layout!=workDetailLayout){
            workDetailLayout=null;
        }
        if (giveAnOfficalLayout!=null&&layout!=giveAnOfficalLayout){
            giveAnOfficalLayout=null;
        }
    }

    private BaseLayout getIndexView(int index) {
        currentIndex = index;
        BaseLayout view = null;
        switch (index) {
            case 0://资料主页
                taskIndex=0;
                if (dataMainLayout == null) {
                    dataMainLayout = new DataMainLayout(this,this);
                }
                view = dataMainLayout;
                break;
            case 1://作业主页
                workIndex=1;
                if (workLayout == null) {
                    workLayout = new WorkLayout(this,this);
                    isaddwork=true;
                } else {
                    isaddwork=false;
                }
                view = workLayout;
                break;
            case 2://作业详情界面
                workIndex=2;
                if (workDetailLayout == null&&listModel!=null) {
                    APPLog.e("添加数据="+listModel.toString());
                    workDetailLayout = new WorkDetailLayout(this,this);
                }
                view = workDetailLayout;
                break;
            case 3://老师批复
                workIndex=3;
                if (giveAnOfficalLayout == null&&listModel!=null) {
                    giveAnOfficalLayout = new GiveAnOfficalLayout(this,this);
                }
                view = giveAnOfficalLayout;
                break;
            case 4://pdf打开
                taskIndex=4;
                if (pdfLayout == null&&pdfPath!=null) {
                    pdfLayout = new PDFLayout(this,this);
                }
                view = pdfLayout;
                break;
            default:
                break;
        }
        return view;
    }

    private void addView(BaseLayout layout) {
        if (layout == null) return;
        addview_task.removeAllViews();
//        clearLayout(layout);
        addview_task.addView(layout);

        if (workDetailLayout==layout&&listModel!=null){
            workDetailLayout.initData(listModel);
        }else if (giveAnOfficalLayout==layout&&listModel!=null){
            giveAnOfficalLayout.initView(listModel);
        }else if (pdfLayout==layout&&pdfPath!=null){
            pdfLayout.openPdf(pdfPath);
        }else if (dataMainLayout==layout){
            dataMainLayout.initView();
        }else if (workLayout==layout&&isaddwork){
            workLayout.refuresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_loging:
                break;
            default:
                break;
        }
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

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            getIndexView(currentIndex).moveLeft();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            getIndexView(currentIndex).moveRight();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void clickWorkCheck(WorkListModel model) {
        toDetail(model);
    }

    @Override
    public void clickWorkOpen(WorkListModel model) {
        toDetail(model);
    }

    @Override
    public void clickWorkContinue(WorkListModel model) {
        toDetail(model);
    }

    @Override
    public void CheckReplay(WorkListModel model) {
        listModel=model;
        addView(getIndexView(3));
    }

    @Override
    public void SubimitWork(WorkListModel model) {
        addView(getIndexView(1));
        workLayout.ChangeModel(model);
    }

    @Override
    public void closeWork() {
        addView(getIndexView(1));
    }

    @Override
    public void openPdf(String openPath) {
//        pdfPath=openPath;
//        addView(getIndexView(4));
        File file=new File(openPath);
        if (file.exists()) {
            FileUtils.getInstance().openFile(this, file);
        }else {
            TaskDataModelUtils.getInstance().deleteData(openPath);
        }
    }

    @Override
    public void onDialogShowOrHide(boolean is, String hitn) {
        dialogShowOrHide(is,hitn);
    }

    public void toDetail(WorkListModel model){
        listModel=model;
        addView(getIndexView(2));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (currentIndex==2){
            //作业详情界面
            addView(getIndexView(1));
        }else if (currentIndex==3){
            addView(getIndexView(2));
        }else if (currentIndex==4){
            addView(getIndexView(0));
        }else {
            insureDialog("请确认退出练习", "退出", new InsureOrQuitListener() {
                @Override
                public void isInsure(Object code, boolean is) {
                    if (is){
                        MainActivity.this.finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(homeKeyEventBrodcast);
        if(dataMainLayout!=null){
            dataMainLayout.onStop();
        }
        if(workLayout!=null){
            workLayout.onStop();
        }
        if(workDetailLayout!=null){
            workDetailLayout.onStop();
        }
        if(giveAnOfficalLayout!=null){
            giveAnOfficalLayout.onStop();
        }
        if(pdfLayout!=null){
            pdfLayout.onStop();
        }
        DownloadAsy.getInstance().clearAllAsy();
        super.onDestroy();
    }
}
