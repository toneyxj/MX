package com.mx.teacher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;

import com.mx.mxbase.utils.Base64Utils;

/**
 * Created by Archer on 16/9/22.
 */
public class URLImageParser implements Html.ImageGetter {
    private Context c;
    private View container;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     *
     * @param t
     * @param c
     */
    public URLImageParser(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        Bitmap bitmap = Base64Utils.base64ToBitmap(source);
        Drawable drawable =new BitmapDrawable(bitmap);
        return drawable;
    }


    public URLImageParser() {
        super();
    }
}
