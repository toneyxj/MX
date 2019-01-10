package com.moxi.studentclient.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.studentclient.R;
import com.moxi.studentclient.adapter.VoteAdatpter;
import com.moxi.studentclient.model.VoteBean;
import com.mx.mxbase.utils.Toastor;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */

public class VoteDialog extends Dialog implements View.OnClickListener {
    static VoteDialog intence;
    static View v;
    Context ctx;
    TextView title;
    GridView gv;
    TextView submite;
    ImageView closer;
    List<VoteBean> data;
    VoteAdatpter adapter;
    int selectIndex=-1;
    public VoteDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.ctx=context;
    }

    public static VoteDialog create(Context ctx){
        if (null==intence)
            intence=new VoteDialog(ctx, R.style.NoTitleDialog);
        v=View.inflate(ctx,R.layout.dialog_vote_layout,null);
        intence.setContentView(v);
        intence.setCanceledOnTouchOutside(false);
        intence.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = intence.getWindow().getAttributes();
        intence.getWindow().setAttributes(lp);
        return intence;
    }

    public void setData(List<VoteBean> data){
        this.data=data;
//        adapter=new VoteAdatpter(getContext(),data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title=(TextView) v.findViewById(R.id.title_tv);
        gv=(GridView) v.findViewById(R.id.gv);
        submite=(TextView) v.findViewById(R.id.submite_tv);
        closer=(ImageView) v.findViewById(R.id.close_iv);
        title.setText("投票");
        closer.setOnClickListener(this);
        submite.setOnClickListener(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        super.onCreate(savedInstanceState);
    }

    private void selectItem(int index) {

        for (int i=0;i<data.size();i++){
            VoteBean bean=data.get(i);
            if (index==i) {
                bean.checked = !(bean.checked);
                if (bean.checked)
                    selectIndex=index;
                else
                    selectIndex=-1;
            }else
                data.get(i).checked=false;
        }

        adapter.setData(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_iv:
                intence.dismiss();
                break;
            case R.id.submite_tv:
                if (-1==selectIndex) {
                    Toastor.getToast(ctx, "未选择").show();
                    return;
                }else {
                    // TODO: 2016/11/3 submite
                    intence.dismiss();
                }
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        gv.setAdapter(adapter);
    }
}
