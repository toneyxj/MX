package com.moxi.updateapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import com.moxi.updateapp.utils.FileUtil;
import com.moxi.updateapp.view.ProgressView;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.AppUtil;
import com.mx.mxbase.utils.Toastor;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import soft.com.updateapp.R;

/**
 * Created by zhengdelong on 2016/10/18.
 */

public class MXDownloadActivity extends Activity {

    FinalHttp fh = new FinalHttp();
    public static String HTTP_HOST = Constant.HTTP_HOST;

    private ProgressView progess;
    int po = 0;

    int downloadSize = 0;

    List<MXUpdateModel> mxUpdateModelList;

    boolean isRun = true;

    List<String> path = new ArrayList<>();

    private String uninstallPackageName = "";

    List<String> unpackage = new ArrayList<>();

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, getClass()
                .getCanonicalName());
        if (null != wakeLock) {
            wakeLock.acquire();
        }

        setContentView(R.layout.activity_download);

        mxUpdateModelList = (List<MXUpdateModel>) getIntent().getSerializableExtra("down");
        progess = (ProgressView) findViewById(R.id.progess);
        Log.d("app", "mxUpdateModelList size===>" + mxUpdateModelList.size());
        if (mxUpdateModelList != null) {
            new Thread(new mThread()).start();
            // TODO: 2016/10/19 检查是否含有需要先卸载的app
            for (int j = 0; j < mxUpdateModelList.size(); j++) {
                MXUpdateModel mx = mxUpdateModelList.get(j);
                if (mx.getUpdateType() == 1) {
                    // TODO: 2016/10/19 必须卸载
                    unpackage.add(mx.getPackageName());
                }
            }
            StringBuffer sb = new StringBuffer();
            for (int k = 0; k < unpackage.size(); k++) {
                if (k == 0) {
                    sb.append(unpackage.get(k));
                } else {
                    sb.append(";" + unpackage.get(k));
                }
            }
            if (unpackage.size() > 0) {
                uninstallPackageName = sb.toString();
            } else {
                uninstallPackageName = "";
            }
            //下载
            for (int i = 0; i < mxUpdateModelList.size(); i++) {
                MXUpdateModel mxUpdateModel = mxUpdateModelList.get(i);
                mxUpdateModel.setApkLocalPath(initApkPath());
                downLoadApk(mxUpdateModel);
            }
        } else {
            finish();
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int currentMsg = msg.what;
            MXDownloadActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progess.setMaxNumber(100);
                    progess.setCurNumber(currentMsg);
                }
            });
        }
    };

    class mThread implements Runnable {

        int msgWgat = 0;

        @Override
        public void run() {


            try{
                while(isRun){
                    Thread.sleep(3000);
                    if (msgWgat == 100){
                        msgWgat = 0;
                    }
                    msgWgat = msgWgat + 10;
                    handler.sendEmptyMessage(msgWgat);
                }
            }catch (Exception e){

            }
        }
    }

    private String initApkPath(){
        String DOWN_SDK_DIR_NAME = "update";
        String DOWN_SDK_DIR =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + DOWN_SDK_DIR_NAME + File.separator;

        long time = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHHmmssSSS");
        String plugnName = simpleDateFormat.format(time) + ".apk";

        File file = new File(DOWN_SDK_DIR);
        if (!file.isDirectory() && !file.exists()) {
            file.mkdirs();
        }
        String apkPath = DOWN_SDK_DIR + plugnName;
        return apkPath;
    }

    private void downLoadApk(final MXUpdateModel mxUpdateModel) {

        HttpHandler handler = fh.download(mxUpdateModel.getDownloadUrl(), mxUpdateModel.getApkLocalPath(), new AjaxCallBack<File>() {
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
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(File file) {
                super.onSuccess(file);
                String s = file.getAbsoluteFile().toString();
                String md5 = "";
                try {
                    md5 = getFileMD5(new File(s));
                }catch (Exception e){
                    Toastor.showLongToast(MXDownloadActivity.this,"文件下载异常，请稍后重试,错误码：6");
                }

                if (md5 == null){
                    MXDownloadActivity.this.finish();
                    Toastor.showLongToast(MXDownloadActivity.this,"文件下载异常，请稍后重试,错误码：5");
                    return;
                }else{
                    Log.i("cov", "local md5===>" + md5);
                }

                if ("".equals(md5)){
                    MXDownloadActivity.this.finish();
                    Toastor.showLongToast(MXDownloadActivity.this,"文件下载异常，请稍后重试,错误码：9");
                    return;
                }

                boolean isFileSucss = false;
                for (int k = 0;k<mxUpdateModelList.size();k++){
                    Log.i("cov", "server md5===>" + mxUpdateModelList.get(k).getMd5Str());
                    if (md5.equals(mxUpdateModelList.get(k).getMd5Str())){
                        isFileSucss = true;
                        break;
                    }
                }
                if (isFileSucss){
                    downloadSize++;
                    path.add(s);
                    if (downloadSize == mxUpdateModelList.size()) {
                        // TODO: 2016/10/18 下载完成开始安装
                        isRun = false;
                        Log.i("cov", "onSuccess");
                        StringBuffer sb = new StringBuffer();
                        String luncherPath = "";
                        for (int m = 0;m<mxUpdateModelList.size();m++){
                            MXUpdateModel mxUpdateModel1 = mxUpdateModelList.get(m);
                            String localPath = mxUpdateModel1.getApkLocalPath();
                            int isLuncher = mxUpdateModel1.getIsLancher();

                            if (m == 0){
                                if (isLuncher == 1){
                                    Log.d("cov", "ap22==>" + 1);
                                    luncherPath = localPath;
                                }else{
                                    Log.d("cov", "ap22==>" + 2);
                                    sb.append(localPath);
                                }

                            }else{
                                if (isLuncher == 1){
                                    Log.d("cov", "ap22==>" + 3);
                                    luncherPath = localPath;
                                }else{
                                    Log.d("cov", "ap22==>" + 4);
                                    if(!sb.toString().equals("")){
                                        sb.append(";" + localPath);
                                    }else{
                                        sb.append(localPath);
                                    }

                                }

                            }
                        }

                        String ap = sb.toString();
                        Log.d("cov", "ap==>" + ap);
                        Log.d("cov", "luncherPath==>" + luncherPath);
                        reInstallApp(ap, luncherPath,MXDownloadActivity.this.getIntent().getIntExtra("install_flag", 3));
                    }
                }else{
                    MXDownloadActivity.this.finish();
                    Toastor.showLongToast(MXDownloadActivity.this,"文件下载异常，请稍后重试,错误码：-2");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                MXDownloadActivity.this.finish();
                Toastor.showLongToast(MXDownloadActivity.this,"文件下载异常，请稍后重试,错误码：-3");
                super.onFailure(t, errorNo, strMsg);

            }
        });

        //调用stop()方法停止下载
//        handler.stop();

    }



    private void reInstallApp(String apkPath,String luncherPath, int flag) {
        Log.d("app", "path==>" + apkPath);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("soft.com.update", "soft.com.update.AppManager");
        intent.setComponent(cn);
        intent.setFlags(flag);
        String tempVersion = FileUtil.getCode();
        if (tempVersion.equals("")) {
            tempVersion = AppUtil.getPackageInfo(this).versionName;
        }
        intent.putExtra("versionName", tempVersion);
        APPLog.e("传递过去的版本号：", "flag = " + flag + "  versionName=" + tempVersion);
        intent.putExtra("install", apkPath);
        intent.putExtra("uninstall", uninstallPackageName);
        intent.putExtra("luncher", luncherPath);//luncher 文件路径

        startActivity(intent);
        this.finish();
    }

    /**
     * get file md5
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (wakeLock != null){
            wakeLock.release();
        }
        super.onDestroy();
        isRun = false;
    }
}
