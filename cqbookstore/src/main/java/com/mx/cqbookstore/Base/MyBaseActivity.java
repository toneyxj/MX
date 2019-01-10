package com.mx.cqbookstore.Base;

import android.os.Bundle;
import android.widget.Toast;

import com.mx.cqbookstore.MyApplication;
import com.mx.cqbookstore.http.JsonUtil;
import com.mx.cqbookstore.http.SoapUtil;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.dialog.HitnDialog;
import com.mx.mxbase.utils.SharePreferceUtil;

/**
 * Created by Administrator on 2016/12/21.
 */

public abstract class MyBaseActivity extends BaseActivity {
    Toast toast;
    public JsonUtil jut;
    public SoapUtil sut;
    public HitnDialog dialog;
    public boolean isfinish=false;

    public  void ToastUtil(String str){
        if (null==toast){
            toast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jut=MyApplication.getMyApplication().getMjsonUtil();
        sut=MyApplication.getMyApplication().getMsoapUtil();
    }

    public boolean isLogin(){
        String id= SharePreferceUtil.getInstance(this).getString("userid");
        return !id.equals("");
    }

    public void hideDialog() {

        if (dialog != null && dialog.isShowing() ) {
            dialog.dismiss();
        }
        dialog = null;
    }
    /**
     * 显示dialog
     */
    public void showDialog(String content) {
        if (visibale) {
            if (dialog == null) {
                dialog = dialogShowOrHide(true, content);
            }
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public boolean visibale=true;
    @Override
    protected void onStop() {
        visibale=false;
        super.onStop();
    }

    @Override
    protected void onPause() {
        visibale=false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        visibale=true;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isfinish=true;
        hideDialog();
    }
}
