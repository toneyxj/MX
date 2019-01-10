package com.moxi.handwritten;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.handwritten.config.WriteConfig;
import com.moxi.handwritten.dialog.SelectDrawStyle;
import com.moxi.handwritten.fragment.WriteFragment;
import com.moxi.handwritten.model.WriteFileBeen;
import com.moxi.handwritten.utils.WriteDataUtils;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.dialog.InputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class WritPadActivity extends BaseActivity implements View.OnClickListener, WriteMainInterface {
    public static float width;
    public static float high;
    @Bind(R.id.show_title)
    TextView show_title;
    @Bind(R.id.new_marks)
    TextView new_marks;
    @Bind(R.id.select)
    TextView select;
    @Bind(R.id.write_fragment)
    FrameLayout write_fragment;
    @Bind(R.id.selec_control)
    LinearLayout selec_control;
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.move)
    TextView move;
    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;
    List<WriteFragment> fragments = new ArrayList<>();

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_writ_pad;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        show_title.setOnClickListener(this);
        new_marks.setOnClickListener(this);
        select.setOnClickListener(this);
        delete.setOnClickListener(this);
        move.setOnClickListener(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (selec_control.getVisibility() == View.VISIBLE) {
            List<WriteFileBeen> checkData = new ArrayList<>();
            if (v == move) {//移动
                fragments.get(fragments.size() - 1).MoveClick();
            } else if (v == delete) {//删除
                fragments.get(fragments.size() - 1).deleteClick();
            } else if (v == last_page) {//上一页
                fragments.get(fragments.size() - 1).moveLeft();
            } else if (v == next_page) {//下一页
                fragments.get(fragments.size() - 1).moveRight();
            } else {
                changeSelect();
            }
            return;
        }
        switch (v.getId()) {
            case R.id.show_title:
                toBack();
                break;
            case R.id.new_marks:
                inputTitleDialog();
                break;
            case R.id.select:
                changeSelect();
                break;
            case R.id.delete:
                break;
            case R.id.move:
                break;
            case R.id.last_page:
                fragments.get(fragments.size() - 1).moveLeft();
                break;
            case R.id.next_page:
                fragments.get(fragments.size() - 1).moveRight();
                break;
            default:
                break;
        }
    }

    public String getTableName() {
        return fragments.get(fragments.size() - 1).getDataName();
    }

    public void refureshTable() {
        fragments.get(fragments.size() - 1).reInit();
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            isFirst = false;
            width = write_fragment.getWidth() / 4f;
            high = write_fragment.getHeight() / 3f;
            addFragment(getResources().getString(R.string.main_name), "writemain", "");
        }
    }

    private void changeSelect() {
        if (selec_control.getVisibility() == View.VISIBLE) {
            selec_control.setVisibility(View.INVISIBLE);
            select.setTextColor(getResources().getColor(R.color.colorBlack));
            select.setBackgroundColor(getResources().getColor(R.color.colorWihte));
            select.setText(getResources().getString(R.string.select));

        } else {
            selec_control.setVisibility(View.VISIBLE);
            select.setTextColor(getResources().getColor(R.color.colorWihte));
            select.setBackgroundResource(R.drawable.arc_di_black);
            select.setText(getResources().getString(R.string.quit));
        }
        fragments.get(fragments.size() - 1).setIsSelect(selec_control.getVisibility() == View.VISIBLE);
    }

    private void toBack() {
        if (selec_control.getVisibility() == View.VISIBLE) {
            changeSelect();
            return;
        }
        back();
    }

    private void back() {
        if (fragments.size() > 1) {
            dbackFragment();
        } else if (fragments.size() <= 1) {
            WritPadActivity.this.finish();
        }
    }

    /**
     * 初始化碎片
     *
     * @param titleName 标题名
     * @param dataName  数据库表名
     */
    private void addFragment(String titleName, String dataName, String floderPath) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();


        WriteFragment writeFragment = WriteFragment.newInstance(titleName, dataName, floderPath);
        mFragmentTransaction.add(R.id.write_fragment, writeFragment);
        fragments.add(writeFragment);
        hideFragment(mFragmentTransaction);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        toBack();
    }

    private void dbackFragment() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.remove(fragments.get(fragments.size() - 1));
        fragments.remove(fragments.size() - 1);
        mFragmentTransaction.show(fragments.get(fragments.size() - 1));
        mFragmentTransaction.commitAllowingStateLoss();
        fragments.get(fragments.size() - 1).initTitle();
    }

    private void hideShowFragmet() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.show(fragments.get(fragments.size() - 1));
        mFragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 隐藏fragment
     *
     * @param mFragmentTransaction
     */
    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        for (int i = 0; i < fragments.size() - 1; i++) {
            if (!fragments.get(i).isHidden()) {
                mFragmentTransaction.hide(fragments.get(i));
            }
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
        WriteDataUtils.getInstance().closeDb();
    }

    /**
     * 设置标题和显示文字
     *
     * @param titleName
     * @param dataName
     */
    @Override
    public void selectItem(String titleName, String dataName) {
        show_title.setText(titleName);
        boolean is = false;
        if (fragments.size() == 1) {
            is = true;
        }
//        setTextLeftImage(show_title, is, R.mipmap.back, R.mipmap.back);
    }

    @Override
    public void ShowIndex(String index) {
        show_index.setText(index);
    }

    @Override
    public void commingFloder(WriteFileBeen data) {
        //文件夹进入
        addFragment(data.fileName, data.PathsOrName, fragments.get(fragments.size() - 1).getFloderPath() + "/" + data.fileName);
    }

    @Override
    public void checkDetailFile(String tableName, WriteFileBeen data) {
        //文件进入
        WriteDrawActivity.startWriteDraw(WritPadActivity.this, tableName, data);
    }

    @Override
    public void createNewFile(final String tableName) {
        List<String> contents = new ArrayList<>();
        contents.add("一般手写");
        contents.add("矢量手写");
        SelectDrawStyle.getdialog(this, "排序", contents, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    WriteDrawActivity.startWriteDraw(WritPadActivity.this, tableName, null);
                } else {
                    startActivity(new Intent(WritPadActivity.this, TextZoomActivity.class));
                }

            }
        });

    }

    @Override
    public void alterFinish() {
        changeSelect();
    }

    @Override
    public void HideShow() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(mFragmentTransaction);
        mFragmentTransaction.commitAllowingStateLoss();
        hideShowFragmet();
    }


    /**
     * 左边设置图片
     *
     * @param view       设置的文本
     * @param is         显示样式
     * @param trueImage  显示true的image
     * @param falseImage 显示为false的image
     */
    private void setTextLeftImage(TextView view, boolean is, int trueImage, int falseImage) {
        Drawable drawable;
        if (is) {// 是否显示列表
            drawable = getResources().getDrawable(trueImage);
        } else {
            drawable = getResources().getDrawable(falseImage);
        }
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    private void inputTitleDialog() {

        InputDialog.getdialog(this, getString(R.string.new_floder), "请输入文件名", new InputDialog.InputListener() {
            @Override
            public void quit() {
                fragments.get(fragments.size()-1).GCFragment();
            }

            @Override
            public void insure(String name) {
                if (name.equals("")) {
                    return;
                }
                if (WriteDataUtils.getInstance().judgeExist(getTableName(), name)) {
                    BaseApplication.Toast("文件名已存在");
                }

                String tableName = WriteConfig.getInstance().getTableName();
                String type = "0";//文件类型0代表文件夹，1代表文件,-1代表新增界面
                String image = "";//type=1文件的保存图片本地地址
                String fileName = name;//文件名
                String createDate = String.valueOf(System.currentTimeMillis());//文件创建日期
                String PathsOrName = tableName;//1文件的路径文件保存地址,0新建表名
                WriteDataUtils.getInstance().createTable(tableName);
                WriteDataUtils.getInstance().insertData(getTableName(), type, image, fileName, createDate, PathsOrName);
                refureshTable();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 10) {
            //请求返回
            fragments.get(fragments.size() - 1).reInit();
        }
    }
}
