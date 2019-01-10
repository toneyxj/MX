package com.moxi.studentclient.dbUtils;

import com.moxi.classRoom.db.CoursewareWhiteBoardModel;
import com.moxi.classRoom.dbUtils.CoursewareWhiteBoardUtils;
import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.studentclient.bean.BoardBean;
import com.moxi.studentclient.bean.ClassSubject;
import com.moxi.studentclient.bean.ConnClassResult;
import com.moxi.studentclient.bean.RequsetMsg.ConnClassMsg;
import com.moxi.studentclient.model.VoteBean;

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
//        JSONObject subject =data.getJSONObject("subject");
//         String className=getString(subject,"name");//上课的名字
//         long classId=getLong(subject,"id");//上课的id

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
//        information.className=className;
//        information.classId=classId;

        UserInformation.getInstance().initBaseInformation(account, password, id, information);
        //通知首页修改登录信息
        // CPortApplication.getBus().post(new OttoBeen("loging", LogingActivity.OTTO_LOGING));
    }

    /**
     * 上课状态 msg 解析
     *
     * @param result
     * @return
     */
    public ConnClassMsg getConnState(String result) {
        ConnClassMsg msg = new ConnClassMsg();
        try {
            JSONObject msgobj = new JSONObject(result);
            msg.setCode(msgobj.getInt("code"));
            msg.setMsg(msgobj.getString("msg"));
            JSONObject resultobj = msgobj.getJSONObject("result");
            if (null != resultobj) {
                ConnClassResult resulte = new ConnClassResult();
                resulte.setHeadimg(resultobj.getString("headimg"));
                JSONObject suject = resultobj.getJSONObject("subject");
                if (null != suject) {
                    ClassSubject csub = new ClassSubject();
                    csub.setName(suject.getString("name"));
                    resulte.setSubject(csub);
                }
                msg.setResult(resulte);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return msg;
    }

    /**
     * 投票列表
      * @param result
     * @return
     */
    public List<VoteBean> getVoteList(String result){
        List<VoteBean> list=new ArrayList<>();
        try {
            JSONObject msgobj = new JSONObject(result);
            JSONArray resultobj=msgobj.getJSONArray("result");
            for (int i=0;i<resultobj.length();i++){
                JSONObject obj=resultobj.getJSONObject(i);
                VoteBean bean=new VoteBean();
                bean.id=obj.getLong("id");
                bean.option=obj.getString("option");
                bean.voteId=obj.getLong("voteId");
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BoardBean getBoardformJson(String result){
        BoardBean bean=new BoardBean();
        try {
            JSONObject msgobj=new JSONObject(result);
            JSONObject resultobj=msgobj.getJSONObject("result");
            bean.time=resultobj.getString("time");
            bean.file=resultobj.getString("file");
            bean.name=resultobj.getString("name");
            return bean;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

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
