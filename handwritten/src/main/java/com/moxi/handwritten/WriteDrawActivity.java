package com.moxi.handwritten;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moxi.handwritten.dialog.SaveDrawDialog;
import com.moxi.handwritten.fragment.DrawFragment;
import com.moxi.handwritten.utils.WriteDataUtils;
import com.moxi.handwritten.view.PaintBackView;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.constant.APPLog;
import com.moxi.handwritten.model.WriteFileBeen;
import com.mx.mxbase.view.WriteDrawLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WriteDrawActivity extends BaseActivity implements View.OnClickListener {
    public static final String FileLast="draw&";
    public static void startWriteDraw(Activity activity, String tabelName, WriteFileBeen been) {
        Intent intent = new Intent(activity, WriteDrawActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tabelName", tabelName);
        bundle.putSerializable("been", been);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 10);
    }

    private String tabelName;
    private WriteFileBeen been;
    /**
     * 标题栏
     */
    @Bind(R.id.complete)
    TextView complete;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add_page)
    ImageButton add_page;
    @Bind(R.id.skin)
    ImageButton skin;

    //铅笔
    @Bind(R.id.pencil)
    WriteDrawLayout pencil;
    //圆珠笔
    @Bind(R.id.ball_pen)
    WriteDrawLayout ball_pen;
    //钢笔
    @Bind(R.id.pen)
    WriteDrawLayout pen;
    //毛笔
    @Bind(R.id.brush)
    WriteDrawLayout brush;
    //橡皮擦
    @Bind(R.id.rubber)
    WriteDrawLayout rubber;

    @Bind(R.id.sweep)
    TextView sweep;

    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;
    //    @Bind(R.id.write_draw_paint)
