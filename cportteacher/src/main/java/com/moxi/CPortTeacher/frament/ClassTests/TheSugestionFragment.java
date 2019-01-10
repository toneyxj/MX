package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.config.OttoCode;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.model.TheSugestionModel;
import com.moxi.CPortTeacher.view.PaintInvalidateRectView;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.utils.FileSaveASY;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.utils.Toastor;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 现场出题
 * Created by Administrator on 2016/10/28.
 */
@SuppressWarnings("ResourceAsColor")
@SuppressLint("ValidFragment")
public class TheSugestionFragment extends CportBaseFragment implements View.OnClickListener{

    private String sendAllRequestCode = "1";

    TheSugestionModel theSugestionModelData ;
    Bitmap subjectTitleBitmap = null;

    @Bind(R.id.fragment_sugestion_recycler)
    RecyclerView recyclerView;

    @Bind(R.id.paintInvalidateRectView)
    PaintInvalidateRectView paintInvalidateRectView;

    @Bind(R.id.subject_seclect)
    RadioGroup subject_seclect;

    @Bind(R.id.pencil_cir)
    View pencil_cir;

    @Bind(R.id.erase_cir)
    View erase_cir;

    @Bind(R.id.cutpicc_cir)
    View cutpicc_cir;

    @Bind(R.id.pencil_img)
    ImageView pencil_img;

    @Bind(R.id.erase_img)
    ImageView erase_img;

    @Bind(R.id.cutpicc_img)
    ImageView cutpicc_img;

    @Bind(R.id.judge_img_wrong)
    ImageView judge_img_wrong;

    @Bind(R.id.judge_img_right)
    ImageView judge_img_right;

    @Bind(R.id.judge)
    RelativeLayout judge;

    @Bind(R.id.multise_radio_seclect)
    RelativeLayout multise_radio_seclect;

    @Bind(R.id.radio_seclect)
    RelativeLayout radio_seclect;

    @Bind(R.id.fragment_radiogroup_answer)
    RadioGroup fragment_radiogroup_answer;

    @Bind(R.id.radio_answer_del_img)
    ImageView radio_answer_del_img;

    @Bind(R.id.radio_answer_add_img)
    ImageView radio_answer_add_img;

    @Bind(R.id.radio_answer_del_rel)
    RelativeLayout radio_answer_del_rel;

    @Bind(R.id.radio_answer_add_rel)
    RelativeLayout radio_answer_add_rel;

    @Bind(R.id.fragment_multise_answer)
    LinearLayout fragment_multise_answer;

    @Bind(R.id.multise_answer_add_rel)
    RelativeLayout multise_answer_add_rel;

    @Bind(R.id.multise_answer_del_rel)
    RelativeLayout multise_answer_del_rel;

    @Bind(R.id.radio_current_size_tx)
    TextView radio_current_size_tx;

    @Bind(R.id.multise_current_size_tx)
    TextView multise_current_size_tx;

    @Bind(R.id.send_all_thesugetion)
    TextView send_all_tx;

    @Bind(R.id.send_some_thegustion)
    TextView send_some_tx;

    List<String> multiseRightAnswerList = new ArrayList<>();

//    private AnswerRecyclerAdapter answerAdapter;

    private boolean isInit = false;

    private int type = 1;
    private String radioSeclectStr = "";
    private String saveFilePath = StringUtils.getSDPath() + "sugestion.png";
    private String isTrue = "-1";

    TheSugestionSeclectFragment theSugestionSeclectFragment = null;

    private int defaultRadioGroupSize = 4;
    private int maxRadioButtonSize = 8;
    private int minRadioButtonSize = 2;

    public static final long INTERVAL = 2000L; //防止连续点击的时间间隔
    private static long lastClickTime = 0L; //上一次点击的时间

    private String[] answerStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public TheSugestionFragment(){

    }

