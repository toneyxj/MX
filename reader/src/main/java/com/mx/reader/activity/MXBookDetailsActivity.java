package com.mx.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.http.LoadAsyncTask;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.ImageLoaderManager;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.AlertDialog;
import com.mx.reader.R;
import com.mx.reader.view.MXDownloadBookDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import butterknife.Bind;

import com.mx.reader.model.NetBookDetailsModel;

import java.io.File;

/**
 * Created by Archer on 16/8/3.
 */
public class MXBookDetailsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_book_details_back)
    LinearLayout llBack;
    @Bind(R.id.tv_book_details_author)
    TextView tvAuthor;
    @Bind(R.id.tv_book_details_buy)
    TextView tvBuy;
    @Bind(R.id.tv_book_details_download)
    TextView tvDownload;
    @Bind(R.id.tv_book_details_name)
    TextView tvName;
    @Bind(R.id.tv_book_details_original_author)
    TextView tvOriginalAuthor;
    @Bind(R.id.tv_book_details_price)
    TextView tvPrice;
    @Bind(R.id.tv_book_details_type_name)
    TextView tvTypeName;
    @Bind(R.id.tv_book_details_desc)
    TextView tvDesc;
    @Bind(R.id.img_book_details_cover)
    ImageView imgCover;

    private String txtFilePath = "";

    private NetBookDetailsModel.NetBookDetails details;

    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 200:
                tvDownload.setText("点击下载");
                break;
            case 201:
                tvDownload.setText("正在下载");
                break;
            default:
                break;
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_book_details;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //设置监听事件
        llBack.setOnClickListener(this);
        //获取传递数据
        Intent preIntent = this.getIntent();
        NetBookDetailsModel netBookDetailsModel = (NetBookDetailsModel) preIntent.getSerializableExtra("net_book_details");
        try {
            details = netBookDetailsModel.getResult();
            setViewValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkFile(){
        String path = Environment.getExternalStorageDirectory() + Constant.FILE_PATH + details.getName() + details.getSaveFile().
                substring(details.getSaveFile().lastIndexOf("."));
        File file = new File(path);
        if (file.exists()){
            txtFilePath = path;
            return true;
        }
        return false;
    }

    private void setViewValues() {
        boolean fileIsExists = checkFile();
        if (fileIsExists){
            tvDownload.setText("点击打开");
        }
        tvAuthor.setText(details.getAuthor());
        tvName.setText(details.getName());
        tvOriginalAuthor.setText("原作者：" + details.getOriginalName());
        tvPrice.setText("价格：" + details.getPrice());
        tvTypeName.setText("分类：" + details.getBookTypeName());
        ImageLoaderManager.getInstance().loadImageUrl(imgCover, details.getCoverImage());
        tvDesc.setText(details.getDesc());
        //设置点击事件
        tvBuy.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
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

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_book_details_back:
                this.finish();
                break;
            case R.id.tv_book_details_download:
//                new LoadAsyncTask(Environment.getExternalStorageDirectory() + Constant.FILE_PATH, details.getName() + details.getSaveFile().
//                        substring(details.getSaveFile().lastIndexOf(".")), getHandler()).execute(details.getSaveFile());
                if (tvDownload.getText().toString().equals("点击打开") && !txtFilePath.equals("")){
                    FileUtils.getInstance().openFile(this,new File(txtFilePath));
                }else{
                    MXDownloadBookDialog mxDownloadBookDialog = new MXDownloadBookDialog(this,details,downloadCall);
                    mxDownloadBookDialog.show();
                }

                break;
            case R.id.tv_book_details_buy:
                new AlertDialog(this).builder().setCancelable(false).setTitle("手机扫描购买：" +
                        details.getName()).setImg(Constant.BOOK_PAY_QRCODE + details.getId()).show();
                break;
            default:
                break;
        }
    }

    DownloadCallBack downloadCall = new DownloadCallBack() {
        @Override
        public void onsuccess(String filePath) {
            Log.d("class","callback filePath==>" + filePath);
            txtFilePath = filePath;
            tvDownload.setText("点击打开");
        }
    };

    public interface DownloadCallBack{
        public void onsuccess(String filePath);
    }

}
