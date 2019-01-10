package com.mx.home.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.AppUtil;
import com.mx.mxbase.utils.Log;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Archer on 16/9/1.
 */
public class AsyncCheckVersion extends AsyncTask<Void, Void, String> {

    private Context context;
    private String test;

    public AsyncCheckVersion(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HashMap<String, String> check = new HashMap<>();
        check.put("version", AppUtil.getPackageInfo(context).versionName);
        OkHttpUtils.getInstance().post().url(Constant.CHECK_VERSION).addParams("version", AppUtil.getPackageInfo(context).versionName).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                test = e.toString();
                Log.e("onError ", "-----" + test + "");
            }

            @Override
            public void onResponse(String response, int id) {
                test = response;
                Log.e("onResponse", "-----" + test + "");
            }
        });
        return test;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}