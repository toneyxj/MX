package com.moxi.handwritten.config;

import android.content.SharedPreferences;

import com.mx.mxbase.base.BaseApplication;


/**
 * Created by Administrator on 2016/8/3.
 */
public class WriteConfig {

    // 初始化类实列
    private static WriteConfig instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static WriteConfig getInstance() {
        if (instatnce == null) {
            synchronized (WriteConfig.class) {
                if (instatnce == null) {
                    instatnce = new WriteConfig();
                }
            }
        }
        return instatnce;
    }

    /**
     * 获得用户名和密码
     *
     * @return 下标0为用户名，1为密码
     */
    private int getIndex() {
        return BaseApplication.preferences.getInt("index", 0);
    }
    /**
     * 获得用户名和密码
     *
     * @return 下标0为用户名，1为密码
     */
    private void setIndex() {
        int index=getIndex()+1;
        SharedPreferences.Editor editor = BaseApplication.editor;
        editor.putInt("index", index);// 登录名
        editor.commit();// 提交
    }
    public String getTableName(){
        setIndex();
        int index=getIndex();

        String tablename="moxi";

        while (true){
            int shan=index/25;
            int yu=index%25;
            tablename+=getName(yu);
            index=shan;
            if (index==0){
                break;
            }
        }
        return tablename;
    }
    private String getName(int index){
        String name="";
        switch (index){
            case 0:
                name="a";
                break;
            case 1:
                name="b";
                break;
            case 2:
                name="c";
                break;
            case 3:
                name="d";
                break;
            case 4:
                name="e";
                break;
            case 5:
                name="f";
                break;
            case 6:
                name="g";
                break;
            case 7:
                name="h";
                break;
            case 8:
                name="i";
                break;
            case 9:
                name="j";
                break;
            case 10:
                name="k";
                break;
            case 11:
                name="l";
                break;
            case 12:
                name="m";
                break;
            case 13:
                name="n";
                break;
            case 14:
                name="o";
                break;
            case 15:
                name="p";
                break;
            case 16:
                name="q";
                break;
            case 17:
                name="r";
                break;
            case 18:
                name="s";
                break;
            case 19:
                name="t";
                break;
            case 20:
                name="u";
                break;
            case 21:
                name="v";
                break;
            case 22:
                name="w";
                break;
            case 23:
                name="x";
                break;
            case 24:
                name="y";
                break;
            case 25:
                name="z";
                break;
            default:
                break;
        }
        return name;
    }
}
