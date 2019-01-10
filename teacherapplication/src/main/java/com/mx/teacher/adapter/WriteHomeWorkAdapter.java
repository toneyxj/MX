package com.mx.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.ListUtils;
import com.mx.teacher.R;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.entity.ExamList;
import com.mx.teacher.entity.Test;
import com.mx.teacher.entity.XZOption;
import com.mx.teacher.util.MxgsaTagHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 16/9/9.
 */
public class WriteHomeWorkAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<List<ExamList>> listExams;
    private OnItemClickListener onItemClickListener;
    private int page, state;
    private Test test;
    private String url;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 做题适配器
     *
     * @param context    上下文
     * @param url        缓存数据的请求地址
     * @param test       缓存数据对象model
     * @param state      作业完成状态
     * @param resultList 作业回复状态
     * @param page       页码
     */
    public WriteHomeWorkAdapter(Context context, String url, Test test, int state, List<ExamList> resultList, int page) {
        this.context = context;
        this.state = state;
        this.page = page;
        this.url = url;
        this.test = test;
        listExams = ListUtils.splitList(resultList, 3);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_write_home_item, parent, false);
        return new WriteHomeWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (page < 2) {
            if (position == 0 && page == 0) {
                ((WriteHomeWorkViewHolder) (holder)).tvHomeWorkType.setVisibility(View.VISIBLE);
            } else {
                ((WriteHomeWorkViewHolder) (holder)).tvHomeWorkType.setVisibility(View.GONE);
            }
            //选择题选项model
            XZOption xzOption = GsonTools.getPerson(listExams.get(page).get(position).getOption(), XZOption.class);
            //设置标题
            setTitleValue(page, position, ((WriteHomeWorkViewHolder) (holder)).tvHomeWorkTitle);
            //设置选项内容
            List<RadioButton> list = new ArrayList<>();
            list.add(((WriteHomeWorkViewHolder) (holder)).answer1);
            list.add(((WriteHomeWorkViewHolder) (holder)).answer2);
            list.add(((WriteHomeWorkViewHolder) (holder)).answer3);
            list.add(((WriteHomeWorkViewHolder) (holder)).answer4);
            setOptionValue(xzOption, list);

            //设置选项
            switch (listExams.get(page).get(position).getResult().toUpperCase()) {
                case "A":
                    ((WriteHomeWorkViewHolder) (holder)).answer1.setChecked(true);
                    test.getResult().getExamList().get(page * 3 + position).setResult("A");
                    break;
                case "B":
                    ((WriteHomeWorkViewHolder) (holder)).answer2.setChecked(true);
                    test.getResult().getExamList().get(page * 3 + position).setResult("A");
                    break;
                case "C":
                    ((WriteHomeWorkViewHolder) (holder)).answer3.setChecked(true);
                    test.getResult().getExamList().get(page * 3 + position).setResult("A");
                    break;
                case "D":
                    ((WriteHomeWorkViewHolder) (holder)).answer4.setChecked(true);
                    test.getResult().getExamList().get(page * 3 + position).setResult("A");
                    break;
                default:
                    break;
            }

            //根据状态判断是否让radiogroup有点击效果
            if (state != -1) {
                disableRadioGroup(((WriteHomeWorkViewHolder) (holder)).radioGroup);
            }

            //点击radiogroup并改变当前答案
            ((WriteHomeWorkViewHolder) (holder)).radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radio_answer_1:
                            updateAchace("A", position);
                            break;
                        case R.id.radio_answer_2:
                            updateAchace("B", position);
                            break;
                        case R.id.radio_answer_3:
                            updateAchace("C", position);
                            break;
                        case R.id.radio_answer_4:
                            updateAchace("D", position);
                            break;
                    }
                }
            });
        }
    }

    private void updateAchace(String result, int position) {
        test.getResult().getExamList().get(page * 3 + position).setResult(result);
        ACache.get(context).put(url, GsonTools.obj2json(test));
    }

    /**
     * 让radiogroup失去点击效果
     *
     * @param testRadioGroup 目标radiogroup
     */
    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * 设置题目value
     *
     * @param page     页码
     * @param position 当点页面序号
     * @param view     显示题目目标view
     */
    private void setTitleValue(int page, int position, TextView view) {
        String head = "<sub><img align=\"middle\" src=\"data:image/png;base64,";
        String behind = "\" /></sub>";
        String title = listExams.get(page).get(position).getTitle();
        if (title.indexOf(head) > 0 && title.indexOf(behind) > 0) {
            String[] s = title.split(head + "|" + behind);
            view.setText(page * 3 + position + 1 + "、");
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
            view.setText(Html.fromHtml((page * 3 + position + 1) + "、" + listExams.get(page).get(position).getTitle(), null, new MxgsaTagHandler(context)));
        }
    }

    /**
     * 设置选项内容
     *
     * @param xzOption    选项json解析对象
     * @param radioButton 选项显示载体
     */
    private void setOptionValue(XZOption xzOption, List<RadioButton> radioButton) {
        int inttemp = xzOption.getOptions().get(0).getABCDS().size();
        for (int i = 0; i < inttemp; i++) {
            String head = "<sub><img align=\"middle\" src=\"data:image/png;base64,";
            String behind = "\" /></sub>";
            String option1 = xzOption.getOptions().get(0).getABCDS().get(i).getId() + "  " + xzOption.
                    getOptions().get(0).getABCDS().get(i).getDesc();
            if (option1.indexOf(head) > 0 && option1.indexOf(behind) > 0) {
                String[] s = option1.split(head + "|" + behind);
                radioButton.get(i).setVisibility(View.VISIBLE);
                radioButton.get(i).setText("");
                for (int j = 0; j < s.length; j++) {
                    if (j % 2 == 0) {
                        radioButton.get(i).append(Html.fromHtml(s[j]));
                    } else {
                        Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
                        ImageSpan imgSpan = new ImageSpan(context, bitmap);
                        SpannableString spanString = new SpannableString("icon");
                        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        radioButton.get(i).append(spanString);
                    }
                }
            } else {
                String ooo = xzOption.getOptions().get(0).getABCDS().get(i).getId() + "  " + xzOption.
                        getOptions().get(0).getABCDS().get(i).getDesc();
                radioButton.get(i).setVisibility(View.VISIBLE);
                radioButton.get(i).setText(Html.fromHtml(ooo));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (page < 2) {
            return listExams == null ? 0 : listExams.size();
        } else {
            return 1;
        }
    }

    /**
     * 答题viewholder
     */
    class WriteHomeWorkViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHomeWorkType;
        private TextView tvHomeWorkTitle;
        private RadioButton answer1, answer2, answer3, answer4;
        private RadioGroup radioGroup;

        public WriteHomeWorkViewHolder(View itemView) {
            super(itemView);

            tvHomeWorkType = (TextView) itemView.findViewById(R.id.tv_write_home_work_type);
            tvHomeWorkTitle = (TextView) itemView.findViewById(R.id.tv_write_home_work_title);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group_write_home);
            answer1 = (RadioButton) itemView.findViewById(R.id.radio_answer_1);
            answer2 = (RadioButton) itemView.findViewById(R.id.radio_answer_2);
            answer3 = (RadioButton) itemView.findViewById(R.id.radio_answer_3);
            answer4 = (RadioButton) itemView.findViewById(R.id.radio_answer_4);
        }
    }
}
