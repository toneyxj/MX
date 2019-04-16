package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ThesugestionAnswerAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerModel;
import com.moxi.CPortTeacher.view.ThesugestionStudentDialog;
import com.moxi.CPortTeacher.view.ThesugestionSubjectiveDialog;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;

/**
 * Created by zhengdelong on 2016/12/1.
 */
@SuppressLint("ValidFragment")
public class ThesugestionResultFragment extends CportBaseFragment implements View.OnClickListener{

    private String thesugestionAnswerIngRequestCode = "1";

    @Bind(R.id.answer_img)
    ImageView answer_img;

    @Bind(R.id.answer_grid)
    GridView answer_grid;

    @Bind(R.id.answering_lin)
    LinearLayout answering_lin;

    @Bind(R.id.right_answer_tx)
    TextView right_answer_tx;

    @Bind(R.id.show_ans)
    TextView show_ans;

    private List<String> choseList = new ArrayList<>();
    private Map<String,Integer> choseMap = new HashMap<>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;
    ThesugestionAnswerModel thesugestionAnswerModel;

    public ThesugestionResultFragment(){

    }

    public ThesugestionResultFragment(ThesugestionAnswerModel thesugestionAnswerModel, ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
        this.thesugestionAnswerModel = thesugestionAnswerModel;
    }

    private void initData(){
        if (thesugestionAnswerModel == null){
            getAnswerData();
        }else{
//            parseThesugestionAnswerData(panduanTestData);
            setData();
        }
    }

