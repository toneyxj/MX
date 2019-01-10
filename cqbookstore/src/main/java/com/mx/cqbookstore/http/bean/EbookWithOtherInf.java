package com.mx.cqbookstore.http.bean;

/**
 * Created by Administrator on 2016/12/29.
 */

public class EbookWithOtherInf extends EbookDetail {
    private Integer Delflag;
    private Integer ReleaseState;
    private Integer PageCount;
    private Integer Epagecount;
    private String EpubPath;
    private String EpubKey;
    private Integer Isencrypt;
    private String Reamrk;

    public Integer getDelflag() {
        return Delflag;
    }

    public void setDelflag(Integer delflag) {
        Delflag = delflag;
    }

    public Integer getReleaseState() {
        return ReleaseState;
    }

    public void setReleaseState(Integer releaseState) {
        ReleaseState = releaseState;
    }

    public Integer getPageCount() {
        return PageCount;
    }

    public void setPageCount(Integer pageCount) {
        PageCount = pageCount;
    }

    public Integer getEpagecount() {
        return Epagecount;
    }

    public void setEpagecount(Integer epagecount) {
        Epagecount = epagecount;
    }

    public String getEpubPath() {
        return EpubPath;
    }

    public void setEpubPath(String epubPath) {
        EpubPath = epubPath;
    }

    public String getEpubKey() {
        return EpubKey;
    }

    public void setEpubKey(String epubKey) {
        EpubKey = epubKey;
    }

    public Integer getIsencrypt() {
        return Isencrypt;
    }

    public void setIsencrypt(Integer isencrypt) {
        Isencrypt = isencrypt;
    }

    public String getReamrk() {
        return Reamrk;
    }

    public void setReamrk(String reamrk) {
        Reamrk = reamrk;
    }
}
