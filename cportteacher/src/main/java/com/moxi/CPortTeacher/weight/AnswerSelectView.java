package com.moxi.CPortTeacher.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.CourseAnswerModel;
import com.moxi.classRoom.widget.ProgressView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class AnswerSelectView extends LinearLayout {

    private TextView[] answer_numbers;
    private ProgressView[] progress_ans;

    public AnswerSelectView(Context context) {
        super(context);
        init(context);
    }

    public AnswerSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswerSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.weight_answer_select,
                this);

        answer_numbers = new TextView[4];
        answer_numbers[0] = (TextView) view.findViewById(R.id.answer_numbers_a);
        answer_numbers[1] = (TextView) view.findViewById(R.id.answer_numbers_b);
        answer_numbers[2] = (TextView) view.findViewById(R.id.answer_numbers_c);
        answer_numbers[3] = (TextView) view.findViewById(R.id.answer_numbers_d);

        progress_ans = new ProgressView[4];
        progress_ans[0] = (ProgressView) view.findViewById(R.id.progress_ans_a);
        progress_ans[1] = (ProgressView) view.findViewById(R.id.progress_ans_b);
        progress_ans[2] = (ProgressView) view.findViewById(R.id.progress_ans_c);
        progress_ans[3] = (ProgressView) view.findViewById(R.id.progress_ans_d);
    }

    public void setData(List<CourseAnswerModel> courseAnswers, int subjectId) {
        if (courseAnswers == null)
            courseAnswers = new ArrayList<>();
        int aCount = 0;
        int bCount = 0;
        int cCount = 0;
        int dCount = 0;
        int count = 0;
        for (CourseAnswerModel course : courseAnswers) {
            if (course.getExerciseId() == subjectId) {
                if ("A".equals(course.getStudentAnswer())) {
                    aCount++;
                    count++;
                }
                if ("B".equals(course.getStudentAnswer())) {
                    bCount++;
                    count++;
                }
                if ("C".equals(course.getStudentAnswer())) {
                    cCount++;
                    count++;
                }
                if ("D".equals(course.getStudentAnswer())) {
                    dCount++;
                    count++;
                }
            }
        }
        if (count > 0) {
            answer_numbers[0].setText(getPercentFormat((float) aCount / count, 3, 1));
            answer_numbers[1].setText(getPercentFormat((float) bCount / count, 3, 1));
            answer_numbers[2].setText(getPercentFormat((float) cCount / count, 3, 1));
            answer_numbers[3].setText(getPercentFormat((float) dCount / count, 3, 1));
        } else {
            answer_numbers[0].setText("0.0%");
            answer_numbers[1].setText("0.0%");
            answer_numbers[2].setText("0.0%");
            answer_numbers[3].setText("0.0%");
        }
        progress_ans[0].setMaxNumber(count);
        progress_ans[0].setCurNumber(aCount);
        progress_ans[1].setMaxNumber(count);
        progress_ans[1].setCurNumber(bCount);
        progress_ans[2].setMaxNumber(count);
        progress_ans[2].setCurNumber(cCount);
        progress_ans[3].setMaxNumber(count);
        progress_ans[3].setCurNumber(dCount);
    }

    private String getPercentFormat(double d, int IntegerDigits, int FractionDigits) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }
}
