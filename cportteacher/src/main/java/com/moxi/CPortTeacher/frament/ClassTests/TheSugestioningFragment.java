package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomSelectAdapter;
import com.moxi.CPortTeacher.adapter.ThesugestionSelectAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.model.TheSugestionModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.NoGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by zhengdelong on 2016/11/25.
 */
@SuppressLint("ValidFragment")
public class TheSugestioningFragment extends CportBaseFragment implements View.OnClickListener {

    private String thesugestionAnswerIngRequestCode = "1";
    private String sendSomeRequestCode = "3";
    private TheSugestionModel theSugestionModel;

    List<ClassSelectModel> data = new ArrayList<>();

    private HttpTimer httpTimer;
    private ThesugestionAnswerModel thesugestionAnswerModel = null;

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    @Bind(R.id.sec_grid)
    NoGridView sec_grid;

    @Bind(R.id.send)
    Button send;

    @Bind(R.id.title_tx)
    TextView title_tx;

    public TheSugestioningFragment(){

    }

    public TheSugestioningFragment(TheSugestionModel theSugestionModel, ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
        this.theSugestionModel = theSugestionModel;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_theguestioning;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("class","ClassRoomSeclectFragment onCreate.....");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("class", "ClassRoomSeclectFragment onViewCreated.....");
        initView();
        getAnswerData();
        this.onRefuresh();
        super.onViewCreated(view, savedInstanceState);
    }

    private void getAnswerData(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        Log.d("class","userId:" + userId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        httpTimer = new HttpTimer(getContext(),valuePairs,this,thesugestionAnswerIngRequestCode, Connector.getInstance().getQuestionResult,true,true);
        httpTimer.setTimerNumber(10);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code.equals(thesugestionAnswerIngRequestCode)){
            parseThesugestionAnswerData(result);
            setView();
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    private void initView(){
        sec_grid.setOnItemClickListener(onItemClickListener);
        send.setOnClickListener(this);
//        setTitle("现场出题");
    }

    private void setView(){
//        ThesugestionSelectAdapter classRoomSelectAdapter = new ThesugestionSelectAdapter(getContext(),thesugestionAnswerModel.getAnswerList());
        ThesugestionSelectAdapter classRoomSelectAdapter = new ThesugestionSelectAdapter(getContext(),thesugestionAnswerModel.getAnswerList());
        sec_grid.setAdapter(classRoomSelectAdapter);
        title_tx.setText("学生名单（共"+ thesugestionAnswerModel.getAnswerList().size() +"人）");
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
        }
    };

    @Override
    public void onDestroyView() {
        if (httpTimer != null){
            httpTimer.stopTimer();
        }
        data.clear();
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send){
            // TODO: 2016/11/30 结束答题
            showFragmentCallBack.show(new ThesugestionResultFragment(thesugestionAnswerModel,showFragmentCallBack),"ThesugestionResultFragment");

        }
    }

    @Override
    public void onclickBack() {
        Log.d("class","onclickBack...");
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager!= null){
            fragmentManager.popBackStack();
        }
        super.onclickBack();
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
            }
        }catch (Exception e){

        }
    }

}
