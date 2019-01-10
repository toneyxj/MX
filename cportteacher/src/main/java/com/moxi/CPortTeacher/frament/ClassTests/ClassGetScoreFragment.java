package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomScoreAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassScoreModel;
import com.moxi.CPortTeacher.model.TClassScoreModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.NoListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * 主观题分数界面
 * Created by zhengdelong on 2016/11/3.
 */
@SuppressLint("ValidFragment")
public class ClassGetScoreFragment extends CportBaseFragment {

    @Bind(R.id.subjective_question)
    Button subjective_question;

    @Bind(R.id.snaa_info)
    TextView snaa_info;

    @Bind(R.id.noscore)
    TextView noscore;

    @Bind(R.id.title)
    TextView title;

    private String getScoreRequestCode = "1";

    /**
     *
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

    @Bind(R.id.pscore)
    TextView pscore;

    /**
     * 列表
     */
    @Bind(R.id.info_list)
    NoListView info_list;

    //每页八条数据
    private int pageCount = 8;
    private int totlePage = 0;
    //当前第几页
    private int currentPage = 1;

    TClassScoreModel tClassScoreModel = null;

    List<ClassScoreModel> data = new ArrayList<>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassGetScoreFragment(){

    }

    public ClassGetScoreFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }


    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_get_score;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
//        initTestData();
        getScoreData();
        setData();
    }

    private void initTestData(){
        ClassScoreModel classScoreModel;
        for (int i = 0;i<30;i++){
            classScoreModel = new ClassScoreModel();
            classScoreModel.setRanking(i+1);
            classScoreModel.setIcon((new Random().nextInt(4) + 1) + "");
            classScoreModel.setName("小李" + i);
            if (i%3 == 0){
                classScoreModel.setCurrentTopic(36);
            }else{
                classScoreModel.setCurrentTopic(100);
            }
            classScoreModel.setTotleTopic(100);
            data.add(classScoreModel);
        }
    }

    private void initView(){
        subjective_question.setOnClickListener(this);
        left_img.setOnClickListener(this);
        right_img.setOnClickListener(this);
        String className = UserInformation.getInstance().getUserInformation().className;
        title.setText(className);
    }

    private void getScoreData(){
        tClassScoreModel = new TClassScoreModel();
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        getData(valuePairs,getScoreRequestCode, Connector.getInstance().getKeguanScore,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code == getScoreRequestCode){
            parseScoreData(result);
            setData();
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    private void setData(){

//        答题人数：20人  交卷:2人  未交卷:1人  用时:00:34:23

        snaa_info.setText("答题人数：" + tClassScoreModel.getCount() + "  交卷:" + tClassScoreModel.getCommitCount() + "人  用时:" + tClassScoreModel.getUseTime());

        noscore.setText("未交卷:" + tClassScoreModel.getUncommited());

        int sumPer = data.size();
        int sumNumers = 0;
        for (int i = 0;i<data.size();i++){
            int current = data.get(i).getCurrentTopic();
            sumNumers = sumNumers + current;
        }
        Log.d("class","sumPer==>" + sumPer);
        Log.d("class","sumNumers==>" + sumNumers);
        if (sumPer != 0){
            pscore.setText("客观题平均分:"+ sumNumers/sumPer +"分");
        }else{
            pscore.setText("客观题平均:0分");
        }


        if (data.size()<=pageCount){
            pages_text.setText("1" + "/" + "1");
            ClassRoomScoreAdapter classRoomScoreAdapter = new ClassRoomScoreAdapter(getContext(),data);
            info_list.setAdapter(classRoomScoreAdapter);
        }else{
            ClassRoomScoreAdapter classRoomScoreAdapter = new ClassRoomScoreAdapter(getContext(),data.subList(0,pageCount));
            info_list.setAdapter(classRoomScoreAdapter);

            int l = data.size()/pageCount;
            int a = data.size()%pageCount;
            if(a>0){
                l = l+1;
            }
            totlePage = l;
            pages_text.setText("1" + "/" + totlePage);
        }

    }

    private void setCurrentPageData(List<ClassScoreModel> currentPageData, int state){
        ClassRoomScoreAdapter syncadd = new ClassRoomScoreAdapter(getContext(),currentPageData);
        info_list.setAdapter(syncadd);
        if(state == 1){
            currentPage ++;
        }else {
            currentPage --;
        }
        pages_text.setText(currentPage + "/" + totlePage);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.subjective_question){
            showFragmentCallBack.show(new ClassRoomSubectiveQuestionFragment(showFragmentCallBack),"ClassRoomSubectiveQuestionFragment");
        }else if(view.getId() == R.id.left_img){
            if (currentPage == 1){
                return;
            }
            int fromIndex = (currentPage-1)*pageCount - pageCount;
            int endIndex = (currentPage-1)*pageCount;
            setCurrentPageData(data.subList(fromIndex,endIndex),0);
        }else if(view.getId() == R.id.right_img){
            try{
                if (currentPage * pageCount -1 == data.size() -1){
                    return;
                }
                //最后一页的数据小于pageCount
                if (pageCount > (data.size() - (currentPage*pageCount))){
                    int fromIndex = (currentPage) * pageCount;
                    int endIndex = data.size();
                    setCurrentPageData(data.subList(fromIndex,endIndex),1);
                }else{
                    int fromIndex = (currentPage) * pageCount;
                    int endIndex = (currentPage+1) * pageCount;
                    setCurrentPageData(data.subList(fromIndex,endIndex),1);
                }
            }catch (Exception e){

            }
        }
    }

    private void parseScoreData(String jsonData){
        try{

            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                JSONObject resultObj =  jsonObject.optJSONObject("result");
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
                for (int i = 0;i<jsonArray.length();i++){
                    rank = rank + 1;
                    classScoreModel = new ClassScoreModel();
                    scoreObj = jsonArray.optJSONObject(i);
                    String headimg = scoreObj.optString("headimg");
                    String studentName = scoreObj.optString("studentName");
                    int totalScore = scoreObj.optInt("totalScore");
                    int score = scoreObj.optInt("score");
                    Log.d("class","headimg===>" + headimg);
                    Log.d("class","studentName===>" + studentName);
                    Log.d("class","totalScore===>" + totalScore);
                    Log.d("class","score===>" + score);

                    classScoreModel.setIcon(headimg);
                    classScoreModel.setName(studentName);
                    classScoreModel.setTotleTopic(totalScore);
                    classScoreModel.setCurrentTopic(score);
                    classScoreModel.setRanking(rank);
                    data.add(classScoreModel);
                }
                tClassScoreModel.setData(data);
            }
        }catch (Exception e){

        }
    }

}
