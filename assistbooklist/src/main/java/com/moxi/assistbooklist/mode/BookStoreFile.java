package com.moxi.assistbooklist.mode;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.Serializable;

/**
 * Created by xj on 2017/6/15.
 */

public class BookStoreFile extends DataSupport implements Serializable {
    @Column(unique = true)
    public long id;
    /**
     * 阅读文件路径
     */
    public String filePath;
    /**
     * 文件路径的md5值
     */
    public String pathMd5;
    /**
     * 阅读文件索引值
     */
    public long _index;

    /**
     * 文件全拼
     */
    public String fullPinyin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long get_index() {
        return _index;
    }

    public void set_index(long _index) {
        this._index = _index;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public File getFile(){
        File file=new File(filePath);
        return file;
    }
    public String getName(){
        File file=getFile();
        return file.getName();
    }
}
