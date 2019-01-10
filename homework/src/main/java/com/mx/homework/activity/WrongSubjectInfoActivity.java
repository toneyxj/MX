package com.mx.homework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mx.homework.R;
import com.mx.homework.adapter.SubjectWrongInfoGridAdapter;
import com.mx.homework.constants.Constant;
import com.mx.homework.http.HttpVolleyCallback;
import com.mx.homework.http.VolleyHttpUtil;
import com.mx.homework.model.SubjectAnswerInfoModel;
import com.mx.homework.model.SubjectInfoModel;
import com.mx.homework.model.SubjectModel;
import com.mx.homework.model.SubjectWrongInfoModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/22.
 */

public class WrongSubjectInfoActivity extends Activity {

    private LinearLayout ll_base_back;
    private TextView tv_base_back;
    private TextView tv_base_mid_title;

    private TextView title_info;
    private ListView info_list;

    private ImageView pre_page;
    private ImageView next_page;
    private TextView page;

    private int subjectId = -1;
    private String subjectName;
    private int wrongCount;

    private int currentPage = 1;
    private int sumPage = 0;

    String appSession = "";

    SubjectWrongInfoGridAdapter subjectWrongInfoGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_wrong_subject_info);
        initView();
        getIntentData();
        initData();
        login();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        subjectId = intent.getIntExtra("subjectId",-1);
        subjectName = intent.getStringExtra("subjectName");
        wrongCount = intent.getIntExtra("wrongCount",-1);
        Log.d("wrong","subjectId==>" + subjectId);
    }

    private void initData(){
        String title = "科目:" + subjectName  + "     " + "共" + wrongCount + "道题";
        title_info.setText(title);
    }

    private void setData(SubjectInfoModel subjectInfoModel){
        sumPage = subjectInfoModel.getPageCount();
        currentPage = subjectInfoModel.getPage();

        page.setText(currentPage + "/" + sumPage);
        setPreNextIcon();

        if (subjectInfoModel.getData() == null){
            return;
        }
        subjectWrongInfoGridAdapter = new SubjectWrongInfoGridAdapter(this,subjectInfoModel.getData());
        info_list.setAdapter(subjectWrongInfoGridAdapter);
    }

    private void setPreNextIcon(){
        pre_page.setImageResource(R.mipmap.arrow_left);
        next_page.setImageResource(R.mipmap.arrow_right);
        if (currentPage == 1){
            pre_page.setImageResource(R.mipmap.arrow_left_dd);
            next_page.setImageResource(R.mipmap.arrow_right);
        }
        if (currentPage == sumPage){
            pre_page.setImageResource(R.mipmap.arrow_left);
            next_page.setImageResource(R.mipmap.arrow_right_dd);
        }
    }

    private void getWrongInfoData(String appSession,int page){
        HashMap param = new HashMap();
        //登陆信息
        param.put("appSession",appSession);
        //科目ID
        param.put("subjectId",subjectId);
        //当前页数
        param.put("page",page + "");
        //每页两条数据
        param.put("rows",2 + "");
        VolleyHttpUtil.get(this, Constant.WRONGSUBJECTINFO, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("wrong","getWrongInfoData==>" + data);
                setData(parseSubjectRongInfoData(data));
            }

            @Override
            public void onFilad(String msg) {

            }
        });
    }

    private void initView(){
        page = (TextView) findViewById(R.id.page);
        pre_page = (ImageView) findViewById(R.id.pre_page);
        next_page = (ImageView) findViewById(R.id.next_page);
        pre_page.setOnClickListener(preClick);
        next_page.setOnClickListener(nextClick);
        info_list = (ListView) findViewById(R.id.info_list);
        title_info = (TextView) findViewById(R.id.title_info);
        tv_base_mid_title = (TextView) findViewById(R.id.tv_base_mid_title);
        tv_base_mid_title.setVisibility(View.GONE);
        tv_base_back = (TextView)findViewById(R.id.tv_base_back);
        tv_base_back.setText("错题库");
        ll_base_back = (LinearLayout) findViewById(R.id.ll_base_back);
        ll_base_back.setVisibility(View.VISIBLE);
        ll_base_back.setOnClickListener(backClick);
    }

    View.OnClickListener preClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentPage == 1 ){
                return;
            }
            getWrongInfoData(appSession,currentPage - 1);
        }
    };

    View.OnClickListener nextClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentPage == sumPage){
                return;
            }
            getWrongInfoData(appSession, currentPage + 1);
        }
    };

    /**
     * 登陆
     */
    private void login(){
        HashMap<String, String> param = new HashMap<>();
        param.put("mobile", "13912341234");
        param.put("password", "123456");
        VolleyHttpUtil.post(this, Constant.LONGINURL, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("wrong",data);
                appSession = parseDataLogin(data);
                Log.d("wrong",appSession);
                getWrongInfoData(appSession,1);
            }

            @Override
            public void onFilad(String msg) {

            }
        });
    }

    private String parseDataLogin(String data){
        try{
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                // TODO: 16/9/22 登陆成功
                JSONObject result = jsonObject.getJSONObject("result");
                String appSeesion = result.optString("appSession","");
                return appSeesion;
            }
        }catch (Exception e){

        }
        return "";
    }

    private SubjectInfoModel parseSubjectRongInfoData(String data){
        SubjectInfoModel subjectInfoModel = new SubjectInfoModel();
        try {
            Log.d("parse","data replace==>" + data);
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                JSONObject result = jsonObject.optJSONObject("result");
                subjectInfoModel.setPage(result.optInt("page",-1));
                subjectInfoModel.setPageCount(result.optInt("pageCount",-1));

                List<SubjectWrongInfoModel> subjectWrongInfoModelList = new ArrayList<>();
                SubjectWrongInfoModel subjectInfoModel1;
                JSONArray list = result.optJSONArray("list");
                List<SubjectAnswerInfoModel> option;
                SubjectAnswerInfoModel subjectAnswerInfoModel;
                for (int  i =0;i<list.length();i++){
                    subjectInfoModel1 = new SubjectWrongInfoModel();
                    JSONObject infoData = list.getJSONObject(i);
                    String title = infoData.optString("title","");
                    String analysis = infoData.optString("analysis","");
                    int allCount = infoData.optInt("allCount",-1);
                    int rightCount = infoData.optInt("rightCount",-1);
                    String answer = infoData.optString("answer","");
                    String wrongAnswer = infoData.optString("wrongAnswer","");
                    Log.d("parse","wrongAnswer" + wrongAnswer);
                    Log.d("parse","allCount" + allCount);
                    Log.d("parse","rightCount" + rightCount);
                    option = new ArrayList<>();
                    String optStr = infoData.optString("option");
                    Log.d("parse","optStr==>" + optStr);
                    JSONObject optObj = new JSONObject(optStr);
                    JSONArray optionArray = optObj.optJSONArray("options");
                    subjectAnswerInfoModel = new SubjectAnswerInfoModel();
                    for (int j = 0;j<optionArray.length();j++){
                        JSONObject optionJson = optionArray.getJSONObject(j);
                        String answ = optionJson.optString("answer");
                        JSONArray optionsArray = optionJson.optJSONArray("ABCDS");

                        List<SubjectModel> subjectModels = new ArrayList<>();
                        SubjectModel subjectModel;
                        for (int k=0;k<optionsArray.length();k++){
                            subjectModel = new SubjectModel();
                            JSONObject abcd = optionsArray.getJSONObject(k);
                            String id = abcd.optString("id","");
                            String desc = abcd.optString("desc","");
                            Log.d("parse","desc" + desc);
                            subjectModel.setId(id);
                            subjectModel.setDesc(desc);
                            subjectModels.add(subjectModel);
                        }
                        subjectAnswerInfoModel.setAnswer(answ);
                        subjectAnswerInfoModel.setAns(subjectModels);
                        option.add(subjectAnswerInfoModel);
                    }

                    subjectInfoModel1.setOption(option);
                    subjectInfoModel1.setTitle(title);
                    subjectInfoModel1.setAnalysis(analysis);
                    subjectInfoModel1.setAllCount(allCount);
                    subjectInfoModel1.setRightCount(rightCount);
                    subjectInfoModel1.setAnswer(answer);
                    subjectInfoModel1.setWrongAnswer(wrongAnswer);
                    subjectWrongInfoModelList.add(subjectInfoModel1);
                }
                subjectInfoModel.setData(subjectWrongInfoModelList);
            }
            Log.d("parse","success");
            return subjectInfoModel;
        }catch (Exception e){
            Log.d("parse",e.getMessage());
        }
        return subjectInfoModel;
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WrongSubjectInfoActivity.this.finish();
        }
    };



}
