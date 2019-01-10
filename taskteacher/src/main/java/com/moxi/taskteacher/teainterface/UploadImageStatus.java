package com.moxi.taskteacher.teainterface;

import android.view.View;

/**
 * 图片加载状态接口
 * Created by Administrator on 2016/10/21.
 */
public interface UploadImageStatus {
    /**
     * 图片加载提示接口
     * @param view 当前控件view
     * @param isSucess 是否成功
     * @param path 图片加载网络路径
     * @param status 当前状态 0开始加载，1加载中，2加载失败，3加载成功
     */
    public void onUploadImage(View view, boolean isSucess, String path, int status);
}
