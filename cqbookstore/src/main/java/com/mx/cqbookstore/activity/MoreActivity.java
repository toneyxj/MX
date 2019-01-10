package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.adapter.HGridViewAdapter;
import com.mx.cqbookstore.adapter.HGridboughtapter;
import com.mx.cqbookstore.adapter.HGridhistorydapter;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.bean.Boughtbean;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.interfaces.OnFlingListener;
import com.mx.cqbookstore.utils.ToolUtils;
import com.mx.cqbookstore.view.HSlidableGridView;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MoreActivity extends MyBaseActivity implements AdapterView.OnItemClickListener,
        OnFlingListener,View.OnClickListener {
    @Bind(R.id.books_gv)
    HSlidableGridView books_gv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.item_title)
    TextView item_title_tv;
    @Bind(R.id.item_ico)
    ImageView item_ico;
    @Bind(R.id.totle_page_tv)
    TextView totle_page_tv;
    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.next_page)
    ImageButton next_page;

    public static final int HISTORY=1;
    public static final int LIKE=2;
    public static final int BUY=3;

    private int flag,GridviewItemHeight;
    List<DBebookbean> historyList;
    List<EbookResouce> sblist;
    List<Boughtbean> bblist;
    HGridViewAdapter gvAdapter1;
    HGridboughtapter gvAdapter2;
    HGridhistorydapter gvAdapter3;
    List<Object> templist;
    int CurrentIndex=0,totalIndex;
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_more;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        flag=getIntent().getIntExtra("FLAG",1);
        books_gv.setOnItemClickListener(this);
        books_gv.setOnFlingListener(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        templist=new ArrayList<>();
        flagWithData();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            books_gv.measure(0,0);
            GridviewItemHeight=books_gv.getHeight();
        }
    }*/
    private void flagWithData() {
        String title="",item_title="";
        int ico=R.mipmap.history_ico;

        switch (flag){
            case HISTORY:
                historyList= DBEbookUtils.getDBBooks();
                title="阅读历史";
                item_title="已阅读("+historyList.size()+")";
                ico=R.mipmap.history_ico;
                initIndex(historyList);
                initTempData(historyList);
                showHistory(templist);
                break;
            case LIKE:
                sblist=getIntent().getParcelableArrayListExtra("DATA");
                title="我的收藏";
                item_title="已收藏("+sblist.size()+")";
                ico=R.mipmap.fivor_ico;
                APPLog.e("sblist:"+sblist.size());
                initIndex(sblist);
                initTempData(sblist);
                showStore(templist);
                break;
            case BUY:
                bblist=getIntent().getParcelableArrayListExtra("DATA");
                /*for (Boughtbean m:bblist){
                    APPLog.e(m.getTitle()+":"+m.getMediaId());
                }*/
                title="我的购买";
                item_title="已购买("+bblist.size()+")";
                ico=R.mipmap.bought_ico;
                APPLog.e("bblist:"+bblist.size());
                initIndex(bblist);
                initTempData(bblist);
                showBought(templist);
                break;
        }
        title_tv.setText(title);
        item_title_tv.setText(item_title);
        item_ico.setImageDrawable(getResources().getDrawable(ico));
        setShowText();
    }

    private void initTempData(List<?> list){
        if (0<templist.size())
            templist.clear();
        if (null==list||0==list.size()){
            // TODO: 2016/10/18 空数据处理
            return;
        }
        int size;
        if (list.size()>12){
            size=12;
        }else
            size=list.size();
        for (int i=0;i<size;i++){

            templist.add(list.get(i));
        }
    }

    private void updataTemp(List<?> list){
        int size=list.size();
        int cunt=(CurrentIndex+1)*12;
        if (0<templist.size())
            templist.clear();
        for (int i=CurrentIndex*12;i<(cunt>list.size()? size:cunt);i++ ){
            templist.add(list.get(i));
        }
    }
    @Override
    public void onActivityResumed(Activity activity) {
       freshAdapter();
    }

    private void freshAdapter() {
        switch (flag){
            case HISTORY:
                gvAdapter3.notifyDataSetChanged();
                break;
            case BUY:
                gvAdapter2.notifyDataSetChanged();
                break;
            case LIKE:
                gvAdapter1.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {setResult(MyActivity.EBDETAIL_RES);}

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {}

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {}

    public void goBack(View v){finish();}
    private void showHistory(List<?> list) {
        APPLog.e("list:"+list.size());
        gvAdapter3=new HGridhistorydapter(this);
        gvAdapter3.setData((List<DBebookbean>)list);
        books_gv.setAdapter(gvAdapter3);

    }

    private void showStore(List<?> list) {
        APPLog.e("list:"+list.size());
        gvAdapter1=new HGridViewAdapter(this);
        gvAdapter1.setData((List<EbookResouce>) list);
        books_gv.setAdapter(gvAdapter1);

    }
    private void showBought(List<?> list) {
        APPLog.e("list:"+list.size());
        gvAdapter2=new HGridboughtapter(this);
        gvAdapter2.setData((List<Boughtbean>) list);
        books_gv.setAdapter(gvAdapter2);

    }
    @Override
    public void onActivityDestroyed(Activity activity) {

    }
    private void initIndex(List list){
        final int  pageSize=12;
        //计算页数
        totalIndex = list.size() / pageSize;
        totalIndex += list.size() % pageSize == 0 ? 0 : 1;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (flag){
            case HISTORY:
                DBebookbean book=(DBebookbean) templist.get(position);
                /*StartUtils.OpenDDRead(this,book);*/
                ToolUtils.getIntence().openEbook(this,book);
                break;
            case LIKE:
                EbookResouce item=(EbookResouce) templist.get(position);
                Intent it=new Intent(MoreActivity.this,ResouceDetailActivity.class);
                it.putExtra("ebook",item);
                startActivity(it);
                /*StoreBook item=(StoreBook) templist.get(position);
                mediaId=item.getProductId();
                bookname=item.getBookName();
                intent=new Intent(this,EbookDetailActivity.class);
                intent.putExtra("saleId",mediaId);
                intent.putExtra("booktitle",bookname);
                APPLog.e("more price:"+item.getPrice());
                double price=0f;
                if (item.getPrice().equals("免费")){
                    price=0;
                }else
                    price=Double.valueOf(item.getPrice().replace("￥",""));
                intent.putExtra("lowestprice",price);
                intent.putExtra("saleprice",price);
                startActivityForResult(intent,MyActivity.EBDETAIL_RES);*/
                break;
            case BUY:
                Boughtbean bean=(Boughtbean) templist.get(position);
                EbookResouce er=new EbookResouce();
                er.setID(bean.getBookID());
                er.setName(bean.getName());
                er.setAuthor(bean.getAuthor());
                Intent itent=new Intent(MoreActivity.this,ResouceDetailActivity.class);
                itent.putExtra("ebook",er);
                startActivity(itent);
               /* MediaDetail item1=(MediaDetail) templist.get(position);
                mediaId=item1.getMediaId();
                bookname=item1.getTitle();
                intent=new Intent(this,EbookDetailActivity.class);
                intent.putExtra("saleId",mediaId);
                intent.putExtra("booktitle",bookname);
                intent.putExtra("FLAG",EbookDetailActivity.BOUGHT);
                startActivity(intent);*/
                break;
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last_page:
                onRightFling();
                break;
            case R.id.next_page:
                onLeftFling();
                break;
        }
    }

    @Override
    public void onLeftFling() {
        if (CurrentIndex >= totalIndex - 1) {
            return;
        } else {
            CurrentIndex++;
            setListData();
        }
    }

    @Override
    public void onRightFling() {
        if (CurrentIndex > 0 && (CurrentIndex <= totalIndex - 1)) {
            CurrentIndex--;
            setListData();
            // TODO: 2016/11/9 kk
            freshAdapter();
        }
    }
    private void setListData(){

        //计算当前页数
        if (CurrentIndex > totalIndex - 1) {
            CurrentIndex = totalIndex - 1;
        }
        if (CurrentIndex<0)CurrentIndex=0;
        if (totalIndex==0)totalIndex=1;

       switch (flag){
           case HISTORY:
               updataTemp(historyList);
               showHistory(templist);
               break;
           case LIKE:
               updataTemp(sblist);
               showStore(templist);
               break;
           case BUY:
              /* updataTemp(bblist);
               showBought(templist);*/
               break;
       }
        setShowText();
    }

    private void setShowText(){
        totle_page_tv.setText(String.valueOf(CurrentIndex + 1) + "/" + totalIndex);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_PAGE_UP){//上一页
            onRightFling();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_PAGE_DOWN){//下一页
            onLeftFling();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
