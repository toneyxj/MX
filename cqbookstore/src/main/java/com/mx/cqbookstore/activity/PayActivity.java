package com.mx.cqbookstore.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.R;
import com.mx.cqbookstore.http.BaseMap;
import com.mx.cqbookstore.http.Config;
import com.mx.cqbookstore.http.MyHandler;
import com.mx.cqbookstore.http.SoapRequest;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.cqbookstore.http.bean.Orderbean;
import com.mx.mxbase.constant.APPLog;

import butterknife.Bind;

public class PayActivity extends MyBaseActivity {
    @Bind(R.id.bookName_tv)
    TextView name;
    @Bind(R.id.orderid_tv)
    TextView orderid_tv;
    @Bind(R.id.price_tv)
    TextView price;
    @Bind(R.id.QR_code_iv)
    ImageView QR_code_iv;
    @Bind(R.id.timer_tv)
    TextView timer;
    @Bind(R.id.error_body)
    View error_body;
    @Bind(R.id.body_rl)
    RelativeLayout body;

    EbookResouce ebr;
    String userId;
    Orderbean order;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_pay;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ebr=getIntent().getParcelableExtra("ebook");
        userId=getIntent().getStringExtra("userId");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        getQRImg();
    }
    public void  doreflash(View v){ getQRImg();}
    public void goBack(View v){ finish();}
    public void QReflash(View v){
        QR_code_iv.setImageResource(R.mipmap.loading_ico);
        getQRImg();}
    private void getQRImg() {
        BaseMap pramers=new BaseMap();
        pramers.put("resourceid",ebr.getID());
        pramers.put("userid",userId);
        pramers.put("productDes",ebr.getName()+"(电子书)");
        SoapRequest request=new SoapRequest(handler,sut,this,1, Config.ACTION_PAY);
        request.execute(pramers);
        showDialog("加载中...");
    }

    MyHandler handler=new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            hideDialog();
            super.handleMessage(msg);
            if (msg.what==MyHandler.SUCEESS){
                APPLog.e("msg:"+(String)msg.obj);
                QR_code_iv.setClickable(false);
                order=jut.preseOrder((String)msg.obj);
                updateView();
                totaltime = 301;
                error_body.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                timeRun.run();

            }else {
                body.setVisibility(View.GONE);
                error_body.setVisibility(View.VISIBLE);
            }
        }
    };
    Bitmap bitmap;
    private void updateView() {
        name.setText(ebr.getName());
        price.setText("¥ "+ebr.getPrice());
        orderid_tv.setText(order.getOrderCode());
        byte[] decode = Base64.decode(order.getImg(),Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        APPLog.e("img:"+(null==bitmap));
        if (order.getLength()<300){
            QR_code_iv.setImageResource(R.mipmap.qr_error);
            QR_code_iv.setClickable(true);
        }else
            QR_code_iv.setImageBitmap(bitmap);

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        stoptimer=true;
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
        bitmap.recycle();
        bitmap=null;
    }
    int totaltime;
    Runnable timeRun=new Runnable() {
        @Override
        public void run() {
            if (totaltime>=0) {
                timehandle.sendEmptyMessageDelayed(3, 1500);
            }
        }
    };

    boolean stoptimer=false;
    Handler timehandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==3) {
                APPLog.e("totaltime:" + totaltime);
                if (totaltime > 0) {
                    if (!stoptimer) {
                        totaltime--;
                        timer.setText("时效: " + totaltime + "s");
                        timeRun.run();
                    } else
                        APPLog.e("stopTimer");
                } else {
                    APPLog.e("刷新");
                    body.setVisibility(View.GONE);
                    getQRImg();
                    timer.setText("");
                }
            }
            super.handleMessage(msg);
        }
    };
}
