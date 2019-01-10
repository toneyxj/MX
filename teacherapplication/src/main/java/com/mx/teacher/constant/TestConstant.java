package com.mx.teacher.constant;

/**
 * Created by Archer on 16/9/18.
 */
public class TestConstant {
    //请求主URL
    public static String HOST_URL = "http://120.25.193.163:8088/";
    //获取老师布置的课后作业
    public static String GET_HOME_WORK_LIST = HOST_URL + "app/homework/homeworks/assigned";
    //根据id获取课后作业
    public static String GET_HOME_WORK_BY_ID = HOST_URL + "app/homework/";
    //学生提交课后练习作业
    public static String SUBMIT_ANSWER_BY_ID = HOST_URL + "app/homework/?/answer";
    //图片加载地址
    public static String HOME_WORK_SUBJECTIVE_IMG = HOST_URL + "img/answerImg/?.jpg";
    //教师批复作业
    public static String TEACHER_CHECK_HOME_WORK = HOST_URL + "app/homework/?/comment";
    //是教师或者学生端登陆
    public static boolean IS_TEACHER = false;
}
