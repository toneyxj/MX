package com.moxi.studentclient.config;

        import com.moxi.classRoom.information.UserInformation;

/**
 * 接口地址
 * Created by Administrator on 2016/11/4.
 */
public class Connector {
    private static Connector instatnce = null;

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
    public static void ClearData(){
        instatnce=null;
    }
    public final String url="http://"+ UserInformation.getInstance().getDstName()+"/";
    /**
     * 登录接口
     */
    public String loging=url+"user/login";
    public String getStudentCurrentClassWork=url+"classWork/getStudentCurrentClassWork";
    public String commitAnswer=url+"classWork/commitAnswer";

    public String commitClasswork=url+"classWork/commitClasswork";
    public String get_courrentClass=url+"user/lessonState";

    public String getStudentCurrentFallWork=url+"fallWork/getStudentCurrentFallWork";
    public String commitAnswerFall=url+"fallWork/commitAnswer";

    public String getVoteStudents=url+"vote/getStudentCurrentVote";
    public String subVoteUrl=url+"vote/studentVote";
    public String RobAnswerUrl=url+"robAnswer/studentRobAnswer";

    public String commitFallwork=url+"fallWork/commitFallwork";
    public String getBoardUrl=url+"board/getBoard";
    public String BoardImagUrl=url+"boardImg";

    public String studentClassWorkList=url+"record/studentClassWorkList";
    public String studentClassWork=url+"record/studentClassWork";

    public String getQuestion=url+"question/getQuestion";
    public String questionCommitAnswer=url+"question/commitAnswer";

    /**
     * 获得课件列表集合
     */
    public final String getList = url + "courseWare/getList";
    /**
     * 下载课件地址
     */
    public final String courseWareDownload = url + "courseWare/download/";


}
