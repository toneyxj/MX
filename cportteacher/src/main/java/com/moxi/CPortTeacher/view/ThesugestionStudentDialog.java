package com.moxi.CPortTeacher.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomSelectAdapter;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerModel;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ThesugestionStudentDialog extends Dialog implements View.OnClickListener {

    ImageView closer;
    GridView show_student_grid;
    Context context;
    List<ClassSelectModel> classSelectModelList;

    public ThesugestionStudentDialog(Context context,List<ClassSelectModel> classSelectModelList) {
        super(context);
        this.context=context;
        this.classSelectModelList = classSelectModelList;
    }

    private void setData(){
        ClassRoomSelectAdapter classRoomSelectAdapter = new ClassRoomSelectAdapter(getContext(),classSelectModelList);
        show_student_grid.setAdapter(classRoomSelectAdapter);
    }

    private void init(){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.thesugestion_dialog, null);
        setContentView(view);

        closer=(ImageView) view.findViewById(R.id.close_iv);
        show_student_grid=(GridView) view.findViewById(R.id.show_student_grid);
        closer.setOnClickListener(this);

        setData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.close_iv:
                 this.dismiss();
                 break;
         }
    }


}
