package com.mx.reader.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.Toastor;
import com.mx.reader.R;
import com.mx.reader.activity.MXBookDetailsActivity;
import com.mx.reader.activity.MXBookDetailsActivity.DownloadCallBack;
import com.mx.reader.model.NetBookDetailsModel;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;

/**
 * Created by Administrator on 2016/11/16.
 */

public class MXDownloadBookDialog extends Dialog implements View.OnClickListener {

    FinalHttp fh = new FinalHttp();
    Context context;

    ProgressView progess;
    Button but_click;
    TextView download_success_tv;
    RelativeLayout downloading_rel;

    NetBookDetailsModel.NetBookDetails details;
    MXBookDetailsActivity.DownloadCallBack downloadCallBack;

    String filePath = "";

    public MXDownloadBookDialog(Context context,NetBookDetailsModel.NetBookDetails details,MXBookDetailsActivity.DownloadCallBack downloadCallBack) {
        super(context,R.style.dialog);
        this.context=context;
        this.details = details;
        this.downloadCallBack = downloadCallBack;
    }

    HttpHandler handler = null;

    private void setData(){
        filePath = Environment.getExternalStorageDirectory() + Constant.FILE_PATH + details.getName() + details.getSaveFile().
                substring(details.getSaveFile().lastIndexOf("."));
        Log.d("book","book url ==>" + details.getSaveFile());
        Log.d("book","book filePath ==>" + filePath);
        handler = fh.download(details.getSaveFile(), filePath, new AjaxCallBack<File>() {
            @Override
            public boolean isProgress() {
                return super.isProgress();
            }

            @Override
            public int getRate() {
                return super.getRate();
            }

            @Override
            public AjaxCallBack<File> progress(boolean progress, int rate) {
                return super.progress(progress, rate);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onLoading(long count, long current) {
                Log.d("book","book downloading current==>" + current);
                Log.d("book","book downloading count==>" + count);
                progess.setMaxNumber(count);
                progess.setCurNumber(current);
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                progess.setMaxNumber(1);
                progess.setCurNumber(1);
                String s = file.getAbsoluteFile().toString();
                Log.d("book","book download success ==>" + s);
                downloadSuccessToSetView();

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.d("book","book download fail errorNo==>" + errorNo);
                Log.d("book","book download fail strMsg ==>" + strMsg);
            }
        });
    }

    private void downloadSuccessToSetView(){
        but_click.setText("确定");
        progess.setVisibility(View.GONE);
        downloading_rel.setVisibility(View.GONE);
        download_success_tv.setVisibility(View.VISIBLE);
        download_success_tv.setText("已下载至本地！\n请在\"我的图书馆\"中查找");
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mx_thebook_download, null);
        setContentView(view);
        downloading_rel = (RelativeLayout)view.findViewById(R.id.downloading_rel);
        download_success_tv = (TextView)view.findViewById(R.id.download_success_tv);
        progess = (ProgressView)view.findViewById(R.id.progess);
        but_click = (Button) view.findViewById(R.id.but_click);
        but_click.setOnClickListener(this);
        setData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        init();
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.but_click:
                 if (but_click.getText().toString().equals("取消")){
                     if (handler != null){
                         handler.stop();
                     }
                     this.dismiss();
                 }else{
                     downloadCallBack.onsuccess(filePath);
                     this.dismiss();
                 }

                 break;
         }
    }


}
