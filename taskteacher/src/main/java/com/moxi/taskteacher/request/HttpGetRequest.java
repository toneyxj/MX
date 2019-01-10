package com.moxi.taskteacher.request;

import android.content.Context;
import android.os.AsyncTask;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/17.
 */
public class HttpGetRequest extends AsyncTask<String, Void, String> {
    private RequestCallBack back;// 传入接口
    private List<ReuestKeyValues> valuePairs;// 请求参数
    private String code;// 请求返回码
    private String Url;// 请求URL地址;
    private final WeakReference<Context> context;// 上下文
    private boolean showFail = false;
    private boolean showHide;

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
     * @param showHide 是否显示提示
     */
    public HttpGetRequest(Context context, RequestCallBack back,
                          List<ReuestKeyValues> valuePairs, String code, String Url, boolean showHide) {
        if (valuePairs==null)valuePairs=new ArrayList<>();
        this.back = back;
        this.valuePairs = valuePairs;
        this.code = code;
        this.Url = Url;
        this.context = new WeakReference<>(context);
        this.showHide = showHide;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        OkHttpClient okHttpClient = new OkHttpClient();
        //添加数据请求url路径
        String RUrl = RequestUtils.getGetUrl(valuePairs, Url);
        APPLog.e("GET请求路径", RUrl);
        Request request = new Request.Builder()
                .url(RUrl)
                .build();
        Response response = null;
        try {
            response = okHttpClient
                    .newCall(request)
                    .execute();
            if (response.isSuccessful()) {
                result = response.body().string();
//                saveSession(response);
            } else {
                APPLog.e("Unexpected code " + response);
            }
        } catch (SocketTimeoutException e) {
            result = getString(R.string.request_overtime);
        } catch (IOException e) {
            result = "";
        }
        return result;

    }

    private String getString(int id) {
        if (getcontext() != null) {
            return getcontext().getString(id);
        }
        return "";
    }


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
        back.onFail(code, showFail, msgCode, msg);
        if (!msg.equals("") && !showFail && showHide && getcontext() != null) {
            ToastUtils.getInstance().showToastShort(msg);
        }
    }
}
