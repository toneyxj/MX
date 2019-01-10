package com.moxi.taskstudent.model;

import com.mx.mxbase.utils.StringUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 课件白板集合类
 * Created by Administrator on 2016/11/30.
 */
public class TaskDataModel extends DataSupport{
    @Column(unique = true)
    public long id;
    /**
     *最近阅读时间
     */
    public long recentlyOpenTime=System.currentTimeMillis();
    /**
     *保存文件的路径
     */
    public String savePath;
    /**
     *文件下载路径
     */
    public String Filename;
    /**
     * 文件id
     */
    public long FileId;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath() {
        if (savePath==null){
            savePath = StringUtils.getSDPath("work")+Filename;
        }
    }

    /**
     * 获得完整的文件名包含后缀
     * @return
     */
    public String getFileName(){
        if (Filename==null)return "";
        return Filename.substring(0,Filename.lastIndexOf("."));
    }
}
