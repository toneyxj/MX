package com.mx.mxbase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mx.mxbase.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


/**
 * 图片请求管理
 */
public class ImageLoaderManager {
    private static Context mContext;
    public static ImageLoaderManager instance;

    public static ImageLoaderManager getInstance() {
        if (instance == null) {
            instance = new ImageLoaderManager(mContext);
        }
        return instance;
    }

    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public ImageLoaderManager(Context context) {
        this.mContext = context;
    }

    public void loadImageUrl(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public void clearCache() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
    }
}
