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
 * Created by zhengdelong on 2016/11/2.
 */
@SuppressLint("ValidFragment")
public class ClassRoomSeclectFragment extends CportBaseFragment {

    private String getAllStudentRequestCode = "1";
    private String sendSomeRequestCode = "3";

    List<ClassSelectModel> data = new ArrayList<>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    @Bind(R.id.sec_grid)
    NoGridView sec_grid;

    @Bind(R.id.send)
    Button send;

    @Bind(R.id.title_tx)
    TextView title_tx;

    public ClassRoomSeclectFragment(){

    }

    public ClassRoomSeclectFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_test2;
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
        getAllStudent();

        super.onViewCreated(view, savedInstanceState);
    }

    private void getAllStudent(){
        long testId = UserInformation.getInstance().getUserInformation().classId;
        Log.d("class","testId:" + testId);

        getData(null,getAllStudentRequestCode, Connector.getInstance().getAllStudent,true);
    }

    private void sendSomeStudent(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        Log.d("class","userId:" + userId);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId",userId + ""));
        boolean isSend = false;
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<data.size();i++){
            if (data.get(i).getState() == 1){
                isSend = true;
                sb.append(data.get(i).getId() + ";");
//                valuePairs.add(new ReuestKeyValues("studentIds",data.get(i).getId() + ""));
            }
        }
        valuePairs.add(new ReuestKeyValues("studentIds",sb.toString()));
        if (isSend){
            getData(valuePairs,sendSomeRequestCode, Connector.getInstance().sendSomeStudent,true);
        }else{
            Toastor.showLongToast(getContext(),"请选择需要发送的学生");
        }

    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code.equals(getAllStudentRequestCode)){
            parseData(result);
            setView();
        }else if (code.equals(sendSomeRequestCode)){
            boolean sendState = getSendSomeStudentCode(result);
            if (sendState){
                if (showFragmentCallBack == null){
                    Log.d("class","showFragmentCallBack is null...");
                }
                showFragmentCallBack.show(new ClassRoomAnswerFragment(showFragmentCallBack),"ClassRoomAnswerFragment");
            }

        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

//    private void initTestData(){
//        ClassSelectModel classSelectModel;
//        for (int i = 0;i<32;i++){
//            classSelectModel = new ClassSelectModel();
//            classSelectModel.setIcon((new Random().nextInt(4) + 1) + "");
//            classSelectModel.setName("小李" + i);
//            classSelectModel.setState(0);
//
//            data.add(classSelectModel);
//        }
//    }

    private void initView(){
        sec_grid.setOnItemClickListener(onItemClickListener);
        sec_grid.setOverScrollMode(View.OVER_SCROLL_NEVER);
        send.setOnClickListener(this);
        setTitle("发送试题");
    }

    private void setView(){
        ClassRoomSelectAdapter classRoomSelectAdapter = new ClassRoomSelectAdapter(getContext(),data);
        sec_grid.setAdapter(classRoomSelectAdapter);
        title_tx.setText("学生名单 （共"+ data.size() +"人）");
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("class","position==>" + position);
            ImageView imageView = (ImageView) view.findViewById(R.id.incn_sec);
            if (imageView.getVisibility() == View.GONE){
                Log.d("class","iin");
                imageView.setVisibility(View.VISIBLE);
                data.get(position).setState(1);
            }else{
                Log.d("class","oon");
                imageView.setVisibility(View.GONE);
                data.get(position).setState(0);
            }
        }
    };

    @Override
    public void onDestroyView() {
        data.clear();
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send){
            sendSomeStudent();
//            showFragmentCallBack.show(new ClassRoomAnswerFragment(showFragmentCallBack),"ClassRoomAnswerFragment");
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

    private boolean getSendSomeStudentCode(String jsonData){
        try {
            JSONObject jsonObject =  new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0 ){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    private void parseData(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                JSONArray resultArr = jsonObject.optJSONArray("result");
                ClassSelectModel classSelectModel = null;
                for (int i = 0;i<resultArr.length();i++){
                    classSelectModel = new ClassSelectModel();
                    JSONObject jsonObject1 = resultArr.optJSONObject(i);
                    String email = jsonObject1.optString("email");
                    String grade = jsonObject1.optString("grade");
                    String headimg = jsonObject1.optString("headimg");
                    int id = jsonObject1.optInt("id");
                    int lesson = jsonObject1.optInt("lesson");
                    String mobile = jsonObject1.optString("mobile");
                    String name = jsonObject1.optString("name");
                    int online = jsonObject1.optInt("online");
                    String parentMobile = jsonObject1.optString("parentMobile");
                    String password = jsonObject1.optString("password");
                    String school = jsonObject1.optString("school");
                    int type = jsonObject1.optInt("type");
                    classSelectModel.setName(name);
                    classSelectModel.setEmail(email);
                    classSelectModel.setGrade(grade);
                    classSelectModel.setHeadimg(headimg);
                    classSelectModel.setId(id);
                    classSelectModel.setLesson(lesson);
                    classSelectModel.setMobile(mobile);
                    classSelectModel.setOnline(online);
                    classSelectModel.setParentMobile(parentMobile);
                    classSelectModel.setPassword(password);
                    classSelectModel.setSchool(school);
                    classSelectModel.setType(type);
                    data.add(classSelectModel);
                }
            }
        }catch (Exception e){

        }
    }

}
