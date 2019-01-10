package com.moxi.classRoom.dbUtils;

import android.content.ContentValues;
import android.database.Cursor;

import com.moxi.classRoom.db.CacheModel;

import org.litepal.crud.DataSupport;

/**
 * 本地缓存处理类
 * Created by Administrator on 2016/10/18.
 */
public class CacheDbUtils {
    private final long hour = 1000 * 3600l;
    private final String sqlAll = "id,code,json,time";
    private final String sqlId = "id";
    private final String sqlJson = "json,time";
    private final String TABLE = "CacheModel";
    // 初始化类实列
    private static CacheDbUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static CacheDbUtils getInstance() {
        if (instatnce == null) {
            synchronized (CacheDbUtils.class) {
                if (instatnce == null) {
                    instatnce = new CacheDbUtils();
                    instatnce.CheckDb();
                }
            }
        }
        return instatnce;
    }

    /**
     * 检查数据库数据
     */
    private void CheckDb() {
        long currenTime = System.currentTimeMillis();
        Cursor cursor = DataSupport.findBySQL("select " + sqlId + " from " + TABLE + " where time<" + currenTime + "");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                DataSupport.delete(CacheModel.class, id);
            }
        }
        if (cursor != null) cursor.close();
    }
    /**
     * 删除下课数据
     */
    public void deleteFinishClassData() {
        Cursor cursor = DataSupport.findBySQL("select " + sqlId + " from " + TABLE + " where time=" + Long.MAX_VALUE + "");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                DataSupport.delete(CacheModel.class, id);
            }
        }
        if (cursor != null) cursor.close();
    }

    /**
     * 永远保存
     *
     * @param code
     * @param json
     */
    public void saveData(String code, String json) {
        CacheModel cacheModel = judjeData(code);
        if (cacheModel == null) {
            CacheModel model = new CacheModel(code, json);
            model.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("json", json);
            DataSupport.update(CacheModel.class, values, cacheModel.id);
        }
    }

    /**
     * 有时间限制的保存
     *
     * @param code 保存的code唯一标记值
     * @param json 保存的字符串
     * @param time 保存时间 以毫秒为单位
     */
    public void saveData(String code, String json, long time) {
        CacheModel cacheModel = judjeData(code);
        if (cacheModel == null) {
            CacheModel model = new CacheModel(code, json, System.currentTimeMillis() + time);
            model.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("json", json);
            values.put("time", System.currentTimeMillis() + time);
            DataSupport.update(CacheModel.class, values, cacheModel.id);
        }
    }

    public void saveModel(CacheModel model) {
        saveData(model.code, model.json, model.time);
    }

    /**
     * 有时间限制的保存，保存一小时
     *
     * @param code 保存的code唯一标记值
     * @param json 保存的字符串
     */
    public void saveHourData(String code, String json) {
        saveData(code, json, hour);
    }

    /**
     * 有时间限制的保存，保存一天
     *
     * @param code 保存的code唯一标记值
     * @param json 保存的字符串
     */
    public void saveDayData(String code, String json) {
        saveData(code, json, hour * 24);
    }

    /**
     * 有时间限制的保存，保存5小时
     *
     * @param code 保存的code唯一标记值
     * @param json 保存的字符串
     */
    public void saveFiveHourData(String code, String json) {
        saveData(code, json, hour * 5);
    }

    /**
     * 保存到下课
     *
     * @param code 保存的code唯一标记值
     * @param json 保存的字符串
     */
    public void saveFinshClassData(String code, String json) {
        CacheModel cacheModel = judjeData(code);
        if (cacheModel == null) {
            CacheModel model = new CacheModel(code, json, Long.MAX_VALUE);
            model.save();
        } else {
            ContentValues values = new ContentValues();
            values.put("json", json);
            values.put("time", Long.MAX_VALUE);
            DataSupport.update(CacheModel.class, values, cacheModel.id);
        }
    }

    /**
     * 获得保存的json数据
     *
     * @param code
     * @return
     */
    public String getJsonData(String code) {
        String content = "";
        Cursor cursor = DataSupport.findBySQL("select " + sqlJson + " from " + TABLE + " where code='" + code + "' order by time desc");
        if (cursor.moveToNext()) {
            long time = cursor.getLong(1);
            if (time > System.currentTimeMillis() || time == -1) {
                content = cursor.getString(0);
            }
        }
        if (cursor != null) cursor.close();
        return content;
    }

    public void deleteData(String code) {
        CacheModel model = judjeData(code);
        if (model != null) {
            deleteData(model.id);
        }
    }

    public void deleteData(long id) {
        DataSupport.delete(CacheModel.class, id);
    }

    /**
     * 判断数据是否已近缓存
     *
     * @param code
     * @return
     */
    public CacheModel judjeData(String code) {
        CacheModel model = null;
        Cursor cursor = DataSupport.findBySQL("select " + sqlAll + " from " + TABLE + " where code='" + code + "' order by time desc");
        if (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String codes = cursor.getString(1);
            String json = cursor.getString(2);
            long time = cursor.getLong(3);
            model = new CacheModel(id, codes, json, time);
        }
        if (cursor != null) cursor.close();
        return model;
    }
}
