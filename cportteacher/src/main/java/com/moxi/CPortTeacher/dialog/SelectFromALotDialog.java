package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.ConfigData;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.model.Student;
import com.moxi.CPortTeacher.utils.MsecTimer;
import com.moxi.CPortTeacher.utils.TimeSetUtils;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.mx.mxbase.base.BaseApplication;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/11/1.
 */
public class SelectFromALotDialog extends CPortRequestBaseDialog implements MsecTimer.TimeListener{
    @Bind(R.id.main_layout)
    RelativeLayout main_layout;

    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.answer_number_and_time)
    TextView answer_number_and_time;
    @Bind(R.id.adview_select_people)
    LinearLayout adview_select_people;
    @Bind(R.id.start_select)
    Button start_select;
    private boolean isStart = false;
    private List<Student> listData = new ArrayList<>();
    private List<Student> middleData = new ArrayList<>();
    private MsecTimer msecTimer;

    private MyHandler handler = new MyHandler(this);

    @Override
    public void cuttentTime(int time) {
        if (time>=3600){
            onClick(start_select);
        }
        answer_number_and_time.setText("抢答人数："+listData.size()+"人\t\t用时"+ TimeSetUtils.secToTime(time));
    }

    private static class MyHandler extends Handler {
        private WeakReference<SelectFromALotDialog> reference;

        public MyHandler(SelectFromALotDialog context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            SelectFromALotDialog activity = (SelectFromALotDialog) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message msg) {
        if (msg.what == 1) {
            addView();
        }
    }


    public SelectFromALotDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setRectViewHeight(main_layout.getHeight());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_select_from_a_lot;
    }

    private void requestData() {
        dialogShowOrHide(true, "学生列表加载中...");
        getData(null, "学生列表", Connector.getInstance().getStudenturl, true);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        start_select.setText("开始抽选");
        CacheDbUtils.getInstance().saveFinshClassData(code, result);
        listData.clear();
        listData.addAll(JsonAnalysis.getInstance().getStudents(result));
        cuttentTime(0);
        addView();
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        start_select.setText("重新获取");
    }

    @Override
    public void initDialog() {
        msecTimer=new MsecTimer(0,this);
        title_dialog.setText("抽选");
        quit_dialog.setOnClickListener(this);

        start_select.setOnClickListener(this);

        setRect_view(R.id.rect_view);

        requestData();
    }

    private void addView() {
        SelectTask();
        //启动抽选
        adview_select_people.removeAllViews();
        for (int i = 0; i < middleData.size(); i++) {
            Student been = middleData.get(i);

            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_select_from_a_lot, null);
            ImageView huanggaun_wai = (ImageView) view.findViewById(R.id.huanggaun_wai);
            ImageView photo_src = (ImageView) view.findViewById(R.id.photo_src);
            TextView name = (TextView) view.findViewById(R.id.name);
            View line_view = (View) view.findViewById(R.id.line_view);

            if (i == 2) {
                line_view.setVisibility(View.INVISIBLE);
            }
            if (isStart && i == 1) {
                photo_src.setImageResource(R.mipmap.huangguan_zheng);
            } else if (i == 1) {
                if (!isFirst) {
                    huanggaun_wai.setVisibility(View.VISIBLE);
                }
                photo_src.setImageResource(ConfigData.getInstance().photos[Integer.parseInt(middleData.get(i).photo)]);
                isFirst = false;
            }

            name.setText(been.name);
            adview_select_people.addView(view);
        }
        if (isStart) {
            handler.sendEmptyMessageDelayed(1, 200);
        }

    }

    /**
     * 抽选
     */
    private void SelectTask() {
        int rom = (int) (Math.random() * (listData.size() - 1));
        middleData.clear();
        for (int i = 0; i < 3; i++) {
            rom = (rom > listData.size() - 1) ? 0 : rom;
            middleData.add(listData.get(rom));
            rom++;
        }
    }


    public static void getdialog(Context context, View.OnClickListener listener) {
        SelectFromALotDialog dialog = new SelectFromALotDialog(context, R.style.AlertDialogStyle, listener);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 6, 0, BaseApplication.ScreenWidth / 6, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }

    private boolean isFirst = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_dialog:
                this.dismiss();
                break;
            case R.id.start_select:
                if (start_select.getText().toString().equals("重新获取")) {
                    requestData();
                    return;
                }
                if (listData.size() == 0) {
                    return;
                }
                if (isStart) {
                    //已开始抽选开始
                    msecTimer.stopTimer();
                    handler.removeMessages(1);
                    isStart = false;
                    addView();
                    start_select.setText("开始抽选");
                } else {
                    //未开始抽选
                    isStart = true;
                    handler.sendEmptyMessageDelayed(1, 200);
                    start_select.setText("结束抽选");
                    msecTimer.startTimer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        msecTimer.stopTimer();
    }
}
