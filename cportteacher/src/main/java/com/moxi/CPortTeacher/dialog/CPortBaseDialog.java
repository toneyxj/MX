package com.moxi.CPortTeacher.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.moxi.CPortTeacher.CPortApplication;
import com.moxi.CPortTeacher.Cinterface.DialogDrawInterface;
import com.moxi.CPortTeacher.config.ConfigData;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.view.PaintInvalidateRectView;
import com.mx.mxbase.base.BaseDialog;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public abstract class CPortBaseDialog extends BaseDialog implements DialogDrawInterface, DialogInterface.OnKeyListener  {
    private int mTouchSlop;
    private int kitWidth = ConfigData.getInstance().getKitWidth();
    private int kitHeight = ConfigData.getInstance().getKitHeight();
    private List<Point> points = ConfigData.getInstance().getPoints();

    public CPortBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 绘制笔画
     */
   private PaintInvalidateRectView rect_view;

    /**
     * 设置绘制view的初始化
     * @param resourcce
     */
    public void setRect_view(int resourcce) {
        rect_view = (PaintInvalidateRectView) findViewById(resourcce);
    }
    public void setRectViewHeight(int height){
        ViewGroup.LayoutParams params=rect_view.getLayoutParams();
        params.height=height;
        rect_view.setLayoutParams(params);
    }

    private int downX;
    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int upX = (int) ev.getRawX();
            int upY = (int) ev.getRawY();
            judgeClick(upX, upY);
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) ev.getRawX();
            downY = (int) ev.getRawY();
        }
        return super.onTouchEvent(ev);
    }

    private void judgeClick(int upX, int upY) {
        int rightX = points.get(0).x + kitWidth;
        int letf = points.get(0).x;
        int top = points.get(0).y;
        if (downX < letf || downX > rightX || Math.abs(upX - downX) > mTouchSlop || downY < top || (points.get(points.size() - 1).y + kitHeight) < downY) {
            return;
        }
        if (upX < letf || upX > rightX || Math.abs(upY - downY) > mTouchSlop) {
            return;
        }
        int index = 0;
        for (Point point : points) {
            if (point.y < downY && point.y < upY && (point.y + kitHeight) > downY && (point.y + kitHeight) > upY) {
                CPortApplication.getBus().post(new OttoBeen(index, "MainActivity"));
                if (rect_view!=null) {
                    switch (index) {
                        case 0:
                            setPencil();
                            break;
                        case 1:
                            setRubber();
                            break;
                        case 2:
//                            setScreenshort();
                            setClearScreen();
                            break;
                        case 3:
//                            setClearScreen();
                            break;
                        default:
                            break;
                    }
                }
                break;
            }
            index++;
        }

    }

    @Override
    public void setPencil() {
        rect_view.setPaint(0);
    }

    @Override
    public void setRubber() {
        rect_view.setPaint(1);
    }

    @Override
    public void setScreenshort() {

    }

    @Override
    public void setClearScreen() {
        rect_view.clearScreen();
    }
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onkeyBack();
            return true;
        } else {
            return false; //默认返回 false
        }
    }

    /**
     * 点击返回按钮
     */
    public void onkeyBack(){

    }
}
