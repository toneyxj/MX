package com.moxi.classRoom.connection;


import android.os.AsyncTask;

import com.moxi.classRoom.config.StatusInformation;
import com.moxi.classRoom.serve.MainService;
import com.moxi.netty.Constants;
import com.moxi.netty.LoginMsg;
import com.mx.mxbase.constant.APPLog;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author 徐飞
 * @version 2016/02/25 14:11
 */
public class NettyClientBootstrap {
    public static final String KRY = "长连接打印";
    private int port = 10000;
    private String host = "192.168.0.224";
    public SocketChannel socketChannel;

    public void startNetty() {
        if (socketChannel != null && socketChannel.isOpen()) {
            APPLog.e(NettyClientBootstrap.KRY, "已经连接");
//            StatusInformation.getInstance().sendStatusRecevier(1);
        } else {
            if (socketChannel != null) {
                socketChannel.close();
                socketChannel = null;
            }
            StatusInformation.getInstance().sendStatusRecevier(0);
            Constants.setClientId("001");// TODO: 2016/2/23
            APPLog.e(NettyClientBootstrap.KRY, "长链接开始");
            //执行链接
            new ChannelAsynck().execute();

        }
    }

    /**
     * 必须异步进行链接
     *
     * @return
     * @throws InterruptedException
     */
    public int start() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                APPLog.e(NettyClientBootstrap.KRY, "connect server  成功---------");
//                StatusInformation.getInstance().sendStatusRecevier(1);
//                MainService.isToDo=true;
                return 1;
            } else {
                APPLog.e(NettyClientBootstrap.KRY, "connect server  失败---------");
//                StatusInformation.getInstance().sendStatusRecevier(-1);
//                startNetty();
                return -1;
            }
        } catch (Exception e) {
            APPLog.e(NettyClientBootstrap.KRY, "无法连接----------------");
//            StatusInformation.getInstance().sendStatusRecevier(-3);
//            MainService.isToDo=false;
            return -3;
        }
    }

    public void closeChannel() {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }

    public boolean isOpen() {
        if (socketChannel != null) {
            APPLog.e(KRY + "---是否链接", socketChannel.isOpen());
            return socketChannel.isOpen();
        }
        return false;
    }


    private class ChannelAsynck extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            MainService.isToDo = false;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int status = 0;
            try {
                status = start();
            } catch (InterruptedException e) {
                e.printStackTrace();
                status = -2;
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch (integer) {
                case 1://链接成功
                    MainService.isToDo = true;
                    //启动登录信息
                    LoginMsg loginMsg = new LoginMsg();// TODO: 2016/2/23
                    loginMsg.setPassword("xia");
                    loginMsg.setUserName("jun");
                   String back= PushClient.sendMessage(loginMsg);
                    //提交登录失败
                    if (back.equals("fail")){
                        //反复执行登录操作
                        onPostExecute(1);
                    }
//                    else if(back.equals("sucess")){
//                        StatusInformation.getInstance().sendStatusRecevier(2);
//                    }
                    break;
                case -1://链接失败,启动重连
                    startNetty();
                    break;
                case -2://链接异常
                    MainService.isToDo = false;
                    break;
                case -3://无法链接需要
                    MainService.isToDo = false;
                    break;
                default:
                    break;
            }
            //发送状态信息
            StatusInformation.getInstance().sendStatusRecevier(integer);
        }
    }
}