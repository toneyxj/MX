package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomSelectAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.NoGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 客观题评分列表界面
 * Created by zhengdelong on 2016/11/3.
 */
@SuppressLint("ValidFragment")
public class ClassRoomSubectiveQuestionFragment extends CportBaseFragment {

    ClassRoomTotalScoreFragment classRoomTotalScoreFragment = null;

    private String classRoomSubjectiveQuestionRequestCode = "1";

    List<ClassSelectModel> data = new ArrayList<>();

    @Bind(R.id.sec_grid)
    NoGridView sec_grid;

    @Bind(R.id.send)
    Button send;

    @Bind(R.id.title_tx)
    TextView title_tx;

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassRoomSubectiveQuestionFragment(){

    }

    public ClassRoomSubectiveQuestionFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_subjective;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getSubjectiveQuestionData();
//        initTestData();
        classRoomTotalScoreFragment = new ClassRoomTotalScoreFragment(showFragmentCallBack);
        initView();
        this.onRefuresh();
    }

    private void initTestData(){
//        ClassSelectModel classSelectModel;
//        for (int i = 0;i<32;i++){
//            classSelectModel = new ClassSelectModel();
//            classSelectModel.setIcon((new Random().nextInt(4) + 1) + "");
//            classSelectModel.setName("小李" + i);
//            classSelectModel.setState(0);
//
//            data.add(classSelectModel);
//        }
    }

    private void getSubjectiveQuestionData(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        getData(valuePairs,classRoomSubjectiveQuestionRequestCode, Connector.getInstance().getSubjectiveExerciseList,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code == classRoomSubjectiveQuestionRequestCode){
            parseRoomSubjectiveData(result);
            setData();
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    private void initView(){
        this.onRefuresh();
        sec_grid.setOnItemClickListener(onItemClickListener);
        send.setOnClickListener(this);
        send.setText("结束评阅");
    }

    private void setData(){

        title_tx.setText("学生名单 （共"+ data.size() +"人）");

        ClassRoomSelectAdapter classRoomSelectAdapter = new ClassRoomSelectAdapter(getContext(),data);
        sec_grid.setAdapter(classRoomSelectAdapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("class","position==>" + position);
//            ImageView imageView = (ImageView) view.findViewById(R.id.incn_sec);
//            if (imageView.getVisibility() == View.GONE){
//                Log.d("class","iin");
//                imageView.setVisibility(View.VISIBLE);
//                data.get(position).setState(1);
//            }else{
//                Log.d("class","oon");
//                imageView.setVisibility(View.GONE);
//                data.get(position).setState(0);
//            }

            int answerId = data.get(position).getId();
            if (answerId > 0){
                showFragmentCallBack.show(new ClassRoomSubjectingFragment(data.get(position),showFragmentCallBack),"ClassRoomSubjectingFragment");
            }else{
                Toastor.showLongToast(getContext(),data.get(position).getName() + "未提交该主观题");
            }


        }
    };


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send){
            showFragmentCallBack.show(classRoomTotalScoreFragment,"ClassRoomTotalScoreFragment");
        }
    }

    @Override
    public void onclickBack() {
        classRoomTotalScoreFragment.onclickBack();
        super.onclickBack();
    }

    @Override
    public void onDestroyView() {
        data.clear();
        super.onDestroyView();
    }


    private void parseRoomSubjectiveData(String jsonData){
        try{

//            {
//                "headimg": "1",				<--学生头像
//                "studentName": "张三",			<--学生姓名
//                "answerId": 5,				<--学生这次答题记录ID(后续打分需要回传给服务器)
//                "answerUrl": "\\1\\4_10.jpg"	<--学生这次答题的答案URL地址
//            },
//            {
//                "headimg": "2",
//                    "studentName": "李四"
//            },

            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){

                JSONObject resultObj = jsonObject.optJSONObject("result");
                JSONObject exerciseObj = resultObj.optJSONObject("exercise");
                String title = exerciseObj.optString("title");

                JSONArray resultArr =  resultObj.optJSONArray("answerList");
                ClassSelectModel classSelectModel = null;
                for (int i = 0;i<resultArr.length();i++){
                    classSelectModel = new ClassSelectModel();
                    JSONObject jsonObject1 = resultArr.optJSONObject(i);

                    int answerId = jsonObject1.optInt("answerId",-1);
                    String answerUrl = "";
                    if (answerId != -1){
                        answerUrl = jsonObject1.optString("answerUrl");
                        int hasScore = jsonObject1.optInt("hasScore");
                        classSelectModel.setId(answerId);
                        classSelectModel.setGrade(answerUrl);
                        classSelectModel.setTitle(title);
                        classSelectModel.setState(hasScore);
                    }
                    String headimg = jsonObject1.optString("headimg");
                    String studentName = jsonObject1.optString("studentName");
                    classSelectModel.setName(studentName);
                    classSelectModel.setHeadimg(headimg);
                    data.add(classSelectModel);
                }

            }
        }catch (Exception e){

        }
    }

}
