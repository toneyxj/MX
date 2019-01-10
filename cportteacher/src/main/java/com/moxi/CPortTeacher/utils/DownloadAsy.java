package com.moxi.CPortTeacher.utils;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */
public class DownloadAsy {
    private static DownloadAsy instatnce = null;
    public static DownloadAsy getInstance() {
        if (instatnce == null) {
            synchronized (DownloadAsy.class) {
                if (instatnce == null) {
                    instatnce = new DownloadAsy();
                }
            }
        }
        return instatnce;
    }
    private List<AsyncTask> list=new ArrayList<>();
    public void addAsy(AsyncTask task){
        list.add(task);
    }
    public void clearAllAsy(){
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isCancelled()){
                list.get(i).cancel(true);
            }
        }
    }

    /**
     * 是否需要取消下载
     * @return true为需要去消除下载
     */
    public boolean isallCancelled(){
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isCancelled()){
               return true;
            }
        }
        return false;
    }
}
