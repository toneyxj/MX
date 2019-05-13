package com.moxi.yuyinhecheng.listener;

/**
 * 语音监听
 * Created by xiajun on 2019/5/10.
 */

public interface SpeekListener {
    /**
     * 语音阅读结束
     */
    void onSpeekOver();

    /**
     * 语音阅读开始
     */
    void onSpeekStart();

    /**
     * 语音阅读出错
     */
    void onSpeekError(Exception e);
}
