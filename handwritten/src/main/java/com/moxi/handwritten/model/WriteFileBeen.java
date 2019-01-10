package com.moxi.handwritten.model;

import java.io.Serializable;

/**
 * 写字板文件数据集合
 * Created by Administrator on 2016/8/2.
 */
public class WriteFileBeen implements Serializable {
    public String type;//文件类型0代表文件夹，1代表文件,-1代表新增界面
    public String image;//type=0文件的保存图片本地地址
    public String fileName;//文件名
    public String createDate="";//文件创建日期
    public String PathsOrName;//1文件的路径文件保存地址,0新建表名
//    public String foloderName;//如果当type=1时需要传入创建的数据库名称
    public boolean isSelect=false;//是否选中
public void changeSelect(){
    isSelect=!isSelect;
}
    public WriteFileBeen(String type, String fileName) {
        this.type = type;
        this.fileName=fileName;
    }

    public WriteFileBeen(String type, String image, String fileName, String createDate, String pathsOrName) {
        this.type = type;
        this.image = image;
        this.fileName = fileName;
        this.createDate = createDate;
        PathsOrName = pathsOrName;
    }

    @Override
    public String toString() {
        return "WriteFileBeen{" +
                "type='" + type + '\'' +
                ", image='" + image + '\'' +
                ", fileName='" + fileName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", PathsOrName='" + PathsOrName + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
