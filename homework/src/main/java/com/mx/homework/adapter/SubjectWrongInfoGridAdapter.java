package com.mx.homework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.homework.R;
import com.mx.homework.activity.WrongSubjectInfoActivity;
import com.mx.homework.model.SubjectAnswerInfoModel;
import com.mx.homework.model.SubjectModel;
import com.mx.homework.model.SubjectWrongInfoModel;
import com.mx.homework.model.SubjectWrongModel;
import com.mx.homework.util.Base64Utils;
import com.mx.homework.util.MxgsaTagHandler;
import com.mx.homework.util.XZOption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class SubjectWrongInfoGridAdapter extends BaseAdapter {

    private List<SubjectWrongInfoModel> data = new ArrayList<SubjectWrongInfoModel>();
    private Context context;

    public SubjectWrongInfoGridAdapter(Context context, List<SubjectWrongInfoModel> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_write_home_item,null);
            viewHolder.tv_write_home_work_title = (TextView) view.findViewById(R.id.tv_write_home_work_title);
            viewHolder.show_info_rel = (RelativeLayout) view.findViewById(R.id.show_info_rel);
            viewHolder.answers_riadio = (RadioGroup) view.findViewById(R.id.answers_riadio);
            viewHolder.show_info = (TextView) view.findViewById(R.id.show_info);
            viewHolder.right_answer = (TextView) view.findViewById(R.id.right_answer);
            viewHolder.sum_answer = (TextView) view.findViewById(R.id.sum_answer);
            viewHolder.answer  = (TextView) view.findViewById(R.id.answer);
            List<SubjectAnswerInfoModel> list = data.get(position).getOption();
            for (int i = 0;i<list.size();i++){
                List<SubjectModel> subjectModels = list.get(i).getAns();
                for (int j = 0;j<subjectModels.size();j++){
                    subjectModels.get(j).getId();
                    String id = subjectModels.get(j).getId();
                    String desc = subjectModels.get(j).getDesc();
                    String wrongAnswer = data.get(position).getWrongAnswer();
                    if(wrongAnswer.contains(id)){
                        addRiadioView(viewHolder.answers_riadio,desc,true,subjectModels);
                    }else{
                        addRiadioView(viewHolder.answers_riadio,desc,false,subjectModels);
                    }
                }
            }

            viewHolder.show_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.show_info_rel.getVisibility() == View.GONE){
                        viewHolder.show_info_rel.setVisibility(View.VISIBLE);
                        viewHolder.show_info.setText("收起解析");
                    }else{
                        viewHolder.show_info_rel.setVisibility(View.GONE);
                        viewHolder.show_info.setText("查看解析");
                    }
                }
            });

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        //标题
        setTitleValue(data.get(position).getTitle(),viewHolder.tv_write_home_work_title);
        viewHolder.right_answer.setText("正确答案:" + data.get(position).getAnswer() + "   回答错误");
        if (data.get(position).getAllCount() <= 0 ){
            viewHolder.sum_answer.setVisibility(View.GONE);
        }else{
            double ret = div(data.get(position).getRightCount(),data.get(position).getAllCount(),2);
            viewHolder.sum_answer.setText("本题共被作答" + data.get(position).getAllCount() + "次, 正确率" +
                    (int)(ret*100) + "%");
        }

        viewHolder.answer.setText("解析:" + data.get(position).getAnalysis());
        return view;
    }

    static class ViewHolder{
        TextView tv_write_home_work_title;
        RadioGroup answers_riadio;
        TextView show_info;
        TextView right_answer;
        TextView sum_answer;
        TextView answer;
        RelativeLayout show_info_rel;
    }

    private void addRiadioView(RadioGroup answers_riadio,String text,boolean active,List<SubjectModel> subjectModels){
        RadioButton tempButton = new RadioButton(context);
        tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
        //选项文字
        tempButton.setText(text);
        tempButton.setChecked(active);
        answers_riadio.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setOptionValue(subjectModels,answers_riadio);
    }

    private void setTitleValue(String str, TextView view) {
            String head = "<sub><img align=\"middle\" src=\"data:image/png;base64,";
            String behind = "\" /></sub>";
            String title = str;
            if (title.indexOf(head) > 0 && title.indexOf(behind) > 0) {
                String[] s = title.split(head + "|" + behind);
                view.setText("");
                for (int j = 0; j < s.length; j++) {
                    if (j % 2 == 0) {
                        view.append(Html.fromHtml(s[j]));
                    } else {
                        Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
                        ImageSpan imgSpan = new ImageSpan(context, bitmap);
                        SpannableString spanString = new SpannableString("icon");
                        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        view.append(spanString);
                    }
                }
            } else {
                view.setText(Html.fromHtml(str, null, new MxgsaTagHandler(context)));
            }
    }

    /*
     * 设置选项内容
     *
     * @param xzOption    选项json解析对象
     * @param radioButton 选项显示载体
     */
    private void setOptionValue(List<SubjectModel> subjectModel, RadioGroup radioGroup) {
        for (int i = 0; i < subjectModel.size(); i++) {
            String head = "<sub><img align=\"middle\" src=\"data:image/png;base64,";
            String behind = "\" /></sub>";
            String option1 =
                    subjectModel.get(i).getId()
                            + "  " +
                            subjectModel.get(i).getDesc();
            if (option1.indexOf(head) > 0 && option1.indexOf(behind) > 0) {
                String[] s = option1.split(head + "|" + behind);
                ((RadioButton)radioGroup.getChildAt(i)).setText("");
                for (int j = 0; j < s.length; j++) {
                    if (j % 2 == 0) {
                        ((RadioButton)radioGroup.getChildAt(i)).append(s[j]);
                    } else {
                        Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
                        ImageSpan imgSpan = new ImageSpan(context, bitmap);
                        SpannableString spanString = new SpannableString("icon");
                        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ((RadioButton)radioGroup.getChildAt(i)).append(spanString);
                    }
                }
            } else {
                ((RadioButton)radioGroup.getChildAt(i)).setText(Html.fromHtml(subjectModel.get(i).getId() + "  "
                        + subjectModel.get(i).getDesc()));
            }
        }
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