//    PaintInvalidateRectView write_draw_paint;
    @Bind(R.id.write_back)
    PaintBackView write_back;

    private List<DrawFragment> drawFragments = new ArrayList<>();

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_write_draw;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            initData(savedInstanceState);
        } else {
            initData(getIntent().getExtras());
        }

        setPencil();
        complete.setOnClickListener(this);
        skin.setOnClickListener(this);
        add_page.setOnClickListener(this);
        title.setText(been != null ? been.fileName.replace(FileLast,"") : "新文档");

        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        sweep.setOnClickListener(this);

        sweep.setVisibility(View.INVISIBLE);
    }

    private void setPencil() {
        pencil.setallValue(R.mipmap.pencil, true);
        ball_pen.setallValue(R.mipmap.ball_pen, false);
        pen.setallValue(R.mipmap.pen, false);
        brush.setallValue(R.mipmap.brush, false);
        rubber.setallValue(R.mipmap.rubber, false);

        pencil.setOnClickListener(penClick);
        ball_pen.setOnClickListener(penClick);
        pen.setOnClickListener(penClick);
        brush.setOnClickListener(penClick);
        rubber.setOnClickListener(penClick);
    }

    private void initData(Bundle bundle) {
        tabelName = bundle.getString("tabelName");
        been = (WriteFileBeen) bundle.getSerializable("been");
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            isFirst = false;
            if (been != null) {
                String[] indexs = been.PathsOrName.split("&");
                int index = 0;
                if (indexs.length == 2) {
                    index = 1;
                    //更新背景
                    click_item = Integer.parseInt(indexs[0]);
                    write_back.setDrawStyle(click_item);
                }
                String[] images = indexs[index].split(",");
                for (int i = 0; i < images.length; i++) {
                    addFragment(images[i]);
                }

                currentPage = 0;
            } else {
                addFragment("");
            }
            showFragment();
        }
    }

    private int indexPen = 0;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (been == null) {
            SaveDrawDialog.getdialog(this, "", saveDrawListener);
        } else {
//            try {
//                WriteDataUtils.getInstance().updateImageDowlaod(tabelName,getImageSpil(),been.PathsOrName);
//                String Imagepath =drawFragments.get(currentPage).getImage();
//                WriteDataUtils.getInstance().updateImagePathDowlaod(tabelName, Imagepath, been.image);
//                setResult(RESULT_OK);
            WriteDrawActivity.this.finish();
//            } catch (IOException e) {
//                APPLog.e("保存修改", e.getMessage());
//                BaseApplication.Toast("保存出错");
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                if (been == null) {
                    SaveDrawDialog.getdialog(this, "", saveDrawListener);
                } else {
                    try {
                        WriteDataUtils.getInstance().updateImageDowlaod(tabelName, getImageSpil(), been.PathsOrName);
                        String Imagepath = drawFragments.get(currentPage).getImage();
                        WriteDataUtils.getInstance().updateImagePathDowlaod(tabelName, Imagepath, been.image);
                        setResult(RESULT_OK);
                        WriteDrawActivity.this.finish();
                    } catch (IOException e) {
                        APPLog.e("保存修改", e.getMessage());
                        BaseApplication.Toast("保存出错");
                    }
                }
                break;
            case R.id.skin:
                showPopupWindow(v);
                break;
            case R.id.sweep://清除灰屑
                drawFragments.get(drawFragments.size() - 1).ClearSweep();
                break;
            case R.id.add_page:
                addFragment("");
                showFragment();
                break;
            case R.id.last_page:
                lastPage();
                break;
            case R.id.next_page:
                nextPage();
                break;
            default:
                break;
        }
    }

    private int currentPage = 0;

    private void lastPage() {
        if (currentPage > 0) {
            currentPage--;
            showFragment();
        }
    }

    private void nextPage() {
        if ((drawFragments.size() - 1) > currentPage) {
            currentPage++;
            showFragment();
        }
    }

    /**
     * 初始化碎片
     */
    private void addFragment(String image) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        DrawFragment writeFragment = DrawFragment.newInstance(image, indexPen);
        mFragmentTransaction.add(R.id.drawpath_layout_fragment, writeFragment);
        hideFragment(mFragmentTransaction);
        drawFragments.add(writeFragment);
        currentPage = drawFragments.size() - 1;
        mFragmentTransaction.commit();
    }

    private void showFragment() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(mFragmentTransaction);
        mFragmentTransaction.show(drawFragments.get(currentPage));
        mFragmentTransaction.commit();

        show_index.setText((currentPage + 1) + "/" + drawFragments.size());
    }


    /**
     * 隐藏fragment
     *
     * @param mFragmentTransaction
     */
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        for (int i = 0; i < drawFragments.size(); i++) {
            if (!drawFragments.get(i).isHidden()) {
                mFragmentTransaction.hide(drawFragments.get(i));
            }
        }
    }

    private String getImageSpil() throws IOException {
        StringBuilder path = new StringBuilder();
        int i = 0;
        path.append(click_item + "&");
        for (DrawFragment fragment : drawFragments) {
            path.append(fragment.saveImagepath());
            if (drawFragments.size() - 1 > i) {
                path.append(",");
            }
            i++;
        }
        return path.toString();
    }


    /**
     * 保存绘制提示框
     */
    View.OnClickListener saveDrawListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SaveDrawDialog dialog = (SaveDrawDialog) v.getTag();
            dialog.dismiss();

            switch (v.getId()) {
                case R.id.qiut:
                    break;
                case R.id.insure:
                    try {
                        String input = dialog.getTxt();
                        if (input.equals("")) {
                            BaseApplication.Toast("输入不能为空");
                            return;
                        }
                        input=FileLast+input;
                        if (WriteDataUtils.getInstance().judgeExist(tabelName, input)) {
                            BaseApplication.Toast("文件中已存在该文件名");
                            return;
                        }
                        String saveDataPath = getImageSpil();
                        String Imagepath = drawFragments.get(currentPage).getImage();

                        been = new WriteFileBeen("1", Imagepath, input, String.valueOf(System.currentTimeMillis()), saveDataPath);

                        WriteDataUtils.getInstance().insertData(tabelName, been);
                        dialog.dismiss();

                        setResult(RESULT_OK);
                        getHandler().sendEmptyMessageDelayed(0, 200);
                    } catch (IOException e) {
                        e.printStackTrace();
                        BaseApplication.Toast("保存出错");
                    }
                    break;
                case R.id.discard:
                    getHandler().sendEmptyMessageDelayed(0, 200);

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {//避免返回时出现白色阴影
            WriteDrawActivity.this.finish();
        }
    }

    View.OnClickListener penClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentIndex = 0;
            switch (v.getId()) {
                case R.id.pencil://铅笔
                    currentIndex = 0;
                    break;
                case R.id.ball_pen://圆珠笔
                    currentIndex = 1;
                    break;
                case R.id.pen://钢笔
                    currentIndex = 2;
                    break;
                case R.id.brush://毛笔
                    currentIndex = 3;
                    break;
                case R.id.rubber://橡皮擦
                    currentIndex = 4;
                    break;
                default:
                    break;
            }
            if (indexPen != currentIndex) {
                setpen();
                indexPen = currentIndex;
                setpen();
                setPaint();
            }

        }
    };

    private void setPaint() {
        for (DrawFragment fragment : drawFragments) {
            fragment.setpaint(indexPen);
        }
    }

    public void setpen() {
        switch (indexPen) {
            case 0://铅笔
                pencil.setChange();
                break;
            case 1://圆珠笔
                ball_pen.setChange();
                break;
            case 2://钢笔
                pen.setChange();
                break;
            case 3://毛笔
                brush.setChange();
                break;
            case 4://橡皮擦
                rubber.setChange();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private int click_item = 0;
    PopupWindow popupWindow;

    private void showPopupWindow(View view) {
//        MotionEvent.TOOL_TYPE_ERASER
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_skin_list, null);
        // 设置按钮的点击事件
        LinearLayout back_white = (LinearLayout) contentView.findViewById(R.id.back_white);
        LinearLayout back_corss = (LinearLayout) contentView.findViewById(R.id.back_corss);
        LinearLayout back_line = (LinearLayout) contentView.findViewById(R.id.back_line);

        ImageView show_index_three = (ImageView) contentView.findViewById(R.id.show_index_three);
        ImageView show_index_one = (ImageView) contentView.findViewById(R.id.show_index_one);
        ImageView show_index_two = (ImageView) contentView.findViewById(R.id.show_index_two);
        if (click_item == 2) {
            show_index_three.setVisibility(View.VISIBLE);
        } else if (click_item == 1) {
            show_index_two.setVisibility(View.VISIBLE);
        } else {
            show_index_one.setVisibility(View.VISIBLE);
        }

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        back_white.setOnClickListener(clickListener);
        back_corss.setOnClickListener(clickListener);
        back_line.setOnClickListener(clickListener);
        // 设置好参数之后再show
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_white://白色底
                    click_item = 0;
                    break;
                case R.id.back_corss://田字格
                    click_item = 1;
                    break;
                case R.id.back_line://线条
                    click_item = 2;
                    break;
                default:
                    break;
            }
            updataBack();
        }
    };

    /**
     * 更新底层画布
     */
    public void updataBack() {
        popupWindow.dismiss();
//        write_draw_paint.setBitmap();
        write_back.setDrawStyle(click_item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP||keyCode==KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            lastPage();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            nextPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
