package com.moxi.studentclient.bean;

import com.moxi.studentclient.model.AnswerHistorybean;
import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class WorkListResult extends BaseModel {
    private ResultList result;

    public ResultList getResult() {
        return result;
    }

    public void setResult(ResultList result) {
        this.result = result;
    }

    public class ResultList {
        private int fromRowId;
        private int page;
        private int pageCount;
        private int rowCount;
        private int rows;
        private List<AnswerHistorybean> list;

        public int getFromRowId() {
            return fromRowId;
        }

        public void setFromRowId(int fromRowId) {
            this.fromRowId = fromRowId;
        }

        public List<AnswerHistorybean> getList() {
            return list;
        }

        public void setList(List<AnswerHistorybean> list) {
            this.list = list;
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

        public int getRowCount() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }
    }
}
