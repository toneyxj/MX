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
import com.moxi.CPortTeacher.adapter.VotePeopleAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.model.Student;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.view.NoGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 投票dialog
 * Created by Administrator on 2016/11/3.
 */
public class VoteDialog extends CPortRequestBaseDialog {
    public VoteDialog(Context context, int themeResId, List<Student> students) {
        super(context, themeResId);
        this.students = students;
    }

    @Bind(R.id.main_layout)
    RelativeLayout main_layout;

    @Bind(R.id.people_item)
    NoGridView people_item;
    @Bind(R.id.send_vote)
    Button send_vote;
    @Bind(R.id.add_student)
    Button add_student;
    private VotePeopleAdapter adapter = null;
    private List<Student> students;

    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setRectViewHeight(main_layout.getHeight());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_vote;
    }

    @Override
    public void initDialog() {
        setRect_view(R.id.rect_view);
        title_dialog.setText("自定义投票");
        quit_dialog.setOnClickListener(this);

        send_vote.setOnClickListener(this);
        add_student.setOnClickListener(this);

        if (students == null) {
            students = new ArrayList<>();
        }
        initAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_dialog:
                this.dismiss();
                break;
            case R.id.send_vote:
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for (Student student : students) {
                    builder.append(student.name);
                    if (i != (students.size() - 1)) {
                        builder.append(";");
                    }
                    i++;
                }
                APPLog.e("传输参数=" + builder.toString());
                StartVote(builder.toString());
                break;
            case R.id.add_student:
                PeopleStudentDialog.getdialog(getContext(), students);
                this.dismiss();
                break;
            case R.id.delete_item:
                int index = (int) v.getTag();
                students.remove(index);
                initAdapter();
                break;
            default:
                break;
        }
    }

    private void StartVote(String peoples) {
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("options", peoples));
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true, "发起投票...");
        getData(values, "开始投票", Connector.getInstance().startVote, true);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        VoteProgressDialog.getdialog(getContext());
        this.dismiss();
    }

    /**
     * 获得选择的学生
     *
     * @return
     */
    public List<Student> getStudents() {
        return students;
    }

    private void initAdapter() {
        setRectViewHeight(10);
        if (adapter == null) {
            adapter = new VotePeopleAdapter(getContext(), students);
            adapter.setListener(this);
            people_item.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public static void getdialog(Context context, List<Student> students) {
        VoteDialog dialog = new VoteDialog(context, R.style.AlertDialogStyle, students);
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
    }
}
