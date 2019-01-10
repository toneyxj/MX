package com.moxi.classRoom.serve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moxi.classRoom.utils.MessageInterface;
import com.moxi.netty.BaseMsg;
import com.moxi.netty.MsgType;
import com.mx.mxbase.constant.APPLog;

/**
 * Created by Administrator on 2016/10/26.
 */
public class MessageReceiver extends BroadcastReceiver {
    public static final String MESSAGE_RECEIVER = "com.moxi.classRoom.serve.MessageReceiver";
    private static MsgType type;
    private static   MessageInterface messageInterface;

    /**
     * 注册信息回调
     * @param type 信息回调类型
     * @param messageInterface 信息回调接口
     */
    public static void initMessageReceiver(MsgType type, MessageInterface messageInterface) {
        MessageReceiver.type = type;
        MessageReceiver.messageInterface = messageInterface;
    }

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MESSAGE_RECEIVER)) {
            BaseMsg baseMsg = (BaseMsg) intent.getSerializableExtra("message");
            APPLog.e("baseMsg1=" + baseMsg.getType());
            if (null != messageInterface && baseMsg.getType() == type) {
                APPLog.e("baseMsg2=" + baseMsg.getType());
                messageInterface.message(baseMsg);
            }
        }
    }
}
