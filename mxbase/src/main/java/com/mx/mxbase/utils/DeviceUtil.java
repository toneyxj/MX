package com.mx.mxbase.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Archer on 16/9/27.
 */
public class DeviceUtil {

    /**
     * 获取设备serial
     *
     * @return
     */
    public static String getDeviceSerial() {
        String serial = "unknown";
        try {
            Class clazz = Class.forName("android.os.Build");
            Class paraTypes = Class.forName("java.lang.String");
            Method method = clazz.getDeclaredMethod("getString", paraTypes);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            serial = (String) method.invoke(new Build(), "ro.serialno");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 是否为合法手机号
     *
     * @param mobile 手机号
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (StringUtils.isNull(mobile)){
            return false;
        }
        if (mobile.length()!=11){
            return false;
        }
        if (!mobile.substring(0,1).equals("1")){
            return false;
        }
        return true;
//        boolean flag = false;
//        if (mobile.length() == 0) {
//            return false;
//        }
//        String[] mobiles = mobile.split(",");
//        int len = mobiles.length;
//        if (len == 1) {
//            return Pattern.matches("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$", mobile);
//        } else {
//            for (int i = 0; i < len; i++) {
//                if (isMobile(mobiles[i])) {
//                    flag = true;
//                } else {
//                    flag = false;
//                }
//            }
//        }
//        return flag;
    }

    public static String getVersionName(Context context) {
        String version = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }
}
