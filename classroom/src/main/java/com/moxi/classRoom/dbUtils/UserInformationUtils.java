package com.moxi.classRoom.dbUtils;

import android.content.ContentValues;
import android.database.Cursor;

import com.moxi.classRoom.information.UserBaseInformation;
import com.moxi.classRoom.information.UserInformation;
import com.mx.mxbase.constant.APPLog;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/11/2.
 */
public class UserInformationUtils {
    private final String sqlAll = "id,myid,address_IP,name,mobile,password,type,subjectId,school" +
            ",grade,parentMobile,email,headimg,lesson,className,classId";
    private final String TABLE = "UserBaseInformation";
    // 初始化类实列
    private static UserInformationUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static UserInformationUtils getInstance() {
        if (instatnce == null) {
            synchronized (UserInformationUtils.class) {
                if (instatnce == null) {
                    instatnce = new UserInformationUtils();
                }
            }
        }
        return instatnce;
    }

    /**
     * 获得保存的数据个人信息 默认id
     *
     * @return
     */
    public UserBaseInformation getInformation() {
        return getInformation(UserInformation.getInstance().getID());
    }

    /**
     * 获得保存的数据个人信息
     *
     * @param Id 个人信息id
     * @return
     */
    public UserBaseInformation getInformation(long Id) {
        UserBaseInformation information = null;
        Cursor cursor = DataSupport.findBySQL("select "+sqlAll+" from " + TABLE + " where myid=" + Id );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);				/* ID主键 */
                Long myid = cursor.getLong(1);				/* ID主键 */
                String address_IP = cursor.getString(2);				/* ip地址 */
                String name = cursor.getString(3);				/* 用户姓名 */
                String mobile = cursor.getString(4);				/* 手机号码（作为登录用户名） */
                String password = cursor.getString(5);			/* 密码 */
                Integer type = cursor.getInt(6);				/* 用户类型：0-学生，1-老师 */
                Long subjectId = cursor.getLong(7);				/* 用户所教授的课程ID==>外键，引用T_Subject的sub_id（必须type为1该字段才有值） */
                String school = cursor.getString(8);				/* 所在学校 */
                String grade = cursor.getString(9);				/* 所在年级 */
                String parentMobile = cursor.getString(10);		/* 父母电话 */
                String email = cursor.getString(11);				/* 邮箱 */
                String headimg = cursor.getString(12);				/* 用户头像名称（存储在客户端，返回给客户端控制显示） */
                Integer lesson = cursor.getInt(13);				/* 用户是否在上课：0-未上课，1-在上课（必须usr_type为1该字段才有值） */
                String className = cursor.getString(14);    //上课的名字
                long classId = cursor.getLong(15);    //上课的id

                information=new UserBaseInformation();
                information.myid=myid;
                information.address_IP=address_IP;
                information.name=name;
                information.mobile=mobile;
                information.password=password;
                information.type=type;
                information.subjectId=subjectId;
                information.school=school;
                information.grade=grade;
                information.parentMobile=parentMobile;
                information.email=email;
                information.headimg=headimg;
                information.lesson=lesson;
                information.className=className;
                information.classId=classId;
            }
        }
        if (cursor != null) cursor.close();
        return information;
    }

    /**
     * 保存个人信息
     *
     * @param information
     */
    public void saveInformation(UserBaseInformation information) {
        //清除上课数据
        CacheDbUtils.getInstance().deleteFinishClassData();
        long id=getUniqueID(information.myid);
        APPLog.e("id="+id);
        if ( id== -1) {
            information.save();
        } else {
            updateInformation(information);
        }
    }

    /**
     * 修改个人信息
     *
     * @param information
     */
    public void updateInformation(UserBaseInformation information) {
        ContentValues values = new ContentValues();
        values.put("myid", information.myid);
        values.put("address_IP", information.address_IP);
        values.put("name", information.name);
        values.put("mobile", information.mobile);
        values.put("password", information.password);
        values.put("type", information.type);
        values.put("subjectId", information.subjectId);
        values.put("school", information.school);
        values.put("parentMobile", information.parentMobile);
        values.put("email", information.email);
        values.put("headimg", information.headimg);
        values.put("lesson", information.lesson);
        values.put("className", information.className);
        values.put("classId", information.classId);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(information.myid));
    }

    /**
     * 获取保存的数据库id
     * @param ID 唯一id
     * @return
     */
    private long getUniqueID(long ID){
        Cursor cursor=DataSupport.findBySQL("select id from "+TABLE+ " where myid="+ID);
        if (cursor.moveToNext()){
            long mainId=cursor.getLong(0);
            return mainId;
        }
        if (cursor!=null)cursor.close();
        return -1;
    }

    public void setName(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        DataSupport.update(UserBaseInformation.class, values,getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setAddress_IP(String address_IP) {
        ContentValues values = new ContentValues();
        values.put("address_IP", address_IP);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setMobile(String mobile) {
        ContentValues values = new ContentValues();
        values.put("mobile", mobile);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setPassword(String password) {
        ContentValues values = new ContentValues();
        values.put("password", password);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setType(Integer type) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setSubjectId(Long subjectId) {
        ContentValues values = new ContentValues();
        values.put("subjectId", subjectId);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setSchool(String school) {
        ContentValues values = new ContentValues();
        values.put("school", school);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setGrade(String grade) {
        ContentValues values = new ContentValues();
        values.put("grade", grade);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setParentMobile(String parentMobile) {
        ContentValues values = new ContentValues();
        values.put("parentMobile", parentMobile);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setEmail(String email) {
        ContentValues values = new ContentValues();
        values.put("email", email);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setHeadimg(String headimg) {
        ContentValues values = new ContentValues();
        values.put("headimg", headimg);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setLesson(Integer lesson) {
        ContentValues values = new ContentValues();
        values.put("lesson", lesson);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
        if (lesson==0){
            CacheDbUtils.getInstance().deleteFinishClassData();
        }
    }

    public void setClassName(String className) {
        ContentValues values = new ContentValues();
        values.put("className", className);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

    public void setClassId(long classId) {
        ContentValues values = new ContentValues();
        values.put("classId", classId);
        DataSupport.update(UserBaseInformation.class, values, getUniqueID(UserInformation.getInstance().getID()));
    }

}
