package com.mx.homework.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Archer on 16/9/9.
 */
public class HomeWorkModel extends DataSupport implements Serializable {
    private String chapter;//章节
    private String title;//标题
    private DoneState state;//状态
    private String subject;//科目
    private String teacher;//老师

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DoneState getState() {
        return state;
    }

    public void setState(DoneState state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public enum DoneState {
        NOT_DOWN("未下载", -1), NOT_DONE("未完成", 0), DONE("已完成", 1),
        HADE_READ("已批复", 2);
        private String name;
        private int index;

        private DoneState(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (DoneState c : DoneState.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
