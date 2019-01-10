package com.mx.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.mx.mxbase.view.CustomRecyclerView;
import com.mx.reader.R;
import com.mx.reader.adapter.LocalAllBookRecyclerAdapter;
import com.mx.reader.utils.PrepareCMS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Archer on 16/8/4.
 */
public class MXStacksActivity extends BaseActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    @Bind(R.id.ll_stacks_back)
    LinearLayout llBack;
    @Bind(R.id.recycler_stacks)
    CustomRecyclerView recyclerStacks;
    @Bind(R.id.tv_stacks_page_index)
    TextView tvPageIndex;
    @Bind(R.id.tv_stacks_total_count)
    TextView tvTotalCount;
    @Bind(R.id.et_stacks_search)
    EditText etSearch;

    private GestureDetector gestureDetector = null;
    private List<File> listFiles;
    private List<List<File>> listFile;
    private LocalAllBookRecyclerAdapter adapter;
    private int index = 0;

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_stacks;
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
        //获取数据
        Intent preIntent = this.getIntent();
        listFiles = (List<File>) preIntent.getSerializableExtra("local_book_list");
        if (listFiles == null) {
            return;
        }
        //初始化view
        recyclerStacks.setLayoutManager(new GridLayoutManager(this, 3));
        gestureDetector = new GestureDetector(this, this);
        recyclerStacks.setGestureDetector(gestureDetector);
        resetAdapter(listFiles, index);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    resetAdapter(listFiles, index);
                } else {
                    searchBookByName(editable.toString());
                }
            }
        });
    }

    private void resetAdapter(List<File> list, final int index) {
        listFile = ListUtils.splitList(list, 9);
        adapter = new LocalAllBookRecyclerAdapter(listFile, this, index);
        recyclerStacks.setAdapter(adapter);
        tvPageIndex.setText((index + 1) + "/" + listFile.size());
        tvTotalCount.setText("总计：" + listFiles.size());
        adapter.setOnItemClickLIstener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FileUtils.getInstance().openFile(MXStacksActivity.this, listFile.get(index).get(position));
                new PrepareCMS(MXStacksActivity.this).insertCMS(listFile.get(index).get(position).getAbsolutePath());
                etSearch.setText("");
                File file = listFiles.remove(position + index * 9);
                listFiles.add(0, file);
                String filePaths = "";
                for (int i = 0; i < (listFiles.size() < 8 ? listFiles.size() : 8); i++) {
                    filePaths += (listFiles.get(i).getPath() + "@#@");
                }
                resetAdapter(listFiles, index);
                share.setCache("mx_read_local_books", filePaths);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog(MXStacksActivity.this).builder().setTitle("提示").
                        setMsg("确认移除" + listFile.get(index).get(position).getName() + "?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File f = listFiles.remove(position + index * 9);
                        if (FileUtils.getInstance().DeleteFolder(f.getPath())) {
                            resetAdapter(listFiles, index);
                            Toastor.showToast(MXStacksActivity.this,"删除成功!");
                        }else{
                            Toastor.showToast(MXStacksActivity.this,"删除失败!");
                        }
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_stacks_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_PAGE_UP:
                if (etSearch.getText().toString().equals("")) {
                    if (index > 0) {
                        index--;
                    }
                }
                resetAdapter(listFiles, index);
                return true;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                if (etSearch.getText().toString().equals("")) {
                    if (index < listFile.size() - 1) {
                        index++;
                    }
                }
                resetAdapter(listFiles, index);
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float minMove = 60;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = motionEvent.getX();
        float endX = motionEvent1.getX();
        float beginY = motionEvent.getY();
        float endY = motionEvent1.getY();

        if (beginX - endX > minMove && Math.abs(v) > minVelocity) {   //左滑
            if (etSearch.getText().toString().equals("")) {
                if (index < listFile.size() - 1) {
                    index++;
                }
            }
        } else if (endX - beginX > minMove && Math.abs(v) > minVelocity) {   //右滑
            if (etSearch.getText().toString().equals("")) {
                if (index > 0) {
                    index--;
                }
            }
        } else if (beginY - endY > minMove && Math.abs(v1) > minVelocity) {   //上滑
        } else if (endY - beginY > minMove && Math.abs(v1) > minVelocity) {   //下滑
        }
        resetAdapter(listFiles, index);
        return false;
    }

    private void searchBookByName(String name) {
        List<File> listTemp = new ArrayList<>();
        listTemp.clear();
        for (File file : listFiles) {
            if (file.getName().substring(0, file.getName().lastIndexOf(".")).contains(name)) {
                listTemp.add(file);
            }
        }
        resetAdapter(listTemp, index);
    }
}
