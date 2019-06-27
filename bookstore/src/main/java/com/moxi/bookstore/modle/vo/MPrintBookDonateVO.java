package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 纸赠电VO
 *
 * @author duxiaojie
 * @date 2018-08-17 17:14
 */
public class MPrintBookDonateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String description;
    private String startDate;
    private String endDate;
    private Integer presentType;
    private Integer presentNum;
    private String terminal;
    private String uri;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPresentType() {
        return presentType == null ? 0 : presentType;
    }

    public void setPresentType(Integer presentType) {
        this.presentType = presentType;
    }

    public Integer getPresentNum() {
        return presentNum == null ? 0 : presentNum;
    }

    public void setPresentNum(Integer presentNum) {
        this.presentNum = presentNum;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
