package com.moxi.taskstudent.utils;

import android.graphics.Bitmap;

import com.moxi.taskstudent.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by Administrator on 2016/12/21.
 */
public class ImageLoadUtils {
    /**
     * 获得 DisplayImageOptions;
     *
     * @return DisplayImageOptions
     */
    public static DisplayImageOptions getoptions() {
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.back_white)
                .showImageForEmptyUri(R.drawable.back_white)
                .showImageOnFail(R.drawable.back_white)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        return options;
    }
}
