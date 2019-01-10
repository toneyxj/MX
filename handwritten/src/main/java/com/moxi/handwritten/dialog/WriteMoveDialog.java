package com.moxi.handwritten.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.moxi.handwritten.R;
import com.moxi.handwritten.model.FloderBeen;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.view.LinerlayoutInter;

import java.util.List;


/**
 * Created by Administrator on 2016/8/4.
 */
public class WriteMoveDialog extends Dialog implements LinerlayoutInter.LinerLayoutInter{
    private LinerlayoutInter add_moves;// dialog显示文字控件
    private List<FloderBeen> contents;// 提示内容
    private selectDialogListenr listenr;

    public WriteMoveDialog(Context context, int theme, List<FloderBeen> contents,
                           selectDialogListenr listenr) {
        super(context, theme);
        this.contents = contents;
        this.listenr = listenr;
    }

    private int indexPage = 0;
    private int totalIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_write_move);
        add_moves = (LinerlayoutInter) findViewById(R.id.add_moves);
        add_moves.setLayoutInter(this);

        int size = contents.size() / 5;
        size += contents.size() % 5 == 0 ? 0 : 1;
        totalIndex = size;

        reInit();
    }

    private void reInit() {
        if (totalIndex <= indexPage) {
            indexPage = totalIndex - 1;
        }
        if (indexPage == totalIndex - 1) {
            addview(contents.subList(indexPage * 5, contents.size()));
        } else {
            addview(contents.subList(indexPage * 5, (indexPage + 1) * 5));
        }
    }


    public void moveRight() {
        if (indexPage<totalIndex-1){
            indexPage++;
            reInit();
        }
    }

    public void moveLeft() {
        if (indexPage>0){
            indexPage--;
            reInit();
        }
    }

    private void addview(List<FloderBeen> list) {
        add_moves.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (FloderBeen been : list) {
            View view = inflater.inflate(R.layout.addview_move_write, null);
            TextView fodler_path = (TextView) view.findViewById(R.id.fodler_path);
            fodler_path.setText(been.fileName);

            fodler_path.setTag(been);
            fodler_path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenr.selectItem((FloderBeen) v.getTag());
                    WriteMoveDialog.this.dismiss();
                }
            });

            add_moves.addView(view);
        }

    }


    /**
     * 显示dialog
     *
     * @param context  上下文
     * @param contents 提示内容
     * @param listenr  如果点击确认返回控件
     */
    public static void getdialog(Context context, List<FloderBeen> contents,
                                 selectDialogListenr listenr) {
        WriteMoveDialog dialog = new WriteMoveDialog(context, R.style.dialog,
                contents, listenr);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 6, 0, BaseApplication.ScreenWidth / 6, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }

    public interface selectDialogListenr {
        public void selectItem(FloderBeen floder);
    }

}
