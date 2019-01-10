package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.VoteProgressAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.model.VoteProgressModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.view.NoListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 投票进度
 * Created by Administrator on 2016/11/4.
 */
public class VoteProgressDialog extends CPortRequestBaseDialog {
    public VoteProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.vote_hitn)
    TextView vote_hitn;
    @Bind(R.id.vote_progress_list)
    NoListView vote_progress_list;
    @Bind(R.id.end_vote)
    Button end_vote;
    private HttpTimer httpTimer;

    private List<VoteProgressModel> listData = new ArrayList<>();
    private VoteProgressAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_vote_progress;
    }

    @Override
    public void initDialog() {
        title_dialog.setText("投票进度");
        quit_dialog.setOnClickListener(this);
        end_vote.setOnClickListener(this);

        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        httpTimer = new HttpTimer(getContext(), values, this, "获取进度", Connector.getInstance().getVoteProgress);
    }

    /**
     * 总人数
     */
    private int totals;
    /**
     * 参与投票人数
     */
    private int voteCount;

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("结束投票")) {
            httpTimer.stopTimer();
            VoteResultDialog.getdialog(getContext(), listData, "投票人数：" + totals + "人\t\t\t\t已投票：" + String.valueOf(voteCount) + "人");
            this.dismiss();
        } else if (code.equals("获取进度")) {
            try {
                JSONObject object = new JSONObject(result);
                JSONObject result1 = object.getJSONObject("result");
                totals = result1.getInt("count");
                voteCount = result1.getInt("voteCount");

                vote_hitn.setText("当前人数：" + totals + "人\t\t\t\t已投票：" + voteCount + "人");
                JSONArray array = result1.getJSONArray("resultList");
                listData.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    int count = item.getInt("count");
                    String voo_option = item.getString("voo_option");
                    listData.add(new VoteProgressModel(totals, count, voo_option));
                }

                if (adapter == null) {
                    adapter = new VoteProgressAdapter(getContext(), listData);
                    vote_progress_list.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (code.equals("退出投票")) {
            httpTimer.stopTimer();
            this.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        switch (v.getId()) {
            case R.id.quit_dialog:
                dialogShowOrHide(true, "退出投票中...");
                getData(values, "退出投票", Connector.getInstance().endVote, true);
                break;
            case R.id.end_vote://结束投票
                dialogShowOrHide(true, "结束投票中...");
                getData(values, "结束投票", Connector.getInstance().endVote, true);
                break;
            default:
                break;
        }
    }

    public static void getdialog(Context context) {
        VoteProgressDialog dialog = new VoteProgressDialog(context, R.style.AlertDialogStyle);
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
    protected void onStop() {
        super.onStop();
        httpTimer.stopTimer();
    }
}
