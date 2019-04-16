
package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.adapter.HGridViewAdapter;
import com.mx.cqbookstore.adapter.HGridboughtapter;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.Boughtbean;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.http.imageloader.GlideUtils;
import com.mx.cqbookstore.utils.ToolUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.SharePreferceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, View.OnTouchListener {
    @Bind(R.id.like_gv)
    GridView like_gv;
    @Bind(R.id.buy_gv)
    GridView buy_gv;
    @Bind(R.id.book_ico)
    ImageView book;
    @Bind(R.id.like_pb)
    ProgressBar like_pb;
    @Bind(R.id.bought_pb)
    ProgressBar bought_pb;
    @Bind(R.id.store_num_tv)
    TextView storeNum;
    @Bind(R.id.bought_num_tv)
    TextView boughtNum;


    @Bind(R.id.book_title_tv)
    TextView book_title_tv;
    @Bind(R.id.author_tv)
    TextView author_tv;
    @Bind(R.id.pubTime_tv)
    TextView pubTime_tv;
    @Bind(R.id.time_tv)
    TextView time_tv;
    @Bind(R.id.publischer_tv)
    TextView publischer_tv;
    @Bind(R.id.progress_tv)
    TextView progress_tv;
    @Bind(R.id.emty1_tv)
    TextView emty1_tv;
    @Bind(R.id.item_ly)
    LinearLayout item_ly;

    DBebookbean lastbooke;
    HGridViewAdapter gvAdapter1;
    HGridboughtapter gvAdapter2;
    List<EbookResouce> sblist;
    List<Boughtbean> bblist;
    List<DBebookbean> dBookList;
    int GridviewItemHeight;
    static final int FAVOR=1,BOUGHT=2;
    String userid;
    public static final int CARTL_RES=1;
    public static final int EBDETAIL_RES=2;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_my;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        sblist=new ArrayList<>();
        bblist=new ArrayList<>();
        userid= SharePreferceUtil.getInstance(this).getString("userid");
        like_gv.setOnItemClickListener(this);
        buy_gv.setOnItemClickListener(this);
        like_gv.setOnTouchListener(this);
        buy_gv.setOnTouchListener(this);

    }



    @Override
    public void onActivityStarted(Activity activity) {
        dBookList= DBEbookUtils.getDBBooks();
        initViewHy();
        //if (sblist.size()==0)
            getStoreBooks();
        //if (bblist.size()==0)
            getBoughtBooks();


    }

    private void initViewHy() {
        APPLog.e("dBookList:"+dBookList.size());
        if (null!=dBookList&0<dBookList.size()){
            lastbooke=dBookList.get(0);
            book_title_tv.setText(lastbooke.bookName);
            author_tv.setText("作者: "+lastbooke.author);
            pubTime_tv.setText("出版时间: "+ lastbooke.pubTime.split("T")[0]);
            time_tv.setText("下载时间: "+ToolUtils.getIntence().dateToStr1(lastbooke.downTime));
            publischer_tv.setText("出版社: "+lastbooke.publisher);
            progress_tv.setText(lastbooke.readprogress);
            GlideUtils.getInstance().loadGreyImage(this,book,lastbooke.bookCover);
            item_ly.setVisibility(View.VISIBLE);
            emty1_tv.setVisibility(View.GONE);
        }else
            emty1_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            book.measure(0,0);
            GridviewItemHeight=book.getHeight();
        }
    }
    private void getStoreBooks(){
        BaseMap pramers= new BaseMap();
        pramers.put("userid",userid);
        SoapRequest request=new SoapRequest(handler,sut,this,FAVOR, Config.ACTION_FAVORLIST);
        request.execute(pramers);

    }

    private void getBoughtBooks(){
        BaseMap pramers= new BaseMap();
        pramers.put("userId",userid);
        SoapRequest request=new SoapRequest(handler,sut,this,BOUGHT, Config.ACTION_BOUGHTRESOUCES);
        request.execute(pramers);

    }

    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS){
                switch (msg.arg1){
                    case FAVOR:
                        like_pb.setVisibility(View.GONE);
                        List<EbookResouce> list=jut.getResouces((String)msg.obj);
                        if (null!=list&&list.size()>0) {
                            sblist.clear();
                            for (EbookResouce book:list) {
                                sblist.add(book);
                            }
                            list.clear();
                            storeNum.setText("已收藏 (" + sblist.size() + " )");
                            if (4<sblist.size())
                                list=sblist.subList(0,4);
                            else
                                list=sblist;
                            gvAdapter1=new HGridViewAdapter(MyActivity.this);
                            gvAdapter1.setData(list);
                            like_gv.setAdapter(gvAdapter1);
                        }
                        break;
                    case BOUGHT:
                        bought_pb.setVisibility(View.GONE);
                        List<Boughtbean> listb=jut.preseBoughtList((String)msg.obj);
                        APPLog.e("boughtmsg:"+(String)msg.obj);
                        int size=0;
                        if (null!=listb)
                            size=listb.size();
                        boughtNum.setText("已购买 ( "+size+" )");
                        if (0<size) {
                            bblist.clear();
                            for (Boughtbean book : listb) {
                                bblist.add(book);
                            }
                            listb.clear();
                            if (4 < bblist.size())
                                listb = bblist.subList(0, 4);
                            else
                                listb = bblist;
                            gvAdapter2 = new HGridboughtapter(MyActivity.this);
                            gvAdapter2.setData(listb);
                            buy_gv.setAdapter(gvAdapter2);
                        }
                        break;
                }
            }
        }
    };

    /*HttpOnNextListener storeListener=new HttpOnNextListener<StoreUpData>() {
        @Override
        public void onNext(StoreUpData data) {
            hideDialog();
            List<StoreBook> list=data.getStoreUpList();
            int size=0;
            if (null!=list)
                size=list.size();
            storeNum.setText("已收藏 ( "+size+" )");
            if (0<size){
                sblist.clear();
                for (StoreBook book:list) {
                    sblist.add(book);
                }
                list.clear();
                if (4<sblist.size())
                    list=sblist.subList(0,4);
                else
                    list=sblist;
                gvAdapter1=new StoreGvAdapter(MyActivity.this,list,334);
                like_gv.setAdapter(gvAdapter1);
            }
        }

        @Override
        public void onError() {
            hideDialog();
        }
    };
    HttpOnNextListener boughtlistListener=new HttpOnNextListener<BoughtBookData>() {
        @Override
        public void onNext(BoughtBookData data) {
            hideDialog();
            List<MediaDetail> list=data.getMediaList();
            APPLog.e("bblist:"+list.size());
            int size=0;
            if (null!=list)
                size=list.size();
            boughtNum.setText("已购买 ( "+size+" )");
            if (0<size) {
                bblist.clear();
                for (MediaDetail book : list) {
                    bblist.add(book);
                }
                list.clear();
                if (4 < bblist.size())
                    list = bblist.subList(0, 4);
                else
                    list = bblist;
                gvAdapter2 = new BoughtGvAdapter(MyActivity.this, list, 334);
                buy_gv.setAdapter(gvAdapter2);
            }
        }

        @Override
        public void onError() {
            hideDialog();
        }
    };
*/
    @Override
    public void onActivityResumed(Activity activity) {
        dBookList= DBEbookUtils.getDBBooks();
        initViewHy();
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

    public void goBack(View v){
        finish();
    }
    public void cartList(View v) {
        /*Intent cartIt=new Intent();
        cartIt.setClass(this,CartActivity.class);
        startActivityForResult(cartIt,CARTL_RES);*/
    }
    public void goAcount(View v){
        /*Intent it=new Intent(this,AcountActivity.class);
        it.putExtra("token",token);
        startActivity(it);*/
    }
    /**
     * 继续阅读
     * @param v
     */
    public void keepRead(View v){
        // TODO: 2016/12/29 open 
        ToolUtils.getIntence().openEbook(this,lastbooke);
    }

    /**
     * 历史阅读
     * @param v
     */
    public void readHistory(View v){
        Intent intent=new Intent(this,MoreActivity.class);
        intent.putExtra("FLAG",MoreActivity.HISTORY);
        startActivity(intent);
    }

    public void goMoreBuyed(View v){
        Intent intent=new Intent(this,MoreActivity.class);
        intent.putExtra("FLAG",MoreActivity.BUY);
        intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) bblist);
        startActivity(intent);
    }

    public void goMoreLiked(View v){
        Intent intent=new Intent(this,MoreActivity.class);
        intent.putExtra("FLAG",MoreActivity.LIKE);
        intent.putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) sblist);
        startActivityForResult(intent,MyActivity.EBDETAIL_RES);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EbookResouce book=null;
        if (R.id.like_gv==parent.getId()){
            book=sblist.get(position);

        }else if (R.id.buy_gv==parent.getId()){
            Boughtbean bean=bblist.get(position);
            book=new EbookResouce();
            book.setID(bean.getBookID());
            book.setName(bean.getName());
        }

        Intent intent=new Intent(MyActivity.this,ResouceDetailActivity.class);
        intent.putExtra("ebook",book);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_MOVE)
            return true;
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      /* getStoreBooks();
       getBoughtBooks();*/
    }
}
