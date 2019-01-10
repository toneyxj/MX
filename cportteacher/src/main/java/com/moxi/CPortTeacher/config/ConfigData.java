package com.moxi.CPortTeacher.config;

import android.content.SharedPreferences;
import android.graphics.Point;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据配置
 * Created by Administrator on 2016/11/1.
 */
public class ConfigData {
    private static ConfigData instatnce = null;
    public final Integer[] photos=new Integer[]{R.mipmap.icon_one,R.mipmap.icon_two,R.mipmap.icon_three,R.mipmap.icon_four};

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static ConfigData getInstance() {
        if (instatnce == null) {
            synchronized (ConfigData.class) {
                if (instatnce == null) {
                    instatnce = new ConfigData();
                }
            }
        }
        return instatnce;
    }

    /**
     * 获得控件绘制点
     *
     * @return
     */
    public List<Point> getPoints() {
        List<Point> values = new ArrayList<>();
        String value = CPortApplication.preferences.getString("pointInScreen", null);
        if (value != null) {
            try {
                JSONArray array = new JSONArray(value);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject ob = array.getJSONObject(i);
                    Point point = new Point(ob.getInt("x"), ob.getInt("y"));
                    values.add(point);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    public int getKitWidth() {
        return CPortApplication.preferences.getInt("kitWidth", 0);
    }

    public int getKitHeight() {
        return CPortApplication.preferences.getInt("kitHeight", 0);
    }

    /**
     * 获得
     *
     * @param points
     */
    public void setPoints(List<Point> points, int kitWidth, int kitHeight) {
        SharedPreferences.Editor editor = CPortApplication.editor;
        JSONArray array = new JSONArray();
        for (Point point : points) {
            JSONObject object = new JSONObject();
            try {
                object.put("x", point.x);
                object.put("y", point.y);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("pointInScreen", array.toString());// 登录名
        editor.putInt("kitWidth", kitWidth);// 登录名
        editor.putInt("kitHeight", kitHeight);// 登录名
        editor.commit();// 提交
    }
}
