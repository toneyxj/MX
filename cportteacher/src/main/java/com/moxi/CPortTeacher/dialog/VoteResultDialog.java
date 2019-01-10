package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.VoteResultAdapter;
import com.moxi.CPortTeacher.model.VoteProgressModel;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.view.NoGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

/**
 * 投票结果
 * Created by Administrator on 2016/11/4.
 */
public class VoteResultDialog extends CPortBaseDialog {
    public VoteResultDialog(Context context, int themeResId, List<VoteProgressModel> list,String hitn) {
        super(context, themeResId);
        this.listData = list;
        Collections.sort(listData, new BosComparator());
        this.hitn=hitn;
    }
    private String hitn;
    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.vote_hitn)
    TextView vote_hitn;
    @Bind(R.id.max_vote_student)
    TextView max_vote_student;
    @Bind(R.id.vote_result_gridview)
    NoGridView vote_result_gridview;

    private List<VoteProgressModel> listData = new ArrayList<>();
    private VoteResultAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_vote_result;
    }

    @Override
    public void initDialog() {
        title_dialog.setText("投票进度");
        quit_dialog.setOnClickListener(this);
        vote_hitn.setText(hitn);
        max_vote_student.setText(listData.get(0).name+"\t"+listData.get(0).curNumber);

        if (adapter == null) {
            adapter = new VoteResultAdapter(getContext(), listData);
            vote_result_gridview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_dialog:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public static void getdialog(Context context, List<VoteProgressModel> list,String hitn) {
        VoteResultDialog dialog = new VoteResultDialog(context, R.style.AlertDialogStyle, list,hitn);
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
    /**
     * 投票人数排序
     */
    private static class BosComparator implements Comparator<VoteProgressModel> {
        @Override
        public int compare(VoteProgressModel lhs, VoteProgressModel rhs) {
            if (lhs.curNumber < rhs.curNumber) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
