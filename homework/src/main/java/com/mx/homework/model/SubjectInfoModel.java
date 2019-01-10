package com.mx.homework.model;

import java.util.List;

/**
 * Created by zhengdelong on 16/9/23.
 */

public class SubjectInfoModel {

    private List<SubjectWrongInfoModel> data;
    //当前页数
    private int page;
    //总共页数
    private int pageCount;

    public List<SubjectWrongInfoModel> getData() {
        return data;
    }

    public void setData(List<SubjectWrongInfoModel> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
