package com.mx.cqbookstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.activity.LoginActivity;
import com.mx.cqbookstore.activity.MyActivity;
import com.mx.cqbookstore.activity.ResouceDetailActivity;
import com.mx.cqbookstore.activity.SearchBookActivity;
import com.mx.cqbookstore.adapter.HGridViewAdapter;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.DataEbookResouce;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.interfaces.OnFlingListener;
import com.mx.cqbookstore.view.HSlidableGridView;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends MyBaseActivity implements OnFlingListener, View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.hgv)
    HSlidableGridView hgv;
    @Bind(R.id.saleTop_tv)
    TextView saleTop_tv;
    @Bind(R.id.pubtimeTop_tv)
    TextView pubtimeTop_tv;
    @Bind(R.id.priceTop_tv)
    RelativeLayout priceTop_tv;
    @Bind(R.id.top_iv)
    ImageView pricetop_iv;
    @Bind(R.id.bottom_iv)
    ImageView pricebottom_iv;
    @Bind(R.id.error_body)
    View error_body;
    @Bind(R.id.index_tv)
    TextView index_tv;
    @Bind(R.id.next_iv)
    ImageButton next_iv;
    @Bind(R.id.back_iv)
    ImageButton back_iv;

    HGridViewAdapter adapter;
    int currentpage=1,listCount=0,pagecount,reqpage=1;
    int orderCol;//0：出版日期1：价格2：销量
    int orderFlag;//1:销量榜,2:最新榜,3:低价榜,4:高价榜
    String ord;//正序asc、倒序desc
    @Override
    protected int getMainContentViewId() { return R.layout.activity_main;}
    public void goBack(View v){ finish();}
    public void searchBook(View v) {ToastUtil("search");
        startActivity(new Intent(this, SearchBookActivity.class));
    }
    public void doreflash(View v){
        if (null==list)
        getServiceInf();}
    public void goMyActivity(View v){
        ToastUtil("我的账户");
        if (null!=MyApplication.getMyApplication().getUser())
            startActivity(new Intent(this, MyActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
    }
    public void getServiceInf() {
        BaseMap pramers=new BaseMap();
        pramers.put("pageIndex",reqpage);
        pramers.put("pageSize",60);
        pramers.put("bookName","");
        pramers.put("orderCol",orderCol);
        pramers.put("order",ord);
        //pramers.put("key","TXhTZXJ2aWNl");
        SoapRequest request=new SoapRequest(handler,sut,this,1, Config.ACTION);
        request.execute(pramers);
        showDialog("加载中...");
    }

    List<EbookResouce> list,templist;
    DataEbookResouce data;
    int lastFlag=1;
    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS) {
                if (null!=list&&lastFlag!=orderFlag){
                    APPLog.e("clear list");
                    list.clear();
                    list=null;
                }

                data = jut.getBookList((String) msg.obj);
                switch (msg.arg1){
                    case 1:
                        list = data.list;
                        if (null != list && list.size() > 0) {
                            listCount = list.size();
                            getPagCount();
                            initTempList();
                            Log.e("templist:", templist.size() + "");
                            adapter.setData(templist);
                            hgv.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        list.addAll(data.list);
                        listCount = list.size();
                        getPagCount();
                        goNext();
                        break;
                }
                APPLog.e("listSize:"+list.size());
                lastFlag=orderFlag;
                index_tv.setText(currentpage+"/"+pagecount);
                error_body.setVisibility(View.GONE);
            }else {
                error_body.setVisibility(View.VISIBLE);
            }
        }
    };

    private void initTempList() {
        if (templist.size()>0)
            templist.clear();
        for (int i=0;i<(listCount>12? 12:listCount);i++){
            templist.add(list.get(i));
        }

    }

    private void getPagCount() {
        pagecount=data.count/12;
        if (data.count%12>0){
            pagecount++;
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        initView();
        templist=new ArrayList<>();
    }

    private void initView() {
        adapter=new HGridViewAdapter(this);
        hgv.setAdapter(adapter);
        hgv.setOnFlingListener(this);
        hgv.setOnItemClickListener(this);
        saleTop_tv.setOnClickListener(this);
        pubtimeTop_tv.setOnClickListener(this);
        priceTop_tv.setOnClickListener(this);
        next_iv.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {

        updataFlag(1);
        if(null==list||templist.size()==0)
            getServiceInf();
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

    private void goNext() {
        if (listCount>currentpage*12) {
            currentpage++;
            if (templist.size() != 0)
                templist.clear();
            for (int i = (currentpage - 1) * 12; i < (currentpage * 12 > listCount ? listCount : currentpage * 12); i++) {
                templist.add(list.get(i));
            }
            adapter.setData(templist);
            //courent_page_tv.setText(courrentPage + "/");
        } else {
            if (listCount<data.count){
                // TODO: 2016/11/10  请求下一100条数据
                reqpage++;
                APPLog.e("reqpage:"+reqpage);
                getNextPageRequest();
            }else
                ToastUtil("已经是最后一页!");
        }
        index_tv.setText(currentpage+"/"+pagecount);
    }

    private void getNextPageRequest() {
        BaseMap pramers=new BaseMap();
        pramers.put("pageIndex",reqpage);
        pramers.put("pageSize",60);
        pramers.put("bookName","");
        pramers.put("orderCol",orderCol);
        pramers.put("order",ord);
        SoapRequest request=new SoapRequest(handler,sut,this,2, Config.ACTION);
        request.execute(pramers);
        showDialog("加载中...");
    }

    private void goPrevious() {
        if (currentpage>1){
            currentpage--;
            if (templist.size()!=0)
                templist.clear();
            for (int i=(currentpage-1)*12;i<currentpage*12;i++){
                templist.add(list.get(i));
            }
            adapter.setData(templist);
            //courent_page_tv.setText(courrentPage+"/");
        }else
            ToastUtil("已经是首页!");
        index_tv.setText(currentpage+"/"+pagecount);
    }
    @Override
    public void onRightFling() {
        goPrevious();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saleTop_tv:
                updataFlag(1);
                break;
            case R.id.priceTop_tv:
                if (3==orderFlag)
                    updataFlag(4);
                else
                    updataFlag(3);
                break;
            case R.id.pubtimeTop_tv:
                updataFlag(2);
                break;
            case R.id.next_iv:
                goNext();
                return;
            case R.id.back_iv:
                goPrevious();
                return;
        }
        getServiceInf();
    }
    private void updataFlag(int flag){
        clearOrdstate();
        orderFlag=flag;
        switch (orderFlag){//1:销量榜,2:最新榜,3:低价榜,4:高价榜
            case 1:
                orderCol=2;
                ord="desc";
                saleTop_tv.setBackgroundColor(Color.parseColor("#515151"));
                saleTop_tv.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                orderCol=0;
                ord="asc";
                pubtimeTop_tv.setBackgroundColor(Color.parseColor("#515151"));
                pubtimeTop_tv.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                orderCol=1;
                ord="asc";
                pricetop_iv.setVisibility(View.VISIBLE);
                break;
            case 4:
                orderCol=1;
                ord="desc";
                pricebottom_iv.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void clearOrdstate(){
        saleTop_tv.setBackgroundColor(Color.parseColor("#EEEEEE"));
        saleTop_tv.setTextColor(Color.parseColor("#000000"));
        pubtimeTop_tv.setBackgroundColor(Color.parseColor("#EEEEEE"));
        pubtimeTop_tv.setTextColor(Color.parseColor("#000000"));
        pricetop_iv.setVisibility(View.INVISIBLE);
        pricebottom_iv.setVisibility(View.INVISIBLE);
        currentpage=1;
        reqpage=1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it=new Intent(this, ResouceDetailActivity.class);
        it.putExtra("ebook",templist.get(position));
        startActivity(it);
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
}
