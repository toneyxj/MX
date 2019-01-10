package com.moxi.taskstudent.config;

/**
 * Created by Administrator on 2016/12/22.
 */
public class Connector {
    private static Connector instatnce = null;
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
    public String userId="4";
    private final String url="http://120.25.193.163:8888/app/";
    private final String downloadFileURL="http://120.25.193.163:8888/";
    /**
     *学生获取布置的作业（学生）：
     */
    public final String getHomeWork=url+"homeWork/getHomeWork";
    /**
     *学生提交作业答案（学生）：
     */
    public final String commitAnswer=url+"homeWork/commitAnswer";
    /**
     *获得资料列表
     */
    public final String getAllSendList=url+"courseWare/getAllSendList";
    /**
     *下载资料
     */
    public final String download=url+"courseWare/download/";

    /**
     * 获得图片地址
     * @param  type 0,题目，1答案，2批复
     * @param filePath
     * @return
     */
    public String getDownloadFileURL(int type,String filePath){
        String myUrl;
        if (type==0){
            myUrl="homeWorkImg";
        }else if(type==1){
            myUrl="homeWorkStudentAnswerImg";
        }else {
            myUrl="homeWorkReplyImg";
        }

        return downloadFileURL+myUrl+filePath;
    }
}
