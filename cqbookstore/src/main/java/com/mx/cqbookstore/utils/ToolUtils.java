package com.mx.cqbookstore.utils;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.mx.cqbookstore.Base.MyBaseActivity;
import com.mx.cqbookstore.activity.LoginActivity;
import com.mx.cqbookstore.activity.ResouceDetailActivity;
import com.mx.cqbookstore.db.DBEbookUtils;
import com.mx.cqbookstore.http.bean.DBebookbean;
import com.mx.cqbookstore.http.bean.EbookResouce;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.model.CQFileModel;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.SharePreferceUtil;


import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/23.
 */
public class ToolUtils {
    static ToolUtils intence;

    public static ToolUtils getIntence(){
        if (null==intence){
            synchronized (ToolUtils.class){
                if (null==intence)
                    intence=new ToolUtils();
            }
        }
        return intence;
    }

    private ToolUtils(){}

    public String formatPrice(double d){
        int num=(int) (d*100)%10;
        String price;
        if (num==0){
            price=d+"0";
        }else
            price=d+"";
        return price;
    }

    //年月日
    public String dateToStr1(Long time){
        Date date=new Date(time);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        String str=(new SimpleDateFormat("yyyy-MM-dd")).format(date);
        return str;
    }

    public void showSoftInput(View view , Context ctx){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    public void hidSoftInput(EditText editText,Context ctx){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    Toast toast;
    public  void ToastUtil(Context context,String str){
        if (null==toast){
            toast=Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }





    public String getIMEINo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei!=null&&TextUtils.isEmpty(imei)) {
           return imei;
        } else
            return "null";
    }

    public String getDeviceNo(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //序列号（sn）
        String sn = tm.getSimSerialNumber();
        if (sn!=null&&TextUtils.isEmpty(sn)) {
            return sn;
        } else
            return "null";
    }

    public String getEbookProgress(String json){
        String progress="";
        try {
            APPLog.e("progress:"+json);
            JSONObject jsonObject=new JSONObject(json);
            progress=jsonObject.getString("readerProgress");
        }catch (Exception e){
            APPLog.e(e.toString());
        }
        return progress;
    }

    public void openEbook(MyBaseActivity ctx, DBebookbean dbook) {
        String id= SharePreferceUtil.getInstance(ctx).getString("userid");
        if (!id.equals("")) {
            CQFileModel fm = new CQFileModel();
            fm.resourceId = dbook.resouceId;
            fm.resurceKey = dbook.epubKey;
            fm.savePath = dbook.path;
            fm.userId=id;
            File file=new File(fm.savePath);
            if (file.exists()) {
                FileUtils.getInstance().openCQReader(ctx, fm);
            }else {
                DBEbookUtils.deletBook(dbook);
                ToastUtil(ctx,"书籍丢失,请重新下载");
                if (ctx instanceof ResouceDetailActivity){

                }else {
                    EbookResouce mer=new EbookResouce();
                    mer.setID(dbook.resouceId);
                    mer.setName(dbook.bookName);
                    mer.setAuthor(dbook.author);
                    Intent it=new Intent(ctx,ResouceDetailActivity.class);
                    it.putExtra("ebook",mer);
                    ctx.startActivity(it);
                }
            }
        }else {
           ctx.startActivity(new Intent(ctx,LoginActivity.class));
        }
    }


}
