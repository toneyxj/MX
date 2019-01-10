package com.moxi.handwritten.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.moxi.handwritten.R;
import com.moxi.handwritten.view.PaintInvalidateRectView;
import com.mx.mxbase.base.baseFragment;
import com.mx.mxbase.http.ClearSweep;
import com.mx.mxbase.interfaces.ClearSweepListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.LocationImageLoader;
import com.mx.mxbase.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/8.
 */
public class DrawFragment extends baseFragment implements ClearSweepListener {

    public static DrawFragment newInstance(
            String image, int indexpen) {
        DrawFragment diFragment = new DrawFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image", image);
        bundle.putInt("indexpen", indexpen);
        diFragment.setArguments(bundle);
        return diFragment;
    }

    @Bind(R.id.write_draw_paint)
    PaintInvalidateRectView write_draw_paint;
    private String image;

    public String getImage() {
        return image;
    }

    @Override
    public void initFragment(View view) {
        image = getArguments().getString("image");
        int indexpen = getArguments().getInt("indexpen");
        if (!image.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(image);
            write_draw_paint.setBitmap(bitmap);
        }
        setpaint(indexpen);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String saveImagepath() throws IOException {
        if (image.equals("")) {
            image = saveImageFile(getBitmap());
        } else {
            try {
                saveImageFile(image, write_draw_paint.getBitmap());
                LocationImageLoader.getInstance().ClearBitmapFromLruCache(image);
            } catch (Exception e) {

            }
        }
        return image;
    }

    /**
     * 将图片持久化到本地文件夹下面
     *
     * @param bitmap 需要持久化的图片
     */
    private String saveImageFile(Bitmap bitmap) throws IOException {
       if ( StringUtils.haveSD(20)){
           throw new IOException();
       }
        String path = null;
        String sd = FileUtils.getInstance().getfileMksPath();
//        if (sd == null) {
//            path = "";
//            BaseApplication.Toast("请插入，SD卡");
//            return path;
//        }
        String my = System.currentTimeMillis() + ".png";
        File file = new File(sd);
        if (!file.exists())
            file.mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(
                sd + my);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();
        path = sd + my;
        return path;
    }

    /**
     * 将图片持久化到本地文件夹下面
     *
     * @param bitmap 需要持久化的图片
     */
    private void saveImageFile(String path, Bitmap bitmap) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_draw_layout;
    }

    public Bitmap getBitmap() {
        return write_draw_paint.getBitmap();
    }

    public void setpaint(int currentIndex) {
        write_draw_paint.setPaint(currentIndex);
    }

    private Bitmap sweepBitmap = null;

    public void ClearSweep() {
        dialogShowOrHeaid(true);
        sweepBitmap = getBitmap();
        new ClearSweep(this, sweepBitmap, Color.WHITE, Color.TRANSPARENT).execute();
    }

    @Override
    public void getSweepBitmap(Bitmap bitmap) {
        write_draw_paint.setBitmap(bitmap);
        StringUtils.recycleBitmap(sweepBitmap);
        dialogShowOrHeaid(false);
    }

    private ProgressDialog dialog;

    private void dialogShowOrHeaid(boolean is) {
        if (is) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("请在清理...");
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
