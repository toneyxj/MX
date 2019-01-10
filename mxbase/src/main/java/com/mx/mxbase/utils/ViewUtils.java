package com.mx.mxbase.utils;

import android.view.View;
import android.view.ViewGroup;

import com.mx.mxbase.constant.APPLog;

/**
 * Created by xj on 2018/4/19.
 */

public class ViewUtils {
    /**
     * 获得未加载完成的view的宽高
     * @param view
     * @return
     */
    public static int[] unDisplayViewSize(View view) {
        ViewGroup.LayoutParams params=view.getLayoutParams();
        int width;
        int height;
        int size[] = new int[2];
        width=params.width;
        height=params.height;
        APPLog.e("params.width",params.width);
        APPLog.e("params.height",params.height);
        if (width<=0||height<=0){
           int iwidth = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
           int iheight = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(iwidth, iheight);

            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
            APPLog.e("params.getMeasuredWidth",width);
            APPLog.e("params.getMeasuredWidth",height);
            width=width<=0? view.getMeasuredWidth():width;
            height=height<=0? view.getMeasuredHeight():height;
        }
        size[0] = width;
        size[1] = height;
        APPLog.e("params.return",width);
        APPLog.e("params.return",height);
        return size;
    }
}
