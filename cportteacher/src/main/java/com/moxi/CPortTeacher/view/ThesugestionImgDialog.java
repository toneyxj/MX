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
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.model.ClassSelectModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerListModel;
import com.moxi.CPortTeacher.model.ThesugestionAnswerModel;
import com.mx.mxbase.utils.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ThesugestionImgDialog extends Dialog implements View.OnClickListener {

    ImageView closer;
    ImageView subject_answer_img;
    TextView title_tv ;
    Context context;
    String imgUrl;


    public ThesugestionImgDialog(Context context, String imgUrl) {
        super(context);
        this.context=context;
        this.imgUrl = imgUrl;
    }

    private void setData(){
        Log.d("class","imgUrl=======>>>>>>>" + imgUrl);
        if (imgUrl.equals("")){
            return;
        }
        ImageLoader.getInstance().displayImage(Connector.getInstance().questionAnswerImg + imgUrl,subject_answer_img);
    }

    private void init(){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.thesugestion_dialog_img, null);
        title_tv = (TextView)view.findViewById(R.id.title_tv);
        title_tv.setText("答案");
        setContentView(view);

        closer=(ImageView) view.findViewById(R.id.close_iv);
        subject_answer_img=(ImageView) view.findViewById(R.id.answer_img_id);
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
