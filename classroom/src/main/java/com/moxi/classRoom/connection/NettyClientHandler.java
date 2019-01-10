package com.moxi.classRoom.connection;


import android.content.Intent;

import com.moxi.classRoom.RoomApplication;
import com.moxi.classRoom.config.StatusInformation;
import com.moxi.classRoom.serve.MessageReceiver;
import com.moxi.netty.BaseMsg;
import com.moxi.netty.PingMsg;
import com.moxi.netty.PushMsg;
import com.mx.mxbase.constant.APPLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;


/**
 * @author 徐飞
 * @version 2016/02/25 14:11
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {
    //设置心跳时间  开始
    public static final int MIN_CLICK_DELAY_TIME = 1000 * 30;
    private long lastClickTime = 0;
    //设置心跳时间   结束

    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = System.currentTimeMillis();
                        PingMsg pingMsg = new PingMsg();
//                        ctx.writeAndFlush(pingMsg);
                        PushClient.sendMessage(pingMsg);
                        APPLog.e(NettyClientBootstrap.KRY, "send ping to server----------"+lastClickTime);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //这里是断线要进行的操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        APPLog.e(NettyClientBootstrap.KRY, "重连了。---------");
        StatusInformation.getInstance().sendStatusRecevier(3);
        NettyClientBootstrap bootstrap = PushClient.getBootstrap();
        bootstrap.startNetty();
    }

    //这里是出现异常的话要进行的操作
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        StatusInformation.getInstance().sendStatusRecevier(-2);
        APPLog.e(NettyClientBootstrap.KRY, "出现异常了。。。。。。。。。。。。。" );
        cause.printStackTrace();
    }

    //这里是接受服务端发送过来的消息
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        APPLog.e("服务器发送来的消息");
        switch (baseMsg.getType()) {
            case LOGIN:
                //向服务器发起登录
//                LoginMsg loginMsg = new LoginMsg();
//                loginMsg.setPassword("yao");
//                loginMsg.setUserName("robin");
//                channelHandlerContext.writeAndFlush(loginMsg);
                StatusInformation.getInstance().sendStatusRecevier(2);
                break;
            case PING:
                APPLog.e(NettyClientBootstrap.KRY, "receive ping from server----------" );
                break;
            case PUSH:
                PushMsg pushMsg = (PushMsg) baseMsg;
//                Intent intent = new Intent(MyApplication.applicationContext, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("hi", pushMsg.getContent());
//                MainApplication.getAppContext().startActivity(intent);
//                System.out.println(pushMsg.getTitle() + " , " +pushMsg.getContent());

                break;
            default:
                APPLog.e(NettyClientBootstrap.KRY, "default.." );
                break;
        }
        //发送消息给广播
        Intent intent=new Intent(MessageReceiver.MESSAGE_RECEIVER);
        intent.putExtra("message",baseMsg);
        RoomApplication.applicationContext.sendBroadcast(intent);

        ReferenceCountUtil.release(baseMsg);
    }
}
