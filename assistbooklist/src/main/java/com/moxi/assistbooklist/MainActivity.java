package com.moxi.assistbooklist;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moxi.assistbooklist.adapter.LocalAllBookLinearAdapter;
import com.moxi.assistbooklist.adapter.LocalAllBookRecyclerAdapter;
import com.moxi.assistbooklist.controler.SacnReadFileUtils;
import com.moxi.assistbooklist.controler.ScanReadFile;
import com.moxi.assistbooklist.mode.BookStoreFile;
import com.moxi.assistbooklist.utils.PrepareCMS;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.mx.mxbase.view.CustomRecyclerView;
import com.mx.mxbase.view.SildeFrameLayout;
import com.onyx.android.sdk.device.DeviceInfo;
import com.onyx.android.sdk.device.EpdController;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener, SildeFrameLayout.SildeEventListener {

    @Bind(R.id.move_layout)
    SildeFrameLayout move_layout;
    @Bind(R.id.ll_stacks_back)
    LinearLayout llBack;
    @Bind(R.id.no_file_layout)
    LinearLayout no_file_layout;
    @Bind(R.id.books_show_layout)
    LinearLayout books_show_layout;
    @Bind(R.id.recycler_stacks)
    CustomRecyclerView recyclerStacks;
    @Bind(R.id.tv_stacks_page_index)
    TextView tvPageIndex;
    @Bind(R.id.tv_stacks_total_count)
    TextView tvTotalCount;
    @Bind(R.id.et_stacks_search)
    EditText etSearch;

    @Bind(R.id.search)
    ImageView search;
    @Bind(R.id.refuresh)
    ImageView refuresh;
    @Bind(R.id.book_type)
    ImageView book_type;
    @Bind(R.id.sort_type)
    ImageView sort_type;
    @Bind(R.id.file_show_style)
    ImageView imgPaiLie;

    @Bind(R.id.tv_boos_stacks_title)
    TextView tvTitle;

    private List<BookStoreFile> listDate = new ArrayList<>();
    private List<BookStoreFile> middleDates = new ArrayList<>();
    private LocalAllBookRecyclerAdapter adapter;
    private LocalAllBookLinearAdapter linearAdapter;
    /**
     * 当前显示页数
     */
    private int index = 0;

    /**
     * 每页显示个数
     */
    private final int pageSize = 12;
    private int linePageSize = 18;
    /**
     * 页面总页数
     */
    private int totalPage;
    private long searchTime = 0;
    private int sortType = 0; //0为最近 1为名称 2为当当
    private boolean NEEDRELOAD = false;
    private int pailieStyle = 0;
    /**开启软键盘*/
    private boolean openSoftKeybord=false;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //设置点击监听
        llBack.setOnClickListener(this);

        sortType = share.getInt("book_stacks_sort");
        pailieStyle = share.getInt("pailieStyle");

        if (pailieStyle == 0) {
            imgPaiLie.setImageResource(R.mipmap.file_show_style_one);
        } else {
            imgPaiLie.setImageResource(R.mipmap.file_show_style_two);
        }

        move_layout.setListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Math.abs(searchTime - System.currentTimeMillis()) > 500) {
                    searchTime = System.currentTimeMillis();
                    addData(0);
                }
            }
        });

        search.setOnClickListener(this);
        refuresh.setOnClickListener(this);
        book_type.setOnClickListener(this);
        sort_type.setOnClickListener(this);
        imgPaiLie.setOnClickListener(this);

        if (!share.getBoolean("no_first_start")){
            updateData("数据初始化中...");
        }else {
            addData(0);
        }

    }

    private void addData(int index) {
        if (isfinish)return;
        String search = etSearch.getText().toString().trim();
        this.index = index;
        listDate.clear();

        if (search == null || search.equals("")) {
            if (sortType == 0) {//按照最近阅读排序
                listDate.addAll(SacnReadFileUtils.getInstance(this).getBookStoreSize(0));
            } else {//按照名称排序
                listDate.addAll(SacnReadFileUtils.getInstance(this).getBookStoreSizeOrderByPinyin(0));
            }
        } else {
            listDate.addAll(SacnReadFileUtils.getInstance(this).getSearchBookStoreSize(search, 0));
        }
        //设置是否第一次进入
        share.setCache("no_first_start",listDate.size()>0);
        if (sortType != 0) {
            dialogShowOrHide(true, "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //排序
                    nameSort();
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            changePage();
                            dialogShowOrHide(false, "");
                        }
                    });
                }
            }).start();
        } else {
            changePage();
        }

    }

    private ScanReadFile.ScanReadListner scanReadListner = new ScanReadFile.ScanReadListner() {
        @Override
        public void onScanReadEnd() {
            if (isfinish)return;
            Thread delThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    List<BookStoreFile> list = SacnReadFileUtils.getInstance(MainActivity.this).getBookStoreSize(0);
                    for (BookStoreFile bookStoreFile : list) {
                        if (!bookStoreFile.getFile().exists()) {
                            SacnReadFileUtils.getInstance(MainActivity.this).deleteFile(bookStoreFile.filePath);
                        } else if (bookStoreFile.pathMd5 == null || bookStoreFile.pathMd5.equals("")) {
                            DataSupport.delete(BookStoreFile.class, bookStoreFile.id);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isfinish)return;
                            dialogShowOrHide(false, "请稍候...");
                            addData(0);
                        }
                    });
                }
            };
            delThread.start();
        }

        @Override
        public void onScanReadFile(BookStoreFile file) {
        }
    };
    /**
     * 修改页面
     */
    private void changePage() {
        int size = listDate.size();
        no_file_layout.setVisibility(size==0?View.VISIBLE:View.GONE);
        books_show_layout.setVisibility(size>0?View.VISIBLE:View.INVISIBLE);
        if (pailieStyle == 0) {
            resetAdapter(size);
        } else {
            setLinearAdapter(size);
        }
    }

    private void setLinearAdapter(int size) {
        totalPage = (size / linePageSize) + ((size % linePageSize == 0) ? 0 : 1);
        if (totalPage <= index) {
            index = totalPage - 1;
        }
        recyclerStacks.setLayoutManager(new GridLayoutManager(this, 2));
        middleDates.clear();
        if (totalPage > 0 && index >= 0) {
            if ((totalPage - 1) > index) {
                middleDates.addAll(listDate.subList(linePageSize * index, linePageSize * (index + 1)));
            } else {
                middleDates.addAll(listDate.subList(linePageSize * index, listDate.size()));
            }
        }
        if (linearAdapter == null) {
            linearAdapter = new LocalAllBookLinearAdapter(middleDates, this, recyclerStacks);
            recyclerStacks.setAdapter(linearAdapter);
            linearAdapter.setOnItemClickLIstener(onItemClickListener);
        } else {
            linearAdapter.notifyDataSetChanged();
        }
        adapter = null;
        tvPageIndex.setText((index + 1) + "/" + totalPage);
        tvTotalCount.setText("总计：" + listDate.size());
    }

    /**
     * 初始化adapter
     */
    private void resetAdapter(int size) {
        totalPage = (size / pageSize) + ((size % pageSize == 0) ? 0 : 1);
        if (totalPage <= index) {
            index = totalPage - 1;
        }
        //修改当前页面的索引值
        recyclerStacks.setLayoutManager(new GridLayoutManager(this, 4));
        middleDates.clear();
        if (totalPage > 0 && index >= 0) {
            if ((totalPage - 1) > index) {
                middleDates.addAll(listDate.subList(pageSize * index, pageSize * (index + 1)));
            } else {
                middleDates.addAll(listDate.subList(pageSize * index, listDate.size()));
            }
        }
        if (adapter == null) {
            adapter = new LocalAllBookRecyclerAdapter(middleDates, this, recyclerStacks);
            recyclerStacks.setAdapter(adapter);
            adapter.setOnItemClickLIstener(onItemClickListener);
        } else {
            adapter.notifyDataSetChanged();
        }
        linearAdapter = null;

        tvPageIndex.setText((index + 1) + "/" + totalPage);
        tvTotalCount.setText("总计：" + listDate.size());
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String path = middleDates.get(position).filePath;
            if (!(new File(path)).exists()) {
                showToast("图书不存在！");
                SacnReadFileUtils.getInstance(MainActivity.this).deleteFile(path);
                addData(index);
                return;
            }
                share.setCache("Recently", path);
                new PrepareCMS(MainActivity.this).insertCMS(middleDates.get(position).filePath);
                FileUtils.getInstance().openFile(MainActivity.this, middleDates.get(position).getFile());
//                new StartFile(MXStacksActivity.this, path);
                SacnReadFileUtils.getInstance(MainActivity.this).updateIndex(middleDates.get(position));
        }

        @Override
        public void onItemLongClick(View view, final int position) {

            final File f = middleDates.get(position).getFile();
            new AlertDialog(MainActivity.this).builder().setTitle("提示").
                    setMsg("确认移除" + f.getName() + "?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StringUtils.deleteFile(f)) {
                        SacnReadFileUtils.getInstance(MainActivity.this).deleteFile(f.getAbsolutePath());
                        listDate.remove(pageSize * index + position);
                        changePage();
                        Toastor.showToast(MainActivity.this, "删除成功!");
                    } else {
                        Toastor.showToast(MainActivity.this, "删除失败!");
                    }
                }
            }).setPositiveButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }).show();
        }
    };

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        DeviceInfo.currentDevice.showSystemStatusBar(this);
        if (NEEDRELOAD) {
            addData(index);
            NEEDRELOAD = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        NEEDRELOAD = true;
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SacnReadFileUtils.getInstance(this).removeListener(scanReadListner);
    }
    private void updateData(String hitn){
        dialogShowOrHide(true, hitn);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SacnReadFileUtils.getInstance(MainActivity.this).SearchBooks(MainActivity.this, scanReadListner);
            }
        }, 1500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_stacks_back:
                onBackPressed();
                break;
            case R.id.search:
                etSearch.setVisibility(etSearch.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                break;
            case R.id.refuresh://ll_book_stacks_tongbu
                updateData("更新文件中...");
                break;
            case R.id.book_type://rl_stacks_class
//                showClassPop();
                break;
            case R.id.sort_type://rl_stacks_sort
                showSortPop();
                break;
            //最近
            case R.id.tv_book_sort_recent:
                if (popupSort != null) {
                    if (!popupSort.isShowing()) {
                    }
                    popupSort.dismiss();
                    popupSort = null;
                }
                if (sortType == 0) return;
                switchPopSort(0);
                break;
            //名称
            case R.id.tv_book_sort_name:
                if (popupSort != null) {
                    if (!popupSort.isShowing()) {
                    }
                    popupSort.dismiss();
                    popupSort = null;
                }
                if (sortType == 1) return;
                switchPopSort(1);
                break;
            case R.id.file_show_style://rl_change_pai_lie
                index = 0;
                if (pailieStyle == 1) {
                    imgPaiLie.setImageResource(R.mipmap.file_show_style_one);
                    pailieStyle = 0;
                } else {
                    imgPaiLie.setImageResource(R.mipmap.file_show_style_two);
                    pailieStyle = 1;
                }
                share.setCache("pailieStyle", pailieStyle);
                changePage();
                break;
            default:
                break;
        }
    }

    /**
     * 分类switch
     *
     * @param sortStyle
     */
    private void switchPopSort(int sortStyle) {
        sortType = sortStyle;
        etSearch.setText("");
        share.setCache("book_stacks_sort", sortType);
        addData(0);
    }

    PopupWindow  popupSort;


    private void showSortPop() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popwindow_sort, null);
        popupSort = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        contentView.findViewById(R.id.tv_book_sort_recent).setOnClickListener(this);
        contentView.findViewById(R.id.tv_book_sort_name).setOnClickListener(this);

        ImageView imgSortRecent = (ImageView) contentView.findViewById(R.id.img_book_sort_by_recent);
        ImageView imgSortName = (ImageView) contentView.findViewById(R.id.img_book_sort_by_name);
        if (sortType == 0) {
//            tvStacksSort.setText("最近");
            imgSortRecent.setVisibility(View.VISIBLE);
            imgSortName.setVisibility(View.INVISIBLE);
        } else {
//            tvStacksSort.setText("名称");
            imgSortRecent.setVisibility(View.INVISIBLE);
            imgSortName.setVisibility(View.VISIBLE);
        }

        popupSort.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    if (popupSort != null) {
                        if (!popupSort.isShowing()) {
                        }
                        popupSort.dismiss();
                        popupSort = null;
                    }
                    return true;
                }
                return false;
            }
        });

        popupSort.setBackgroundDrawable(new BitmapDrawable());
        popupSort.setOutsideTouchable(false);
        popupSort.showAsDropDown(sort_type, -10, 0);
    }

    /**
     * 点击其它地方关闭软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (StringUtils.isShouldHideInput(v, ev)) {
                setOpenSoftKeybord(false);
                StringUtils.closeIMM(this, v.getWindowToken());
                v.clearFocus();
                return true;
            }else {
                setOpenSoftKeybord(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOpenSoftKeybord(boolean openSoftKeybord) {
        if (openSoftKeybord==this.openSoftKeybord)return;
        this.openSoftKeybord = openSoftKeybord;
        if (!openSoftKeybord)
        etSearch.setVisibility(View.INVISIBLE);
    }

    private void moveLeft() {
        if (index > 0) {
            index--;
            changePage();
        }
    }

    private void moveRight() {
        if (index < totalPage - 1) {
            index++;
            changePage();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
                moveLeft();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
                moveRight();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHandler().sendEmptyMessageDelayed(1000, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1000) {
            EpdController.invalidate(getWindow().getDecorView(), EpdController.UpdateMode.GC);
        }
    }

    /**
     * 按名称排序
     */
    private void nameSort() {
        Collections.sort(listDate, new StartNameComparator());
    }

    /**
     * 开始的特殊字符处理
     */
    private static class StartNameComparator implements Comparator<BookStoreFile> {
        @Override
        public int compare(BookStoreFile lhs, BookStoreFile rhs) {
            if (lhs == null && rhs == null) return 0;
            if (lhs == null || rhs == null) return lhs == null ? 1 : -1;

            String name1 = lhs.getFullPinyin();
            String name2 = rhs.getFullPinyin();
            if (name1 == null && name2 == null) {
                return 0;
            } else if (name1 == null && name2 != null) {
                return 1;
            } else if (name1 != null && name2 == null) {
                return -1;
            } else if (name1.equals("") && !name2.equals("")) {
                return 1;
            } else if (!name1.equals("") && name2.equals("")) {
                return -1;
            }
            List<String> list1 = getNumbers(name1);
            List<String> list2 = getNumbers(name2);
            if (list1.size() > 0 && list2.size() > 0) {
                String[] text1 = name1.split("\\d+");
                String[] text2 = name2.split("\\d+");
                if ((text1.length == 0 && text2.length == 0)
                        || (text1.length != 0 && text2.length == 0 && text1[0].equals(""))
                        || (text1.length == 0 && text2.length != 0 && text2[0].equals(""))) {
                    String num1 = list1.get(0);
                    String num2 = list2.get(0);
                    if (num1.length() != num2.length()) {
                        return num1.length() > num2.length() ? 1 : -1;
                    }

                    int type = name1.compareTo(name2);
                    if (type > 0) {
                        return 1;
                    } else if (type < 0) {
                        return -1;
                    }
                }

                int len = text1.length > text2.length ? text2.length : text1.length;

                for (int i = 0; i < len; i++) {
                    if (text1[i].equals(text2[i]) && (i < list1.size() && i < list2.size())) {
                        String num1 = list1.get(i);
                        String num2 = list2.get(i);
                        if (num1.length() != num2.length()) {
                            return num1.length() > num2.length() ? 1 : -1;
                        }
                        int type = name1.compareTo(name2);
                        if (type > 0) {
                            return 1;
                        } else if (type < 0) {
                            return -1;
                        }
                    }
                }
            }
            int type = name1.compareTo(name2);
            if (type > 0) {
                return 1;
            } else if (type < 0) {
                return -1;
            }
            return 0;
        }
    }

    public static List<String> getNumbers(String str) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    @Override
    public void onSildeEventLeft() {
        moveLeft();
    }

    @Override
    public void onSildeEventRight() {
        moveRight();
    }
}
