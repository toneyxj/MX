package com.moxi.studentclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moxi.studentclient.bean.AnswerOption;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SubjectUtils {
    public static void setInfoTitle(Context context, String title, TextView view) {
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"](\\s*)(data:image/)\\S+(base64,)([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(title);
        while (m.find()) {
            String str = m.group(4);
            title = title.replace(m.group(), "#@M#@X@" + str + "#@M#@X@");
        }
        String titleResult = title;
        if (titleResult.indexOf("#@M#@X@") > 0) {
            String[] s = titleResult.split("#@M#@X@");
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
//            int i = title.indexOf("<br/>A");
//            String subjectTitle = title;
//            if (i > 0) {
//                subjectTitle = title.substring(0, i);
//            } else if (title.indexOf("<br/>Ａ") > 0) {
//                subjectTitle = title.substring(0, title.indexOf("<br/>Ａ"));
//            }
//
//            Log.d("TAG", "---i:" + i + "---subjectTitle:" + subjectTitle);

            view.setText(Html.fromHtml(title, null, new MxgsaTagHandler(context)));
        }
    }

    /**
     * @param title
     * @param view
     */
    public static void setTitle(Context context, String title, TextView view) {
//        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"](\\s*)(data:image/)\\S+(base64,)([^'\"]+)['\"][^>]*>");
//        Matcher m = p.matcher(title);
//        while (m.find()) {
//            String str = m.group(4);
//            title = title.replace(m.group(), "#@M#@X@" + str + "#@M#@X@");
//        }
//        String titleResult = title;
//        if (titleResult.indexOf("#@M#@X@") > 0) {
//            String[] s = titleResult.split("#@M#@X@");
//            view.setText("");
//            for (int j = 0; j < s.length; j++) {
//                if (j % 2 == 0) {
//                    view.append(Html.fromHtml(s[j]));
//                } else {
//                    Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
//                    ImageSpan imgSpan = new ImageSpan(context, bitmap);
//                    SpannableString spanString = new SpannableString("icon");
//                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    view.append(spanString);
//                }
//            }
//        } else {
        int i = title.indexOf("A.");
        String subjectTitle = title;
        if (i > 0) {
            subjectTitle = title.substring(0, i);
        }
        view.setText(Html.fromHtml(subjectTitle, null, new MxgsaTagHandler(context)));
//        }
    }

    public static void setRadioGroup(Context context, String options, RadioButton radioBtn1, RadioButton radioBtn2, RadioButton radioBtn3, RadioButton radioBtn4) {

        AnswerOption ao = GsonTools.getPerson(options, AnswerOption.class);
        if (ao == null){
            Toast.makeText(context,"数据格式出错",Toast.LENGTH_SHORT).show();
            return;
        }

        List<AnswerOption.Option> option_s = ao.getOptions();
        AnswerOption.Option option = null;
        if (option_s.size() > 0) {
            option = option_s.get(0);
        }
        if (option == null)
            return;

        List<AnswerOption.Answer> answers = option.getABCDS();
        if (answers == null)
            return;
        switch (answers.size()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                radioBtn1.setText(Html.fromHtml(answers.get(0).getId() + "." + answers.get(0).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn2.setText(Html.fromHtml(answers.get(1).getId() + "." + answers.get(1).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn3.setText(Html.fromHtml(answers.get(2).getId() + "." + answers.get(2).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                radioBtn1.setText(Html.fromHtml(answers.get(0).getId() + "." + answers.get(0).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn2.setText(Html.fromHtml(answers.get(1).getId() + "." + answers.get(1).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn3.setText(Html.fromHtml(answers.get(2).getId() + "." + answers.get(2).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn4.setText(Html.fromHtml(answers.get(3).getId() + "." + answers.get(3).getDesc(), null, new MxgsaTagHandler(context)));
                radioBtn4.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void setAnswer(Context context, String studentAnswer, String answer, TextView view) {
        String anserResult = "回答错误";
        if (answer.equals(studentAnswer)) {
            anserResult = "回答正确";
        }
        view.setText(Html.fromHtml("正确答案：" + answer + "  " + anserResult, null, new MxgsaTagHandler(context)));
    }

    public static void setAnalysis(Context context, String analysis, String kNowledge, TextView view) {
        String text = "解释：" + analysis;
        if (TextUtils.isEmpty(analysis)) {
            text = "知识点：" + kNowledge;
        }
        view.setText(Html.fromHtml(text, null, new MxgsaTagHandler(context)));
    }
}
