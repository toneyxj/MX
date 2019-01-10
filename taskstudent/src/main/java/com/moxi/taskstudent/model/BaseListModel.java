package com.moxi.taskstudent.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
public class BaseListModel<t> {
    /**
     *
     */
    public String fromRowId;
    /**
     *当前请求页数
     */
    public int page;
    /**
     *总页数
     */
    public int pageCount;
    /**
     *当前页数据条数
     */
    public int rowCount;
    /**
     *请求每页条数
     */
    public int rows;
    /**
     *解析数据集合
     */
    public List<t> listData;
}
