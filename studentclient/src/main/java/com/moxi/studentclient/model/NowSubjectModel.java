package com.moxi.studentclient.model;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 16/9/29.
 */
public class NowSubjectModel extends BaseModel {

    private NowSubject result;

    public NowSubject getResult() {
        return result;
    }

    public void setResult(NowSubject result) {
        this.result = result;
    }

    public static class NowSubject {
        private String answer;//正确答案
        private int id;//题目ID，后续要用来提交答案
        private int option;//选项个数
        private String time;//出题时间
        private String title;//题目图片
        private int type;//题型：1-单选，2-多选，3-主观，4-判断
        private int userId;//出题老师编号
        private String studentAnser;

        public String getStudentAnser() {
            if (studentAnser == null)
                return "";
            return studentAnser;
        }

        public void setStudentAnser(String studentAnser) {
            this.studentAnser = studentAnser;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOption() {
            return option;
        }

        public void setOption(int option) {
            this.option = option;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
