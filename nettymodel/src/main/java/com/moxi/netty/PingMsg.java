package com.moxi.netty;

/**
 * 心跳检测消息类型
 *
 * @author 徐飞
 * @version 2016/02/24 19:40
 */
public class PingMsg extends BaseMsg {
    public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