    private void getAnswerData(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        Log.d("class","userId:" + userId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        getData(valuePairs,thesugestionAnswerIngRequestCode, Connector.getInstance().getQuestionResult,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code.equals(thesugestionAnswerIngRequestCode)){
            parseThesugestionAnswerData(result);
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }


    private void setData(){
        ImageLoader.getInstance().displayImage(Connector.getInstance().thesugestionImageUrl + thesugestionAnswerModel.getTitle(),answer_img);
        int type = thesugestionAnswerModel.getType();
        android.util.Log.d("class","type===>" + type);
        if (type ==1){
            //单选题
            for (int i = 0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                String answer = thesugestionAnswerModel.getAnswerList().get(i).getAnswer();
                android.util.Log.d("class","answer==>" + answer);
                if(!answer.equals("")){
                    if (i == 0){
                        choseList.add(answer);
                    }else{
                        if (!choseList.contains(answer)){
                            choseList.add(answer);
                        }
                    }
                }
            }
            android.util.Log.d("class","choseList.size()===>" + choseList.size());
            if (choseList.size() <= 0 ){
                right_answer_tx.setText("正确答案:" + thesugestionAnswerModel.getAnswer());
                Toastor.showLongToast(getActivity(),"所有学生均未答题！");
                return;
            }
            for (int j = 0;j<thesugestionAnswerModel.getAnswerList().size();j++){
                for (int k = 0;k<choseList.size();k++){
                    String answer = thesugestionAnswerModel.getAnswerList().get(j).getAnswer();
                    if (choseList.get(k).equals(answer)&&!(answer.equals(""))){
                        android.util.Log.d("class","second ans ===>" + answer);
                        if (choseMap.size() == 0){
                            choseMap.put(answer,1);
                        }else{
                            if (choseMap.containsKey(answer)){
                                Integer val = choseMap.get(answer);
                                choseMap.remove(answer);
                                choseMap.put(answer,val + 1);
                            }else{
                                choseMap.put(answer,1);
                            }
                        }
                    }
                }
            }
            choseMap = sortMapByKey(choseMap);
            List<RadioSelectAnswer> radioSelectAnswerList = new ArrayList<>();
            RadioSelectAnswer radioSelectAnswer;
            Iterator iter = choseMap.entrySet().iterator();
            while (iter.hasNext()){
                radioSelectAnswer = new RadioSelectAnswer();
                Map.Entry e = (Map.Entry)iter.next();
                String key = (String) e.getKey();
                int value = (Integer) e.getValue();
                radioSelectAnswer.setAnswer(key);
                radioSelectAnswer.setSize(value);
                radioSelectAnswerList.add(radioSelectAnswer);
            }
            int max = thesugestionAnswerModel.getAnswerList().size();
            initCutomView(type,max,radioSelectAnswerList);
        }else if (type == 2){
            //多选题
            for (int i = 0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                String answer = thesugestionAnswerModel.getAnswerList().get(i).getAnswer();
                String[] ansStrs = answer.split(";");
                for (int m = 0;m<ansStrs.length;m++){
                    if (i == 0 && m == 0 && !(ansStrs[0].equals(""))){
                        choseList.add(ansStrs[m]);
                    }else{
                        if (!choseList.contains(ansStrs[m])){
                            choseList.add(ansStrs[m]);
                        }
                    }
                }
            }
            android.util.Log.d("class","choseList.size()===>" + choseList.size());
            if (choseList.size() <= 0 ){
                String answer = thesugestionAnswerModel.getAnswer();
                answer = answer.replace(";","  ");
                right_answer_tx.setText("正确答案:" + answer);
                Toastor.showLongToast(getActivity(),"所有学生均未答题！");
                return;
            }
            for (int j = 0;j<thesugestionAnswerModel.getAnswerList().size();j++){
                for (int k = 0;k<choseList.size();k++){
                    String answer = thesugestionAnswerModel.getAnswerList().get(j).getAnswer();
                    String[] ansStr = answer.split(";");
                    for (int n = 0;n<ansStr.length;n++){
                        // TODO: 2016/12/2 fixbugger 
                        if (choseList.get(k).equals(ansStr[n]) && !(ansStr[n].equals(""))){
                            android.util.Log.d("class","ansStr[n]===>" + ansStr[n]);
                            if (choseMap.size() == 0){
                                choseMap.put(ansStr[n],1);
                            }else{
                                if (choseMap.containsKey(ansStr[n])){
                                    Integer val = choseMap.get(ansStr[n]);
                                    choseMap.remove(ansStr[n]);
                                    choseMap.put(ansStr[n],val + 1);
                                }else{
                                    choseMap.put(ansStr[n],1);
                                }
                            }

                        }
                    }
                }
            }

            List<RadioSelectAnswer> radioSelectAnswerList = new ArrayList<>();
            RadioSelectAnswer radioSelectAnswer;
            choseMap = sortMapByKey(choseMap);
            if (choseMap == null){
                String answer = thesugestionAnswerModel.getAnswer();
                answer = answer.replace(";","  ");
                right_answer_tx.setText("正确答案:" + answer);
                Toastor.showLongToast(getActivity(),"所有学生均未答题！");
                return;
            }
            Iterator iter = choseMap.entrySet().iterator();
            while (iter.hasNext()){
                radioSelectAnswer = new RadioSelectAnswer();
                Map.Entry e = (Map.Entry)iter.next();
                String key = (String) e.getKey();
                int value = (Integer) e.getValue();
                radioSelectAnswer.setAnswer(key);
                radioSelectAnswer.setSize(value);
                radioSelectAnswerList.add(radioSelectAnswer);
            }
            int max = thesugestionAnswerModel.getAnswerList().size();
            initCutomView(type,max,radioSelectAnswerList);
        }else if (type == 3){
            //主观题
            show_ans.setVisibility(View.VISIBLE);
            show_ans.setOnClickListener(this);
            right_answer_tx.setVisibility(View.GONE);
            show_ans.setText("查看答案");
        }else if (type == 4){
            //判断题
            List<RadioSelectAnswer> radioSelectAnswerList = new ArrayList<>();
            int rightSize = 0;
            for (int i = 0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                if (thesugestionAnswerModel.getAnswerList().get(i).getAnswer().equals("1")){
                    rightSize ++;
                }
            }
            int wrongSize = thesugestionAnswerModel.getAnswerList().size() - rightSize;
            RadioSelectAnswer radioSelectAnswer = new RadioSelectAnswer();
            radioSelectAnswer.setAnswer("正确");
            radioSelectAnswer.setSize(rightSize);
            radioSelectAnswerList.add(radioSelectAnswer);
            RadioSelectAnswer wrongSelectAnswer = new RadioSelectAnswer();
            wrongSelectAnswer.setAnswer("错误");
            wrongSelectAnswer.setSize(wrongSize);
            radioSelectAnswerList.add(wrongSelectAnswer);

            Log.d("class","rightSize====>" + rightSize);
            Log.d("class","wrongSize====>" + wrongSize);

            for (int i = 0;i<radioSelectAnswerList.size();i++){
                Log.d("class","ans====>" + radioSelectAnswerList.get(i).getAnswer());
                Log.d("class","ans====>" + radioSelectAnswerList.get(i).getSize());
            }
            int max = thesugestionAnswerModel.getAnswerList().size();
            initCutomView(type,max,radioSelectAnswerList);
        }
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_answer_sugestion;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        initView();
        this.onRefuresh();
    }

    private void initView(){
        answer_grid.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("class","position==>" + position);
            TextView name = (TextView) view.findViewById(R.id.name);
            String tag = name.getText().toString();
            int type = thesugestionAnswerModel.getType();
            Log.d("class","type====>" + type);
            Log.d("class","size====>" + thesugestionAnswerModel.getAnswerList().size());
            List<ClassSelectModel> classSelectModelList = new ArrayList<>();
            if(type == 1){
                ClassSelectModel classSelectModel;
                for (int i =0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                    Log.d("class","============");
                    Log.d("class","tag====>" + tag);
                    Log.d("class","getAnswer====>" + thesugestionAnswerModel.getAnswerList().get(i).getAnswer());
                    if (tag.contains(thesugestionAnswerModel.getAnswerList().get(i).getAnswer())
                            && !(thesugestionAnswerModel.getAnswerList().get(i).getAnswer().equals(""))){
                        classSelectModel = new ClassSelectModel();
                        classSelectModel.setName(thesugestionAnswerModel.getAnswerList().get(i).getStudentName());
                        classSelectModel.setHeadimg(thesugestionAnswerModel.getAnswerList().get(i).getHeadimg());
                        classSelectModel.setState(0);
                        classSelectModelList.add(classSelectModel);
                    }
                }
            }else if (type == 2){
                ClassSelectModel classSelectModel;
                for (int i =0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                    String answer = thesugestionAnswerModel.getAnswerList().get(i).getAnswer();
                    String[] ans = answer.split(";");
                    for (int k = 0;k<ans.length;k++){
                        Log.d("class","============");
                        Log.d("class","tag====>" + tag);
                        Log.d("class","ans[k])====>" + ans[k]);
                        Log.d("class","getAnswer====>" + thesugestionAnswerModel.getAnswerList().get(i).getAnswer());
                        if (tag.contains(ans[k])&&!(ans[k].equals(""))){
                            Log.d("class","======true======");
                            classSelectModel = new ClassSelectModel();
                            classSelectModel.setName(thesugestionAnswerModel.getAnswerList().get(i).getStudentName());
                            classSelectModel.setHeadimg(thesugestionAnswerModel.getAnswerList().get(i).getHeadimg());
                            classSelectModel.setState(0);
                            classSelectModelList.add(classSelectModel);
                        }
                    }
                }
            }else if (type == 4){
                if (tag.contains("正确")){

                    ClassSelectModel classSelectModel;
                    for (int i =0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                        if (thesugestionAnswerModel.getAnswerList().get(i).getAnswer().equals("1")){
                            classSelectModel = new ClassSelectModel();
                            classSelectModel.setName(thesugestionAnswerModel.getAnswerList().get(i).getStudentName());
                            classSelectModel.setHeadimg(thesugestionAnswerModel.getAnswerList().get(i).getHeadimg());
                            classSelectModel.setState(0);
                            classSelectModelList.add(classSelectModel);
                        }
                    }
                }else{
                    ClassSelectModel classSelectModel;
                    for (int i =0;i<thesugestionAnswerModel.getAnswerList().size();i++){
                        if (!thesugestionAnswerModel.getAnswerList().get(i).getAnswer().equals("1")){
                            classSelectModel = new ClassSelectModel();
                            classSelectModel.setName(thesugestionAnswerModel.getAnswerList().get(i).getStudentName());
                            classSelectModel.setHeadimg(thesugestionAnswerModel.getAnswerList().get(i).getHeadimg());
                            classSelectModel.setState(0);
                            classSelectModelList.add(classSelectModel);
                        }
                    }
                }
            }
            ThesugestionStudentDialog thesugestionStudentDialog = new ThesugestionStudentDialog(getActivity(),classSelectModelList);
            thesugestionStudentDialog.show();
        }

    };


    private void initCutomView(int type,int max,List<RadioSelectAnswer> radioSelectAnswerList){

        ThesugestionAnswerAdapter thesugestionSelectAdapter = new ThesugestionAnswerAdapter(getActivity(),type,max,radioSelectAnswerList);
        answer_grid.setAdapter(thesugestionSelectAdapter);

        if (type==1){
            right_answer_tx.setText("正确答案:" + thesugestionAnswerModel.getAnswer());
        }else if(type ==2){
            String answer = thesugestionAnswerModel.getAnswer();
            answer = answer.replace(";","  ");
            Log.d("class","tyoe==2==正确答案===>" + answer);
            right_answer_tx.setText("正确答案:" + answer);
        }else if (type == 3){
            right_answer_tx.setVisibility(View.GONE);
            answer_grid.setVisibility(View.GONE);
        }else if (type == 4){
            if (thesugestionAnswerModel.getAnswer().equals("1")){
                right_answer_tx.setText("正确答案:" + "正确");
            }else{
                right_answer_tx.setText("正确答案:" + "错误");
            }

        }

//        int max = thesugestionAnswerModel.getAnswerList().size();
//        NumberFormat numberFormat = NumberFormat.getInstance();
//        // 设置精确到小数点后2位
//        numberFormat.setMaximumFractionDigits(2);
//        View view;
//        Iterator iter = radioSelectAnswer.entrySet().iterator();
//        while (iter.hasNext()){
//
//            Map.Entry e = (Map.Entry)iter.next();
//            String answers = (String) e.getKey();
//            int size = (Integer) e.getValue();
//
//            view = LayoutInflater.from(context).inflate(R.layout.thesugestion_item, null);
//            TextView tempNameTextView = (TextView) view.findViewById(R.id.name);
//            com.moxi.classRoom.widget.ProgressView tempProgressAns = (com.moxi.classRoom.widget.ProgressView) view.findViewById(R.id.progress_ans);
//            TextView tempAnswerNumbersTextView = (TextView) view.findViewById(R.id.answer_numbers);
//            String answer = answers;
//            tempNameTextView.setText(answer);
//            tempProgressAns.setMaxNumber(max);
//            int current = size;
//            tempProgressAns.setCurNumber(current);
//            String result = numberFormat.format((float) current / (float) max * 100);
//            tempAnswerNumbersTextView.setText(result + "%");
//            answering_lin.addView(view);
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_ans){
            ThesugestionSubjectiveDialog thesugestionSubjectiveDialog = new ThesugestionSubjectiveDialog(getActivity(),thesugestionAnswerModel);
            thesugestionSubjectiveDialog.show();
        }
    }

