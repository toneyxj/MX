package com.moxi.leavemessage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moxi.leavemessage.R;
import com.moxi.leavemessage.URL;
import com.moxi.leavemessage.adapter.MsgHistoryAdapter;
import com.moxi.leavemessage.bean.MsgHistoryBean;
import com.moxi.leavemessage.http.HttpVolleyCallback;
import com.moxi.leavemessage.http.VolleyHttpUtil;
import com.moxi.leavemessage.view.ScrollDisabledListView;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.Toastor;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 留言列表
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private MsgHistoryAdapter adapter;
    private ScrollDisabledListView listView;

    private int pageCount = 9;
    private int allPage = 1;
    private int countPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        adapter = new MsgHistoryAdapter(this);
        listView = (ScrollDisabledListView) findViewById(R.id.listview);
        listView.setListViewMove(listViewMove);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        findViewById(R.id.editBut).setOnClickListener(this);
        findViewById(R.id.msgBut).setOnClickListener(this);

    }

    ScrollDisabledListView.ListViewMove listViewMove = new ScrollDisabledListView.ListViewMove() {
        @Override
        public void actionMove(int up) {
            Log.i("TAG", "up:" + up);
            if (up > 0) {
                if (countPage < allPage) {
                    countPage++;
                    int itemPostion = countPage * pageCount;
                    if (itemPostion > adapter.getCount())
                        itemPostion = adapter.getCount();
                    listView.setSelection(itemPostion - 1);
                }
            } else {
                Log.i("TAG", "countPage:" + countPage);
                if (countPage > 1) {
                    countPage--;
                    int itemPostion = countPage * pageCount;
                    Log.i("TAG", "itemPostion:" + itemPostion);
                    listView.setSelection(itemPostion - pageCount);
                }

            }
        }
    };

    private void getHistoryData() {
        if (URL.getUser(this) == null) {
            finish();
            return;
        }

        ((Button) findViewById(R.id.editBut)).setText("编辑");
        findViewById(R.id.editBut).setTag("编辑");
        findViewById(R.id.delBut).setVisibility(View.GONE);

        dialogShowOrHide(true, "获取数据中...");
        HashMap<String, String> param = new HashMap<>();
        param.put("userId", String.valueOf(URL.userId));
        VolleyHttpUtil.post(this, URL.HISTORY_DATA, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                dialogShowOrHide(false, "");
                MsgHistoryBean bean = new Gson().fromJson(data, MsgHistoryBean.class);
                if (bean.getCode() == 0 && bean.getResult() != null) {
                    adapter.setData(bean.getResult());
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                }
                int allCount = adapter.getCount();
                allPage = 1;
                if (allCount > 0) {
                    allPage = allCount / pageCount;
                    if (allCount % pageCount > 0) {
                        allPage++;
                    }
                }

            }

            @Override
            public void onFilad(String msg) {
                dialogShowOrHide(false, "");
                Toast.makeText(getApplicationContext(), "请求失败，检查网络后，重试", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        getHistoryData();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editBut:
                boolean isEdit = true;
                String tag = "取消";
                int isVisible = View.VISIBLE;
                if (view.getTag() != null && view.getTag().toString().equals("取消")) {
                    isEdit = false;
                    tag = "编辑";
                    isVisible = View.GONE;
                }
                List<MsgHistoryBean.ResultBean> beans = adapter.getData();
                for (MsgHistoryBean.ResultBean bean : beans) {
                    bean.setEdit(isEdit);
                }
                adapter.notifyDataSetChanged();
                ((Button) view).setText(tag);
                view.setTag(tag);
                findViewById(R.id.delBut).setVisibility(isVisible);
                findViewById(R.id.delBut).setOnClickListener(this);
                break;
            case R.id.delBut:
                String chatIds = "";
                List<MsgHistoryBean.ResultBean> delBeans = adapter.getData();
                for (MsgHistoryBean.ResultBean bean : delBeans) {
                    if (bean.isDelEdit())
                        chatIds += "," + bean.getId();
                }
                if (TextUtils.isEmpty(chatIds)) {
                    Toast.makeText(getApplicationContext(), "没有删除的选项", Toast.LENGTH_SHORT).show();
                    return;
                }
                getDel(chatIds.substring(1));
                break;
            case R.id.msgBut:
                Intent choseIt = new Intent();
                choseIt.setClass(this, ChosePersonActivity.class);
                startActivity(choseIt);
                break;
        }
    }


    private void getDel(String chatIds) {
        if (URL.getUser(this) == null) {
            finish();
            return;
        }
        dialogShowOrHide(true, "删除中...");
        HashMap<String, String> param = new HashMap<>();
        param.put("userId", String.valueOf(URL.userId));
        param.put("chatIds", chatIds);
        VolleyHttpUtil.post(this, URL.deleteChat, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                dialogShowOrHide(false, "");
                onClick(findViewById(R.id.editBut));
                getHistoryData();
            }

            @Override
            public void onFilad(String msg) {
                dialogShowOrHide(false, "");
                Toast.makeText(getApplicationContext(), "请求失败，检查网络后，重试", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MsgHistoryBean.ResultBean bean = (MsgHistoryBean.ResultBean) adapterView.getAdapter().getItem(i);
        Intent chatIntent = new Intent();
        chatIntent.putExtra("user", bean.getUser1() == URL.userId ? String.valueOf(bean.getUser2()) : String.valueOf(bean.getUser1()));
        chatIntent.setClass(this, ChatActivity.class);
        startActivity(chatIntent);
    }
}
