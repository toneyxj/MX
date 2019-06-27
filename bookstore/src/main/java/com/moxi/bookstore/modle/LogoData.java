package com.moxi.bookstore.modle;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.moxi.bookstore.utils.MD5;
import com.moxi.bookstore.utils.ToolUtils;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by xiajun on 2019/6/26.
 */

public class LogoData {
    // 初始化类实列
    private static LogoData instatnce = null;

    public static LogoData getInstance() {
        if (instatnce == null) {
            synchronized (LogoData.class) {
                if (instatnce == null) {
                    instatnce = new LogoData();
                }
            }
        }
        return instatnce;
    }

    private String device_sn;
    private String user_id=null;
    private String event_id = "1001";
    private String from_platform = "android";
    private String banben=null;

    /**
     * 获得permanent_id拼接的字符串
     * @return
     */
    public String getPermanent_id(long time){
        StringBuilder builder=new StringBuilder();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");//小写的mm表示的是分钟
        String str = sdf.format(new Date(time));
        builder.append(str);
        APPLog.e("时间字符串:"+str);
        int haom= (int) (time%1000);
        APPLog.e("haom:"+haom);
        builder.append(haom);
        builder.append(getRandom());
        builder.append(getRandom());
        builder.append(getRandom());
        return builder.toString();
    }

    private int getRandom(){
        Random ra =new Random();
        return ra.nextInt(800000)+100000;
    }

    public String getDevice_sn(){
        if (StringUtils.isNull(device_sn)){
            device_sn=getDeviceId(MyApplication.applicationContext);
        }
        return device_sn;
    }

    public String getFrom_platform(){
        return from_platform;
    }
    public String getEvent_id(){
        return event_id;
    }
    public String getUser_id(){
        if (StringUtils.isNull(user_id)){
            user_id= ToolUtils.getIntence().getuserId(MyApplication.applicationContext);
        }
        if (StringUtils.isNull(user_id)){
            user_id="0";
        }
        return user_id;
    }


    public void clear() {
        instatnce = null;
    }


    public String getBanben(){
        if (banben==null){
            banben=getAppVersionName(MyApplication.applicationContext);
        }
        if (StringUtils.isNull(banben)){
            banben="2.4.1";
        }
        return banben;
    }
    /**
     * 返回当前程序版本名
     */
    public  String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            APPLog.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * 渠道标志为：
     * andriod（a_）
     * 识别符来源标志：
     * 1， wifi mac地址（wifi_）；
     * 2， IMEI（imei_）；
     * 3， 序列号（sn_）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public  String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志  
        deviceId.append("");
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn_");
                deviceId.append(sn);
                APPLog.d("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei_");
                deviceId.append(imei);
                APPLog.d("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                APPLog.d("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("noDeviceID");
        }
        APPLog.d("getDeviceId : ", deviceId.toString());
        return deviceId.toString();
    }

    public String getToken(LogoDataDb logoDataDb,String timespace){
        return MD5.stringToMD5(MD5.stringToMD5(timespace+getDevice_sn()+logoDataDb.permanent_id)
                +"5ead1621f9be1b077099eeddc5cec2e1"+timespace);

    }

}
