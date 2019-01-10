package com.moxi.taskteacher.httpconstans;

/**
 * Created by Archer on 2016/12/22.
 */
public class TeaContsans {
    //主地址
//    public static String HTTP_HOST = "http://192.168.0.254:8080/";
    public static String HTTP_HOST = "http://120.25.193.163:8888/";
    //获取所有作业
    public static String GET_ALL_WORK = HTTP_HOST + "app/homeWork/getAllHomeWork";
    //老师获取批复数据
    public static String GET_REVIEW_WORK = HTTP_HOST + "/app/homeWork/getAllHomeWorkReply";
    //获取所有学生
    public static String GET_ALL_STUDENT = HTTP_HOST + "/app/user/getStudent";
    //全部发送
    public static String SEND_TO_ALL = HTTP_HOST + "/app/homeWork/sendHomeWork";
    //老师批复作业
    public static String REVIEW_HOME_WORK = HTTP_HOST + "/app/homeWork/commitReply";
    //老师获取所有可见
    public static String GET_ALL_COURSEWARE = HTTP_HOST + "app/courseWare/getAllList";
    /**
     *下载资料
     */
    public static final String download=HTTP_HOST+"app/courseWare/download/";
    //题目图片拼接路径
    public static String TITLE_IMAGE_PATH = HTTP_HOST + "homeWorkImg";
    //答案图片拼接路径
    public static String ANSWER_IMAGE_PATH = HTTP_HOST + "homeWorkStudentAnswerImg";
    //老师端查看批复作业路径
    public static String TEACHER_REVIEW_PATH = HTTP_HOST + "homeWorkReplyImg";
}
