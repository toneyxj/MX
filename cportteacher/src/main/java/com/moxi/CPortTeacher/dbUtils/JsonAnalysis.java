package com.moxi.CPortTeacher.dbUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.activity.LogingActivity;
import com.moxi.CPortTeacher.model.FirstAnswerPeopleRankBeen;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.model.SetSubjectModel;
import com.moxi.CPortTeacher.model.Student;
import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.dbUtils.CoursewareWhiteBoardUtils;
import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json数据解析
 * Created by Administrator on 2016/11/4.
 */
public class JsonAnalysis {
    private static JsonAnalysis instatnce = null;
    /**
     * 初始化gson
     */
    private static final Gson gson = new Gson();

    /**
     * 获得解析数据的array字符串
     *
     * @param result 传入完整请求所得
     * @return 返回array字符串
     */
    private String getJsonArray(String result) {

        JSONObject object = null;
        try {
            object = new JSONObject(result);
            JSONArray data = object.getJSONArray("result");
            return data.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "[]";
    }

    /**
     * 数据解析单例模式
     */
    public static JsonAnalysis getInstance() {
        if (instatnce == null) {
            synchronized (JsonAnalysis.class) {
                if (instatnce == null) {
                    instatnce = new JsonAnalysis();
                }
            }
        }
        return instatnce;
    }

    public void setLogingInformation(String account, String password, String IP, String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        JSONObject data = object.getJSONObject("result");

        UserBaseInformation information = new UserBaseInformation();
        Long id = getLong(data, "id");				/* ID主键 */
        String name = getString(data, "name");			/* 用户姓名 */
        String mobile = getString(data, "mobile");	/* 手机号码（作为登录用户名） */
        Integer type = getInt(data, "type");			/* 用户类型：0-学生，1-老师 */
        Long subjectId = getLong(data, "subjectId");			/* 用户所教授的课程ID==>外键，引用T_Subject的sub_id（必须type为1该字段才有值） */
        String school = getString(data, "school");			/* 所在学校 */
        String grade = getString(data, "grade");			/* 所在年级 */
        String parentMobile = getString(data, "parentMobile");	/* 父母电话 */
        String email = getString(data, "email");			/* 邮箱 */
        String headimg = getString(data, "headimg");			/* 用户头像名称（存储在客户端，返回给客户端控制显示） */
        Integer lesson = getInt(data, "lesson");			/* 用户是否在上课：0-未上课，1-在上课（必须usr_type为1该字段才有值） */
        JSONObject subject = data.getJSONObject("subject");
        String className = getString(subject, "name");//上课的名字
        long classId = getLong(subject, "id");//上课的id

        information.myid = id;
        information.address_IP = IP;
        information.name = name;
        information.mobile = mobile;
        information.password = password;
        information.type = type;
        information.subjectId = subjectId;
        information.school = school;
        information.grade = grade;
        information.parentMobile = parentMobile;
        information.email = email;
        information.headimg = headimg;
        information.lesson = lesson;
        information.className = className;
        information.classId = classId;

        UserInformation.getInstance().initBaseInformation(account, password, id, information);
        //通知首页修改登录信息
        CPortApplication.getBus().post(new OttoBeen("loging", LogingActivity.OTTO_LOGING));
    }

    private String getString(JSONObject object, String key) {
        String values = "";
        try {
            values = object.getString(key);
        } catch (JSONException e) {
        }
        if (null == values) values = "";
        return values;
    }

    private int getInt(JSONObject object, String key) {
        int value = 0;
        try {
            value = object.getInt(key);
        } catch (JSONException e) {

        }
        return value;
    }

    private double getDouble(JSONObject object, String key) {
        double value = 0.0;
        try {
            value = object.getDouble(key);
        } catch (JSONException e) {

        }
        return value;
    }

    private long getLong(JSONObject object, String key) {
        long value = 0l;
        try {
            value = object.getLong(key);
        } catch (JSONException e) {

        }
        return value;
    }

    /**
     * 解析学生数据
     */
    public List<Student> getStudents(String result) {
        List<Student> ps = gson.fromJson(getJsonArray(result), new TypeToken<List<Student>>() {
        }.getType());
        return ps;
    }

    /**
     * 解析学生数据
     */
    public List<FirstAnswerPeopleRankBeen> getFirstAnswerPeopleRankBeens(String result) {
        List<FirstAnswerPeopleRankBeen> ps = gson.fromJson(result, new TypeToken<List<FirstAnswerPeopleRankBeen>>() {
        }.getType());
        return ps;
    }

    /**
     * 课堂记录
     */
    public List<SetSubjectModel> getSetSubjectModels(String result) {
        List<SetSubjectModel> ps = gson.fromJson(result, new TypeToken<List<SetSubjectModel>>() {
        }.getType());
        return ps;
    }

    /**
     * 课堂记录
     */
    public List<CoursewareWhiteBoardModel> getCoursewareWhiteBoardModels(String result) {
        List<CoursewareWhiteBoardModel> models = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject data = array.getJSONObject(i);
                CoursewareWhiteBoardModel model = new CoursewareWhiteBoardModel();
                long id = data.getLong("id");
                String file = data.getString("file");

                model.Filename = file;
                model.FileId = id;
                model.setSavePath();
                if (CoursewareWhiteBoardUtils.getInstance().getIndexId( UserInformation.getInstance().getID(),id) == -1) {
                    models.add(model);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return models;
    }

}
