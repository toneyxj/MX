package com.moxi.classRoom.connection;


import com.moxi.classRoom.config.StatusInformation;
import com.moxi.netty.BaseMsg;
import com.moxi.netty.MsgType;
import com.mx.mxbase.constant.APPLog;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * netty推送客户端
 *
 * @author 徐飞
 * @version 2016/02/25 15:15
 */
public class PushClient {

    private static NettyClientBootstrap bootstrap;

    public static NettyClientBootstrap getBootstrap() {
        return bootstrap;
    }

    public static void setBootstrap(NettyClientBootstrap bootstrap) {
        PushClient.bootstrap = bootstrap;
    }

    public static void create() {
        NettyClientBootstrap bootstrap = new NettyClientBootstrap();
        PushClient.setBootstrap(bootstrap);
    }

    public static void start(BaseMsg msg) {
        NettyClientBootstrap bootstrap = PushClient.getBootstrap();
        bootstrap.startNetty();
        PushClient.setBootstrap(bootstrap);
    }

    private static String backStatus = "";

    /**
     * 发送消息
     *
     * @param msg 消息类型
     */
    public static String sendMessage(BaseMsg msg) {
        APPLog.e("发送消息------------------------------------------------");
        if (msg.getType() != MsgType.LOGIN && StatusInformation.getInstance().getStatus() != 2) {
            return "请检查链接，确认已经登录链接服务器！";
        }
        backStatus = "fail";
        ChannelFuture f = bootstrap.socketChannel.writeAndFlush(msg);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //提交完成
                APPLog.e("提交完成");
                backStatus = "sucess";
            }
        });

        return backStatus;
    }

    /**
     * 调用此方法关闭通道后会自动重连，如果想真正实现关闭，
     * 需要修改handler中重写的断线处理方法。
     *
     * @see NettyClientHandler
     */
    public static void close() {
        NettyClientBootstrap bootstrap = PushClient.getBootstrap();
        bootstrap.closeChannel();
        PushClient.setBootstrap(bootstrap);
    }

    /**
     * @return 返回通道连接状态
     */
    public static boolean isOpen() {
        NettyClientBootstrap bootstrap = PushClient.getBootstrap();
        return bootstrap.isOpen();
    }
}
