package com.moxi.taskstudent.taskInterface;

import com.moxi.taskstudent.model.WorkListModel;

/**
 * 主界面接口
 * Created by Administrator on 2016/12/22.
 */
public interface MainInterface {
    /**
     * 点击打开查看   ，老师已批复
     */
    void clickWorkCheck(WorkListModel model);

    /**
     * 打开 ，老师未批复
     */
    void clickWorkOpen(WorkListModel model);

    /**
     *下载了未做题
     */
    void clickWorkContinue(WorkListModel model);

    /**
     * 查看批复
     * @param model
     */
    void CheckReplay(WorkListModel model);

    /**
     * 提交作业
     */
    void SubimitWork(WorkListModel model);

    /**
     * 关闭回到作业列表界面
     */
    void closeWork();

    /**
     * 打开pdf
     * @param openPath
     */
    void openPdf(String openPath);

    void onDialogShowOrHide(boolean is,String  hitn);
}
