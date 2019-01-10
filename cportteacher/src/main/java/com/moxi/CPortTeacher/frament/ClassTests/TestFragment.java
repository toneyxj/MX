package com.moxi.CPortTeacher.frament.ClassTests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.frament.ClassTestFragment;
import com.moxi.classRoom.BaseFragment;
import com.mx.mxbase.utils.Log;

import butterknife.Bind;

/**
 * Created by zhengdelong on 2016/11/2.
 */
@SuppressLint("ValidFragment")
public class TestFragment extends BaseFragment {

    @Bind(R.id.startf)
    Button startf;

    ClassTestFragment.ShowFragmentCallBack showFragmentCallBack;

    public TestFragment(){

    }

    public TestFragment(ClassTestFragment.ShowFragmentCallBack showFragmentCallBack){
        this.showFragmentCallBack = showFragmentCallBack;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("class","onCreateView.....");
//        View view = inflater.inflate(R.layout.fragment_class_seclect,null);
//        Button button = (Button)view.findViewById(R.id.startf);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("class","TestFragment click....");
//                showFragmentCallBack.show(new ClassRoomSeclectFragment(showFragmentCallBack));
//            }
//        });
//        return view;
//    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_class_seclect;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        startf.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("class","onCreate.....");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("class","onViewCreated.....");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startf){
            Log.d("class","TestFragment click....");
            showFragmentCallBack.show(new ClassRoomSeclectFragment(showFragmentCallBack),"asdasd");
        }
    }
}
