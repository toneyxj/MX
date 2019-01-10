package com.moxi.studentclient.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.classRoom.request.HttpGetRequest;
import com.moxi.classRoom.request.RequestCallBack;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.studentclient.R;
import com.moxi.studentclient.bean.RequsetMsg.ConnClassMsg;
import com.moxi.studentclient.config.Connector;
import com.moxi.studentclient.dbUtils.JsonAnalysis;
import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/11/7.
 */

public class ConnectStateView extends LinearLayout implements RequestCallBack{
    public final static int UNCONNECT=0;//未连接
    public final static int CONNECT=1;//连接上课堂
    public final static int INTERUPT=2;//连接中断
    Context context;
    ImageView iv;
    TextView tv;
    public int state=UNCONNECT;
    String className="英语课";
    boolean cancel=false;
    ProgressDialog pd;
    public ConnectStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }

    private void initView() {
        View v=View.inflate(context, R.layout.connect_state_view,this);
        iv=(ImageView)v.findViewById(R.id.connect_state_iv);
        tv=(TextView)v.findViewById(R.id.connect_state_tv);
        pd=new ProgressDialog(context);
        updata();
    }

    boolean first=true;
    HttpGetRequest pollrequest;
    public void startCheckState(){
        if (-1== NetWorkUtil.getConnectedType(context)){
            Toastor.getToast(context,"断网了").show();
            return;
        }
        List<ReuestKeyValues> values = new ArrayList<>();
        String url= Connector.getInstance().get_courrentClass;
        pollrequest=new HttpGetRequest(context,this,values,"code",url,false);
        pollrequest.execute();
        if (first){
            pd.show();
            first=false;
        }
    }

    public void setUpdate(String value){
        if("断网了".equals(value)){
            iv.setBackgroundResource(R.mipmap.unconnect_ico);
            tv.setText("未连接");
        }else if("未连接".equals(value)){
            iv.setBackgroundResource(R.mipmap.connect_interupt_ico);
            tv.setText("未连接");
        }else{
            iv.setBackgroundResource(R.mipmap.connecte_ico);
            tv.setText(value);
        }
    }

    private void updata(){
        switch (state){
            case UNCONNECT:
                iv.setBackgroundResource(R.mipmap.unconnect_ico);
                tv.setText("未连接");
                break;
            case CONNECT:
                iv.setBackgroundResource(R.mipmap.connecte_ico);
                tv.setText(className);
                break;
            case INTERUPT:
                iv.setBackgroundResource(R.mipmap.connect_interupt_ico);
                tv.setText(className);
                break;
        }
    }
    ConnClassMsg ccmsg;
    @Override
    public void onSuccess(String result, String code) {
        state=CONNECT;
        ccmsg=JsonAnalysis.getInstance().getConnState(result);
        className=ccmsg.getResult().getSubject().getName();
        if (pd.isShowing())
            pd.dismiss();
        delayhandler.sendEmptyMessageDelayed(1,3000);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        state=UNCONNECT;
        Toastor.getToast(context,msg).show();
        if (pd.isShowing())
            pd.dismiss();
        delayhandler.sendEmptyMessageDelayed(1,3000);
    }

    public void cancelPoll(){
        cancel=true;
    }

    Handler delayhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updata();
            if (!cancel)
               startCheckState();
        }
    };

}
