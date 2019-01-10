package com.moxi.taskteacher.request;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.moxi.taskteacher.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/2/25.
 */
public class HttpDowladFiles extends AsyncTask<String, Void, String> {
    private DowloadCallBack back;// 传入接口
    private String dowloadUrl;// 请求URL地址;
    private String backUrl;// 返回保存Sd卡地址
    private WeakReference<Context> context;// 上下文
    private String code;
    private boolean showHint;
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<HttpDowladFiles> reference;

        public MyHandler(HttpDowladFiles context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            HttpDowladFiles activity = (HttpDowladFiles) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }else {

            }
        }
    }

    public String getBackUrl() {
        return backUrl;
    }

    private void handleMessage(Message msg) {
        if (msg.what == 1) {
            if (back!=null)
            back.downloadUnderway(code,(Integer) msg.obj,this);
        }
    }

    /**
     * 请求构造方法
     *  @param context    上下文内容
     * @param back       下载完成回调
     * @param dowloadUrl 请求链接
     * @param backUrl    返回保存Sd卡地址
     * @param showHint   是否显示下载失败提示
     */
    public HttpDowladFiles(Context context, String code, DowloadCallBack back, String dowloadUrl, String backUrl, boolean showHint) {
        this.back = back;
        this.dowloadUrl = dowloadUrl;
        this.context = new WeakReference<>(context);
        this.backUrl = backUrl;
        this.showHint = showHint;
        this.code=code;
    }
    @Override
    protected String doInBackground(String... arg0) {
        String result = "";//返回值
        APPLog.e("文件下载路径="+dowloadUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.MINUTES);
        okHttpClient.setReadTimeout(10, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(10, TimeUnit.MINUTES);
        //添加数据请求url路径
        final Request request = new Request.Builder()
                .url(dowloadUrl)
                .build();
        //包装Response使其支持进度回调
        Response response = null;
        try {
            response = ProgressHelper.addProgressResponseListener(okHttpClient, listener).newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                result = "下载成功";
                result = writeFile(backUrl, input, result);
            }
        } catch (IOException e) {
            result = "";
        }
        return result;

    }

    ProgressResponseBody.ProgressResponseListener listener = new ProgressResponseBody.ProgressResponseListener() {
        @Override
        public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
            if (!done) {
                if (context.get()==null){
                    HttpDowladFiles.this.cancel(true);
                    return;
                }
                int dowladSize = (int) ((100 * bytesRead) / contentLength);
                Message message = new Message();
                message.what = 1;
                message.obj = dowladSize;
                handler.sendMessage(message);
            }
        }
    };


    private String writeFile(String savePath, InputStream input, String result) {
        File file = null;
        OutputStream output = null;
        try {
            file = creatSDFile(savePath);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (((len = input.read(buffer)) != -1)) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (IOException e) {
            result = "下载失败";
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                result = "下载失败";
            }
        }
        return result;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    private File creatSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            //删除以前的文件
            deleteFile(file);
        }
        file.createNewFile();
        return file;
    }

    // 将SD卡文件删除
    public void deleteFile(File file) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory()) {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {// 在doInBackground执行完成后系统会自动调用，result是返回值
        String msg = result;
        boolean is = true;
        if (result == null || result.trim().equals("")) {
            msg = "哦哦！下载失败";
            is = false;
        } else if (result.equals("网络未连接")) {
            msg = "网络未连接！请检查网络";
            is = false;
        }else if (result.equals("下载失败")){
            msg = "下载失败";
            is = false;
        }
        if (back!=null)
        back.dowladSucess(code,is, backUrl);

        if (showHint)
            ToastUtils.getInstance().showToastShort(msg);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public interface DowloadCallBack {
        /*
         *
         * 请求成功回调
         */
        public void dowladSucess(String code, boolean is, String resultPath);

//        /**
//         * 取消下载
//         */
//        public void stopDowlad(HttpDowladFiles dowlad, String resultPath);

        /**
         * 下载中进度
         *
         * @param progress 文件下载进度默认进度按百分制
         */
        public void downloadUnderway(String code, int progress, HttpDowladFiles dowladFiles);
    }
}
