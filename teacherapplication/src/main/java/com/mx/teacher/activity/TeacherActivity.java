package com.mx.teacher.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.mxbase.http.MXHttpHelper;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;
import com.mx.teacher.R;
import com.mx.teacher.adapter.TeacherGridAdapter;
import com.mx.teacher.adapter.TeacherGridFixAdapter;
import com.mx.teacher.adapter.TeacherMenuAdapter;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.constant.Constant;
import com.mx.teacher.entity.TeacherEntry;
import com.mx.teacher.entity.TeacherGridEntry;
import com.mx.teacher.http.HttpVolleyCallback;
import com.mx.teacher.http.VolleyHttpUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class TeacherActivity extends Activity {

    private GridView info_grid;
    private List<TeacherEntry> menuDataList = new ArrayList<>();
    private TeacherMenuAdapter teacherAdapter;
    private ListView menuListView;
    private ImageView more_img;
    private LinearLayout ll_base_more;
    private LinearLayout ll_base_back;
    private TextView homeBackTv;
    private TextView titleTv;
    private TextView tv_base_more;
    private boolean UpdateDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UpdateDate) {
            menuListView.getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
            setFixWork();
        }
    }

    private void initData() {
        TeacherEntry canUsedWork = new TeacherEntry();
        canUsedWork.setMenuStr("可分发的作业");
        menuDataList.add(canUsedWork);
        TeacherEntry recivedWork = new TeacherEntry();
        recivedWork.setMenuStr("待批复的作业");
        menuDataList.add(recivedWork);
        teacherAdapter = new TeacherMenuAdapter(this, menuDataList);
        menuListView.setAdapter(teacherAdapter);
        menuListView.post(new Runnable() {
            @Override
            public void run() {
                menuListView.getChildAt(0).setBackgroundColor(Color.parseColor("#80000000"));
            }
        });
        setDiscrbuteAdapter();
    }

    /**
     * 设置分发的数据
     */
    private void setDiscrbuteAdapter() {
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            if (ACache.get(this).getAsString(Constant.HOMEWORKURL) != null) {
                String temp = ACache.get(this).getAsString(Constant.HOMEWORKURL);
                List data = parseDiscrbuteData(temp);
                if (data == null) {
                    return;
                }
                TeacherGridAdapter teacherGridAdapter = new TeacherGridAdapter(TeacherActivity.this, parseDiscrbuteData(temp));
                info_grid.setAdapter(teacherGridAdapter);
            } else {
                Toastor.showToast(this, "网络不可用，请检查网络连接!");
            }
        } else {
            VolleyHttpUtil.get(this, Constant.HOMEWORKURL, new HttpVolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    ACache.get(TeacherActivity.this).put(Constant.HOMEWORKURL, response);
                    android.util.Log.d("book", response);
                    List data = parseDiscrbuteData(response);
                    if (data == null) {
                        return;
                    }
                    TeacherGridAdapter teacherGridAdapter = new TeacherGridAdapter(TeacherActivity.this, parseDiscrbuteData(response));
                    info_grid.setAdapter(teacherGridAdapter);
                }

                @Override
                public void onFilad(String msg) {

                }
            });
        }
    }

    private void setFixWork() {
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            if (ACache.get(this).getAsString(Constant.HOMEWORKFIXURL) != null) {
                String temp = ACache.get(this).getAsString(Constant.HOMEWORKFIXURL);
                List data = parseDiscrbuteData(temp);
                if (data == null) {
                    return;
                }
                TeacherGridFixAdapter teacherGridAdapter = new TeacherGridFixAdapter(TeacherActivity.this, parseDiscrbuteData(temp));
                info_grid.setAdapter(teacherGridAdapter);
            } else {
                Toastor.showToast(this, "网络不可用，请检查网络连接!");
            }
        } else {
            VolleyHttpUtil.get(this, Constant.HOMEWORKFIXURL, new HttpVolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    android.util.Log.d("book", response);
                    ACache.get(TeacherActivity.this).put(Constant.HOMEWORKFIXURL, response);
                    List data = parseDiscrbuteData(response);
                    if (data == null) {
                        return;
                    }
                    TeacherGridFixAdapter teacherGridAdapter = new TeacherGridFixAdapter(TeacherActivity.this, parseDiscrbuteData(response));
                    info_grid.setAdapter(teacherGridAdapter);
                }

                @Override
                public void onFilad(String msg) {

                }
            });
        }
    }

    private void initView() {
        info_grid = (GridView) findViewById(R.id.info_grid);
        menuListView = (ListView) findViewById(R.id.menu);
        more_img = (ImageView) findViewById(R.id.more_img);
        more_img.setVisibility(View.GONE);
        ll_base_more = (LinearLayout) findViewById(R.id.ll_base_more);
        ll_base_more.setVisibility(View.VISIBLE);
        ll_base_more.setOnClickListener(resetClick);
        tv_base_more = (TextView) findViewById(R.id.tv_base_more);
        tv_base_more.setText(getResources().getString(R.string.reset_teacher));
        tv_base_more.setOnClickListener(ll_base_moreClicl);
        ll_base_back = (LinearLayout) findViewById(R.id.ll_base_back);
        homeBackTv = (TextView) findViewById(R.id.tv_base_back);
        titleTv = (TextView) findViewById(R.id.tv_base_mid_title);
        homeBackTv.setText(getResources().getString(R.string.back_tx));
        titleTv.setText(getResources().getString(R.string.home_teacher_title));
        ll_base_back.setOnClickListener(backClick);
        ll_base_back.setVisibility(View.VISIBLE);

        menuListView.setOnItemClickListener(menuOnItemClickListener);
    }

    AdapterView.OnItemClickListener menuOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("book", "onItemClick:" + position);
            if (position == 0) {
                UpdateDate = false;
                menuListView.getChildAt(0).setBackgroundColor(Color.parseColor("#00000000"));
                setDiscrbuteAdapter();
            } else if (position == 1) {
                UpdateDate = true;
                menuListView.getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
                setFixWork();
            }
        }
    };

    /**
     * 重置
     */
    View.OnClickListener ll_base_moreClicl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            VolleyHttpUtil.get(TeacherActivity.this, Constant.HOMERESETURL, new HttpVolleyCallback() {
                @Override
                public void onSuccess(String data) {
                    Log.d("book", "重置:" + data);
                    ACache.get(TeacherActivity.this).clear();
                    TeacherActivity.this.finish();
                    TeacherActivity.this.startActivity(TeacherActivity.this.getIntent());
                }

                @Override
                public void onFilad(String msg) {

                }
            });
        }
    };

    /**
     * 返回
     */
    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            backClick();
        }
    };

    /**
     * 重置
     */
    View.OnClickListener resetClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private void backClick() {
        this.finish();
    }

    private List parseDiscrbuteData(String data) {

        List<TeacherGridEntry> teacherGridEntries = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                TeacherGridEntry teacherGridEntry;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    teacherGridEntry = new TeacherGridEntry();
                    teacherGridEntry.setArrStatus(jsonObject1.optInt("arrStatus"));
                    String attTime = jsonObject1.optString("arrTime", "");
                    if (attTime == null) {
                        teacherGridEntry.setArrTime("");
                    } else {
                        teacherGridEntry.setArrTime(attTime);
                    }
                    teacherGridEntry.setChapter(jsonObject1.optString("chapter"));
                    teacherGridEntry.setId(jsonObject1.optInt("id"));
                    teacherGridEntry.setReplyStatus(jsonObject1.optInt("replyStatus"));
                    teacherGridEntry.setState(jsonObject1.optInt("state"));
                    teacherGridEntry.setSubject(jsonObject1.optString("subject"));
                    teacherGridEntry.setTeacher(jsonObject1.optString("teacher"));
                    teacherGridEntry.setTitle(jsonObject1.optString("title"));

                    teacherGridEntries.add(teacherGridEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherGridEntries;
    }
}

