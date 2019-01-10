package com.moxi.studentclient.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moxi.classRoom.utils.ToastUtils;
import com.moxi.studentclient.CommandService;
import com.moxi.studentclient.R;
import com.moxi.studentclient.activity.SubjectNowTestActivity;
import com.moxi.studentclient.activity.SubjectTestActivity;
import com.moxi.studentclient.activity.WeekOutSubjectActivity;
import com.moxi.studentclient.dialog.ResponderActivity;
import com.moxi.studentclient.dialog.VoteActivity;
import com.moxi.studentclient.interfacess.DrawToolListener;

/**
 * Created by Administrator on 2016/11/2.
 */

public class DrawToolView extends LinearLayout implements View.OnClickListener {
    Context context;
    ImageView pen, rubber, screenshout, cleaner, responder, vote;
    DrawToolListener listener;

    boolean penflag = false, rubberflag = false, shoutflag = false, cleanerflag = false, respflag = false, voteflag = false;

    public DrawToolView(Context context) {
        super(context);
    }

    public DrawToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public DrawToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnDrawToolListener(DrawToolListener listener) {
        this.listener = listener;
    }

    private void initView() {
        View v = View.inflate(context, R.layout.drawtool_layout, this);
        pen = (ImageView) v.findViewById(R.id.pen_iv);
        rubber = (ImageView) v.findViewById(R.id.rubber_iv);
        screenshout = (ImageView) v.findViewById(R.id.screenshout_iv);
        cleaner = (ImageView) v.findViewById(R.id.screenclean_iv);
        responder = (ImageView) v.findViewById(R.id.responder_iv);
        ((ImageView) v.findViewById(R.id.newTest_iv)).setOnClickListener(this);
        vote = (ImageView) v.findViewById(R.id.vote_iv);
        pen.setOnClickListener(this);
        rubber.setOnClickListener(this);
        screenshout.setOnClickListener(this);
        cleaner.setOnClickListener(this);
        responder.setOnClickListener(this);
        vote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pen_iv:
                rubberflag = false;
                shoutflag = false;
                cleanerflag = false;
                penflag = !penflag;
                respflag = false;
                voteflag = false;
                break;
            case R.id.rubber_iv:
                rubberflag = !rubberflag;
                shoutflag = false;
                cleanerflag = false;
                penflag = false;
                respflag = false;
                voteflag = false;
                break;
            case R.id.screenshout_iv:
                rubberflag = false;
                shoutflag = !shoutflag;
                cleanerflag = false;
                penflag = false;
                respflag = false;
                voteflag = false;
                break;
            case R.id.screenclean_iv:
                rubberflag = false;
                shoutflag = false;
                cleanerflag = !cleanerflag;
                penflag = false;
                respflag = false;
                voteflag = false;
                break;
            case R.id.responder_iv:
                rubberflag = false;
                shoutflag = false;
                cleanerflag = false;
                penflag = false;
                respflag = !respflag;
                voteflag = false;
                break;
            case R.id.vote_iv:
                rubberflag = false;
                shoutflag = false;
                cleanerflag = false;
                penflag = false;
                respflag = false;
                voteflag = !voteflag;
                break;
            case R.id.newTest_iv:
                //现场出题
                Intent it = new Intent();//现场出题
                it.setClass(context, SubjectNowTestActivity.class);
                ((Activity) context).startActivityForResult(it, NowTest);
                break;
        }
        updataButten();
    }

    private void updataButten() {
        if (CommandService.getLinkState() == -1) {
            ToastUtils.getInstance().showToastShort("未连接课堂");
            return;
        }
        if (penflag) {
            pen.setSelected(true);
            listener.withPen();
        } else {
            pen.setSelected(false);
        }

        if (rubberflag) {
            rubber.setSelected(true);
            listener.withRubber();
        } else {
            rubber.setSelected(false);
        }

        if (shoutflag) {
            screenshout.setSelected(true);
            listener.doClassExam();
            Intent it = new Intent();//课堂考试
            it.setClass(context, SubjectTestActivity.class);
            ((Activity) context).startActivityForResult(it, Test);
        } else {
            screenshout.setSelected(false);
        }

        if (cleanerflag) {
            cleaner.setSelected(true);
//            cleaner.setBackgroundResource(R.mipmap.user_login_ico);
//            cleaner.setImageResource(R.mipmap.cleanner_select_ico);
            // TODO: 2016/11/2  start cleaner
            listener.doelimination();//淘汰答题
            Intent it = new Intent();
            it.setClass(context, WeekOutSubjectActivity.class);
            ((Activity) context).startActivityForResult(it, WEEKOUT);
        } else {
            cleaner.setSelected(false);
//            cleaner.setBackgroundResource(R.mipmap.user_unlogin_ico);
//            cleaner.setImageResource(R.mipmap.cleanner_normal_ico);
        }


        if (respflag) {
            responder.setSelected(true);
//            responder.setBackgroundResource(R.mipmap.user_login_ico);
//            responder.setImageResource(R.mipmap.responder_select);
            // TODO: 2016/11/2  start cleaner
            listener.doResponder();
            Intent responderIt = new Intent();
            responderIt.setClass(context, ResponderActivity.class);
            ((Activity) context).startActivityForResult(responderIt, RESPON);
        } else {
            responder.setSelected(false);
//            responder.setBackgroundResource(R.mipmap.user_unlogin_ico);
//            responder.setImageResource(R.mipmap.responder_normal);
        }

        if (voteflag) {
            vote.setSelected(true);
//            vote.setBackgroundResource(R.mipmap.user_login_ico);
//            vote.setImageResource(R.mipmap.vote_select_ico);
            // TODO: 2016/11/2  start cleaner
            listener.doVote();

            Intent voteIt = new Intent();
            voteIt.setClass(context, VoteActivity.class);
            ((Activity) context).startActivityForResult(voteIt, VOTE);

        } else {
            vote.setSelected(false);
//            vote.setBackgroundResource(R.mipmap.user_unlogin_ico);
//            vote.setImageResource(R.mipmap.vote_normal_ico);
        }


    }

    public static final int VOTE = 0, RESPON = 1, Test = 2, WEEKOUT = 3, NowTest = 4;

    /**
     * 操作完成按钮状态重置
     */
    public void finishDeal() {
        rubberflag = false;
        shoutflag = false;
        cleanerflag = false;
        penflag = false;
        respflag = false;
        voteflag = false;
        updataButten();
    }


}
