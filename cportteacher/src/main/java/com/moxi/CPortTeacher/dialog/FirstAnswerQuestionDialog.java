package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 抢答
 * Created by Administrator on 2016/10/31.
 */
public class FirstAnswerQuestionDialog extends CPortRequestBaseDialog  {

    @Bind(R.id.main_layout)
    RelativeLayout main_layout;

    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.start_firstanswer)
    Button start_firstanswer;

    public FirstAnswerQuestionDialog(Context context, int themeResId, View.OnClickListener listener) {
        super(context, themeResId);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            setRectViewHeight(main_layout.getHeight());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_first_answer_question;
    }

    @Override
    public void initDialog() {
        title_dialog.setText("抢答");
        quit_dialog.setOnClickListener(this);

        start_firstanswer.setOnClickListener(this);
        setRect_view(R.id.rect_view);

    }

    public static void getdialog(Context context, View.OnClickListener listener) {
        FirstAnswerQuestionDialog dialog = new FirstAnswerQuestionDialog(context, R.style.AlertDialogStyle, listener);
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
        switch (v.getId()) {
            case R.id.quit_dialog:
                this.dismiss();
                break;
            case R.id.start_firstanswer:
                startRobAnswer();
                break;
            default:
                break;
        }
    }

    private void startRobAnswer( ) {
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "开启抢答...");
        getData(values, "开启抢答", Connector.getInstance().startRobAnswer, true);
    }
    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("开启抢答")) {
            FirstAnswerQuestionPeopleRankDialog.getdialog(getContext(), this);
            FirstAnswerQuestionDialog.this.dismiss();
        }else if (code.equals("抢答初始化")){
            FirstAnswerQuestionPeopleRankDialog.getdialog(getContext(), this);
            FirstAnswerQuestionDialog.this.dismiss();
        }
    }


}
