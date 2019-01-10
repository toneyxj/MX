package com.moxi.CPortTeacher.model;

import com.google.gson.annotations.SerializedName;

/**
 * 出题记录数据been
 * Created by Administrator on 2016/11/9.
 */
public class SetSubjectModel {
    /**
     * 记录ID
     */
    @SerializedName("classWorkId")
    public String recordId;
    /**
     * 记录时间
     */
    @SerializedName("timeSpan")
    public String time;
    /**
     * 小时时间
     */
    @SerializedName("classWorkTime")
    public String timeHours;
    /**
     * 日期时间
     */
    @SerializedName("classWorkDate")
    public String timeData;
    /**
     * 记录内容
     */
    @SerializedName("classWorkTitle")
    public String content;
    /**
     * 记录状态
     */
    public String status;
    /**
     * 是否接收练习题
     */
    public boolean isReceive;



}