    public TheSugestionFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_the_sugestion;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theSugestionModelData = new TheSugestionModel();
        theSugestionSeclectFragment = new TheSugestionSeclectFragment(theSugestionModelData,showFragmentCallBack);
        init();
        this.onRefuresh();
    }

    /**
     * 初始化视图
     */
    private void init() {
        paintInvalidateRectView.setCode(ClassTestFragment.code );
        Log.d("class","init....");
        //单选题答案初始化
        Bitmap nillBitmap = null;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(nillBitmap);
        for (int i = 0;i<defaultRadioGroupSize;i++) {
            RadioButton tempButton = new RadioButton(getActivity());
//            String time = String.valueOf(System.currentTimeMillis());
//            int id = Integer.parseInt(time);
//            Log.d("class","radio id===>" + id);
            int id = i + 10000;
            tempButton.setId(id);
            if ( i == 0){
                subject_seclect.check(id);
            }
            tempButton.setBackgroundResource(R.drawable.classfiy_backphoto);   // 设置RadioButton的背景图片
            tempButton.setPadding(0, 10, 0, 10);                 // 设置文字距离按钮四周的距离
            tempButton.setText(answerStr[i]);
            tempButton.setTextSize(22);
            tempButton.setTag(answerStr[i]);
            tempButton.setTextColor(getResources().getColorStateList(R.color.classfiy_textcolor));
            tempButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tempButton.setChecked(false);
            tempButton.setButtonDrawable(bitmapDrawable);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            layoutParams.setMargins(16, 0, 0, 0);
            fragment_radiogroup_answer.addView(tempButton, i, layoutParams);
        }

        send_all_tx.setOnClickListener(this);
        send_some_tx.setOnClickListener(this);
        multise_answer_add_rel.setOnClickListener(this);
        multise_answer_del_rel.setOnClickListener(this);
        radio_answer_del_rel.setOnClickListener(this);
        radio_answer_add_rel.setOnClickListener(this);
        judge_img_wrong.setOnClickListener(this);
        judge_img_right.setOnClickListener(this);
        pencil_img.setOnClickListener(this);
        erase_img.setOnClickListener(this);
        cutpicc_img.setOnClickListener(this);
        pencil_cir.setVisibility(View.VISIBLE);
        erase_cir.setVisibility(View.INVISIBLE);
        cutpicc_cir.setVisibility(View.INVISIBLE);
//        subject_seclect.check(R.id.default_check);
        fragment_radiogroup_answer.setOnCheckedChangeListener(onRadioCheckedChangeListener);
        subject_seclect.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    public void onResume() {
        if (this.isHidden())return;
            Log.d("class", "onResume....the...");
            subject_seclect.check(R.id.default_check);
            if (theSugestionModelData != null) {

            }
        paintInvalidateRectView.onResume();
        paintInvalidateRectView.onPause();
        super.onResume();
    }
    @Subscribe
    public void resume(OttoBeen been){
        if (isfinish||this.isHidden())return;
        if (been.code.equals(OttoCode.RESUME)){
            paintInvalidateRectView.onResume();
            paintInvalidateRectView.onPause();
        }else if (been.code.equals(OttoCode.STOP)){
            paintInvalidateRectView.onstop();
        }
    }

    @Override
    public void onStop() {
        if (this.isHidden())return;
        super.onStop();
        paintInvalidateRectView.onstop();
    }

    private void initMuchSelect(){

        if (isInit){
            return;
        }
        isInit = true;
        //多选题答案初始化
        for (int j = 0;j<defaultRadioGroupSize;j++){
            TextView tempTextView = new TextView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,1);
            layoutParams.setMargins(16,0,0,0);
//            tempTextView.setId( 1001 + j);
            tempTextView.setTextSize(22);
            tempTextView.setText(answerStr[j]);
            tempTextView.setBackgroundResource(R.drawable.classfiy_seclect_backphoto);
            tempTextView.setGravity(Gravity.CENTER);
            tempTextView.setClickable(true);
            tempTextView.setTag("0");
            fragment_multise_answer.addView(tempTextView,j,layoutParams);
            tempTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String)v.getTag();
                    if (tag.equals("1")){
                        TextView textView = (TextView) v;
                        textView.setTextColor(Color.BLACK);
                        textView.setBackgroundResource(R.drawable.di_white_bian_font_line1);
                        textView.setTag("0");
                        String answerStr = textView.getText().toString();
                        multiseRightAnswerList.remove(answerStr);
                    }else{
                        TextView textView = (TextView) v;
                        textView.setTextColor(Color.WHITE);
                        textView.setBackgroundResource(R.drawable.di_black);
                        textView.setTag("1");
                        String answerStr = textView.getText().toString();
                        multiseRightAnswerList.add(answerStr);
                    }
                    Log.d("class","=============");
                    for (int i = 0;i<multiseRightAnswerList.size();i++){
                        Log.d("class","多选题：" + multiseRightAnswerList.get(i));
                    }
                }
            });
        }
    }

