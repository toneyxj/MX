package com.moxi.taskstudent.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/12/21.
 */
public class DbDownloadModel extends DataSupport {
    @Column(unique = true)
    public long id;
    public String downlaodPath;
    //当前状态
    public String status="未完成";

    public DbDownloadModel(String downlaodPath) {
        this.downlaodPath = downlaodPath;
    }

    public DbDownloadModel() {
    }
}
