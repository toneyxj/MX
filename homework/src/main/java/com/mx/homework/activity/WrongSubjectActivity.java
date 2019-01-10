package com.mx.homework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.homework.R;
import com.mx.homework.adapter.SubjectWrongGridAdapter;
import com.mx.homework.constants.Constant;
import com.mx.homework.http.HttpVolleyCallback;
import com.mx.homework.http.VolleyHttpUtil;
import com.mx.homework.model.SubjectWrongModel;
import com.mx.mxbase.utils.MXUamManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhengdelong on 16/9/22.
 */

public class WrongSubjectActivity extends Activity{

    private LinearLayout ll_base_back;
    private TextView tv_base_back;
    private TextView tv_base_mid_title;

    private TextView title_info;
    private GridView wrong_grid;
    private int subject = 0;
    private int wrongSubject = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wrong);
        initView();
        initData();
    }

    private void initView(){
        tv_base_mid_title = (TextView) findViewById(R.id.tv_base_mid_title);
        tv_base_mid_title.setVisibility(View.GONE);
        tv_base_back = (TextView)findViewById(R.id.tv_base_back);
        tv_base_back.setText("错题库");
        ll_base_back = (LinearLayout) findViewById(R.id.ll_base_back);
        ll_base_back.setVisibility(View.VISIBLE);
        title_info = (TextView) findViewById(R.id.title_info);
        wrong_grid = (GridView) findViewById(R.id.wrong_grid);
        ll_base_back.setOnClickListener(backClick);
    }

    private void initData(){
        getWrongSubjectData(MXUamManager.queryUser(this));
    }

    private void setData(List<SubjectWrongModel> data){
        String sum = "当前共有"+ subject + "门课程," + wrongSubject + "道错题";
        Log.d("wrong" , sum);
        title_info.setText(sum);
        SubjectWrongGridAdapter subjectWrongGridAdapter = new SubjectWrongGridAdapter(this,data);
        wrong_grid.setAdapter(subjectWrongGridAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WrongSubjectActivity.this.finish();
        }
    };

    /**
     * 获取错题数据
     * @param appSeesion 登录信息
     */
    private void getWrongSubjectData(String appSeesion){
        if (appSeesion == null){
            return;
        }
        if(appSeesion.equals("")){
            return;
        }
        Log.d("wrong","appSeesion==>" + appSeesion);
        HashMap<String, String> param = new HashMap<>();
        param.put("appSession", appSeesion);
        VolleyHttpUtil.get(this, Constant.WRONGSUBJECT, param, new HttpVolleyCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("wrong","data===>" + data);
                List wrongData = parseWrongSubjectData(data);
                setMax(wrongData);
                setData(wrongData);
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

    private void setMax(List<SubjectWrongModel> data){
        int max = -1;
        for(int i = 0;i<data.size();i++){
            int index = data.get(i).getWrongCount();
            if (index >= max){
                max = index;
            }
        }
        for (int j = 0;j<data.size();j++){
            if (data.get(j).getWrongCount() == max){
                data.get(j).setMaxShow(true);
            }else{
                data.get(j).setMaxShow(false);
            }
        }
    }

    private List<SubjectWrongModel> parseWrongSubjectData(String data){
        int max = -1;
        List<SubjectWrongModel> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                SubjectWrongModel subjectWrongModel;
                for (int i = 0;i<jsonArray.length();i++){
                    subject++;
                    JSONObject js = jsonArray.getJSONObject(i);
                    subjectWrongModel = new SubjectWrongModel();
                    int subJectId = js.optInt("subjectId",-1);
                    String subjectName = js.optString("subjectName","");
                    int wrongCount = js.optInt("wrongCount",-1);
                    wrongSubject = wrongSubject + wrongCount;
                    subjectWrongModel.setMaxShow(false);
                    subjectWrongModel.setSubjectId(subJectId);
                    subjectWrongModel.setSubjectName(subjectName);
                    subjectWrongModel.setWrongCount(wrongCount);
                    result.add(subjectWrongModel);
                }
            }
        }catch (Exception e){

        }
        return result;
    };

}
