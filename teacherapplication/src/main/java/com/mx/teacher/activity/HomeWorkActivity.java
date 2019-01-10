package com.mx.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.http.MXHttpHelper;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.DensityUtil;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.CustomRecyclerView;
import com.mx.teacher.R;
import com.mx.teacher.adapter.HomeWorkAdapter;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.constant.TestConstant;
import com.mx.teacher.entity.AllExamsModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 *
 */
public class HomeWorkActivity extends BaseActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    @Bind(R.id.custom_recycler_home_work)
    CustomRecyclerView customRecycler;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_back)
    TextView tvBaseBack;
    @Bind(R.id.ll_base_right)
    LinearLayout llBaseRight;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.tv_base_mid_title)
    TextView tvMidTitle;

    private GestureDetector gestureDetector = null;
    private int state = -1;//－1未全部 0为未下载 1 为未完成 2为已完成 3为已批复

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.arg1 == 100) {
            if (msg.what == 0) {
                AllExamsModel allExamsModel = (AllExamsModel) msg.obj;
                if (allExamsModel != null) {
                    ACache.get(this).put(TestConstant.GET_HOME_WORK_LIST, GsonTools.obj2json(allExamsModel));
                    setHomeWorkAdapter(allExamsModel.getResult(), state);
                }
            } else {
                Toastor.showToast(this, msg.obj.toString());
            }
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_home_work;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //初始化view
        llBack.setVisibility(View.VISIBLE);
        llBaseRight.setVisibility(View.VISIBLE);
        tvBaseBack.setText("首页");
        tvBaseRight.setText("作业状态");
        tvMidTitle.setText("课后作业");

        llBack.setOnClickListener(this);
        llBaseRight.setOnClickListener(this);

        //课后作业相关
        customRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        gestureDetector = new GestureDetector(this, this);
        customRecycler.setGestureDetector(gestureDetector);
        initPopwindow();
    }

    /**
     * 获取数据
     */
    private void getData(int state) {
        HashMap<String, String> exams = new HashMap<String, String>();
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            if (ACache.get(this).getAsString(TestConstant.GET_HOME_WORK_LIST) == null) {
                Toastor.showToast(this, "网络不可用，请检查网络设置");
            } else {
                String tem = ACache.get(this).getAsString(TestConstant.GET_HOME_WORK_LIST);
                AllExamsModel allExamsModel = GsonTools.getPerson(tem, AllExamsModel.class);
                setHomeWorkAdapter(allExamsModel.getResult(), state);
            }
        } else {
            dialogShowOrHide(true, "请稍后...");
            MXHttpHelper.getInstance(HomeWorkActivity.this).postStringBack(100,
                    TestConstant.GET_HOME_WORK_LIST, exams, getHandler(), AllExamsModel.class);
            dialogShowOrHide(false, "请稍后...");
        }
    }

    private void setHomeWorkAdapter(final List<AllExamsModel.AllExams> list, int state) {
        List<AllExamsModel.AllExams> temp = new ArrayList<>();
        switch (state) {
            case -1:
                temp = list;
                break;
            case 0:
                temp.clear();
                for (AllExamsModel.AllExams exams : list) {
                    String url = TestConstant.GET_HOME_WORK_BY_ID + exams.getId();
                    if (ACache.get(this).getAsString(url) == null) {
                        temp.add(exams);
                    }
                }
                break;
            case 1:
                temp.clear();
                for (AllExamsModel.AllExams exams : list) {
                    String url = TestConstant.GET_HOME_WORK_BY_ID + exams.getId();
                    if (ACache.get(this).getAsString(url) != null && exams.getReplyStatus() == -1) {
                        temp.add(exams);
                    }
                }
                break;
            case 2:
                temp.clear();
                for (AllExamsModel.AllExams exams : list) {
                    String url = TestConstant.GET_HOME_WORK_BY_ID + exams.getId();
                    if (ACache.get(this).getAsString(url) != null && exams.getReplyStatus() == 0) {
                        temp.add(exams);
                    }
                }
                break;
            case 3:
                temp.clear();
                for (AllExamsModel.AllExams exams : list) {
                    String url = TestConstant.GET_HOME_WORK_BY_ID + exams.getId();
                    if (ACache.get(this).getAsString(url) != null && exams.getReplyStatus() == 1) {
                        temp.add(exams);
                    }
                }
                break;
        }
        if (temp.size() == 0 && temp != null) {
            Toastor.showToast(this, "暂无数据");
        }
        HomeWorkAdapter adapter = new HomeWorkAdapter(this, temp);
        customRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final String url = TestConstant.GET_HOME_WORK_BY_ID + list.get(position).getId();
                if (!NetWorkUtil.isNetworkAvailable(HomeWorkActivity.this)) {
                    if (ACache.get(HomeWorkActivity.this).getAsString(url) == null || ACache.get(HomeWorkActivity.this).getAsString(url).equals("")) {
                        Toastor.showToast(HomeWorkActivity.this, "网络不可用，请检查网络连接");
                    } else {
                        Intent intent = new Intent(HomeWorkActivity.this, MXWriteHomeWorkActivity.class);
                        intent.putExtra("home_work_state", list.get(position).getReplyStatus());
                        intent.putExtra("home_work_url", url);
                        intent.putExtra("home_work_json", ACache.get(HomeWorkActivity.this).getAsString(url));
                        startActivity(intent);
                    }
                } else if (ACache.get(HomeWorkActivity.this).getAsString(url) != null && !ACache.get(HomeWorkActivity.this).getAsString(url).equals("")) {
                    Intent intent = new Intent(HomeWorkActivity.this, MXWriteHomeWorkActivity.class);
                    intent.putExtra("home_work_state", list.get(position).getReplyStatus());
                    intent.putExtra("home_work_url", url);
                    intent.putExtra("home_work_json", ACache.get(HomeWorkActivity.this).getAsString(url));
                    startActivity(intent);
                } else {
                    OkHttpUtils.post().url(url).build().connTimeOut(10000).execute(new StringCallback() {

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            dialogShowOrHide(true, "请稍后...");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            dialogShowOrHide(false, "请稍后...");
                            Toastor.showToast(HomeWorkActivity.this, "请求网络失败，请检查网络链接");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    ACache.get(HomeWorkActivity.this).put(url, response);
                                    Intent intent = new Intent(HomeWorkActivity.this, MXWriteHomeWorkActivity.class);
                                    intent.putExtra("home_work_state", list.get(position).getReplyStatus());
                                    intent.putExtra("home_work_url", url);
                                    intent.putExtra("home_work_json", response);
                                    startActivity(intent);
                                }
                                dialogShowOrHide(false, "请稍后...");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        getData(state);
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
    public void onDisConnect() {
        super.onDisConnect();
    }

    @Override
    public void onConnect(NetWorkUtil.netType type) {
        super.onConnect(type);
        getData(state);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_base_back:
//                ACache.get(this).clear();
//                getData(state);
                this.finish();
                break;
            case R.id.ll_base_right:
                showPopwindow(llBaseRight);
                break;
            case R.id.radio_btn_all:
                state = -1;
                getData(state);
                dismissPopwindow();
                break;
            case R.id.radio_btn_no_download:
                state = 0;
                getData(state);
                dismissPopwindow();
                break;
            case R.id.radio_btn_not_done:
                state = 1;
                getData(state);
                dismissPopwindow();
                break;
            case R.id.radio_btn_done:
                state = 2;
                getData(state);
                dismissPopwindow();
                break;
            case R.id.radio_btn_review:
                state = 3;
                getData(state);
                dismissPopwindow();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    private PopupWindow popupWindow;

    private void initPopwindow() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.mx_dialog_menu_toast, null);
        popupWindow = new PopupWindow(contentView,
                DensityUtil.dip2px(this, 140), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        contentView.findViewById(R.id.radio_btn_all).setOnClickListener(this);
        contentView.findViewById(R.id.radio_btn_no_download).setOnClickListener(this);
        contentView.findViewById(R.id.radio_btn_not_done).setOnClickListener(this);
        contentView.findViewById(R.id.radio_btn_done).setOnClickListener(this);
        contentView.findViewById(R.id.radio_btn_review).setOnClickListener(this);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.mx_onclick_selector));
    }

    private void showPopwindow(View view) {
        popupWindow.showAsDropDown(view, 100, 10);
    }

    private void dismissPopwindow() {
        popupWindow.dismiss();
    }
}
