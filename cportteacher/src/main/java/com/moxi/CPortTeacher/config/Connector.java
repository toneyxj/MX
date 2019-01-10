package com.moxi.CPortTeacher.config;

import com.moxi.classRoom.information.UserInformation;

/**
 * 接口地址
 * Created by Administrator on 2016/11/4.
 */
public class Connector {

    private static Connector instatnce = null;
//    public static String MXHOST = "http://192.168.0.254";

    /**
     * 接口单例模式数据
     */
    public static Connector getInstance() {
        if (instatnce == null) {
            synchronized (Connector.class) {
                if (instatnce == null) {
                    instatnce = new Connector();
                }
            }
        }
        return instatnce;
    }

    public void ClearData() {
        instatnce = null;
    }

    private final String url = "http://" + UserInformation.getInstance().getDstName() + "/";
    public final String HOST = url + "classWorkImg";


    /**
     * 登录接口
     */
    public final String loging = url + "user/login";
    /**
     * 开始上课
     */
    public final String startLesson = url + "user/startLesson";
    /**
     * 结束上课
     */
    public final String endLesson = url + "user/endLesson";
    /**
     * 上课记录
     */
    public final String teacherClassWorkList = url + "record/teacherClassWorkList";
    //获取在线学生
    public final String getStudenturl = url + "classWork/getStudent";
    //获取淘汰答题题目数据
    public final String getExamsBySubjectId = url + "fallWork/getCourseExercise?subjectId=";
    //开始答题(淘汰答题)
    public String startFallWork = url + "fallWork/startFallWork";
    //获取答题进度（淘汰答题）
    public String getFallWorkProgess = url + "fallWork/getFallWorkProgess";
    //结束答题（淘汰答题）
    public String endFallWork = url + "fallWork/endFallWork";
    //查看答题结果（淘汰答题）
    public String getFallWorkScore = url + "fallWork/getFallWorkScoreList";
    /**
     * 获获得投票进度
     */
    public final String getVoteProgress = url + "vote/getVoteProgress";
    /**
     * 发起投票
     */
    public final String startVote = url + "vote/startVote";
    /**
     * 结束投票
     */
    public final String endVote = url + "vote/endVote";
    /**
     * 开始抢答
     */
    public final String startRobAnswer = url + "robAnswer/startRobAnswer";
    /**
     * 获取抢答结果
     */
    public final String getRobAnswerResult = url + "robAnswer/getRobAnswerResult";
    /**
     * 结束抢答
     */
    public final String endRobAnswer = url + "robAnswer/endRobAnswer";
    /**
     * 发送白板
     */
    public final String boardSend = url + "board/send";
    /**
     * 获得课件列表集合
     */
    public final String getList = url + "courseWare/getList";
    /**
     * 下载课件地址
     */
    public final String courseWareDownload = url + "courseWare/download/";

    public final String roomTestData = url + "/classWork/getCourseExercise";
    public final String sendAllTestData = url + "/classWork/startClassWork";
    public final String getAllStudent = url + "/classWork/getStudent";
    public final String sendSomeStudent = url + "/classWork/startClassWork";
    public final String answeringStudent = url + "/classWork/getClassWorkProgess";
    public final String endClassWork = url + "/classWork/endClassWork";
    public final String getKeguanScore = url + "/classWork/getClassWorkScoreList";
    public final String getClassWorkIdScore = url + "/record/teacherClassWork";
    public final String getSubjectiveExerciseList = url + "/classWork/getSubjectiveExerciseList";
    public final String addSubjectiveExerciseScore = url + "/classWork/addSubjectiveExerciseScore";
    public final String getCourseExerciseScoreList = url + "/classWork/getCourseExerciseScoreList";
    public final String getStudentScoreList = url + "/classWork/getStudentScoreList";
    public final String addQuestion = url + "/question/addQuestion";
    public final String getQuestionResult = url + "/question/getQuestionResult";
    public final String thesugestionImageUrl = url + "questionImg/";
    public final String questionAnswerImg = url + "questionAnswerImg/";


    public String getUrl() {
        return url;
    }
}
