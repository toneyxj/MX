package com.moxi.taskstudent.model;

import com.google.gson.annotations.SerializedName;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.utils.DbDownlaodUtils;

/**
 * 工作作业列表
 * Created by Administrator on 2016/12/21.
 */
public class WorkListModel {
    //信息id
//    @SerializedName("homeWorkId")
    public String id;
    //信息标题
    @SerializedName("name")
    public String title;
    //老师批复状态
    private String taskStatus;
    //文件下载路径
    @SerializedName("file")
    public String downloadFile;
    //回答题目路径
    public String answerFile;
    //老师恢复路径
    public String replyFile;
    //课程名
    public String subjectName;
    //老师名字
    public String teacherName;
    //作业类型
    public String type;

    public String getTaskStatus() {
        if (taskStatus == null) {
            if (!answerFile .equals("") && !replyFile.equals("")) {
                taskStatus = "已批复";
            } else if (!answerFile .equals("")) {
                taskStatus = "未批复";
            } else if (DbDownlaodUtils.getInstance().isSave(Connector.getInstance().getDownloadFileURL(0,downloadFile))) {
                taskStatus = "未完成";
            } else {
                taskStatus = "未下载";
            }
        }
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 获得点击button提示
     *
     * @return
     */
    public String getButtonStr() {
        String value = getTaskStatus();
        String status = "";
        if (value.equals("未下载")) {
            status = "下载";
        } else if (value.equals("未完成")) {
            status = "继续";
        } else if (value.equals("未批复")) {
            status = "打开";
        } else {
            status = "查看";
        }
        return status;
    }

    @Override
    public String toString() {
        return "WorkListModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", downloadFile='" + downloadFile + '\'' +
                ", answerFile='" + answerFile + '\'' +
                ", replyFile='" + replyFile + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
