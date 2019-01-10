package com.moxi.CPortTeacher.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.ClassRoomSelectAdapter;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerModel;
import com.mx.mxbase.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ThesugestionSubjectiveDialog extends Dialog implements View.OnClickListener {

    ImageView closer;
    GridView show_student_grid;
    TextView title_tv;
    Context context;
    ThesugestionAnswerModel thesugestionAnswerModel;

    List<ClassSelectModel> selectModelList = new ArrayList<>();

    public ThesugestionSubjectiveDialog(Context context, ThesugestionAnswerModel thesugestionAnswerModel) {
        super(context);
        this.context=context;
        this.thesugestionAnswerModel = thesugestionAnswerModel;
    }

    private void setData(){
        ClassRoomSelectAdapter classRoomSelectAdapter = new ClassRoomSelectAdapter(getContext(),selectModelList);
        show_student_grid.setAdapter(classRoomSelectAdapter);
    }

    private void init(){

        if (selectModelList.size()>0){
            selectModelList.clear();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.thesugestion_dialog, null);
        setContentView(view);

        closer=(ImageView) view.findViewById(R.id.close_iv);
        show_student_grid=(GridView) view.findViewById(R.id.show_student_grid);
        title_tv = (TextView)view.findViewById(R.id.title_tv);
        title_tv.setText("学生列表");
        closer.setOnClickListener(this);

        List<ThesugestionAnswerListModel> ansList = thesugestionAnswerModel.getAnswerList();
        ClassSelectModel classSelectModel;
        for (int i = 0;i<ansList.size();i++){
            classSelectModel = new ClassSelectModel();
            classSelectModel.setHeadimg(ansList.get(i).getHeadimg());
            classSelectModel.setState(0);
            classSelectModel.setName(ansList.get(i).getStudentName());
            classSelectModel.setTitle(ansList.get(i).getAnswer());
            selectModelList.add(classSelectModel);
        }

        setData();

        show_student_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgUrl = selectModelList.get(position).getTitle();
                Log.d("class","imgUrl===>" + imgUrl);
                ThesugestionImgDialog thesugestionImgDialog = new ThesugestionImgDialog(getContext(),imgUrl);
                thesugestionImgDialog.show();
            }
        });

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
