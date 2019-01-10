package com.mx.zdreader.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.mx.reader.aidl.ILastReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
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
import com.mx.mxbase.utils.KeyBoard;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.mx.mxbase.view.CustomRecyclerView;
import com.mx.zdreader.R;
import com.mx.zdreader.adapter.LocalAllBookRecyclerAdapter;
import com.mx.zdreader.utils.PrepareCMS;

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
    private List<File> listFiles = new ArrayList<>();
    private List<List<File>> listFile;
    private LocalAllBookRecyclerAdapter adapter;
    private String lastReadFile = "";
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
        //初始化view
        recyclerStacks.setLayoutManager(new GridLayoutManager(this, 3));
        gestureDetector = new GestureDetector(this, this);
        recyclerStacks.setGestureDetector(gestureDetector);
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
                new PrepareCMS(MXStacksActivity.this).insertCMS(listFile.get(index).get(position).getAbsolutePath());
                FileUtils.getInstance().openFile(MXStacksActivity.this, listFile.get(index).get(position));
                etSearch.setText("");
                lastReadFile = listFile.get(index).get(position).getAbsoluteFile() + "";
                File file = listFiles.remove(position + index * 9);
                listFiles.add(0, file);
                String filePaths = "";
                for (int i = 0; i < (listFiles.size() < 8 ? listFiles.size() : 8); i++) {
                    filePaths += (listFiles.get(i).getPath() + "@#@");
                }
                resetAdapter(listFiles, index);
                share.setCache("mx_read_local_books", filePaths);
                Intent intentService = new Intent(ACTION_BIND_SERVICE);
                intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MXStacksActivity.this.bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
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
                            Toastor.showToast(MXStacksActivity.this, "删除成功!");
                        } else {
                            Toastor.showToast(MXStacksActivity.this, "删除失败!");
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

    private static final String ACTION_BIND_SERVICE = "android.mx.aidl.LAST_READER";
    private ILastReader iLastReader;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iLastReader = ILastReader.Stub.asInterface(iBinder);
            try {
                iLastReader.lasetReaderFile(lastReadFile);
                if (iLastReader != null) {
                    unbindService(mServiceConnection);
                    iLastReader = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iLastReader = null;
        }
    };

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        new ScanReadFile().execute();
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
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            if (etSearch.getText().toString().equals("")) {
                if (listFile != null && index > 0) {
                    index--;
                }
                resetAdapter(listFiles, index);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            if (etSearch.getText().toString().equals("")) {
                if (listFile != null && index < listFile.size() - 1) {
                    index++;
                }
                resetAdapter(listFiles, index);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
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
                if (listFile != null && index < listFile.size() - 1) {
                    index++;
                }
            }
        } else if (endX - beginX > minMove && Math.abs(v) > minVelocity) {   //右滑
            if (etSearch.getText().toString().equals("")) {
                if (listFile != null && index > 0) {
                    index--;
                }
            }
        } else if (beginY - endY > minMove && Math.abs(v1) > minVelocity) {   //上滑
        } else if (endY - beginY > minMove && Math.abs(v1) > minVelocity) {   //下滑
        }
        if (etSearch.getText().toString().equals("")) {
            resetAdapter(listFiles, index);
        }
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

    class ScanReadFile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listFiles.clear();
            searchFile(new File("/mnt/sdcard/"), new String[]{".pdf", ".mobi", ".txt", ".epub"});
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String bookStr = share.getString("mx_read_local_books");
            if (!bookStr.equals("")) {
                bookStr = bookStr.substring(0, bookStr.lastIndexOf("@#@"));
            }
            String[] books = bookStr.split("@#@");
            if (listFiles.size() > 0) {
                if (books.length > 0) {
                    for (int i = books.length - 1; i > -1; i--) {
                        int tFile = -1;
                        for (int j = 0; j < listFiles.size(); j++) {
                            if (listFiles.get(j).getPath().equals(books[i])) {
                                tFile = j;
                            }
                        }
                        if (tFile != -1) {
                            File f = listFiles.remove(tFile);
                            listFiles.add(0, f);
                        }
                    }
                }
                resetAdapter(listFiles, index);
            }
        }
    }

    private void searchFile(File root, String[] keyword) {
        List<File> temp = new ArrayList<>();
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory() && file.canRead()) {//判断是否为文件夹并为keduwenjian
                searchFile(file, keyword);
            } else {
                if (file.getName().indexOf(keyword[0]) >= 0 ||
                        file.getName().indexOf(keyword[1]) >= 0 ||
                        file.getName().indexOf(keyword[2]) >= 0 ||
                        file.getName().indexOf(keyword[3]) >= 0) {
                    if (file.getName().equals("zhude.pdf") || file.getName().equals("help.pdf")) {
                    } else {
                        temp.add(file);
                    }
                }
            }
        }
        listFiles.addAll(temp);
        temp.clear();
    }
}
