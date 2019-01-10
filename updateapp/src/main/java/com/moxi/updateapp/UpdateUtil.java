package com.moxi.updateapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhengdelong on 2016/10/19.
 */

public class UpdateUtil {

    public String CHECK_VERSION = "http://120.25.193.163/app/appversion/checkNew";
    WeakReference<Activity>  activity;
    Context context;

    public UpdateUtil(Activity activity, Context context) {
        this.activity = new WeakReference<Activity>(activity);
        this.context = context;
    }
    public Activity getActivity(){
        return activity.get();
    }

    public void startUpdate() {
        Log.d("app", "click update...");
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<MXAppInfo> mxAppInfos = new ArrayList<>();
        MXAppInfo mxAppInfo;
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packageName = packageInfo.packageName;
            if (packageName.startsWith("com.moxi")) {
                mxAppInfo = new MXAppInfo();
                mxAppInfo.setVersionName(Constant.CLENT_APP+packageInfo.versionName);
                mxAppInfo.setVersionCode(packageInfo.versionCode);
                mxAppInfo.setPackageName(packageInfo.packageName);
                mxAppInfos.add(mxAppInfo);
            }
        }
        checkUpdate(mxAppInfos);
    }

    private void checkUpdate(List<MXAppInfo> params) {
        try {
            JsonArray jsonEle = new JsonArray();
            JsonObject jsonObject;
            for (int i = 0; i < params.size(); i++) {
                jsonObject = new JsonObject();
                String packageName = params.get(i).getPackageName();
                int versionCode = params.get(i).getVersionCode();
                String versionName = params.get(i).getVersionName();
                jsonObject.addProperty("packageName", packageName);
//                   if (packageName.equals("com.moxi.bookstore")){
//                    jsonObject.addProperty("versionCode", 220);
//                    jsonObject.addProperty("versionName", "2.2.0");
//                }else {
//                    jsonObject.addProperty("versionCode", versionCode);
//                    jsonObject.addProperty("versionName", versionName);
//                }
                jsonObject.addProperty("versionCode", versionCode);
                jsonObject.addProperty("versionName", versionName);
                jsonEle.add(jsonObject);
            }

            final OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            Log.d("update", "param===>" + jsonEle.toString());
            okHttpUtils.post().url(CHECK_VERSION).addParams("update", jsonEle.toString())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("update", "data==>" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    if (getActivity()==null)return;
                    Log.d("update", "data==>" + response);
                    try {
                        JSONObject reObj = new JSONObject(response);
                        int code = reObj.optInt("code", -1);
                        if (code == 0) {
                            JSONArray jsonArray = reObj.optJSONArray("result");
                            final List<MXUpdateModel> mxUpdateModel = new ArrayList<MXUpdateModel>();
                            MXUpdateModel mxUpdateModel1;
                            final StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mxUpdateModel1 = new MXUpdateModel();
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                mxUpdateModel1.setAppDesc(jsonObject1.optString("appDesc"));
                                mxUpdateModel1.setDownloadUrl(jsonObject1.optString("downloadUrl"));
                                mxUpdateModel1.setPackageName(jsonObject1.optString("packageName"));
                                mxUpdateModel1.setVersionName(jsonObject1.optString("versionName"));
                                mxUpdateModel1.setMd5Str(jsonObject1.optString("md5"));
                                mxUpdateModel1.setIsLancher(jsonObject1.optInt("isLuncher"));
                                mxUpdateModel1.setVersionCode(jsonObject1.optInt("versionCode"));
                                mxUpdateModel1.setUpdateType(jsonObject1.optInt("updateType"));
                                mxUpdateModel.add(mxUpdateModel1);
                                stringBuffer.append(jsonObject1.optString("appDesc") + "\n");
                            }
                            if (mxUpdateModel.size() > 0) {
                                // TODO: 2016/10/14 有更新
//                                Intent intent = new Intent(getActivity(), MXUpdateActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("down", (Serializable) mxUpdateModel);
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);

                                new AlertDialog(getActivity()).builder().
                                        setTitle("更新提示").setMsg(stringBuffer.toString()).setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // TODO: 16/9/2
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), MXDownloadActivity.class);
                                        Bundle bundle = new Bundle();
                                        intent.putExtra("install_flag", 3);
                                        bundle.putSerializable("down", (Serializable) mxUpdateModel);
                                        intent.putExtras(bundle);
                                        getActivity().startActivity(intent);
                                    }
                                }).setPositiveButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                }).show();
                            } else {
                                Intent sound = new Intent();//com.onyx.content.browser.activity.OnyxOTAActivity
                                ComponentName cnSound = new ComponentName("com.onyx", "com.onyx.content.browser.activity.SimpleOTAActivity");
                                sound.setComponent(cnSound);
                                getActivity().startActivity(sound);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("update", e.getMessage());
        }
    }

    public void checkInstall(String packageName) {
        List<MXAppInfo> params = new ArrayList<>();
        MXAppInfo mxAppInfo = new MXAppInfo();
        mxAppInfo.setPackageName(packageName);
        mxAppInfo.setVersionCode(0);
        mxAppInfo.setVersionName("");
        params.add(mxAppInfo);
        checkUpdate(params, 4);
    }

    /**
     * @param params
     * @param flag
     */
    private void checkUpdate(List<MXAppInfo> params, final int flag) {
        try {
            JsonArray jsonEle = new JsonArray();
            JsonObject jsonObject;
            for (int i = 0; i < params.size(); i++) {
                jsonObject = new JsonObject();
                String packageName = params.get(i).getPackageName();
                int versionCode = params.get(i).getVersionCode();
                String versionName = params.get(i).getVersionName();
                jsonObject.addProperty("packageName", packageName);
                jsonObject.addProperty("versionCode", versionCode);
                jsonObject.addProperty("versionName", Constant.CLENT_APP+versionName);
                jsonEle.add(jsonObject);
            }

            final OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            Log.d("update", "param===>" + jsonEle.toString());
            okHttpUtils.post().url(CHECK_VERSION).addParams("update", jsonEle.toString())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("update", "data==>" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    if (getActivity()==null)return;
                    Log.d("update", "data==>" + response);
                    try {
                        JSONObject reObj = new JSONObject(response);
                        int code = reObj.optInt("code", -1);
                        if (code == 0) {
                            JSONArray jsonArray = reObj.optJSONArray("result");
                            final List<MXUpdateModel> mxUpdateModel = new ArrayList<MXUpdateModel>();
                            MXUpdateModel mxUpdateModel1;
                            final StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mxUpdateModel1 = new MXUpdateModel();
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                mxUpdateModel1.setAppDesc(jsonObject1.optString("appDesc"));
                                mxUpdateModel1.setDownloadUrl(jsonObject1.optString("downloadUrl"));
                                mxUpdateModel1.setPackageName(jsonObject1.optString("packageName"));
                                mxUpdateModel1.setVersionName(jsonObject1.optString("versionName"));
                                mxUpdateModel1.setMd5Str(jsonObject1.optString("md5"));
                                mxUpdateModel1.setIsLancher(jsonObject1.optInt("isLuncher"));
                                mxUpdateModel1.setVersionCode(jsonObject1.optInt("versionCode"));
                                mxUpdateModel1.setUpdateType(jsonObject1.optInt("updateType"));
                                mxUpdateModel.add(mxUpdateModel1);
                                stringBuffer.append(jsonObject1.optString("appDesc") + "\n");
                            }
                            if (mxUpdateModel.size() > 0) {
                                // TODO: 2016/10/14 有更新
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MXDownloadActivity.class);
                                intent.putExtra("install_flag", flag);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("down", (Serializable) mxUpdateModel);
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);
                            } else {
                                Toastor.showToast(getActivity(), "下载失败，请链接网络后重试");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("update", e.getMessage());
        }
    }
}