    private void parseThesugestionAnswerData(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                thesugestionAnswerModel = new ThesugestionAnswerModel();
                List<ThesugestionAnswerListModel> listModels = new ArrayList<>();
                JSONObject resultObj = jsonObject.optJSONObject("result");
                JSONArray anserListArr = resultObj.optJSONArray("answerList");
                ThesugestionAnswerListModel thesugestionAnswerListModel;
                for (int i = 0;i<anserListArr.length();i++){
                    thesugestionAnswerListModel = new ThesugestionAnswerListModel();
                    thesugestionAnswerListModel.setAnswer(anserListArr.getJSONObject(i).optString("answer"));
                    thesugestionAnswerListModel.setHeadimg(anserListArr.getJSONObject(i).optString("headimg"));
                    thesugestionAnswerListModel.setStudentId(anserListArr.getJSONObject(i).optInt("studentId"));
                    thesugestionAnswerListModel.setStudentName(anserListArr.getJSONObject(i).optString("studentName"));
                    listModels.add(thesugestionAnswerListModel);
                }
                JSONObject questionObj = resultObj.optJSONObject("question");
                thesugestionAnswerModel.setAnswer(questionObj.optString("answer"));
                thesugestionAnswerModel.setId(questionObj.optInt("id"));
                thesugestionAnswerModel.setOption(questionObj.optInt("option"));
                thesugestionAnswerModel.setTime(questionObj.optString("time"));
                thesugestionAnswerModel.setTitle(questionObj.optString("title"));
                thesugestionAnswerModel.setType(questionObj.optInt("type"));
                thesugestionAnswerModel.setUserId(questionObj.optInt("userId"));
                thesugestionAnswerModel.setAnswerList(listModels);
                setData();
            }
        }catch (Exception e){
            Log.d("class","Exception====>" + e.getMessage());
        }
    }

    public class RadioSelectAnswer {

        String answer;
        int size = 0;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public Map<String, Integer> sortMapByKey(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Integer> sortMap = new TreeMap<String, Integer>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    @Override
    public void onDestroyView() {
        if (choseList != null){
            choseList.clear();
        }
        if (choseMap != null){
            choseMap.clear();
        }
        super.onDestroyView();
    }

    String panduanTestData = "{\n" +
            "    \"code\": 0,\n" +
            "    \"msg\": \"*获取最近一次现场出题答案成功\",\n" +
            "    \"result\": {\n" +
            "        \"answerList\": [\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"1\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"1\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张5\",\n" +
            "                \"answer\": \"0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张7\",\n" +
            "                \"answer\": \"0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张9\",\n" +
            "                \"answer\": \"0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张11\",\n" +
            "                \"answer\": \"0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 5,\n" +
            "                \"headimg\": \"2\",\n" +
            "                \"studentName\": \"李33\",\n" +
            "                \"answer\": \"0\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"question\": {\n" +
            "            \"answer\": \"1\",\n" +
            "            \"id\": 10,\n" +
            "            \"option\": 1,\n" +
            "            \"time\": \"2016-11-29 17:38:19\",\n" +
            "            \"title\": \"3/3_20161129173819.jpg\",\n" +
            "            \"type\": 4,\n" +
            "            \"userId\": 3\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String duoxuanTestData = "{\n" +
            "    \"code\": 0,\n" +
            "    \"msg\": \"*获取最近一次现场出题答案成功\",\n" +
            "    \"result\": {\n" +
            "        \"answerList\": [\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"A;B;C\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"C;D;E\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"A;C\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张5\",\n" +
            "                \"answer\": \"B;D\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张7\",\n" +
            "                \"answer\": \"B;C;D\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张9\",\n" +
            "                \"answer\": \"C;E\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张11\",\n" +
            "                \"answer\": \"B;D\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 5,\n" +
            "                \"headimg\": \"2\",\n" +
            "                \"studentName\": \"李33\",\n" +
            "                \"answer\": \"A;C\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"question\": {\n" +
            "            \"answer\": \"C\",\n" +
            "            \"id\": 10,\n" +
            "            \"option\": 1,\n" +
            "            \"time\": \"2016-11-29 17:38:19\",\n" +
            "            \"title\": \"3/3_20161129173819.jpg\",\n" +
            "            \"type\": 2,\n" +
            "            \"userId\": 3\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String danxuanTestData = "{\n" +
            "    \"code\": 0,\n" +
            "    \"msg\": \"*获取最近一次现场出题答案成功\",\n" +
            "    \"result\": {\n" +
            "        \"answerList\": [\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"A\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"C\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张三\",\n" +
            "                \"answer\": \"A\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张5\",\n" +
            "                \"answer\": \"B\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张7\",\n" +
            "                \"answer\": \"B\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张9\",\n" +
            "                \"answer\": \"E\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 4,\n" +
            "                \"headimg\": \"1\",\n" +
            "                \"studentName\": \"张11\",\n" +
            "                \"answer\": \"D\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"studentId\": 5,\n" +
            "                \"headimg\": \"2\",\n" +
            "                \"studentName\": \"李33\",\n" +
            "                \"answer\": \"C\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"question\": {\n" +
            "            \"answer\": \"D\",\n" +
            "            \"id\": 10,\n" +
            "            \"option\": 1,\n" +
            "            \"time\": \"2016-11-29 17:38:19\",\n" +
            "            \"title\": \"3/3_20161129173819.jpg\",\n" +
            "            \"type\": 1,\n" +
            "            \"userId\": 3\n" +
            "        }\n" +
            "    }\n" +
            "}";

}
