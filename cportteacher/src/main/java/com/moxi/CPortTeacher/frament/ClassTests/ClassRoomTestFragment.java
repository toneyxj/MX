package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassExaminationAdapter;
import com.moxi.CPortTeacher.adapter.newrev.OptionAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassExaminationModel;
import com.moxi.CPortTeacher.model.OptionsModel;
import com.moxi.CPortTeacher.utils.MxgsaTagHandler;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * 随堂测试
 * Created by Administrator on 2016/10/28.
 */
@SuppressLint("ValidFragment")
public class ClassRoomTestFragment extends CportBaseFragment {

    ClassRoomSeclectFragment classRoomSeclectFragment = null;

    private String answerIngRequestCode = "3";
    private String classRoomRequestCode = "1";
    private String sendAllRequestCode = "2";

    @Bind(R.id.sec_grid)
    GridView sec_grid;

    @Bind(R.id.info)
    TextView info;

    @Bind(R.id.send_all)
    Button send_all;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.send_some)
    Button send_some;

    @Bind(R.id.lv_option)
    ListView lvOption;

    private List<ClassExaminationModel> classExaminationModels = new ArrayList<ClassExaminationModel>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassRoomTestFragment() {

    }

    public ClassRoomTestFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack) {
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_room_test;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sec_grid.setOnItemClickListener(onItemClickListener);
//        initData();
        initView();
        getAnswerData();
        classRoomSeclectFragment = new ClassRoomSeclectFragment(showFragmentCallBack);
        this.onRefuresh();
    }

    private void getRoomTestData() {
        long testId = UserInformation.getInstance().getUserInformation().classId;
        Log.d("class", "testId:" + testId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("subjectId", testId + ""));
        getData(valuePairs, classRoomRequestCode, Connector.getInstance().roomTestData, true);
    }

    private void getAnswerData() {
        long userId = UserInformation.getInstance().getUserInformation().myid;
        Log.d("class", "userId:" + userId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", userId + ""));
        getData(valuePairs, answerIngRequestCode, Connector.getInstance().answeringStudent, false);
    }

    private void setSendAllTestData() {
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", userId + ""));
        getData(valuePairs, sendAllRequestCode, Connector.getInstance().sendAllTestData, true);
    }

    @Override
    public void onSuccess(String result, String code) {

        Log.d("class", "result==>" + result + "   code====>" + code);
        if (code.equals(classRoomRequestCode)) {
            List<ClassExaminationModel> classExaminationModel = parseData(result);
            Log.d("class", "title==>" + classExaminationModel.get(0).getTitle());
            setData();
        } else if (code.equals(sendAllRequestCode)) {

            boolean state = parseSendAllStudentData(result);
            if (state) {
                showFragmentCallBack.show(new ClassRoomAnswerFragment(showFragmentCallBack), "ClassRoomAnswerFragment");
            }

        } else if (code.equals(answerIngRequestCode)) {
            //当前是否正在答题
            boolean isAnswer = parseAnswerData(result);
            if (isAnswer) {
                Log.d("class", "isAnswer is true..");
                showFragmentCallBack.show(new ClassRoomAnswerFragment(showFragmentCallBack), "ClassRoomAnswerFragment");
            }
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        Log.d("class", "code==>" + code + "   failCode====>" + failCode + "   msg====>" + msg);
        if (code.equals(answerIngRequestCode)) {
            Log.d("class", "isAnswer is false..");
            getRoomTestData();
        }
        super.onFail(code, showFail, failCode, msg);
    }

    private void initView() {
        send_some.setOnClickListener(this);
        send_all.setOnClickListener(this);
    }

    private void setData() {
        ClassExaminationAdapter classExaminationAdapter = new ClassExaminationAdapter(getContext(), classExaminationModels);
        sec_grid.setAdapter(classExaminationAdapter);
        sec_grid.requestFocus();
        sec_grid.setSelection(0);
        if (classExaminationModels.size() > 0) {
            setTitle(classExaminationModels.get(0).getTitle(), info);
            setOptionAdapter(0);
        }
        String className = UserInformation.getInstance().getUserInformation().className;
        int sumSub = classExaminationModels.size();
        title.setText(className + "试题    共" + sumSub + "题");
    }

    /**
     * @param title
     * @param view
     */
    private void setTitle(String title, TextView view) {
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
                    ImageSpan imgSpan = new ImageSpan(getContext(), bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.append(spanString);
                }
            }
        } else {
            view.setText(Html.fromHtml(title, null, new MxgsaTagHandler(getContext())));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_some) {
            // TODO: 2016/11/2 选择发送
            Log.d("class", "选择发送 click");
//            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
//            fm.beginTransaction().remove(this).addToBackStack(null).add(new TestFragment(),"select").commit();
            if (showFragmentCallBack == null) {
                Log.d("class", "showFragmentCallBack showFragmentCallBack is null");
            }
            showFragmentCallBack.show(classRoomSeclectFragment, "ClassRoomSeclectFragment");
        } else if (v.getId() == R.id.send_all) {
            setSendAllTestData();
        }
    }

    @Override
    public void onclickBack() {
        super.onclickBack();
        classRoomSeclectFragment.onclickBack();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            String sub = classExaminationModels.get(position).getTitle();
            setTitle(sub, info);

            setOptionAdapter(position);

            for (int i = 0; i < classExaminationModels.size(); i++) {
                View view1 = adapterView.getChildAt(i);
                TextView textView = (TextView) view1.findViewById(R.id.inf);
                textView.setBackgroundResource(R.drawable.qimu_backgroundshape);
                textView.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            TextView textView = (TextView) view.findViewById(R.id.inf);
            textView.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
            textView.setTextColor(getResources().getColor(R.color.colorWithe));
        }
    };

    /**
     * 设置选项适配器
     *
     * @param position
     */
    private void setOptionAdapter(int position) {
        String optionsJson = classExaminationModels.get(position).getOption();
        OptionsModel optionsModel = GsonTools.getPerson(optionsJson, OptionsModel.class);
        try {
            lvOption.setVisibility(View.VISIBLE);
            OptionAdapter adapter = new OptionAdapter(getActivity(), optionsModel.getOptions().get(0).getABCDS());
            lvOption.setAdapter(adapter);
        } catch (Exception e) {
            lvOption.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d("class", "onDestroyView...");
        if (classExaminationModels != null) {
            classExaminationModels.clear();
        }
        super.onDestroyView();
    }

    private boolean parseSendAllStudentData(String jsonData) {
        boolean state = false;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                state = true;
            }
        } catch (Exception e) {

        }
        return state;
    }

    private boolean parseAnswerData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private List<ClassExaminationModel> parseData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                ClassExaminationModel classExaminationModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    classExaminationModel = new ClassExaminationModel();
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    String answer = jsonObject1.optString("answer", "");
                    String analysis = jsonObject1.optString("analysis", "");
                    int id = jsonObject1.optInt("id", -1);
                    String knowledge = jsonObject1.optString("knowledge", "");
                    int subjectId = jsonObject1.optInt("subjectId", -1);
                    String title = jsonObject1.optString("title", "");
                    int type = jsonObject1.optInt("type", -1);
                    String option = jsonObject1.optString("option");

                    classExaminationModel.setId_nums((i + 1) + "");
                    classExaminationModel.setId(id);
                    classExaminationModel.setAnalysis(analysis);
                    classExaminationModel.setAnswer(answer);
                    classExaminationModel.setKnowledge(knowledge);
                    classExaminationModel.setSubjectId(subjectId);
                    classExaminationModel.setTitle(title);
                    classExaminationModel.setType(type);
                    classExaminationModel.setOption(option);
                    classExaminationModels.add(classExaminationModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classExaminationModels;
    }


}
