package com.moxi.CPortTeacher.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengdelong on 2016/11/21.
 */

public class CourseExerciseScore {

    private List<CourseAnswerModel> courseAnswerModels;
    private List<ClassExaminationModel> exerciseModels;

    public List<CourseAnswerModel> getCourseAnswerModels() {
        return courseAnswerModels;
    }

    public void setCourseAnswerModels(List<CourseAnswerModel> courseAnswerModels) {
        this.courseAnswerModels = courseAnswerModels;
    }

    public List<ClassExaminationModel> getExerciseModels() {
        if (exerciseModels == null)
            return new ArrayList<>();
        return exerciseModels;
    }

    public void setExerciseModels(List<ClassExaminationModel> exerciseModels) {
        this.exerciseModels = exerciseModels;
    }
}
