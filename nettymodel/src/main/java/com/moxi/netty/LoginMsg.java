package com.moxi.netty;

/**
 * 登录验证消息类型
 *
 * @author 徐飞
 * @version 2016/02/24 19:40
 */
public class LoginMsg extends BaseMsg {
    private String userName;
    private String password;
    public LoginMsg() {
        super();
        setType(MsgType.LOGIN);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
