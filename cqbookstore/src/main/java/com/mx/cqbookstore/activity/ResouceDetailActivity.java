package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.bean.EbookDetail;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.http.bean.EbookWithOtherInf;
import com.mx.cqbookstore.http.imageloader.GlideUtils;
import com.mx.cqbookstore.utils.ToolUtils;
import com.mx.cqbookstore.view.ProgressArcView;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.http.LoadAsyncTask;
import com.mx.mxbase.utils.SharePreferceUtil;

import butterknife.Bind;

public class ResouceDetailActivity extends MyBaseActivity {
    @Bind(R.id.error_body)
    View error_body;
    @Bind(R.id.body_ll)
    LinearLayout body_ll;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.book_title_tv)
    TextView book_title_tv;
    @Bind(R.id.author_tv)
    TextView author_tv;
    @Bind(R.id.isbn_tv)
    TextView isbn_tv;
    @Bind(R.id.publischer_tv)
    TextView publischer_tv;
    @Bind(R.id.pubTime_tv)
    TextView pubTime_tv;
    @Bind(R.id.low_price_tv)
    TextView low_price_tv;
    @Bind(R.id.like_tv)
    TextView like_tv;
    @Bind(R.id.addcarlist_tv)
    TextView addcarlist_tv;
    @Bind(R.id.btn2_tv)
    TextView buy;
    @Bind(R.id.btn1_tv)
    TextView try_tv;
    @Bind(R.id.book_ico)
    ImageView book_ico;
    @Bind(R.id.abstract_tv)
    TextView probtion_tv;
    @Bind(R.id.probtion_pd)
    ProgressBar probtion_pd;
    @Bind(R.id.dprogress_pa)
    ProgressArcView dprogress_pa;

    EbookResouce ebr;
    EbookWithOtherInf ewo;
    EbookDetail med;
    DBebookbean dbook,book;
    String probtion;
   public static final int DETARQ=1,ADDF=2,GETKEY=3,PREDOWN=4,CANCELF=5,PROBTION=6;
    String userid;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_resouce_detail;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
       ebr=getIntent().getParcelableExtra("ebook");
       title.setText(ebr.getName());
        APPLog.e(ebr.getName()+":"+ebr.getAuthor());
    }

    public void doreflash(View v){
        getDetailServerInf();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        userid= SharePreferceUtil.getInstance(this).getString("userid");
        getDetailServerInf();


    }

    private void getResouceKey() {
        BaseMap pramers=new BaseMap();
        pramers.put("userId",userid);
        pramers.put("resourceId",ebr.getID());
        pramers.put("key","RXB1YlNlcnZpY2U=");
        SoapRequest request=new SoapRequest(handler,sut,this,GETKEY, Config.ACTION_RESOUCEKEY);
        request.execute(pramers);
        showDialog("加载中...");
    }

    private void getDetailServerInf() {
        BaseMap pramers=new BaseMap();
        pramers.put("userid",userid);
        pramers.put("resourceid",ebr.getID());
        SoapRequest request=new SoapRequest(handler,sut,this,DETARQ, Config.ACTION_RESOUCEDETAIL);
        request.execute(pramers);
        showDialog("加载中...");

    }

    private void doAddFavor(){
        BaseMap pramers=new BaseMap();
        pramers.put("userid",userid);
        pramers.put("resourceid",ebr.getID());
        SoapRequest request;
        if (med.getIfCollect()==0)
            request=new SoapRequest(handler,sut,this,ADDF, Config.ACTION_ADDLIKE);
        else
            request=new SoapRequest(handler,sut,this,CANCELF, Config.ACTION_CANCLELIKE);
        request.execute(pramers);
        showDialog("加载中...");
    }
    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS){
                switch (msg.arg1){
                    case DETARQ:
                        error_body.setVisibility(View.GONE);
                        APPLog.e("msg:"+(String)msg.obj);
                        med=jut.getEbDetial((String) msg.obj);
                        body_ll.setVisibility(View.VISIBLE);
                        initDbook();
                        getProbtion();
                        break;
                    case ADDF:
                        ToastUtil("收藏成功");
                        med.setIfCollect(1);
                        break;
                    case CANCELF:
                        ToastUtil("取消收藏");
                        med.setIfCollect(0);
                        break;
                    case PREDOWN:
                        String url=(String)msg.obj;
                        APPLog.e("下载地址:"+url);
                        dbook.downUrl=url;
                        dprogress_pa.setVisibility(View.VISIBLE);
                        goDownLoad(url);
                        setBtnsClickable(false);
                        break;
                    case GETKEY://资源key
                        APPLog.e("key:"+(String)msg.obj);
                        //ewo=jut.preseEWO((String)msg.obj);
                        dbook.epubKey=(String)msg.obj;
                        break;
                    case PROBTION:
                        probtion=(String)msg.obj;
                        probtion_tv.setText(probtion.substring(1,300)+"...");
                        probtion_tv.setVisibility(View.VISIBLE);
                        probtion_pd.setVisibility(View.GONE);

                        break;
                }
                if (visibale)
                    updataView();
            }else {
                if (msg.arg1==DETARQ){
                error_body.setVisibility(View.VISIBLE);
                body_ll.setVisibility(View.GONE);
                }else if (msg.arg1==PROBTION){
                    probtion_pd.setVisibility(View.GONE);
                    probtion_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    };


    Handler downhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==200){
                ToastUtil("下载完成");
                dbook.path=(String) msg.obj;
                dprogress_pa.setVisibility(View.GONE);
                saveToDB();
                buy.setText("阅读");
                setBtnsClickable(true);
            }else if (msg.what==201){
                String str=(String) msg.obj;
                dprogress_pa.initsetNumber(100,Integer.valueOf(str.replace("%","")));
                APPLog.e("下载进度"+Integer.valueOf(str.replace("%","")));
            }else if(msg.what==404){
                dprogress_pa.setVisibility(View.GONE);
                ToastUtil((String)msg.obj);
                setBtnsClickable(true);
            }
        }
    };
    private void goDownLoad(String url) {
        if (url.contains("\"")){
            url=url.replace("\"","");
        }
        LoadAsyncTask dloadTask=new LoadAsyncTask(Config.DOWNPATH,ebr.getName()+".epub",downhandler);
        dloadTask.execute(url);
        showDialog("正在下载...");
    }

    private void initDbook() {
        book=DBEbookUtils.selectBook(ebr.getID());
        if (null==book) {
            dbook = new DBebookbean();
            dbook.bookName = ebr.getName();
            dbook.author = ebr.getAuthor();
            dbook.resouceId = ebr.getID();
            dbook.bookCover = med.getCoverPath();
            if (med.getIsPay() == 1 || med.getPrice() == 0)
                dbook.isBought = 1;
            else
                dbook.isBought = 0;
            dbook.publisher = med.getPublisher();
            dbook.pubTime = med.getPublishDate();
        }else
            dbook=book;
    }

    private boolean hasDbook(){


        return null!=book;
    }
    private void saveToDB(){
        dbook.downTime=System.currentTimeMillis();
        DBEbookUtils.addBook(dbook);
        APPLog.e("save DB"+DBEbookUtils.getDBBooks().size());
        ToastUtil("下载成功");

        book=DBEbookUtils.selectBook(med.getID());
    }
    private void updataView() {

        book_title_tv.setText(med.getName());
        author_tv.setText("作者:"+med.getAuthor());
        isbn_tv.setText("ISBN:"+med.getISBN());
        publischer_tv.setText("出版社:"+med.getPublisher());
        pubTime_tv.setText("出版年月:"+med.getPublishDate().split("T")[0]);
        low_price_tv.setText("¥:"+med.getPrice());
        GlideUtils.getInstance().loadGreyImage(this,book_ico,ebr.getCoverPath());
        String like="收藏";
        if (med.getIfCollect()==1) {
            like_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.fivor_ico), null, null, null);
            like="已收藏";
        }else {
            like_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favor_nomal), null, null, null);
        }
        like_tv.setText(like);
        if (med.getIsPay()==1){//已购买
            addcarlist_tv.setVisibility(View.INVISIBLE);
            if (hasDbook())
                buy.setText("阅读");
            else
                buy.setText("下载");
            try_tv.setVisibility(View.GONE);
        }else {
            addcarlist_tv.setText("加入购物车");
            buy.setText("购买");
            try_tv.setVisibility(View.VISIBLE);
        }
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
    public void goBack(View v){ finish();}
    public void cartList(View v){
        ToastUtil("购物车");
    }
    public void tryDownload(View v){ToastUtil("试读");
        // TODO: 2016/12/28 在线试读
        if (null!=probtion){
            Intent it=new Intent(ResouceDetailActivity.this,ProbtionActivity.class);
            it.putExtra("html",probtion);
            startActivity(it);
        }else {
            getProbtion();
        }

    }
    private void getProbtion(){
        BaseMap pramers=new BaseMap();
        pramers.put("userId",userid);
        pramers.put("resourceId",ebr.getID());
        SoapRequest request;
        request=new SoapRequest(handler,sut,this,PROBTION, Config.ACTION_PROBATION);
        request.execute(pramers);
        //showDialog("加载试读...");
    }
    private void openEbook() {

        ToolUtils.getIntence().openEbook(this,book);
    }

    public void allDownload(View v){ToastUtil("购买");
        book=DBEbookUtils.selectBook(ebr.getID());
        if ((med.getIsPay()==1||med.getPrice()==0)&&hasDbook()){
            openEbook();
            return;
        }
        if (!isLogin()){
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }
        if (null==book)
            buy.setText("下载");
        if (med.getPrice()==0||med.getIsPay()==1) {
                getResouceKey();
                getDownUrl();
            return;
        }
        if (med.getIsPay()!=1){
            goPay();
        }

    }
    public void addCarlist(View v){
        if (med.getIsPay()==1){
            ToastUtil("已购买,不能加入购物车");
            return;
        }
        ToastUtil("加入购物车");
    }
    public void addFavor(View v){ToastUtil("收藏");
        if (isLogin()){
            userid=SharePreferceUtil.getInstance(this).getString("userid");
            APPLog.e("userid:"+userid);
            doAddFavor();
        }else {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
    private void getDownUrl(){
        BaseMap pramers=new BaseMap();
        pramers.put("userId",userid);
        pramers.put("resourceId",ebr.getID());
        SoapRequest request;
        request=new SoapRequest(handler,sut,this,PREDOWN, Config.ACTION_GETRESOUCE_URL);
        request.execute(pramers);
        showDialog("准备下载...");
    }
    private void setBtnsClickable(boolean b){
        try_tv.setClickable(b);
        buy.setClickable(b);
        addcarlist_tv.setClickable(b);
        like_tv.setClickable(b);
    }
    private void goPay(){
        Intent it=new Intent(this,PayActivity.class);
        it.putExtra("ebook",ebr);
        it.putExtra("userId",userid);
        startActivity(it);
    }
}
