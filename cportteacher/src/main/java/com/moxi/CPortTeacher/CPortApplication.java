package com.moxi.CPortTeacher;

import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDex;
import android.widget.TextView;

import com.moxi.CPortTeacher.utils.TheardBus;
import com.moxi.classRoom.RoomApplication;

/**
 * Created by Administrator on 2016/10/24.
 */
public class CPortApplication extends RoomApplication {
    public static final String photoPath="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2493318733,409513354&fm=80&w=179&h=119&img.JPEG";

    private static final TheardBus bus = new TheardBus();

    public static TheardBus getBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this) ;
        super.onCreate();

    }

    /**
     * 左边设置图片
     * @param view 设置的文本
     * @param is 显示样式
     * @param trueImage 显示true的image
     * @param falseImage 显示为false的image
     *                   @param  position 图片放置方位 0代表左，1代表上，2代表右，其他代表下
     */
    public static void setTextAllImage(TextView view, boolean is,int trueImage,int falseImage,int position) {
        Drawable drawable;
        if (is) {// 是否显示列表
            drawable = applicationContext.getResources().getDrawable(trueImage);
        } else {
            drawable = applicationContext.getResources().getDrawable(falseImage);
        }
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        if (position==0) {
            view.setCompoundDrawables(drawable, null, null, null);
        }else if (position==1){
            view.setCompoundDrawables(null, drawable, null, null);
        }else if (position==2){
            view.setCompoundDrawables(null, null, drawable, null);
        }else {
            view.setCompoundDrawables(null, null, null, drawable);
        }
    }
}
