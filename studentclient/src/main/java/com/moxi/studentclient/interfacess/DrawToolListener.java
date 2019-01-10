package com.moxi.studentclient.interfacess;

/**
 * Created by Administrator on 2016/11/2.
 */

public interface DrawToolListener {
    public void withPen();
    public void withRubber();
    public void doClassExam();//课堂练习
    public void doelimination();//淘汰答题
    public void doResponder();//抢答
    public void doVote();//投票
}
