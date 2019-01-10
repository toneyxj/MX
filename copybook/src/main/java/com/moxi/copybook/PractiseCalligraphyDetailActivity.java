package com.moxi.copybook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moxi.copybook.adapter.PrictiseTextAdapter;
import com.moxi.copybook.dialog.SettingDialog;
import com.moxi.copybook.dialog.SettingInterface;
import com.moxi.copybook.view.PrictiseDrawTextCanvar;
import com.moxi.copybook.view.PrictiseWriteTextWiew;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.model.PrictiseTextBeen;
import com.mx.mxbase.utils.PinyinUtils;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.view.NoGridView;
import com.mx.mxbase.view.WriteDrawLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 练字详情页
 */
public class PractiseCalligraphyDetailActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    //字体文件
    private String sdFilePath = StringUtils.getFilePath("font/");
    /**
     * 当前显示文本
     */
    private List<List<PrictiseTextBeen>> listdata = new ArrayList<>();
    /**
     * 数据原
     */
    private List<String> resuorces = new ArrayList<>();
    /**
     * gridview显示宽度
     */
    private int gridViewWidth = BaseApplication.ScreenWidth - MyApplication.dip2px(60);
    /**
     * 当前显示练习主目录坐标
     */
    private int currentIndex = 0;
    /**
     * 界面显示文字索引
     */
    private int textTndex = 0;

    @Bind(R.id.show_title)
    TextView show_title;
    @Bind(R.id.title)
    TextView thistitle;
    @Bind(R.id.setting)
    TextView setting;
    @Bind(R.id.no_gridview_one)
    NoGridView no_gridview_one;

    @Bind(R.id.no_gridview_two)
    NoGridView no_gridview_two;

    @Bind(R.id.last_group)
    TextView last_group;
    @Bind(R.id.next_group)
    TextView next_group;
    @Bind(R.id.show_pingyin)
    TextView show_pingyin;
    //绘制底图
    @Bind(R.id.prictise_write_text)
    PrictiseWriteTextWiew prictise_write_text;
    //书写文字
    @Bind(R.id.draw_text_canver)
    PrictiseDrawTextCanvar draw_text_canver;

    @Bind(R.id.selet_paint)
    RadioGroup selet_paint;
    @Bind(R.id.pencil)
    WriteDrawLayout pencil;
    @Bind(R.id.rubber)
    WriteDrawLayout rubber;
    @Bind(R.id.last_one)
    TextView last_one;
    @Bind(R.id.next_one)
    TextView next_one;
    /**
     * 保存手写文字的路径
     */
    private Map<String, List<Path>> paths = new HashMap<>();

    /**
     * 开启练字模式
     *
     * @param context 当前上下文
     * @param Style   0为小学生，1为中学生
     */
    public static void Start(Context context, int Style, String title, String lastPage) {
        Intent intent = new Intent(context, PractiseCalligraphyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("style", Style);
        bundle.putString("title", title);
        bundle.putString("lastPage", lastPage);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private int style;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_practise_calligraphy_detail;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            initData(savedInstanceState);
        } else {
            initData(getIntent().getExtras());
        }

        show_title.setText(lastPage);
        thistitle.setText(title);
        setting.setOnClickListener(this);
        if (style == 0) {
            resuorces.add("白日依山尽");
            resuorces.add("黄河入海流");
            resuorces.add("欲穷千里目");
            resuorces.add("更上一层楼");
        } else {
            resuorces.add("莫笑农家腊酒浑");
            resuorces.add("丰年留客足鸡豚");
            resuorces.add("山重水复疑无路");
            resuorces.add("柳暗花明又一村");
            resuorces.add("萧鼓追随春社近");
            resuorces.add("衣冠简朴古风存");
            resuorces.add("从今若许闲乘月");
            resuorces.add("拄杖无时夜叩门");
        }

        setInit(0, true);
        initView();
    }

    private void initView() {
        pencil.setallValue(R.mipmap.ball_pen, true);
        rubber.setallValue(R.mipmap.rubber, false);

        show_title.setOnClickListener(this);
        last_group.setOnClickListener(this);
        next_group.setOnClickListener(this);
        pencil.setOnClickListener(this);
        rubber.setOnClickListener(this);
        last_one.setOnClickListener(this);
        next_one.setOnClickListener(this);

        selet_paint.setOnCheckedChangeListener(this);

        no_gridview_one.setOnItemClickListener(this);
        no_gridview_two.setOnItemClickListener(this);
    }

    private void setInit(int current, boolean add) {
        setInit(current, add, false);
    }

    private void setInit(int current, boolean add, boolean last) {
        if (add && (current >= resuorces.size())) {
            showToast("已经到最后一页了");
            return;
        } else if (!add && current < 0) {
            showToast("当前已经是第一页");
            return;
        }
        currentIndex = current;
        listdata.clear();
        for (int i = currentIndex; i < (currentIndex + 2); i++) {
            List<PrictiseTextBeen> prictiseTextBeens = PinyinUtils.getPingYinAndToneList(resuorces.get(i));
            listdata.add(prictiseTextBeens);
        }
        int size1 = listdata.get(0).size();
        int size2 = listdata.get(1).size();
        int size = size1 >= size2 ? size1 : size2;
        int number = 0;
        if (size <= 5) {
            number = 5;
        } else if (size <= 10) {
            number = size;
        } else {
            number = 10;
        }
        int gridWidth = gridViewWidth / number;
        no_gridview_one.setNumColumns(number);
        no_gridview_two.setNumColumns(number);
        PrictiseTextAdapter adapterOne = new PrictiseTextAdapter(this, listdata.get(0), gridWidth);
        PrictiseTextAdapter adaptertwo = new PrictiseTextAdapter(this, listdata.get(1), gridWidth);

        no_gridview_one.setAdapter(adapterOne);
        no_gridview_two.setAdapter(adaptertwo);

        if (last) {
            textTndex = size1 + size2 - 1;
        } else {
            textTndex = 0;
        }
        setShowText();
    }

    /**
     * 设置需要绘制的文字
     */
    private void setShowText() {
        List<PrictiseTextBeen> list1 = listdata.get(0);
        List<PrictiseTextBeen> list2 = listdata.get(1);
        int size1 = list1.size();
        int size2 = list2.size();
        PrictiseTextBeen been = null;
        if (size1 > textTndex) {
            been = list1.get(textTndex);
        } else if (size1 + size2 > textTndex) {
            been = list2.get(textTndex - size1);
        } else {
            //该组字帖已经做完,自动跳转下一页
            setInit(currentIndex + 2, true);
        }

        if (been != null) {

            //获得保存名
            String saveName = draw_text_canver.getDrawTextName();
            if (saveName != null) {
                //保存原路径
                List<Path> ps = draw_text_canver.getPathList();
                paths.put(saveName, ps);
            }
            //绘制底板
            prictise_write_text.setInitValue(been);

            //设置绘制路径
            String plistSaveName = "page" + currentIndex + "index" + textTndex;
            List<Path> pathList = paths.get(plistSaveName);
            draw_text_canver.setDrawTextName(plistSaveName);
            draw_text_canver.setPaths(pathList);
            show_pingyin.setText(been.pingyin);
            show_pingyin.setTypeface(getTypeFace());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_title:
                onBackPressed();
                break;
            case R.id.setting://设置
                SettingDialog.getDialog(PractiseCalligraphyDetailActivity.this, new SettingInterface() {
                    @Override
                    public void settingFont(String fontPath) {
                        //设置字体返回
                        ((PrictiseTextAdapter) no_gridview_one.getAdapter()).notifyDataSetChanged();
                        ((PrictiseTextAdapter) no_gridview_two.getAdapter()).notifyDataSetChanged();
                        setShowText();
                    }
                });
                break;
            case R.id.last_group:
                setInit(currentIndex - 2, false);
                break;
            case R.id.next_group:
                setInit(currentIndex + 2, true);
                break;
            case R.id.pencil:
                break;
            case R.id.rubber:
                draw_text_canver.setPaths(null);
                break;
            case R.id.last_one:
                if (textTndex == 0) {
                    setInit(currentIndex - 2, false, true);
                } else {
                    textTndex--;
                    setShowText();
                }
                break;
            case R.id.next_one:
                textTndex++;
                setShowText();
                break;
            default:
                break;
        }
    }

    private Typeface getTypeFace() {
        String path = BaseApplication.preferences.getString("fontPath", null);
        if (path != null) {
            return Typeface.createFromFile(path);
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.no_gridview_one:
                break;
            case R.id.no_gridview_two:
                List<PrictiseTextBeen> list1 = listdata.get(0);
                int size1 = list1.size();
                position += size1;
                break;
            default:
                break;
        }
        if (position != textTndex) {
            textTndex = position;
            setShowText();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.bottom_1) {// 笔触最小
            draw_text_canver.setPaint(0);
        } else if (checkedId == R.id.bottom_2) {//笔触中等
            draw_text_canver.setPaint(1);
        } else if (checkedId == R.id.bottom_3) {// 笔触最大
            draw_text_canver.setPaint(2);
        }
    }

    private String title;
    private String lastPage;

    private void initData(Bundle bundle) {
        style = bundle.getInt("style");
        title = bundle.getString("title");
        lastPage = bundle.getString("lastPage");
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
        outState.putInt("style", style);
        outState.putString("title", title);
        outState.putString("lastPage", lastPage);
    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            setInit(currentIndex - 2, false);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            setInit(currentIndex + 2, true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
