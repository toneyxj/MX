package com.moxi.taskteacher.model;

import com.mx.mxbase.model.BaseModel;

import java.util.List;

/**
 * Created by Archer on 2016/12/22.
 */
public class ReviewWorkList extends BaseModel {
    private ReviewWork result;

    public ReviewWork getResult() {
        return result;
    }

    public void setResult(ReviewWork result) {
        this.result = result;
    }

    public class ReviewWork {
        private List<Review> list;

        public List<Review> getList() {
            return list;
        }

        public void setList(List<Review> list) {
            this.list = list;
        }

        public class Review {
            private int id;
            private String time;
            private String subjectName;
            private String answerfile;
            private String file;
            private String name;
            private String studentName;
            private String replyFile;
            private String type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public String getAnswerfile() {
                return answerfile;
            }

            public void setAnswerfile(String answerfile) {
                this.answerfile = answerfile;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getReplyFile() {
                return replyFile;
            }

            public void setReplyFile(String replyFile) {
                this.replyFile = replyFile;
            }
        }
    }
}
