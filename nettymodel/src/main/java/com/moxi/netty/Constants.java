package com.moxi.netty;

/**
 * 消息基本信息
 *
 * @author 徐飞
 * @version 2016/02/24 19:40
 */
public class Constants {
    private static String clientId;

    public static String getClientId() {
        return clientId;
    }

    public static void setClientId(String clientId) {
        Constants.clientId = clientId;
    }
}
