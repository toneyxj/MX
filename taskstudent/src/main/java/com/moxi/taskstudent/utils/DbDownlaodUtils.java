package com.moxi.taskstudent.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.moxi.taskstudent.model.DbDownloadModel;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/12/21.
 */
public class DbDownlaodUtils  {
    private final String sqlId = "id";
    private final String sqlJson = "id,downlaodPath";
    private final String TABLE = "DbDownloadModel";
    private static DbDownlaodUtils instatnce = null;
    public static DbDownlaodUtils getInstance() {
        if (instatnce == null) {
            synchronized (DbDownlaodUtils.class) {
                if (instatnce == null) {
                    instatnce = new DbDownlaodUtils();
                }
            }
        }
        return instatnce;
    }
    public  long isSaveDownload(String download){
        Cursor cursor = DataSupport.findBySQL("select " + sqlId + " from " + TABLE + " where downlaodPath='" + download + "'");
          long id=-1;
        if (cursor.moveToNext()){
            id=cursor.getLong(0);
        }
        if (cursor != null) cursor.close();
        return id;
    }

    /**
     * 判断是否保存
     * @param download 下载文件路径
     * @return
     */
    public  boolean isSave(String download){
        Cursor cursor = DataSupport.findBySQL("select " + sqlId + " from " + TABLE + " where downlaodPath='" + download + "'");
          boolean is=cursor.moveToNext();
        if (cursor != null) cursor.close();
        return is;
    }

    /**
     * 保存
     * @param model
     */
    public  void save(DbDownloadModel model){
        long id=isSaveDownload(model.downlaodPath);
        if (id==-1){
            model.save();
        }else {
            updataStatus(id,model.status);
        }
    }

    /**
     * 更新状态
     * @param id 数据id
     * @param status 修改后的状态
     */
    public  void updataStatus(long id,String status){
        ContentValues values=new ContentValues();
        values.put("status",status);
        DataSupport.update(DbDownloadModel.class,values,id);
    }
    public DbDownloadModel getModel(String downlaodPath){
        long id=isSaveDownload(downlaodPath);
        if (id==-1)return null;
        DbDownloadModel model=DataSupport.find(DbDownloadModel.class,id);
        return model;
    }
}