//
    RadioGroup.OnCheckedChangeListener onRadioCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //获取变更后的选中项的ID
            int radioButtonId = group.getCheckedRadioButtonId();
            Log.d("class","radioButtonId===>" + radioButtonId);
            Log.d("class","checkedId===>" + checkedId);
            //根据ID获取RadioButton的实例
            RadioButton rb = (RadioButton)getActivity().findViewById(radioButtonId);
            radioSeclectStr = rb.getTag().toString();
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //获取变更后的选中项的ID
            int radioButtonId = group.getCheckedRadioButtonId();
            //根据ID获取RadioButton的实例
            RadioButton rb = (RadioButton)getActivity().findViewById(radioButtonId);
            if (rb.getText().toString().equals("单选题")){
                Log.d("sug","click 单选题");
                type = 1;
                theSugestionModelData.setType(1);
                radio_seclect.setVisibility(View.VISIBLE);
                multise_radio_seclect.setVisibility(View.GONE);
                judge.setVisibility(View.GONE);
            }else if (rb.getText().toString().equals("多选题")){
                theSugestionModelData.setType(2);
                initMuchSelect();
                Log.d("sug","click 多选题");
                type = 2;
                radio_seclect.setVisibility(View.GONE);
                multise_radio_seclect.setVisibility(View.VISIBLE);
                judge.setVisibility(View.GONE);
            }else if (rb.getText().toString().equals("主观题")){
                theSugestionModelData.setType(3);
                type = 3;
                Log.d("sug","click 主观题");
                radio_seclect.setVisibility(View.INVISIBLE);
                multise_radio_seclect.setVisibility(View.GONE);
                judge.setVisibility(View.GONE);
            }else if (rb.getText().toString().equals("判断题")){
                theSugestionModelData.setType(4);
                type = 4;
                Log.d("sug","click 判断题");
                radio_seclect.setVisibility(View.GONE);
                multise_radio_seclect.setVisibility(View.GONE);
                judge.setVisibility(View.VISIBLE);
            }
        }
    };
