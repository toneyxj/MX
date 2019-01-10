package com.moxi.taskteacher.cache;

import com.google.gson.Gson;
import com.moxi.taskteacher.model.TaskDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据解析集合类
 * Created by Administrator on 2016/12/22.
 */
public class JsonData {
    private static JsonData instatnce = null;
    public static JsonData getInstance() {
        if (instatnce == null) {
            synchronized (JsonData.class) {
                if (instatnce == null) {
                    instatnce = new JsonData();
                }
            }
        }
        return instatnce;
    }
    /**
     * 初始化gson
     */
    private  final Gson gson = new Gson();
    /**
     * gson解析模板
     */
    private String getResult(String json) {
        String result="";
        try {
            JSONObject object=new JSONObject(json);
            result= object.getJSONObject("result").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<TaskDataModel> getTaskDataModels(String result) {
        List<TaskDataModel> list=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj=array.getJSONObject(i);
                long id=obj.getLong("id");
                String file=obj.getString("file");
                TaskDataModel model=new TaskDataModel();
                model.FileId=id;
                model.Filename=file;
                model.setSavePath();
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
