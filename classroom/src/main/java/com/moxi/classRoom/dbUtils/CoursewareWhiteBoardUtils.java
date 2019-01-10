package com.moxi.classRoom.dbUtils;

import android.content.ContentValues;
import android.database.Cursor;

import com.moxi.classRoom.db.CoursewareWhiteBoardModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 课件白板数据库工具操作类
 * Created by Administrator on 2016/11/30.
 */
public class CoursewareWhiteBoardUtils {
    private final String sqlAll = "id,tearchId,recentlyOpenTime,savePath,Filename,FileId";
    private final String TABLE = "CoursewareWhiteBoardModel";
    private final String sqlId = "id";
    // 初始化类实列
    private static CoursewareWhiteBoardUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static CoursewareWhiteBoardUtils getInstance() {
        if (instatnce == null) {
            synchronized (CoursewareWhiteBoardUtils.class) {
                if (instatnce == null) {
                    instatnce = new CoursewareWhiteBoardUtils();
                }
            }
        }
        return instatnce;
    }

    /**
     * 获得所有的阅读记录
     *
     * @param tearchId 教师id
     * @return 返回阅读记录集合 ,返回按时间排序后的数据结果
     */
    public List<CoursewareWhiteBoardModel> getAllReadRecorder(long tearchId) {
        List<CoursewareWhiteBoardModel> list = new ArrayList<>();
        Cursor cursor = DataSupport.findBySQL("select " + sqlAll + " from " + TABLE + "  where tearchId=" + tearchId  + " ORDER BY recentlyOpenTime DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CoursewareWhiteBoardModel model = new CoursewareWhiteBoardModel();
                model.id = cursor.getLong(0);
                model.tearchId = cursor.getLong(1);
                model.recentlyOpenTime = cursor.getLong(2);
                model.savePath = cursor.getString(3);
                model.Filename = cursor.getString(4);
                model.FileId = cursor.getLong(5);
                list.add(model);
            }
        }
        if (cursor != null) cursor.close();
        return list;
    }

    /**
     * 获得最近阅读最多4条记录
     *
     * @param tearchId 教师id
     * @return 返回阅读记录集合 ,返回按时间排序后的数据结果
     */
    public List<CoursewareWhiteBoardModel> getFourReadRecorder(long tearchId) {
        List<CoursewareWhiteBoardModel> list = new ArrayList<>();
        Cursor cursor = DataSupport.findBySQL("select " + sqlAll + " from " + TABLE + "  where tearchId=" + tearchId + " ORDER BY recentlyOpenTime DESC limit 4");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CoursewareWhiteBoardModel model = new CoursewareWhiteBoardModel();
                model.id = cursor.getLong(0);
                model.tearchId = cursor.getLong(1);
                model.recentlyOpenTime = cursor.getLong(2);
                model.savePath = cursor.getString(3);
                model.Filename = cursor.getString(4);
                model.FileId = cursor.getLong(5);
                list.add(model);
            }
        }
        if (cursor != null) cursor.close();
        return list;
    }

    /**
     * 保存数据
     *
     * @param model
     */
    public void saveData(CoursewareWhiteBoardModel model) {
        long id=getIndexId(model.tearchId, model.FileId);
        if ( id!= -1) {
            updataData(model,id);
        }else {
            model.save();
        }
    }

    /**
     * 通过保存路径获取索引id
     *
     * @param FileId 文件下载id
     * @return
     */
    public long getIndexId(long tearchId, long FileId) {
        long id = -1;
        String sql="select " + sqlId + " from " + TABLE + "  where tearchId=" + tearchId  + " and FileId=" + FileId;
        Cursor cursor = DataSupport.findBySQL(sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                id = cursor.getLong(0);
            }
            cursor.close();
        }
        return id;
    }

    /**
     * 更新数据
     * @param model 更新数据源
     */
    public void updataData(CoursewareWhiteBoardModel model,long id){
        ContentValues values = new ContentValues();
        values.put("tearchId", model.tearchId);
        values.put("recentlyOpenTime", model.recentlyOpenTime);
        values.put("savePath", model.savePath);
        values.put("Filename", model.Filename);
        values.put("FileId", model.FileId);
        DataSupport.update(CoursewareWhiteBoardModel.class, values, id);
    }

    /**
     * 更新保存时间
     * @param id 数据索引id
     */
    public void upDataTime(long id){
        ContentValues values = new ContentValues();
        values.put("recentlyOpenTime",String.valueOf(System.currentTimeMillis()));
        DataSupport.update(CoursewareWhiteBoardModel.class, values, id);
    }
    /**
     * 通过下载路径更新阅读时间
     * @param tearchId 教师id
     * @param FileId 下载id
     */
    public void upDataTime(long tearchId,long FileId){
        long id=getIndexId(tearchId,FileId);
        if (id!=-1){
            upDataTime(id);
        }
    }

    /**
     * 获得单个保存数据集合
     * @param tearchId 教师id
     * @param FileId 下载id
     * @return
     */
    public CoursewareWhiteBoardModel getOneModel(long tearchId,long FileId){
        long id=getIndexId(tearchId,FileId);
        if (id==-1){
            return null;
        }else {
            return getOneModel(id);
        }
    }

    /**
     * 通过索引id获得model数据
     * @param id 索引id
     * @return 实体数据
     */
    public CoursewareWhiteBoardModel getOneModel(long id){
        return DataSupport.find(CoursewareWhiteBoardModel.class,id);
    }
    public void deleteData(long id){
        DataSupport.delete(CoursewareWhiteBoardModel.class,id);
    }
}
