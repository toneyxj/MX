package com.moxi.classRoom.ClassInterface;

/**
 * 加载完成接口
 * Created by Administrator on 2016/10/20.
 */
public interface UploadSucess {
    /**
     * 加载进度回调
     * @param path 文件加载的路径
     * @param isSucess 是否加载成功
     * @param progress 加载进度
     */
    public void  onUpload(String path, boolean isSucess, float progress);
}
