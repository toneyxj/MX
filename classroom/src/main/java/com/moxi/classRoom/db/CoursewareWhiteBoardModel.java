package com.moxi.classRoom.db;

import com.moxi.classRoom.information.UserInformation;
import com.mx.mxbase.utils.StringUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 课件白板集合类
 * Created by Administrator on 2016/11/30.
 */
public class CoursewareWhiteBoardModel extends DataSupport{
    @Column(unique = true)
    public long id;
    /**
     *教师id
     */
    public long tearchId=UserInformation.getInstance().getID();
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
            savePath = StringUtils.getSDPath("classRoom/"+ UserInformation.getInstance().getID())+Filename;
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

    @Override
    public String toString() {
        return "CoursewareWhiteBoardModel{" +
                "id=" + id +
                ", tearchId=" + tearchId +
                ", recentlyOpenTime=" + recentlyOpenTime +
                ", savePath='" + savePath + '\'' +
                ", Filename='" + Filename + '\'' +
                ", FileId=" + FileId +
                '}';
    }
}
