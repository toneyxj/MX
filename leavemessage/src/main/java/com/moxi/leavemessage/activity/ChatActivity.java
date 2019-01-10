package com.moxi.leavemessage.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moxi.leavemessage.R;
import com.moxi.leavemessage.URL;
import com.moxi.leavemessage.adapter.ChatAdapter;
import com.moxi.leavemessage.adapter.MsgHistoryAdapter;
import com.moxi.leavemessage.bean.ChatBean;
import com.moxi.leavemessage.bean.MsgHistoryBean;
import com.moxi.leavemessage.http.HttpVolleyCallback;
import com.moxi.leavemessage.http.VolleyHttpUtil;
import com.moxi.leavemessage.view.ScrollDisabledListView;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.StringUtils;

import java.util.HashMap;

/**
 * 留言窗
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private ChatAdapter adapter;
    private ScrollDisabledListView listView;

    private String user;

    private int pageCount = 6;
    private int allPage = 1;
    private int countPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getIntent().getStringExtra("user");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        adapter = new ChatAdapter(this);
        listView = (ScrollDisabledListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setListViewMove(listViewMove);
        findViewById(R.id.backTitleTv).setOnClickListener(this);

        findViewById(R.id.sendBut).setOnClickListener(this);
        findViewById(R.id.brashBut).setOnClickListener(this);
        if (getReceiversUser() != null)
            getHistoryChatData(getReceiversUser());
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

    private void getHistoryChatData(String user2) {
        if (URL.getUser(this) == null) {
            finish();
            return;
        }
        dialogShowOrHide(true, "获取数据中...");
        HashMap<String, String> param = new HashMap<>();
        param.put("userId1", String.valueOf(URL.userId));
        param.put("userId2", user2);
        VolleyHttpUtil.post(this, URL.openChat, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                dialogShowOrHide(false, "");
                ChatBean bean = new Gson().fromJson(data, ChatBean.class);
                if (bean.getCode() == 0 && bean.getResult() != null) {
                    adapter.setData(bean.getResult());
                    adapter.notifyDataSetChanged();
                    if (adapter.getCount() > 0)
                        listView.setSelection(adapter.getCount() - 1);
                }

                int allCount = adapter.getCount();
                allPage = 1;
                if (allCount > 0) {
                    allPage = allCount / pageCount;
                    if (allCount % pageCount > 0) {
                        allPage++;
                    }
                }
                countPage = allPage;
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
            case R.id.brashBut://刷新数据
                if (getReceiversUser() != null)
                    getHistoryChatData(getReceiversUser());
                break;
            case R.id.sendBut:
                String content = ((EditText) findViewById(R.id.contentEt)).getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    send(content);
                } else {
                    Toast.makeText(getApplicationContext(), "留言内容不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backTitleTv:
                finish();
                break;
        }
    }

    private void send(final String content) {
        if (URL.getUser(this) == null)
            return;
        dialogShowOrHide(true, "发送中...");
        HashMap<String, String> param = new HashMap<>();
        param.put("sendUserId", String.valueOf(URL.userId));
        param.put("content", content);
        param.put("receivers", user);
        VolleyHttpUtil.post(this, URL.addChatDetail, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                dialogShowOrHide(false, "");
                addSendList(content);
                ((EditText) findViewById(R.id.contentEt)).setText("");
            }

            @Override
            public void onFilad(String msg) {
                dialogShowOrHide(false, "");
                Toast.makeText(getApplicationContext(), "请求失败，检查网络后，重试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addSendList(String content) {
        ChatBean.ResultBean bean = new ChatBean.ResultBean();
        bean.setCreatetime("刚刚");
        bean.setSendUser(URL.userId);
        bean.setContent(content);
        bean.setSendHeadimg(String.valueOf(URL.img));
        adapter.addData(bean);
        adapter.notifyDataSetChanged();
        listView.setSelection(adapter.getCount() - 1);

        int allCount = adapter.getCount();
        allPage = 1;
        if (allCount > 0) {
            allPage = allCount / pageCount;
            if (allCount % pageCount > 0) {
                allPage++;
            }
        }
        countPage = allPage;
    }

    private String getReceiversUser() {
        if (user == null)
            return null;
        String[] users = user.split(",");
        if (users != null && users.length > 0) {
            return users[0];
        }
        return user;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
