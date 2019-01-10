package com.moxi.taskteacher.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.moxi.taskteacher.model.TaskDataModel;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 课件白板数据库工具操作类
 * Created by Administrator on 2016/11/30.
 */
public class TaskDataModelUtils {
    private final String sqlAll = "id,recentlyOpenTime,savePath,Filename,FileId";
    private final String TABLE = "TaskDataModel";
    private final String sqlId = "id";
    // 初始化类实列
    private static TaskDataModelUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static TaskDataModelUtils getInstance() {
        if (instatnce == null) {
            synchronized (TaskDataModelUtils.class) {
                if (instatnce == null) {
                    instatnce = new TaskDataModelUtils();
                }
            }
        }
        return instatnce;
    }

    /**
     * 获得所有的阅读记录
     *
     * @return 返回阅读记录集合 ,返回按时间排序后的数据结果
     */
    public List<TaskDataModel> getAllReadRecorder() {
        List<TaskDataModel> list = new ArrayList<>();
        Cursor cursor = DataSupport.findBySQL("select " + sqlAll + " from " + TABLE +" ORDER BY recentlyOpenTime DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TaskDataModel model = new TaskDataModel();
                model.id = cursor.getLong(0);
                model.recentlyOpenTime = cursor.getLong(1);
                model.savePath = cursor.getString(2);
                model.Filename = cursor.getString(3);
                model.FileId = cursor.getLong(4);

                File file=new File(model.savePath);
                if (!file.exists()){
                    deleteData(model.id);
                    continue;
                }
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
    public void saveData(TaskDataModel model) {
        long id=getIndexId( model.FileId);
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
    public long getIndexId( long FileId) {
        long id = -1;
        String sql="select " + sqlId + " from " + TABLE + "  where FileId=" + FileId;
        Cursor cursor = DataSupport.findBySQL(sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                id = cursor.getLong(0);
            }
            cursor.close();
        }
        return id;
    }

    public boolean isHaveDownload(long FileId){
        long id=getIndexId(FileId);
        if (id!=-1){
            TaskDataModel model=DataSupport.find(TaskDataModel.class,id);
            if (model!=null){
                File file=new File(model.getSavePath());
                if (file.exists()){
                    return true;
                }else {
                    DataSupport.delete(TaskDataModel.class,id);
                }
            }
        }
        return false;
    }

    /**
     * 更新数据
     * @param model 更新数据源
     */
    public void updataData(TaskDataModel model, long id){
        ContentValues values = new ContentValues();
        values.put("recentlyOpenTime", model.recentlyOpenTime);
        values.put("savePath", model.savePath);
        values.put("Filename", model.Filename);
        values.put("FileId", model.FileId);
        DataSupport.update(TaskDataModel.class, values, id);
    }

    /**
     * 更新保存时间
     * @param id 数据索引id
     */
    public void upDataTime(long id){
        ContentValues values = new ContentValues();
        values.put("recentlyOpenTime",String.valueOf(System.currentTimeMillis()));
        DataSupport.update(TaskDataModel.class, values, id);
    }
    /**
     * 通过下载路径更新阅读时间
     * @param tearchId 教师id
     * @param FileId 下载id
     */
    public void upDataTime(long tearchId,long FileId){
        long id=getIndexId(FileId);
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
    public TaskDataModel getOneModel(long tearchId, long FileId){
        long id=getIndexId(FileId);
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
    public TaskDataModel getOneModel(long id){
        return DataSupport.find(TaskDataModel.class,id);
    }
    public void deleteData(long id){
        DataSupport.delete(TaskDataModel.class,id);
    }

    /**
     * 删除文件
     * @param savepath
     */
    public void deleteData(String savepath){
        DataSupport.deleteAll(TaskDataModel.class,"savePath=?",savepath);
    }
}
