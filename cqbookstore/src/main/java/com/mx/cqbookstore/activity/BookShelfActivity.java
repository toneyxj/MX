package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.MainActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.adapter.HGridstoreapter;
import com.mx.cqbookstore.adapter.NetBookPageRecyclerAdapter;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.bean.DataEbookResouce;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.interfaces.OnFlingListener;
import com.mx.cqbookstore.utils.ToolUtils;
import com.mx.cqbookstore.view.HSlidableGridView;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.view.CustomRecyclerView;

import java.util.List;

import butterknife.Bind;

public class BookShelfActivity extends MyBaseActivity implements GestureDetector.OnGestureListener, OnFlingListener {
    @Bind(R.id.recycler_page_book_store)
    CustomRecyclerView recyclerView;
    @Bind(R.id.books_gv)
    HSlidableGridView books_gv;
    @Bind(R.id.progress_bar_hitn)
    ProgressBar progress_bar_hitn;
    @Bind(R.id.progress_net_hitn)
    ProgressBar progress_net_hitn;

    @Bind(R.id.emty_rl_location)
    RelativeLayout emty_rl_location;
    @Bind(R.id.emty_rl_net)
    RelativeLayout emty_rl_net;

    HGridstoreapter hadapter;
    List<DBebookbean> dBookList;
    GestureDetector gestureDetector;
    public void goMain(View v){ startActivity(new Intent(this, MainActivity.class));}
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_book_shelf;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        gestureDetector = new GestureDetector(this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //给recyclerView设置ontouch事件
        recyclerView.setGestureDetector(gestureDetector);
        books_gv.setOnFlingListener(this);
        books_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2017/1/5 openbook
                ToastUtil("open book");
                ToolUtils.getIntence().openEbook(BookShelfActivity.this,dBookList.get(position));
            }
        });
    }

    @Override
    public void onActivityStarted(Activity activity) {
        emty_rl_net.setVisibility(View.GONE);
        dBookList= DBEbookUtils.getDBBooks();
        getRecommt();
    }

    public void reflashRecomm(View v){
        getRecommt();
    }
    public void goStacks(View v) {
        Intent it=new Intent(this,StacksActivity.class);
        startActivity(it);
    }
    private void getRecommt() {
        BaseMap pramers=new BaseMap();
        pramers.put("pageIndex",1);
        pramers.put("pageSize",4);
        pramers.put("bookName","");
        pramers.put("orderCol",2);
        pramers.put("order","desc");
        //pramers.put("key","TXhTZXJ2aWNl");
        SoapRequest request=new SoapRequest(handler,sut,this,1, Config.ACTION);
        request.execute(pramers);
        progress_net_hitn.setVisibility(View.VISIBLE);
        //showDialog("加载中...");
    }
    List<EbookResouce> recommlist;
    NetBookPageRecyclerAdapter adapter;
    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
           // hideDialog();
            progress_net_hitn.setVisibility(View.GONE);
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS) {
                emty_rl_net.setVisibility(View.GONE);
               DataEbookResouce data = jut.getBookList((String) msg.obj);
                recommlist=data.list;
                adapter = new NetBookPageRecyclerAdapter(recommlist, BookShelfActivity.this, 1);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setOnItemClickLIstener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it=new Intent(BookShelfActivity.this, ResouceDetailActivity.class);
                        it.putExtra("ebook",recommlist.get(position));
                        startActivity(it);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }else {
                emty_rl_net.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public void onActivityResumed(Activity activity) {
        progress_bar_hitn.setVisibility(View.GONE);
        if (null!=dBookList&&dBookList.size()>0){
            emty_rl_location.setVisibility(View.GONE);
            books_gv.setVisibility(View.VISIBLE);
            hadapter=new HGridstoreapter(this);
            List<DBebookbean> templist;
            if (dBookList.size()>8){
                templist=dBookList.subList(0,8);
            }else
                templist=dBookList;
            hadapter.setData(templist);
            books_gv.setAdapter(hadapter);
        }else {
            emty_rl_location.setVisibility(View.VISIBLE);
            books_gv.setVisibility(View.GONE);
        }
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
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLeftFling() {

    }

    @Override
    public void onRightFling() {

    }
}
