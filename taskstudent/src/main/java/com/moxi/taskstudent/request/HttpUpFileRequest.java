package com.moxi.taskstudent.request;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 多文件上传
 * Created by Administrator on 2016/2/17.
 */
public class HttpUpFileRequest extends AsyncTask<String, Void, String> {
    private UpFileCallBack back;// 传入接口
    private List<RequestKeyValues> valuePairs;// 请求参数
    private String code;// 请求返回码
    private String Url;// 请求URL地址;
    private String result = "";//数据请求返回值
    private List<RequestKeyValues> filePaths;
    private final WeakReference<Context> context;// 上下文
    private boolean showHitn;

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<HttpUpFileRequest> reference;

        public MyHandler(HttpUpFileRequest context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            HttpUpFileRequest activity = (HttpUpFileRequest) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        if (msg.what == 1) {
            if (back!=null)
            back.UpUnderway(code, (Integer) msg.obj);
        }
    }

    public Context getcontext() {
        final Context context = this.context.get();
        if (context == null) {
            return null;
        }
        return context;
    }

    /**
     * 请求构造方法
     *
     * @param context    上下文内容
     * @param back       接口用于得到返回值
     * @param valuePairs 请求参数
     * @param code       返回标记code
     * @param Url        请求链接
     * @param filePaths  文件集合
     * @param showHitn   是否显示错误提示
     */
    public HttpUpFileRequest(Context context, UpFileCallBack back,
                             List<RequestKeyValues> valuePairs, String code, String Url,
                             List<RequestKeyValues> filePaths, boolean showHitn) {
        if (valuePairs==null)valuePairs=new ArrayList<>();
        if (filePaths==null)filePaths=new ArrayList<>();
        this.back = back;
        this.valuePairs = valuePairs;
        this.code = code;
        this.Url = Url;
        this.context = new WeakReference<>(context);
        this.filePaths = filePaths;
        this.showHitn = showHitn;
    }

    private String getString(int id) {
        if (getcontext() != null) {
            return getcontext().getString(id);
        }
        return "";
    }

    @Override
    protected String doInBackground(String... arg0) {

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.MINUTES);
        okHttpClient.setReadTimeout(10, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(10, TimeUnit.MINUTES);

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        //添加请求参数值
        if (valuePairs != null && valuePairs.size() != 0) {
            for (int i = 0; i < valuePairs.size(); i++) {
                RequestKeyValues kv = valuePairs.get(i);
                Log.e(kv.key, kv.value);
                builder.addFormDataPart(kv.key, kv.value);
            }
        }
        //添加文件
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        for (RequestKeyValues path : filePaths) {
            Log.e(path.key, path.value);
            builder.addFormDataPart(path.key, path.value, RequestBody.create(MEDIA_TYPE_PNG, new File(path.value)));
        }
        RequestBody requestBody = builder.build();
        //添加数据请求url路径
        final Request request = new Request.Builder()
                .url(Url)
                .post(ProgressHelper.addProgressRequestListener(requestBody, progressRequestListener))
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (SocketTimeoutException e) {
            result = getString(R.string.request_overtime);
        } catch (IOException e) {
            result = "";
        }
        return result;

    }

    ProgressRequestBody.ProgressRequestListener progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
            if (!done) {
                int dowladSize = (int) ((100 * bytesWritten) / contentLength);

                Message message = new Message();
                message.what = 1;
                message.obj = dowladSize;
                handler.sendMessage(message);
            }
        }
    };

    @Override
    protected void onPostExecute(String result) {// 在doInBackground执行完成后系统会自动调用，result是返回值
        APPLog.e(Url+"：输出结果"+result);
        String msg = "";
        //如果应用已退出那么就直接返回不进行后续操作
        if (getcontext() == null) return;

        int msgCode = 0;
        if (result == null || result.trim().equals("")) {
            msg = getString(R.string.request_fail);
            msgCode = 0;
        } else if (result.equals(getString(R.string.request_overtime))) {
            msg = result;
            msgCode = 1;
        } else if (result.equals(getString(R.string.request_no_network))) {
            msg = result;
            msgCode = 2;
        }

        JSONObject object;
        try {
            object = new JSONObject(result);
            String status = object.getString("code");
            if (!status.equals("0")) {// 不等于1代表数据请求不成功
                msg = object.getString("msg");
                msgCode = 0;
            } else {
                if (back!=null)
                    back.onSuccess(result, code);// 请求成功接口
                return;
            }
        } catch (JSONException e) {
            if (msg.equals("")) {
                msg = getString(R.string.request_unknown);
                msgCode = 1;
            }
        }
        if (back!=null)
            back.onFail(code, false, msgCode, msg);
        if (!msg.equals("")  && getcontext() != null) {
            ToastUtils.getInstance().showToastShort(msg);
        }
    }

    public interface UpFileCallBack {
        /*
    *
    * 请求成功回调
    */
        public void onSuccess(String result, String code);

        /**
         * 请求失败回调
         *
         * @param code     请求标识
         * @param showFail 是否显示失败界面
         * @param failCode 失败原因；0数据请求失败，1未知错误，2网络未连接
         * @param msg      错误描述
         */
        public void onFail(String code, boolean showFail, int failCode, String msg);

        /**
         * 上传进度
         *
         * @param progress 文件下载进度默认进度按百分制
         */
        public void UpUnderway(String code, int progress);
    }
}

