package com.moxi.studentclient.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.studentclient.activity.LogingActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/3 0003.
 */
public class Utils {

    public static String timeSecondfrom(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return String.format("%02d", h) + ":" + String.format("%02d", d) + ":" + String.format("%02d", s);
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveBitmapFile(Bitmap bitmap,String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists() && dir.isDirectory()) {
            dir.mkdirs();
        }
        try {
            File file = new File(filePath + File.separator + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String titleWithPage(int page) {
        String title = "闯关答题－－";
        switch (page) {
            case 0:
                title += "第一关";
                break;
            case 1:
                title += "第二关";
                break;
            case 2:
                title += "第三关";
                break;
            case 3:
                title += "第四关";
                break;
            case 4:
                title += "第五关";
                break;
            case 5:
                title += "第六关";
                break;
            case 6:
                title += "第七关";
                break;
            case 7:
                title += "第八关";
                break;
        }
        return title;
    }

    public static boolean isLogin(Context context){
        long userId= UserInformation.getInstance().getID();
        UserBaseInformation user=UserInformation.getInstance().getUserInformation();
        if (-1!=userId&&null!=user)
            return true;
        else {
            Intent logInt = new Intent();
            logInt.setClass(context, LogingActivity.class);
            context.startActivity(logInt);
            return false;
        }
    }
}
