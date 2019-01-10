package com.moxi.taskstudent.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moxi.taskstudent.model.BaseListModel;
import com.moxi.taskstudent.model.TaskDataModel;
import com.moxi.taskstudent.model.WorkListModel;

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
    public BaseListModel getMessageBeens(String result) {
        BaseListModel ads = gson.fromJson(result, new TypeToken<BaseListModel>() {
        }.getType());
        return ads;
    }

    public List<WorkListModel> getWorkListModels(String result) {
        List<WorkListModel> ads = gson.fromJson(result, new TypeToken<List<WorkListModel>>() {
        }.getType());
        return ads;
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
