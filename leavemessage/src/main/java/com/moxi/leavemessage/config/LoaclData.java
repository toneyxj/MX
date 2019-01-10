package com.moxi.leavemessage.config;

import com.moxi.leavemessage.R;

/**
 * Created by qiaokeli on 16/12/22.
 */

public class LoaclData {

    private static LoaclData instatnce;

    public static LoaclData getInstatnce() {
        if (instatnce == null)
            instatnce = new LoaclData();
        return instatnce;
    }
    public int getStudentImg(int index){
        Integer[] images=new Integer[]{R.mipmap.student_ico_001,R.mipmap.student_ico_002,R.mipmap.student_ico_003,R.mipmap.student_ico_004};
        if(index>images.length-1||index<0){
            return images[0];
        }
        return images[index];
    }

    public int getTeachHeadImg(int index){
        Integer[] images=new Integer[]{R.mipmap.teacher_ico_001,R.mipmap.teacher_ico_002,R.mipmap.teacher_ico_003};
        if(index>images.length-1||index<0){
            return images[0];
        }
        return images[index];
    }
}
