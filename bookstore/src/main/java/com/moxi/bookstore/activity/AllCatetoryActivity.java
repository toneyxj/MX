package com.moxi.bookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

import com.moxi.bookstore.R;
import com.moxi.bookstore.adapter.CatetoryAdapter;
import com.moxi.bookstore.base.BookStoreBaseActivity;
import com.moxi.bookstore.bean.CatetoryChanle;
import com.moxi.bookstore.bean.CatetoryChanleItem;
import com.moxi.bookstore.bean.CatetoryData;
import com.moxi.bookstore.http.HttpManager;
import com.moxi.bookstore.http.deal.Subcatetorydeal;
import com.moxi.bookstore.http.listener.HttpOnNextListener;
import com.moxi.bookstore.http.subscribers.ProgressSubscriber;
import com.moxi.bookstore.interfacess.ChanelInterf;
import com.moxi.bookstore.request.json.Connector;
import com.moxi.bookstore.utils.ToolUtils;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;

public class AllCatetoryActivity extends BookStoreBaseActivity implements ChanelInterf {


    @Bind(R.id.catetory_list)
    ExpandableListView catetoryExpGrid;
    @Bind(R.id.error_body)
    View errorbody;

    CatetoryData catetoryData;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_all_catetory;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        errorbody.setVisibility(View.GONE);
        //屏蔽group点击
        catetoryExpGrid.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        //去掉箭头
        catetoryExpGrid.setGroupIndicator(null);
        catetoryExpGrid.setDivider(null);
        catetoryExpGrid.setCacheColorHint(0x000000);
        catetoryExpGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE)
                    return true;
                else
                    return false;
            }
        });

        if (null==catetoryData){
            getAllcatetory();
        }
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
    //请求分类数据
    public void getAllcatetory(){
        Subcatetorydeal catetorydeal=new Subcatetorydeal(new ProgressSubscriber(simpleOnNextListener,this));
        HttpManager manager=HttpManager.getInstance();
        manager.doHttpDeal(catetorydeal);
        showDialog("加载中...");
    }

    //   回调一一对应
    List<CatetoryChanle> data;
    HttpOnNextListener simpleOnNextListener = new HttpOnNextListener<CatetoryData>() {
        @Override
        public void onNext(CatetoryData subjects) {
            if (isfinish)return;
            hideDialog();
            catetoryData=subjects;
            data=subjects.getCatetoryList().get(0).getCatetoryList();
            data=reOrderData();
            catetoryExpGrid.setVisibility(View.VISIBLE);
            errorbody.setVisibility(View.GONE);
            catetoryExpGrid.setAdapter(new CatetoryAdapter(AllCatetoryActivity.this,data,AllCatetoryActivity.this));
            //默认展开
            for (int i=0;i<data.size();i++) {
                catetoryExpGrid.expandGroup(i);
            }

            APPLog.e("已封装：\n" + subjects.getCatetoryList().get(0).getName());
        }

        @Override
        public void onError() {
            if (isfinish)return;
            hideDialog();
            catetoryExpGrid.setVisibility(View.GONE);
            errorbody.setVisibility(View.VISIBLE);
        }

    };

    //finish activity
    public void goBack(View v){finish();}
    public void searchBook(View v){startActivity(new Intent(AllCatetoryActivity.this,SearchBookActivity.class));}
    public void doreflash(View v){getAllcatetory();}
    public void goMyActivity(View v){
        if (ToolUtils.getIntence().hasLogin(this)) {
            getDDUserInfor(true,true);
        }
    }
    @Override
    public void Success(String result, String code) {
        super.Success(result, code);
        if (code.equals(Connector.getInstance().getMonthlyChannelListNotify)){
            //获取到信息进入界面
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    @Override
    public void goChanelPage(String group, CatetoryChanleItem item) {
        Intent intent=new Intent(AllCatetoryActivity.this,ChanelActivity.class);
        intent.putExtra("group",group);
        intent.putExtra("chaneltitle",item.getName());
        intent.putExtra("chaneltype",item.getCode());
        startActivity(intent);
    }

    private List<CatetoryChanle> reOrderData(){

            List<CatetoryChanle> list = new ArrayList<>();
            list.add(data.get(4));
            list.add(data.get(6));
            list.add(data.get(5));
            list.add(data.get(2));
            list.add(data.get(1));
            list.add(data.get(0));
            CatetoryChanle sh=data.get(3);
            List<CatetoryChanleItem> itemList=sh.getCatetoryList();
            Iterator<CatetoryChanleItem> it= itemList.iterator();
            while (it.hasNext()){
                String code=it.next().getCode();
                if ("LXGX".equals(code))
                   it.remove();
                else if ("YCTJ".equals(code))
                    it.remove();
                else if ("YEZJ".equals(code))
                    it.remove();
                else if ("QZJY".equals(code))
                    it.remove();
            }

            list.add(sh);
            list.add(data.get(7));
            list.add(data.get(8));
            return list;

    }

}
