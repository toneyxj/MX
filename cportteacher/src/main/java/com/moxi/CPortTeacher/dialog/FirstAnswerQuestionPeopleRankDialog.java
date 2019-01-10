package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.ConfigData;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.model.FirstAnswerPeopleRankBeen;
import com.moxi.CPortTeacher.utils.MsecTimer;
import com.moxi.CPortTeacher.utils.TimeSetUtils;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.base.BaseApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 抢答排名
 * Created by Administrator on 2016/11/1.
 */
public class FirstAnswerQuestionPeopleRankDialog extends CPortRequestBaseDialog implements MsecTimer.TimeListener{
    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.answer_number_and_time)
    TextView answer_number_and_time;
    @Bind(R.id.adview_rank)
    LinearLayout adview_rank;
    @Bind(R.id.end_firstanswer)
    Button end_firstanswer;
    private List<FirstAnswerPeopleRankBeen> listData=new ArrayList<>();

   private HttpTimer httpTimer;
    private MsecTimer msecTimer;

    public FirstAnswerQuestionPeopleRankDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_first_answer_question_people;
    }

    @Override
    public void initDialog() {
        title_dialog.setText("抢答");
        setOnKeyListener(this);
        quit_dialog.setOnClickListener(this);
        end_firstanswer.setOnClickListener(this);
        msecTimer=new MsecTimer(0,this);
        msecTimer.startTimer();

        //开启获取抢答结果
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        httpTimer=new HttpTimer(getContext(),values,this,"抢答结果", Connector.getInstance().getRobAnswerResult);
    }
    private void addView(){
        adview_rank.removeAllViews();
        for (int i = 0; i < listData.size(); i++) {
            FirstAnswerPeopleRankBeen been=listData.get(i);

            View view= LayoutInflater.from(getContext()).inflate(R.layout.adapter_first_answer_rank,null);
            TextView index_number= (TextView) view.findViewById(R.id.index_number);
            ImageView photo_src= (ImageView) view.findViewById(R.id.photo_src);
            TextView name= (TextView) view.findViewById(R.id.name);
            TextView time= (TextView) view.findViewById(R.id.time);

            index_number.setText(String.valueOf(i+1)+".");
//            ImageLoader.getInstance().displayImage(been.photo, photo_src, ImageLoadUtils.getoptions(360));
            photo_src.setImageResource(ConfigData.getInstance().photos[Integer.parseInt(been.photo)]);
            name.setText(been.name);
            time.setText(been.getTime());

            adview_rank.addView(view);
        }

    }


    public static void getdialog(Context context, View.OnClickListener listener) {
        FirstAnswerQuestionPeopleRankDialog dialog = new FirstAnswerQuestionPeopleRankDialog(context, R.style.AlertDialogStyle, listener);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 6, 0, BaseApplication.ScreenWidth / 6, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        switch (v.getId()) {
            case R.id.quit_dialog:
                if (end_firstanswer.getVisibility()==View.VISIBLE) {
                    dialogShowOrHide(true, "退出抢答中...");
                    getData(values, "退出抢答", Connector.getInstance().endRobAnswer, true);
                }else {
                    this.dismiss();
                }
                break;
            case R.id.end_firstanswer:
                dialogShowOrHide(true,"结束抢答中...");
                getData(values,"结束抢答",Connector.getInstance().endRobAnswer,true);
                break;
            default:
                break;
        }
    }

    /**
     * 抢答总人数
     */
    private int totals=0;
    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("抢答结果")){
            try {
                JSONObject object=new JSONObject(result);
                JSONObject data=object.getJSONObject("result");
                JSONArray array=data.getJSONArray("resultList");
                 totals=array.length();
                listData.clear();
                listData.addAll(JsonAnalysis.getInstance().getFirstAnswerPeopleRankBeens(array.toString()));
                addView();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (code.equals("结束抢答")){
            httpTimer.stopTimer();
            msecTimer.stopTimer();
            end_firstanswer.setVisibility(View.GONE);
        }else  if (code.equals("退出抢答")){
            httpTimer.stopTimer();
            msecTimer.stopTimer();
            this.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        httpTimer.stopTimer();
        msecTimer.stopTimer();
    }

    @Override
    public void cuttentTime(int time) {
       if (time>=3600){
           List<ReuestKeyValues> values = new ArrayList<>();
           values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
           getData(values,"结束抢答",Connector.getInstance().endRobAnswer,false);
           return;
       }
        int cuT=time;
        answer_number_and_time.setText("抢答人数:"+totals+"人\t\t\t用时"+ TimeSetUtils.secToTime(cuT));
    }

    @Override
    public void onkeyBack() {
        super.onkeyBack();
        if (end_firstanswer.getVisibility()!=View.VISIBLE) {
            this.dismiss();
        }
    }
}
