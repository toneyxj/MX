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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassExaminationAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.ClassExaminationModel;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.utils.MxgsaTagHandler;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * 客观题评分界面
 * Created by zhengdelong on 2016/11/3.
 */
@SuppressLint("ValidFragment")
public class ClassRoomSubjectingFragment extends CportBaseFragment {

    private String addSubjectiveExerciseScore = "1";

    @Bind(R.id.custom_num)
    EditText custom_num;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.sub_name)
    TextView sub_name;

    @Bind(R.id.content_img)
    ImageView content_img;

    @Bind(R.id.sec_grid)
    GridView sec_grid;

    @Bind(R.id.sure_back)
    Button sure_back;

    private int score = 0;

    ClassSelectModel classSelectModel = null;
    private List<ClassExaminationModel> data =  new ArrayList<ClassExaminationModel>();

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public ClassRoomSubjectingFragment(){

    }

    public ClassRoomSubjectingFragment(ClassSelectModel classSelectModel, ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.classSelectModel = classSelectModel;
        this.showFragmentCallBack = showFragmentCallBack;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_subjecting;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        initData();
        setData();
    }

    private void initView(){
        setTitle("主观题评分");
        sec_grid.setOnItemClickListener(onItemClickListener);
        sure_back.setOnClickListener(this);

        String className = UserInformation.getInstance().getUserInformation().className;
        title.setText(className);

    }

    private void initData(){
        ClassExaminationModel classExaminationModel;
        for (int i = 1; i<4;i++){
            classExaminationModel = new ClassExaminationModel();
            if (i == 1){
                classExaminationModel.setId_nums("0");
            }else if (i == 2){
                classExaminationModel.setId_nums("5");
            }else if (i == 3){
                classExaminationModel.setId_nums("10");
            }

            classExaminationModel.setSuject(getString(R.string.test_a));
            data.add(classExaminationModel);
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("class","position==>" + position);
            for (int i = 0;i<data.size();i++){
                View view1 = adapterView.getChildAt(i);
                TextView textView = (TextView) view1.findViewById(R.id.inf);
                textView.setBackgroundResource(R.drawable.qimu_backgroundshape);
                textView.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            TextView textView = (TextView) view.findViewById(R.id.inf);
            textView.setBackgroundResource(R.drawable.qimu_backgroundshape_selected);
            textView.setTextColor(getResources().getColor(R.color.colorWithe));
            String numers = textView.getText().toString();
            if (numers.equals("0")){
                score = 0;
                custom_num.setText("0");
            }else if (numers.equals("5")){
                score = 5;
                custom_num.setText("5");
            }else if (numers.equals("10")){
                score = 10;
                custom_num.setText("10");
            }
        }
    };

    private void setData(){

        Log.d("class","img url===>" + Connector.getInstance().HOST + classSelectModel.getGrade());
        ImageLoader.getInstance().displayImage(Connector.getInstance().HOST + classSelectModel.getGrade(),content_img);

        setTitle(classSelectModel.getTitle(),sub_name);

        sure_back.setOnClickListener(this);
        ClassExaminationAdapter classExaminationAdapter = new ClassExaminationAdapter(getContext(),data);
        sec_grid.setAdapter(classExaminationAdapter);
        sec_grid.setSelection(0);
    }

    private void submitScore(){
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        Log.d("class","answerId==>" + classSelectModel.getId());
        Log.d("class","score==>" + score);
        valuePairs.add(new ReuestKeyValues("answerId",classSelectModel.getId() + ""));
        valuePairs.add(new ReuestKeyValues("score",score + ""));
        getData(valuePairs,addSubjectiveExerciseScore, Connector.getInstance().addSubjectiveExerciseScore,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        Log.d("class","result==>" + result + "   code====>" + code);
        if (code == addSubjectiveExerciseScore){
            boolean state = parseSubmitData(result);
            if (state){
                getFragmentManager().popBackStack();
            }
        }
        super.onSuccess(result, code);
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
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
    public void onClick(View view) {
        if (view.getId() == R.id.sure_back){
            submitScore();
        }
    }
    private boolean parseSubmitData(String jsonData){
        boolean state = false;
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.optInt("code",-1);
            if (code == 0){
                state = true;
            }
        }catch (Exception e){

        }
        return state;
    }

}
