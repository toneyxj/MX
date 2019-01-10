package com.mx.cqbookstore.http.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class DBebookbean extends DataSupport implements Serializable {
    @Column(unique = true)
    public long id;
    public String bookName;
    public String author;
    public Double downprogress;
    public String readprogress="0.00%";
    public String path;
    public String resouceId;
    public Integer isBought;//1:已购买 0:未购买
    public long downTime;
    public String downUrl;
    public long readTime;
    public String publisher;
    public String pubTime;
    public String bookCover;//封面
    public String epubKey;
    public DBebookbean() {
    }


}
