package com.moxi.classRoom.information;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 用户基本信息
 * Created by Administrator on 2016/2/25.
 */
public class UserBaseInformation extends DataSupport{
    @Column(unique = true)
    public Long id;				/* ID主键 */
    public Long myid;
    public String address_IP;				/* ip地址 */
    public String name;			/* 用户姓名 */
    public String mobile;			/* 手机号码（作为登录用户名） */
    public String password;		/* 密码 */
    public Integer type;			/* 用户类型：0-学生，1-老师 */
    public Long subjectId;			/* 用户所教授的课程ID==>外键，引用T_Subject的sub_id（必须type为1该字段才有值） */
    public String school;			/* 所在学校 */
    public String grade;			/* 所在年级 */
    public String parentMobile;	/* 父母电话 */
    public String email;			/* 邮箱 */
    public String headimg;			/* 用户头像名称（存储在客户端，返回给客户端控制显示） */
    public Integer lesson;			/* 用户是否在上课：0-未上课，1-在上课（必须usr_type为1该字段才有值） */
    public String className;//上课的名字
    public long classId;//上课的id

    @Override
    public String toString() {
        return "UserBaseInformation{" +
                "id=" + id +
                ", myid=" + myid +
                ", address_IP='" + address_IP + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", subjectId=" + subjectId +
                ", school='" + school + '\'' +
                ", grade='" + grade + '\'' +
                ", parentMobile='" + parentMobile + '\'' +
                ", email='" + email + '\'' +
                ", headimg='" + headimg + '\'' +
                ", lesson=" + lesson +
                ", className='" + className + '\'' +
                ", classId=" + classId +
                '}';
    }
}
