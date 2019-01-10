package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomAnswerAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassAnswerModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.view.NoListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 等待学生答题界面
 * Created by zhengdelong on 2016/11/3.
 */
@SuppressLint("ValidFragment")
public class ClassRoomAnswerFragment extends CportBaseFragment {

    private String answerIngRequestCode = "1";
    private String finishAnswerRequestCode = "3";

    /**
     * 标题
     */
    @Bind(R.id.title)
    TextView title;

    /**
     * 标题下的描述
     */
    @Bind(R.id.snaa_info)
    TextView snaa_info;

    /**
     * 列表
     */
    @Bind(R.id.info_list)
    NoListView info_list;

    /**
     * 结束答题
     */
    @Bind(R.id.end_ans)
    Button end_ans;

    /**
     * 重新发送
     */
    @Bind(R.id.resend)
    Button resend;

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
     * 计时
     */
    @Bind(R.id.chronometer)
    Chronometer chronometer;

    //每页八条数据
    private int pageCount = 8;
    private int totlePage = 0;
    //当前第几页
    private int currentPage = 1;

    HttpTimer httpTimer;

    List<ClassAnswerModel> data = new ArrayList<>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassRoomAnswerFragment(){

    }

    public ClassRoomAnswerFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_answer;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
//        initTestData();
        getAnswerData();
        setData();
        this.onRefuresh();
        chronometer.start();
    }
//
//    private void initTestData(){
//        ClassAnswerModel classAnswerModel;
//        for (int i = 0;i<30;i++){
//            classAnswerModel = new ClassAnswerModel();
//            classAnswerModel.setIcon((new Random().nextInt(4) + 1) + "");
//            classAnswerModel.setName("小李" + i);
//            classAnswerModel.setAnsed("20/35");
//            if (i%3 == 0){
//                classAnswerModel.setCurrentTopic(36);
//            }else{
//                classAnswerModel.setCurrentTopic(18);
//            }
//            classAnswerModel.setTotleTopic(36);
//            data.add(classAnswerModel);
//        }
//    }

    private void getAnswerData(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        Log.d("class","userId:" + userId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        httpTimer = new HttpTimer(getContext(),valuePairs,this,answerIngRequestCode, Connector.getInstance().answeringStudent,true,true);
        httpTimer.setTimerNumber(10);
    }

    /**
     * 结束答题
     */
    private void endClassWork(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        getData(valuePairs,finishAnswerRequestCode,Connector.getInstance().endClassWork,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code == answerIngRequestCode){
            data.clear();
            boolean state = parseData(result);
            Log.d("class","state==>" + state);
            if (state){
                setData();
            }
        }else if(code == finishAnswerRequestCode){
            boolean state = parseEndClass(result);
            if (state){
                showFragmentCallBack.show(new ClassGetScoreFragment(showFragmentCallBack),"ClassGetScoreFragment");
            }

        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    private void initView(){
        end_ans.setOnClickListener(this);
        resend.setOnClickListener(this);
        left_img.setOnClickListener(this);
        right_img.setOnClickListener(this);
        chronometer.setFormat("%s");

        String className = UserInformation.getInstance().getUserInformation().className;
        title.setText(className);

    }

    private void setData(){

        int sumPer = data.size();
        int finshPer = 0;
        for (int i = 0;i<data.size();i++){
            if (data.get(i).getFinish() == 1){
                finshPer++;
            }
        }
        snaa_info.setText("当前答题人数：" + sumPer + "人  已完成"+ finshPer +"人  用时:");


        if (data.size()<=pageCount){
            pages_text.setText("1" + "/" + "1");
            ClassRoomAnswerAdapter classRoomAnswerAdapter = new ClassRoomAnswerAdapter(getContext(),data);
            info_list.setAdapter(classRoomAnswerAdapter);
        }else{
            ClassRoomAnswerAdapter classRoomAnswerAdapter = new ClassRoomAnswerAdapter(getContext(),data.subList(0,pageCount));
            info_list.setAdapter(classRoomAnswerAdapter);

            int l = data.size()/pageCount;
            int a = data.size()%pageCount;
            if(a>0){
                l = l+1;
            }
            totlePage = l;
            pages_text.setText("1" + "/" + totlePage);
        }

    }

    private void setCurrentPageData(List<ClassAnswerModel> currentPageData,int state){
        ClassRoomAnswerAdapter syncadd = new ClassRoomAnswerAdapter(getContext(),currentPageData);
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
        if (view.getId() == R.id.end_ans){
            endClassWork();

        }else if (view.getId() == R.id.resend){

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

    private boolean parseEndClass(String jsonData){
        boolean state = false;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                state = true;
            }

        }catch (Exception e){

        }
        return state;
    }

    private boolean parseData(String jsonData){
        boolean state = false;
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                ClassAnswerModel classAnswerModel = null;
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                for (int i = 0;i<jsonArray.length();i++){
                    classAnswerModel = new ClassAnswerModel();
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
//                    "total": 10,
//                            "finish": 0,
//                            "count": 0,
//                            "usr_id": 13,
//                            "usr_name": "小五"
                    classAnswerModel.setTotleTopic(jsonObject1.optInt("total"));
                    classAnswerModel.setFinish(jsonObject1.optInt("finish"));
                    classAnswerModel.setCurrentTopic(jsonObject1.optInt("count"));
                    classAnswerModel.setUsr_id(jsonObject1.optInt("usr_id"));
                    classAnswerModel.setName(jsonObject1.optString("usr_name"));
                    classAnswerModel.setIcon(jsonObject1.optString("usr_headimg"));
                    data.add(classAnswerModel);
                }
                state = true;
            }

        }catch (Exception e){

        }
        return state;
    }

    @Override
    public void onDestroyView() {
        data.clear();
        if (httpTimer != null){
            httpTimer.stopTimer();
        }
        if (chronometer != null){
            chronometer.stop();
        }

        super.onDestroyView();
    }
}

