package com.moxi.leavemessage;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mx.mxbase.utils.MXUamManager;

/**
 * Created by qiaokeli on 16/12/22.
 */

public class URL {
    /**
     * 打包时注意：1＼教师端 选择 userpagekageName；学生端选择 userPagekageName
     * 2＼build.gradle applicationId "com.moxi.leavemessage.teacher" 教师端，
     * applicationId "com.moxi.leavemessage.student" 学生端
     */

    private static final String BaseUrl = "http://120.25.193.163:8888";

    private static final String userPagekageName="com.moxi.tuser";
   // private static final String userPagekageName="com.moxi.suser";

    public static final String HISTORY_DATA = BaseUrl + "/app/chat/getChatRecord";
    public static final String addChatDetail = BaseUrl + "/app/chat/addChatDetail";
    public static final String openChat = BaseUrl + "/app/chat/openChat";
    public static final String deleteChat = BaseUrl + "/app/chat/deleteChat";

    public static final String LEAVEMSG_CHOSE_TEACH =  BaseUrl +"/app/chat/getUser?type=1";
    public static final String LEAVEMSG_CHOSE_STUDENT =  BaseUrl +"/app/chat/getUser?type=0";

    public static int userId = 2;
    public static int type = 1;//type＝＝1，教师，2，学生
    public static int img = 0;//头像

    /**
     * code : 0
     * msg : *登录成功
     * result : {"email":"13900000001@qq.com","grade":"高一","headimg":"1","id":1,"lasttime":"2016-12-23 09:49:33","lesson":null,"mobile":"13900000001","name":"赵老师","online":1,"parentMobile":"","password":"e10adc3949ba59abbe56e057f20f883e","school":"重庆一中","subject":{"id":1,"name":"语文"},"subjectId":1,"type":1}
     */

    private int code;
    private String msg;
    private ResultBean result;

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static Object getUser(Context context) {
        String userJson = MXUamManager.querUserBId(context);
        if (TextUtils.isEmpty(userJson)) {

            if (isAppInstalled(context, userPagekageName)) {
                ComponentName componetName = new ComponentName(
                        userPagekageName,
                        "com.mx.user.activity.MXLoginActivity");
                Intent loginIt = new Intent();
                loginIt.setComponent(componetName);
                loginIt.setAction("android.intent.action.MAIN");
                loginIt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(loginIt);
            } else {
                Toast.makeText(context.getApplicationContext(), "登录模块未安装", Toast.LENGTH_SHORT).show();
            }

            return null;
        }
        URL uRLBean = new Gson().fromJson(userJson, URL.class);
        if (uRLBean == null) {
            Toast.makeText(context.getApplicationContext(), "请登录", Toast.LENGTH_SHORT).show();
            return null;
        }
        userId = uRLBean.getResult().getId();
        int headImg = 0;
        if (!TextUtils.isEmpty(uRLBean.getResult().getHeadimg())) {
            headImg = Integer.parseInt(uRLBean.getResult().getHeadimg().substring(0, 1));
        }
        img = headImg;
        type = uRLBean.getResult().getTypeX() == 1 ? 1 : 2;
        return true;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }


    public static class ResultBean {
        /**
         * email : 13900000001@qq.com
         * grade : 高一
         * headimg : 1
         * id : 1
         * lasttime : 2016-12-23 09:49:33
         * lesson : null
         * mobile : 13900000001
         * name : 赵老师
         * online : 1
         * parentMobile :
         * password : e10adc3949ba59abbe56e057f20f883e
         * school : 重庆一中
         * subject : {"id":1,"name":"语文"}
         * subjectId : 1
         * type : 1
         */

        private String email;
        private String grade;
        private String headimg;
        private int id;
        private String lasttime;
        private Object lesson;
        private String mobile;
        private String name;
        private int online;
        private String parentMobile;
        private String password;
        private String school;
        private SubjectBean subject;
        private int subjectId;
        @SerializedName("type")
        private int typeX;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLasttime() {
            return lasttime;
        }

        public void setLasttime(String lasttime) {
            this.lasttime = lasttime;
        }

        public Object getLesson() {
            return lesson;
        }

        public void setLesson(Object lesson) {
            this.lesson = lesson;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getParentMobile() {
            return parentMobile;
        }

        public void setParentMobile(String parentMobile) {
            this.parentMobile = parentMobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public SubjectBean getSubject() {
            return subject;
        }

        public void setSubject(SubjectBean subject) {
            this.subject = subject;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public int getTypeX() {
            return typeX;
        }

        public void setTypeX(int typeX) {
            this.typeX = typeX;
        }

        public static class SubjectBean {
            /**
             * id : 1
             * name : 语文
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
