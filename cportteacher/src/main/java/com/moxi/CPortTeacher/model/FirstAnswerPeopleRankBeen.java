package com.moxi.CPortTeacher.model;

import com.google.gson.annotations.SerializedName;
import com.moxi.CPortTeacher.utils.TimeSetUtils;

/**
 * 抢答人员排名
 * Created by Administrator on 2016/11/1.
 */
public class FirstAnswerPeopleRankBeen {
    @SerializedName("usr_headimg")
    public String photo;
    @SerializedName("usr_name")
    public String name;
    @SerializedName("use_time")
    public int time;
    @SerializedName("usr_id")
    public long id;

    public String getTime() {
        return TimeSetUtils.secToTime(time);
    }
}
