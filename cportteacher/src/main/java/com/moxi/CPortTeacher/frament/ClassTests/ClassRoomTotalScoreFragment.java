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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassExaminationAdapter;
import com.moxi.CPortTeacher.adapter.ClassRoomScoreAdapter;
import com.moxi.CPortTeacher.adapter.newrev.OptionAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassExaminationModel;
import com.moxi.CPortTeacher.model.ClassScoreModel;
import com.moxi.CPortTeacher.model.CourseAnswerModel;
import com.moxi.CPortTeacher.model.CourseExerciseScore;
import com.moxi.CPortTeacher.model.ExamsDetailsHistoryModel;
import com.moxi.CPortTeacher.model.OptionsModel;
import com.moxi.CPortTeacher.model.TClassScoreModel;
import com.moxi.CPortTeacher.utils.MxgsaTagHandler;
import com.moxi.CPortTeacher.weight.AnswerSelectView;
import com.moxi.CPortTeacher.weight.SubjectInfoView;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.NoListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 总成绩界面，发送成绩
 * Created by zhengdelong on 2016/11/4.
 */
@SuppressLint("ValidFragment")
public class ClassRoomTotalScoreFragment extends CportBaseFragment {

    @Bind(R.id.score_tx)
    TextView score_tx;

    @Bind(R.id.timu_tx)
    TextView timu_tx;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.subjective_question)
    Button subjective_question;

    @Bind(R.id.snaa_info)
    TextView snaa_info;

    @Bind(R.id.noscore)
    TextView noscore;
    @Bind(R.id.sec_students_grid)
    GridView secStudentsGrid;
    @Bind(R.id.students_info_rel)
    RelativeLayout studentsInfoRel;
    @Bind(R.id.subjectInfoView)
    SubjectInfoView subjectInfoView;

    private String getScoreRequestCode = "1";
    private String getTimuRequestCode = "2";
    private String classWorkId = "";

    TClassScoreModel tClassScoreModel = null;

    @Bind(R.id.pscore)
    TextView pscore;

    /**
     * 底部页码
     */
    @Bind(R.id.pages_text)
    TextView pages_text;

    /**
     * 上一页
     */
    @Bind(R.id.left_img)
    ImageView left_img;

    /**
     * 下一页
     */
    @Bind(R.id.right_img)
    ImageView right_img;

    /**
     * 列表
     */
    @Bind(R.id.info_list)
    NoListView info_list;

    @Bind(R.id.sec_grid)
    GridView sec_grid;

    @Bind(R.id.info)
    TextView info;

    @Bind(R.id.score_rel)
    RelativeLayout score_rel;

    @Bind(R.id.timu_rel)
    RelativeLayout timu_rel;

    @Bind(R.id.answerSelectView)
    AnswerSelectView answerSelectView;

    @Bind(R.id.lv_option)
    ListView lvOption;

    //每页八条数据
    private int pageCount = 8;
    private int totlePage = 0;
    //当前第几页
    private int currentPage = 1;

    List<ClassScoreModel> data = new ArrayList<>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassRoomTotalScoreFragment() {

    }

    public ClassRoomTotalScoreFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack) {
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_total_score;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            classWorkId = "";
        } else {
            classWorkId = bundle.getString("classWorkId", "");
            if (classWorkId == null) {
                classWorkId = "";
            }
        }
        Log.d("class", "classWorkId===>" + classWorkId);
        courseExerciseScore = new CourseExerciseScore();
        initView();
        getScoreData();
        getCourseExerciseScoreListData();
        setData();
    }

    private void initView() {
        sec_grid.setOnItemClickListener(onItemClickListener);
        secStudentsGrid.setOnItemClickListener(onItemStudentClickListener);
        subjective_question.setOnClickListener(this);
        left_img.setOnClickListener(this);
        right_img.setOnClickListener(this);
        score_tx.setOnClickListener(this);
        timu_tx.setOnClickListener(this);
        String className = UserInformation.getInstance().getUserInformation().className;
        title.setText(className);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("class", "position==>" + position);
            String sub = courseExerciseScore.getExerciseModels().get(position).getTitle();
            Log.d("class", "sub===>" + sub);
            setTitle(courseExerciseScore.getExerciseModels().get(position).getTitle(), info);
            answerSelectView.setData(courseExerciseScore.getCourseAnswerModels(), courseExerciseScore.getExerciseModels().get(position).getSubjectId());
            for (int i = 0; i < courseExerciseScore.getExerciseModels().size(); i++) {
                View view1 = adapterView.getChildAt(i);
                TextView textView = (TextView) view1.findViewById(R.id.inf);
                textView.setBackgroundResource(R.drawable.qimu_backgroundshape);
                textView.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            TextView textView = (TextView) view.findViewById(R.id.inf);
            textView.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
            textView.setTextColor(getResources().getColor(R.color.colorWithe));

            setOptionAdapter(position);
        }
    };
    AdapterView.OnItemClickListener onItemStudentClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            subjectInfoView.parseExamsDetails(position, null);
            for (int i = 0; i < adapterView.getCount(); i++) {
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

    private void getScoreData() {
        tClassScoreModel = new TClassScoreModel();
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", userId + ""));
        if (!classWorkId.equals("")) {
            valuePairs.add(new ReuestKeyValues("classWorkId", classWorkId));
            getData(valuePairs, getScoreRequestCode, Connector.getInstance().getClassWorkIdScore, true);
        } else {
            getData(valuePairs, getScoreRequestCode, Connector.getInstance().getKeguanScore, true);
        }

    }

    private void getCourseExerciseScoreListData() {
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", userId + ""));
        getData(valuePairs, getTimuRequestCode, Connector.getInstance().getCourseExerciseScoreList, true);
    }

    private void getStudentScoreListData(String studentId) {
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", userId + ""));
        valuePairs.add(new ReuestKeyValues("studentId", studentId));
        getData(valuePairs, "getStudentScoreListData", Connector.getInstance().getStudentScoreList, true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class", "result==>" + result + "   code====>" + code);
        if (code == getScoreRequestCode) {
            parseScoreData(result);
            setData();
        } else if (code == getTimuRequestCode) {
            parseCourseExciserData(result);
            setTiMuData();
        } else if ("getStudentScoreListData".equals(code)) {
            ExamsDetailsHistoryModel edmHistory = GsonTools.getPerson(result, ExamsDetailsHistoryModel.class);
            setStudentTiMu(edmHistory);
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    private void setTiMuData() {
        ClassExaminationAdapter classExaminationAdapter = new ClassExaminationAdapter(getContext(), courseExerciseScore.getExerciseModels());
        sec_grid.setAdapter(classExaminationAdapter);
        sec_grid.requestFocus();
        sec_grid.setSelection(0);
        if (courseExerciseScore.getExerciseModels().size() > 0) {
            setTitle(courseExerciseScore.getExerciseModels().get(0).getTitle(), info);
            answerSelectView.setData(courseExerciseScore.getCourseAnswerModels(), courseExerciseScore.getExerciseModels().get(0).getSubjectId());
            setOptionAdapter(0);
        }
    }

    /**
     * 设置选项适配器
     *
     * @param position
     */
    private void setOptionAdapter(int position) {
        String optionsJson = courseExerciseScore.getExerciseModels().get(position).getOption();
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

    private void setData() {

//        答题人数：20人  交卷:2人  未交卷:1人  用时:00:34:23

        snaa_info.setText("答题人数：" + tClassScoreModel.getCount() + "  交卷:" + tClassScoreModel.getCommitCount() + "人  用时:" + tClassScoreModel.getUseTime());

        noscore.setText("未交卷:" + tClassScoreModel.getUncommited());

        int sumPer = data.size();
        int sumNumers = 0;
        for (int i = 0; i < data.size(); i++) {
            int current = data.get(i).getCurrentTopic();
            sumNumers = sumNumers + current;
        }
        Log.d("class", "sumPer==>" + sumPer);
        Log.d("class", "sumNumers==>" + sumNumers);
        if (sumPer != 0) {
            pscore.setText("客观题平均分:" + sumNumers / sumPer + "分");
        } else {
            pscore.setText("客观题平均:0分");
        }


        if (data.size() <= pageCount) {
            pages_text.setText("1" + "/" + "1");
            ClassRoomScoreAdapter classRoomScoreAdapter = new ClassRoomScoreAdapter(getContext(), data);
            classRoomScoreAdapter.setStudentOnclick(this);
            info_list.setAdapter(classRoomScoreAdapter);
        } else {
            ClassRoomScoreAdapter classRoomScoreAdapter = new ClassRoomScoreAdapter(getContext(), data.subList(0, pageCount));
            classRoomScoreAdapter.setStudentOnclick(this);
            info_list.setAdapter(classRoomScoreAdapter);

            int l = data.size() / pageCount;
            int a = data.size() % pageCount;
            if (a > 0) {
                l = l + 1;
            }
            totlePage = l;
            pages_text.setText("1" + "/" + totlePage);
        }

    }

    private void setCurrentPageData(List<ClassScoreModel> currentPageData, int state) {
        ClassRoomScoreAdapter syncadd = new ClassRoomScoreAdapter(getContext(), currentPageData);
        syncadd.setStudentOnclick(this);
        info_list.setAdapter(syncadd);
        if (state == 1) {
            currentPage++;
        } else {
            currentPage--;
        }
        pages_text.setText(currentPage + "/" + totlePage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.subjective_question) {
            Log.d("class", "发送成绩...");
            Toastor.showLongToast(getContext(), "发送成功");
        } else if (view.getId() == R.id.left_img) {
            if (currentPage == 1) {
                return;
            }
            int fromIndex = (currentPage - 1) * pageCount - pageCount;
            int endIndex = (currentPage - 1) * pageCount;
            setCurrentPageData(data.subList(fromIndex, endIndex), 0);
        } else if (view.getId() == R.id.right_img) {
            try {
                if (currentPage * pageCount - 1 == data.size() - 1) {
                    return;
                }
                //最后一页的数据小于pageCount
                if (pageCount > (data.size() - (currentPage * pageCount))) {
                    int fromIndex = (currentPage) * pageCount;
                    int endIndex = data.size();
                    setCurrentPageData(data.subList(fromIndex, endIndex), 1);
                } else {
                    int fromIndex = (currentPage) * pageCount;
                    int endIndex = (currentPage + 1) * pageCount;
                    setCurrentPageData(data.subList(fromIndex, endIndex), 1);
                }
            } catch (Exception e) {

            }
        } else if (R.id.score_tx == view.getId()) {
            //点击分数统计
            clickScore();
            noscore.setText("未交卷:" + tClassScoreModel.getUncommited());
            studentsInfoRel.setVisibility(View.GONE);
            score_rel.setVisibility(View.VISIBLE);
            timu_rel.setVisibility(View.GONE);

        } else if (R.id.timu_tx == view.getId()) {
            //点击题目统计
            clickTimu();
            noscore.setText("未交卷:" + tClassScoreModel.getUncommited());
            studentsInfoRel.setVisibility(View.GONE);
            score_rel.setVisibility(View.GONE);
            timu_rel.setVisibility(View.VISIBLE);

        } else if (R.id.icon == view.getId()) {
            //点击学生头像
            if (view.getTag() == null)
                return;
            ClassScoreModel bean = (ClassScoreModel) view.getTag();
            noscore.setText(bean.getName());
            getStudentScoreListData(String.valueOf(bean.getStudentId()));
            studentsInfoRel.setVisibility(View.VISIBLE);
            score_rel.setVisibility(View.GONE);
            timu_rel.setVisibility(View.GONE);

        }
    }

    private void setStudentTiMu(ExamsDetailsHistoryModel edmHistory) {
        List<ClassExaminationModel> examinationModels = new ArrayList<>();
        ClassExaminationModel classExaminationModel;
        for (int i = 0; i < edmHistory.getResult().getExerciseList().size(); i++) {
            classExaminationModel = new ClassExaminationModel();
            classExaminationModel.setId_nums(String.valueOf(i + 1));
            examinationModels.add(classExaminationModel);
        }
        String studentName=noscore.getText().toString();
        noscore.setText(studentName+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t分数："+edmHistory.getResult().getTotalScore());
        ClassExaminationAdapter classExaminationAdapter = new ClassExaminationAdapter(getContext(), examinationModels);
        secStudentsGrid.setAdapter(classExaminationAdapter);
        secStudentsGrid.requestFocus();
        secStudentsGrid.setSelection(0);
        if (examinationModels.size() > 0) {
            subjectInfoView.parseExamsDetails(0, edmHistory.getResult());
        }
    }

    private void clickScore() {
        Log.d("class", "clickScore...");
        score_tx.setTextColor(getResources().getColor(R.color.colorWihte));
        timu_tx.setTextColor(getResources().getColor(R.color.colorBlack));
        score_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
        timu_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
        score_rel.setVisibility(View.VISIBLE);
        timu_rel.setVisibility(View.GONE);
    }

    private void clickTimu() {
        score_tx.setTextColor(getResources().getColor(R.color.colorBlack));
        timu_tx.setTextColor(getResources().getColor(R.color.colorWihte));
        score_tx.setBackgroundResource(R.drawable.qimu_backgroundshape);
        timu_tx.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
        score_rel.setVisibility(View.GONE);
        timu_rel.setVisibility(View.VISIBLE);
    }

    private void parseScoreData(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                JSONObject resultObj = jsonObject.optJSONObject("result");
                String useTime = resultObj.optString("useTime");
                String uncommited = resultObj.optString("uncommited");
                int commitCount = resultObj.optInt("commitCount");
                int count = resultObj.optInt("count");
                tClassScoreModel.setCommitCount(commitCount);
                tClassScoreModel.setCount(count);
                tClassScoreModel.setUseTime(useTime);
                tClassScoreModel.setUncommited(uncommited);
                JSONArray jsonArray = resultObj.optJSONArray("scoreList");
                ClassScoreModel classScoreModel;
                JSONObject scoreObj;
                int rank = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    rank = rank + 1;
                    classScoreModel = new ClassScoreModel();
                    scoreObj = jsonArray.optJSONObject(i);
                    String headimg = scoreObj.optString("headimg");
                    String studentName = scoreObj.optString("studentName");
                    int totalScore = scoreObj.optInt("totalScore");
                    int score = scoreObj.optInt("score");
                    Log.d("class", "headimg===>" + headimg);
                    Log.d("class", "studentName===>" + studentName);
                    Log.d("class", "totalScore===>" + totalScore);
                    Log.d("class", "score===>" + score);
                    classScoreModel.setStudentId(scoreObj.optInt("studentId"));
                    classScoreModel.setIcon(headimg);
                    classScoreModel.setName(studentName);
                    classScoreModel.setTotleTopic(totalScore);
                    classScoreModel.setCurrentTopic(score);
                    classScoreModel.setRanking(rank);
                    data.add(classScoreModel);
                }
                tClassScoreModel.setData(data);
            }
        } catch (Exception e) {

        }
    }

    private CourseExerciseScore courseExerciseScore;

    private void parseCourseExciserData(String jsonData) {
        courseExerciseScore = new CourseExerciseScore();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code", -1);
            if (code == 0) {
                JSONObject resultObj = jsonObject.optJSONObject("result");
                JSONArray answerListArr = resultObj.optJSONArray("answerList");
                CourseAnswerModel courseAnswerModel;
                List<CourseAnswerModel> answerList = new ArrayList<>();
                for (int i = 0; i < answerListArr.length(); i++) {
                    courseAnswerModel = new CourseAnswerModel();
                    JSONObject answerOjb = answerListArr.getJSONObject(i);
                    courseAnswerModel.setExerciseId(answerOjb.optInt("exerciseId"));
                    courseAnswerModel.setStudentAnswer(answerOjb.optString("studentAnswer"));
                    courseAnswerModel.setStudentId(answerOjb.optInt("studentId"));
                    courseAnswerModel.setStudentName(answerOjb.optString("studentName"));
                    courseAnswerModel.setStudentScore(answerOjb.optInt("studentScore"));
                    answerList.add(courseAnswerModel);
                }

                JSONArray exerciseListArr = resultObj.optJSONArray("exerciseList");
                ClassExaminationModel exerciseModel;
                List<ClassExaminationModel> exerciseList = new ArrayList<>();
                for (int j = 0; j < exerciseListArr.length(); j++) {
                    JSONObject exerciseOjb = exerciseListArr.getJSONObject(j);
                    exerciseModel = new ClassExaminationModel();
                    exerciseModel.setId_nums(String.valueOf(j + 1));
                    exerciseModel.setSubjectId(exerciseOjb.optInt("exerciseId"));
                    exerciseModel.setAnalysis(exerciseOjb.optString("analysis"));
                    exerciseModel.setAnswer(exerciseOjb.optString("answer"));
                    exerciseModel.setKnowledge(exerciseOjb.optString("knowledge"));
                    exerciseModel.setTitle(exerciseOjb.optString("title"));
                    exerciseModel.setType(exerciseOjb.optInt("type"));
                    exerciseModel.setOption(exerciseOjb.optString("option"));
                    exerciseList.add(exerciseModel);
                }
                if (courseExerciseScore == null)
                    courseExerciseScore = new CourseExerciseScore();
                courseExerciseScore.setCourseAnswerModels(answerList);
                courseExerciseScore.setExerciseModels(exerciseList);
            }
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
