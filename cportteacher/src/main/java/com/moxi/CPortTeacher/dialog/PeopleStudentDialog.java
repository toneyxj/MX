package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.StutentAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.model.Student;
import com.moxi.classRoom.dbUtils.CacheDbUtils;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.view.NoGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 展示所有的学生
 * Created by Administrator on 2016/11/3.
 */
public class PeopleStudentDialog extends CPortRequestBaseDialog implements AdapterView.OnItemClickListener {
    public PeopleStudentDialog(Context context, int themeResId, List<Student> students) {
        super(context, themeResId);
        this.students = students;
        totalSize = this.students.size();
    }

    private List<Student> students;

    @Bind(R.id.title_dialog)
    TextView title_dialog;
    @Bind(R.id.quit_dialog)
    ImageButton quit_dialog;

    @Bind(R.id.hint_information)
    TextView hint_information;

    @Bind(R.id.people_student)
    NoGridView people_student;
    @Bind(R.id.insure)
    Button insure;
    private List<Student> listData = new ArrayList<>();
    private StutentAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.dialog_people_student;
    }

    @Override
    public void initDialog() {
        title_dialog.setText("学生列表");
        quit_dialog.setOnClickListener(this);
        insure.setOnClickListener(this);
        people_student.setOnItemClickListener(this);
        hint_information.setOnClickListener(this);
        requestData();
    }

    private void requestData() {
        hint_information.setVisibility(View.GONE);
        dialogShowOrHide(true, "学生列表加载中...");
        getData(null, "学生列表", Connector.getInstance().getStudenturl, true);
    }

    public static void getdialog(Context context, List<Student> students) {
        PeopleStudentDialog dialog = new PeopleStudentDialog(context, R.style.AlertDialogStyle, students);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 9, 0, BaseApplication.ScreenWidth / 9, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == insure) {
            getSelectStudent();
        } else if (v == hint_information) {
            requestData();
        } else if (v == quit_dialog) {
            VoteDialog.getdialog(getContext(), students);
            PeopleStudentDialog.this.dismiss();
        }
    }

    private void getSelectStudent() {
        List<Student> studentss = new ArrayList<>();
        for (Student student : listData) {
            if (student.isSelect) {
                studentss.add(student);
            }
        }
        VoteDialog.getdialog(getContext(), studentss);
        PeopleStudentDialog.this.dismiss();
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        hint_information.setVisibility(View.GONE);
        CacheDbUtils.getInstance().saveFinshClassData(code, result);
        listData.clear();
        listData.addAll(JsonAnalysis.getInstance().getStudents(result));
        spliStudent();
        if (adapter == null) {
            adapter = new StutentAdapter(getContext(), listData);
            people_student.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void spliStudent() {
        List<Long> ids = new ArrayList<>();
        for (Student student : students) {
            ids.add(student.id);
        }
        for (int i = 0; i < listData.size(); i++) {
            if (ids.contains(listData.get(i).id)) {
                listData.get(i).isChangeSelect();
            }
        }
    }


    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        hint_information.setVisibility(View.VISIBLE);
    }

    private int totalSize = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listData.get(position).isSelect) {
            totalSize--;
        } else {
            if (totalSize >= 6) {
                ToastUtils.getInstance().showToastShort("最多选择6个进行投票哟！！");
                return;
            }
            totalSize++;
        }
        listData.get(position).isChangeSelect();
        adapter.updateSelect(position, people_student);
    }
}
