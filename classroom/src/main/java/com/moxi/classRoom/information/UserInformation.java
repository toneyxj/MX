package com.moxi.classRoom.information;

import android.content.SharedPreferences;

import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.dbUtils.UserInformationUtils;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.constant.APPLog;

import org.litepal.tablemanager.Connector;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserInformation {
    // 初始化类实列
    private static UserInformation instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static UserInformation getInstance() {
        if (instatnce == null) {
            synchronized (Connector.class) {
                if (instatnce == null) {
                    instatnce = new UserInformation();
                }
            }
        }
        return instatnce;
    }


    /**
     * 注销账户
     */
    public void logoutAccount(){
        setLoging(false);
        initScoket("",0);
        initBaseInformation(null,null,-1L,null);
        //退出登录删除
        CacheDbUtils.getInstance().deleteFinishClassData();
    }

    private String dstName;
    private int dstPort;

    /**
     * 设置scoket链接必须信息
     *
     * @param dstName ip地址
     * @param dstport 端口号
     */
    public void initScoket(String dstName, int dstport) {
        setDstName(dstName);
        setDstPort(dstport);
    }

    /**
     * 设置最近用户登录信息 同时设置登录信号
     *
     * @param account  账号
     * @param password 密码
     * @param id       用户id
     */
    public void initBaseInformation(String account, String password, long id,UserBaseInformation information) {
        setAccount(account);
        setPassword(password);
        setID(id);
        if (information!=null){
            UserInformationUtils.getInstance().saveInformation(information);
        }
        setLoging(information!=null);
    }

    /**
     * @return 目标主机名或IP地址连接。
     */
    public String getDstName() {
        if (dstName == null) {
            dstName = MyApplication.preferences.getString("dstName", null);
        }
        APPLog.e("IP地址getDstName="+dstName);
        return dstName;
    }

    /**
     * @param dstName 目标主机名或IP地址连接。
     */
    public void setDstName(String dstName) {
        if (null == this.dstName || !this.dstName.equals(dstName)) {
            SharedPreferences.Editor editor = MyApplication.editor;
            editor.putString("dstName", dstName);// 登录名
            editor.commit();// 提交
        }
        APPLog.e("IP地址setDstName="+dstName);
        this.dstName = dstName;
    }

    /**
     * 获得登录信号
     * @return
     */
    public boolean isLoging() {
        return MyApplication.preferences.getBoolean("loging", false);
    }

    public void setLoging(boolean loging) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putBoolean("loging", loging);// 登录名
        editor.commit();// 提交
    }
    /**
     * @return 获得登录用户id
     */
    public long getID() {
        return MyApplication.preferences.getLong("id", -1l);
    }

    /**
     * @param ID 设置用户id
     */
    public void setID(long ID) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putLong("id", ID);// 登录名
        editor.commit();// 提交
    }

    /**
     * @return 登录账号
     */
    public String getAccount() {
        return MyApplication.preferences.getString("account", null);
    }

    /**
     * @param account 设置登录账号
     */
    public void setAccount(String account) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putString("account", account);// 登录名
        editor.commit();// 提交
    }

    /**
     * @return 登录密码
     */
    public String getPassword() {
        return MyApplication.preferences.getString("password", null);
    }

    /**
     * @param password 设置登录密码
     */
    public void setPassword(String password) {
        SharedPreferences.Editor editor = MyApplication.editor;
        editor.putString("password", password);// 登录名
        editor.commit();// 提交
    }

    /**
     * @return 端口连接到目标主机。
     */
    public int getDstPort() {
        if (dstPort == 0) {
            dstPort = MyApplication.preferences.getInt("dstPort", -1);
        }
        return dstPort;
    }

    /**
     * @param dstPort 端口连接到目标主机。
     */
    public void setDstPort(int dstPort) {
        if (0 == this.dstPort || !(this.dstPort == dstPort)) {
            SharedPreferences.Editor editor = MyApplication.editor;
            editor.putInt("dstPort", dstPort);// 登录名
            editor.commit();// 提交
        }
        this.dstPort = dstPort;
    }

    /**
     * 获得用户基本信息
     *
     * @return
     */
    public UserBaseInformation getUserInformation() {
        if (getID() == -1) return null;
        return UserInformationUtils.getInstance().getInformation(getID());
    }
}
