package com.moxi.taskstudent.request;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.moxi.taskstudent.utils.ToastUtils;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.utils.ImageLoadUtils;
import com.mx.mxbase.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片压缩处理类
 * Created by Administrator on 2016/5/24.
 */
public class PictureHandle extends AsyncTask<String, Void, List<String>> {
    private PictureHandleListener back;// 传入接口
    private List<String> paths;// 请求参数
    private String code;// 请求返回码
    private int size;

    /**
     * 请求构造方法
     *
     * @param back    接口用于得到返回值
     * @param code    返回标记code
     */
    public PictureHandle( PictureHandleListener back,
                        List<String> paths, String code,int size) {
        this.back = back;
        this.paths = paths;
        this.code = code;
        this.size=size;
    }

    @Override
    protected List<String> doInBackground(String... arg0) {
        APPLog.e("图片处理传入路径", "路径集" + paths.toString());
        if (paths == null || paths.size() == 0) {
            return null;
        }
        List<String> imagePaths=new ArrayList<String>();
        for(String file:paths) {
            Bitmap bitmap = ImageLoadUtils.compressImage(file, size);
            String path= null;
            try {
                path = ImageLoadUtils.saveImageFile(bitmap);
            } catch (IOException e) {
                if (imagePaths.size()!=0){
                    for(String imagePath:imagePaths){
                        StringUtils.deleteFile(new File(imagePath));
                    }
                }
                return null;
            }
            imagePaths.add(path);
        }
        return imagePaths;

    }

    @Override
    protected void onPostExecute(List<String> result) {// 在doInBackground执行完成后系统会自动调用，result是返回值
        if (result == null) {
            ToastUtils.getInstance().showToastShort("图片处理失败");
            back.HandleFail(code);
            return;
        }
        APPLog.e("图片处理：","返回结果"+result.toString());
        back.HandleSucess(result,code);

    }
    /**
     * Created by Administrator on 2016/4/1.
     */
    public interface PictureHandleListener {
        /*
         *
         * 图片处理成功
         */
        public void HandleSucess(List<String> results, String code);

        /*
         *
         * 图片处理失败
         */
        public void HandleFail(String code);
    }
}