//
    /**
     * 获取题目数据
     */
    private void getTheSugestionData(){
        subjectTitleBitmap = paintInvalidateRectView.CurrentBitmap();
        new FileSaveASY(subjectTitleBitmap, saveFilePath, new FileSaveASY.saveSucess() {
            @Override
            public void onSaveSucess(boolean is) {
                if (is) {
                    if (type == 1){
                        theSugestionModelData.setType(1);
                        theSugestionModelData.setAnswer(radioSeclectStr);
                        theSugestionModelData.setOption(Integer.parseInt(radio_current_size_tx.getText().toString()));
                        theSugestionModelData.setFilePath(saveFilePath);
                    }else if (type == 2){
                        int size = multiseRightAnswerList.size();
                        String answer = distMultiseListData();
                        Log.d("class","answer====>"+ answer);
                        theSugestionModelData.setType(2);
                        theSugestionModelData.setAnswer(answer);
                        theSugestionModelData.setOption(Integer.parseInt(multise_current_size_tx.getText().toString()));
                        theSugestionModelData.setFilePath(saveFilePath);
                    }else if (type == 3){
                        theSugestionModelData.setType(3);
                        theSugestionModelData.setAnswer("");
                        theSugestionModelData.setOption(0);
                        theSugestionModelData.setFilePath(saveFilePath);
                    }else if (type == 4){
                        theSugestionModelData.setType(4);
                        theSugestionModelData.setAnswer(isTrue);
                        theSugestionModelData.setOption(2);
                        theSugestionModelData.setFilePath(saveFilePath);
                    }
                    if (checkData(theSugestionModelData)){
                        sendAll();
                    }

                }else {
                    StringUtils.deleteFile(saveFilePath);
                    ToastUtils.getInstance().showToastShort("图片获取失败！请重试");
                }
            }
        }).execute();
    }

    private String distMultiseListData(){
        Collections.sort(multiseRightAnswerList,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        Log.d("sug","====================");
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<multiseRightAnswerList.size();i++){
            Log.d("sug","multiseRightAnswerList====>" + multiseRightAnswerList.get(i));
            sb.append(multiseRightAnswerList.get(i));
        }
        Log.d("sug","====================>>" + sb.toString());
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pencil_img){
            pencil_cir.setVisibility(View.VISIBLE);
            erase_cir.setVisibility(View.INVISIBLE);
            cutpicc_cir.setVisibility(View.INVISIBLE);
            paintInvalidateRectView.setPaint(0);
        }else if (v.getId() == R.id.erase_img){
            pencil_cir.setVisibility(View.INVISIBLE);
            erase_cir.setVisibility(View.VISIBLE);
            cutpicc_cir.setVisibility(View.INVISIBLE);
            paintInvalidateRectView.setPaint(1);
        }else if(v.getId() == R.id.cutpicc_img){
            pencil_cir.setVisibility(View.VISIBLE);
            erase_cir.setVisibility(View.INVISIBLE);
            cutpicc_cir.setVisibility(View.INVISIBLE);
            paintInvalidateRectView.clearScreenp();
            paintInvalidateRectView.setPaint(0);
        } else if(v.getId() == R.id.judge_img_right){
            isTrue = "1";
            judge_img_wrong.setBackgroundResource(R.drawable.di_white_bian_font_line1);
            judge_img_wrong.setTag("0");
            String tag = (String)v.getTag();
            if (tag.equals("1")){
                isTrue = "-1";
                judge_img_right.setBackgroundResource(R.drawable.di_white_bian_font_line1);
                judge_img_right.setTag("0");
                judge_img_right.setImageResource(R.mipmap.right);
            }else{
                isTrue = "1";
                judge_img_right.setBackgroundResource(R.drawable.di_black);
                judge_img_right.setTag("1");
                judge_img_right.setImageResource(R.mipmap.w_right);
                judge_img_wrong.setImageResource(R.mipmap.wrong);
            }
        }else if(v.getId() == R.id.judge_img_wrong){
            isTrue = "0";
            judge_img_right.setBackgroundResource(R.drawable.di_white_bian_font_line1);
            judge_img_right.setTag("0");
            String tag = (String)v.getTag();
            if (tag.equals("1")){
                isTrue = "-1";
                Log.d("class","1");
                judge_img_wrong.setBackgroundResource(R.drawable.di_white_bian_font_line1);
                judge_img_wrong.setTag("0");
                judge_img_wrong.setImageResource(R.mipmap.wrong);
            }else{
                isTrue = "0";
                Log.d("class","0");
                judge_img_wrong.setBackgroundResource(R.drawable.di_black);
                judge_img_wrong.setTag("1");
                judge_img_right.setImageResource(R.mipmap.right);
                judge_img_wrong.setImageResource(R.mipmap.w_wrong);
            }
        }else if(v.getId() == R.id.radio_answer_add_rel){
            addRadioButton();
        }else if(v.getId() == R.id.radio_answer_del_rel){
            delRadioButton();
        }else if (v.getId() == R.id.multise_answer_del_rel){
            delMultiseTextView();
        }else if (v.getId() == R.id.multise_answer_add_rel){
            addMultiseTextView();
        }else if (v.getId() == R.id.send_all_thesugetion){
            if (checkClickTime()){
                getTheSugestionData();
            }

        }else if (v.getId() == R.id.send_some_thegustion){
            if (theSugestionSeclectFragment != null){

                subjectTitleBitmap = paintInvalidateRectView.CurrentBitmap();
                new FileSaveASY(subjectTitleBitmap, saveFilePath, new FileSaveASY.saveSucess() {
                    @Override
                    public void onSaveSucess(boolean is) {
                        if (is) {
                            if (type == 1){
                                theSugestionModelData.setType(1);
                                theSugestionModelData.setAnswer(radioSeclectStr);
                                theSugestionModelData.setOption(Integer.parseInt(radio_current_size_tx.getText().toString()));
                                theSugestionModelData.setFilePath(saveFilePath);
                            }else if (type == 2){
                                int size = multiseRightAnswerList.size();
                                String answer = distMultiseListData();
                                theSugestionModelData.setType(2);
                                theSugestionModelData.setAnswer(answer);
                                theSugestionModelData.setOption(Integer.parseInt(multise_current_size_tx.getText().toString()));
                                theSugestionModelData.setFilePath(saveFilePath);
                            }else if (type == 3){
                                theSugestionModelData.setType(3);
                                theSugestionModelData.setAnswer("");
                                theSugestionModelData.setOption(0);
                                theSugestionModelData.setFilePath(saveFilePath);
                            }else if (type == 4){
                                theSugestionModelData.setType(4);
                                theSugestionModelData.setAnswer(isTrue);
                                theSugestionModelData.setOption(2);
                                theSugestionModelData.setFilePath(saveFilePath);
                            }
                            showFragmentCallBack.show(theSugestionSeclectFragment,"TheSugestionSeclectFragment");
                        }else {
                            StringUtils.deleteFile(saveFilePath);
                            ToastUtils.getInstance().showToastShort("图片获取失败！请重试");
                        }
                    }
                }).execute();
            }
        }
    }

    private void sendAll(){
        long userId = UserInformation.getInstance().getUserInformation().myid;
        PostFormBuilder pos = OkHttpUtils.post().url(Connector.getInstance().addQuestion);
        pos.addParams("userId",userId + "");
        if (type == 1){
            pos.addParams("type",1 + "");
        }else if (type == 2){
            pos.addParams("type",2 + "");
        }else if (type == 3){
            pos.addParams("type",3 + "");
        }else if (type == 4){
            pos.addParams("type",4 + "");
        }else{
            return;
        }
        pos.addParams("studentIds","");
        Log.d("class","send theSugestionModelData.getAnswer() ===>" + theSugestionModelData.getAnswer());
        pos.addParams("answer",theSugestionModelData.getAnswer());
        pos.addParams("option",theSugestionModelData.getOption() + "");
        Log.d("class","filePath===>" + theSugestionModelData.getFilePath());
        pos.addFile("file","sugestion.png",new File(theSugestionModelData.getFilePath()));
        pos.build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("class","data=====>" + response);
                if (response != null){
                    boolean state = getSendAllState(response);
                    if (state){
                        // TODO: 2016/11/29 发送成功
                        Log.d("class","发送成功");
                        showFragmentCallBack.show(new TheSugestioningFragment(theSugestionModelData,showFragmentCallBack),"TheSugestioningFragment");
                    }else{
                        // TODO: 2016/11/29 发送失败
                        Log.d("class","发送失败");
                    }
                }else{
                    // TODO: 2016/11/29 发送失败
                    Log.d("class","发送失败");
                }
            }
        });

    }

    private boolean checkData(TheSugestionModel theSugestionModel){
        int type = theSugestionModel.getType();
        if (type == 1 || type == 2){
            String answer = theSugestionModel.getAnswer();
            if (!answer.equals("")){
                return true;
            }
        }else if (type == 3){
            return true;
        }else if (type == 4){
            if (!isTrue.equals("-1")){
                return true;
            }
        }
        Toastor.showLongToast(getActivity(),"请先泽正确答案！");
        return false;
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","data=====>" + result);
        if (code.equals(sendAllRequestCode)){
            boolean state = getSendAllState(result);
            if (state){
                // TODO: 2016/11/29 发送成功
                Log.d("class","发送成功");
            }else{
                // TODO: 2016/11/29 发送失败
                Log.d("class","发送失败");
            }
        }

        super.onSuccess(result, code);
    }

    private boolean getSendAllState(String jsonData){
        boolean state = false;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                state = true;
            }
        }catch (Exception e){
            state = false;
        }
        return state;
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        Log.d("class","code=====>" + code + "   failCode=====>" + failCode + "    msg=====>" + msg );
        super.onFail(code, showFail, failCode, msg);
    }

    /**
     * 单选题删除一个选项
     */
    private void delRadioButton(){
        int size = Integer.parseInt(radio_current_size_tx.getText().toString());
        if (size <= minRadioButtonSize){
            return;
        }
        fragment_radiogroup_answer.removeViewAt(size-1);
        size = size - 1;
        radio_current_size_tx.setText(size + "");
    }

    /**
     * 单选题增加一个选项
     */
    private void addRadioButton(){
        int size = Integer.parseInt(radio_current_size_tx.getText().toString());
        if (size >= maxRadioButtonSize){
            return;
        }
        Bitmap nillBitmap = null;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(nillBitmap);
        RadioButton tempButton = new RadioButton(getActivity());
        tempButton.setBackgroundResource(R.drawable.classfiy_backphoto);   // 设置RadioButton的背景图片
        tempButton.setPadding(0, 10, 0, 10);                 // 设置文字距离按钮四周的距离
        tempButton.setText(answerStr[size]);
        tempButton.setTag(answerStr[size]);
        tempButton.setTextSize(22);
        tempButton.setTextColor(getResources().getColorStateList(R.color.classfiy_textcolor));
        tempButton.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tempButton.setChecked(false);
        tempButton.setButtonDrawable(bitmapDrawable);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1);
        layoutParams.setMargins(16,0,0,0);
        fragment_radiogroup_answer.addView(tempButton,
                size,
                layoutParams);
        size = size + 1;
        radio_current_size_tx.setText(size + "");
    }

    /**
     * 多选题删除一个选项
     */
    private void delMultiseTextView(){

        int size = Integer.parseInt(multise_current_size_tx.getText().toString());
        if (size <= minRadioButtonSize){
            return;
        }
        TextView textView = (TextView) fragment_multise_answer.getChildAt(size-1);
        multiseRightAnswerList.remove(textView.getText().toString());
        fragment_multise_answer.removeViewAt(size-1);
        size = size - 1;
        multise_current_size_tx.setText(size + "");

    }

    /**
     * 多选题增加一个选项
     */
    private void addMultiseTextView(){
        int size = Integer.parseInt(multise_current_size_tx.getText().toString());
        if (size >= maxRadioButtonSize){
            return;
        }
        TextView tempTextView = new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1);
        layoutParams.setMargins(16,0,0,0);
        tempTextView.setTextSize(22);
        tempTextView.setText(answerStr[size]);
        tempTextView.setBackgroundResource(R.drawable.classfiy_seclect_backphoto);
        tempTextView.setGravity(Gravity.CENTER);
        tempTextView.setClickable(true);
        tempTextView.setTag("0");
        fragment_multise_answer.addView(tempTextView,size,layoutParams);
        tempTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String)v.getTag();
                if (tag.equals("1")){
                    TextView textView = (TextView) v;
                    textView.setTextColor(Color.BLACK);
                    textView.setBackgroundResource(R.drawable.di_white_bian_font_line1);
                    textView.setTag("0");
                    String answerStr = textView.getText().toString();
                    multiseRightAnswerList.remove(answerStr);
                }else{
                    TextView textView = (TextView) v;
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.di_black);
                    textView.setTag("1");
                    String answerStr = textView.getText().toString();
                    multiseRightAnswerList.add(answerStr);
                }
                Log.d("class","=============");
                for (int i = 0;i<multiseRightAnswerList.size();i++){
                    Log.d("class","多选题：" + multiseRightAnswerList.get(i));
                }
            }
        });
        size = size + 1;
        multise_current_size_tx.setText(size + "");

    }

    @Override
    public void onclickBack() {
        super.onclickBack();
        theSugestionSeclectFragment.onclickBack();
    }

    class AnswerRecyclerAdapter extends RecyclerView.Adapter {

        private String[] answerStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        private Context context;

        public AnswerRecyclerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_sugetion_answer_item, parent, false);
            return new AnswerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((AnswerViewHolder) holder).tvAnswerIndex.setText(answerStr[position]);
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class AnswerViewHolder extends RecyclerView.ViewHolder {
            private TextView tvAnswerIndex;

            public AnswerViewHolder(View itemView) {
                super(itemView);
                tvAnswerIndex = (TextView) itemView.findViewById(R.id.tv_sug_item_answer_index);
            }
        }
    }

    public static boolean checkClickTime() {
        long time = System.currentTimeMillis();
        lastClickTime = time;
        if ( ( time - lastClickTime ) > INTERVAL )
        {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        Log.d("class","onDestroyView....the..");
        isInit = false;
        isTrue = "-1";
        if (multiseRightAnswerList != null){
            multiseRightAnswerList.clear();
        }
        fragment_radiogroup_answer.removeAllViews();
        fragment_multise_answer.removeAllViews();
        super.onDestroyView();
    }
}
