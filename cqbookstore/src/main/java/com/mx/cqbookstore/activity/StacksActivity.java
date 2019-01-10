package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.adapter.HGridstoreapter;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.interfaces.OnFlingListener;
import com.mx.cqbookstore.utils.ToolUtils;
import com.mx.cqbookstore.view.HSlidableGridView;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.view.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StacksActivity extends MyBaseActivity implements OnFlingListener,AdapterView.OnItemLongClickListener {
    @Bind(R.id.ll_stacks_back)
    LinearLayout llBack;
    @Bind(R.id.books_gv)
    HSlidableGridView books_gv;
    @Bind(R.id.emty_rl_location)
    RelativeLayout emty_body;
    @Bind(R.id.tv_stacks_total_count)
    TextView total_count;
    @Bind(R.id.tv_stacks_page_index)
    TextView index_tv;
    @Bind(R.id.et_stacks_search)
    EditText etSearch;

    HGridstoreapter hadapter;
    List<DBebookbean> dBookList,templist,searchlist;
    int currentpage=1,pagecount=1,totalcount=0;
    final int SEARCH=1,DB=0;
    int mode=0;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_stacks;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        initView();

    }

    private void initView() {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)){
                    APPLog.e("没有关键字");
                    mode=DB;

                }else {
                    APPLog.e("有关键字");
                    mode=SEARCH;
                    searchBook();
                }
                iniTemplist();

            }
        });
        hadapter=new HGridstoreapter(this);
        books_gv.setOnFlingListener(this);
        books_gv.setOnItemLongClickListener(this);
        books_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<DBebookbean> data;
                if (mode==DB){
                    data=dBookList;
                }else
                    data=searchlist;
                ToolUtils.getIntence().openEbook(StacksActivity.this,data.get(position));
            }
        });

    }

    private void searchBook() {

        searchlist=new ArrayList<>();
        for (DBebookbean item:dBookList) {
            if (item.bookName.contains(etSearch.getText()))
                searchlist.add(item);
        }
        pagecount=1;
        currentpage=1;
        totalcount=0;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        dBookList= DBEbookUtils.getDBBooks();
        iniTemplist();
    }

    private void iniTemplist() {
        List<DBebookbean> data;
        if (mode==DB){
            data=dBookList;
        }else
            data=searchlist;

        if (null!=data&&data.size()>0){
            emty_body.setVisibility(View.GONE);
            books_gv.setVisibility(View.VISIBLE);
            totalcount=data.size();
            pagecount=totalcount/12;
            if (totalcount%12>0)
                pagecount++;
            if (totalcount>12){
                templist=data.subList(0,12);
            }else
                templist=data;
            hadapter.setData(templist);
            books_gv.setAdapter(hadapter);
        }else {
            emty_body.setVisibility(View.VISIBLE);
            books_gv.setVisibility(View.GONE);
            ToastUtil("没找到书籍");
            pagecount=1;
            currentpage=1;
            totalcount=0;
        }
        total_count.setText("总计: "+totalcount);
        index_tv.setText(currentpage+"/"+pagecount);
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
    public void onLeftFling() {
        goNext();
    }

    @Override
    public void onRightFling() {
        goPrevious();
    }

    private void goNext() {
        List<DBebookbean> data;
        if (mode==DB){
            data=dBookList;
        }else
            data=searchlist;

        if (totalcount>currentpage*12) {
            currentpage++;
            if (templist.size() != 0)
                templist.clear();
            for (int i = (currentpage - 1) * 12; i < (currentpage * 12 > totalcount ? totalcount : currentpage * 12); i++) {
                templist.add(data.get(i));
            }
            hadapter.setData(templist);

        } else {

                ToastUtil("已经是最后一页!");
        }
        index_tv.setText(currentpage+"/"+pagecount);
    }

    private void goPrevious() {
        List<DBebookbean> data;
        if (mode==DB){
            data=dBookList;
        }else
            data=searchlist;
        if (currentpage>1){
            currentpage--;
            if (templist.size()!=0)
                templist.clear();
            for (int i=(currentpage-1)*12;i<currentpage*12;i++){
                templist.add(data.get(i));
            }
            hadapter.setData(templist);
            //courent_page_tv.setText(courrentPage+"/");
        }else
            ToastUtil("已经是首页!");
        index_tv.setText(currentpage+"/"+pagecount);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            onRightFling();
            return true;
        } else if ( keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            onLeftFling();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2017/1/6 删除书籍
        final List<DBebookbean> data;
        if (mode==DB){
            data=dBookList;
        }else
            data=searchlist;
        final DBebookbean item=data.get(position);
        AlertDialog ad=new AlertDialog(StacksActivity.this).builder();
        ad.setTitle("提示");
        ad.setCancelable(true);
        ad.setMsg("确认删除"+item.bookName+" ?");
        ad.setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBEbookUtils.deletBook(item);
                data.remove(item);
                dBookList= DBEbookUtils.getDBBooks();
                iniTemplist();
            }
        });
        ad.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ad.show();
        return true;
    }
}
