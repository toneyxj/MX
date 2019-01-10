package com.mx.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.http.MXHttpHelper;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.FileUtils;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.CustomRecyclerView;
import com.mx.reader.R;
import com.mx.reader.adapter.LocalBookPageRecyclerAdapter;
import com.mx.reader.adapter.NetBookPageRecyclerAdapter;
import com.mx.reader.model.BookTypesModel;
import com.mx.reader.utils.PrepareCMS;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Archer on 16/7/27.
 */
public class MXBookStoreActivity extends BaseActivity implements GestureDetector.OnGestureListener,
        View.OnClickListener {

    private MXHttpHelper httpHelper;
    private BookTypesModel bookTypes;
    private GestureDetector gestureDetector = null;
    private NetBookPageRecyclerAdapter adapter;
    private LocalBookPageRecyclerAdapter localAdapter;
    private List<List<BookTypesModel.BookType>> listNetBook;
    private int page = 0;
    private List<File> listFiles = new ArrayList<>();

    @Bind(R.id.recycler_page_book_store)
    CustomRecyclerView recyclerView;
    @Bind(R.id.ll_book_store_found_more)
    LinearLayout llFoundMore;
    @Bind(R.id.ll_book_store_back)
    LinearLayout llBack;
    @Bind(R.id.recycler_page_local_book)
    RecyclerView recyclerLocalBook;
    @Bind(R.id.ll_book_store_total)
    LinearLayout llBookLocalAll;

    private String lastReadFile = "";

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.arg1 == 1001) {
            if (msg.what == 0) {
                bookTypes = (BookTypesModel) msg.obj;
                listNetBook = ListUtils.splitList(bookTypes.getResult(), 4);
                page = 0;
                resetAdapter(page);
            } else {
                Toastor.showToast(MXBookStoreActivity.this, "请检查网络连接!");
            }
        }
    }

    private void resetAdapter(final int index) {
        adapter = new NetBookPageRecyclerAdapter(listNetBook, this, index);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLIstener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent netBook = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("net_book_type", bookTypes);
                bundle.putInt("net_book_type_id", position);
                netBook.putExtras(bundle);
                netBook.setClass(MXBookStoreActivity.this, MXNetBookListActivity.class);
                startActivity(netBook);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.mx_activity_book_store;
    }


    private void initData() {
        httpHelper = MXHttpHelper.getInstance(this);
        httpHelper.postStringBack(1001, Constant.GET_BOOK_TYPE_LIST, null, getHandler(), BookTypesModel.class);

        gestureDetector = new GestureDetector(this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //给recyclerView设置ontouch事件
        recyclerView.setGestureDetector(gestureDetector);
        resetAdapter(0);

        llFoundMore.setOnClickListener(this);
        llBack.setOnClickListener(this);
        llBookLocalAll.setOnClickListener(this);
        recyclerLocalBook.setLayoutManager(new GridLayoutManager(this, 4));
    }

    private void resetLocalAdapter() {
        localAdapter = new LocalBookPageRecyclerAdapter(listFiles, this);
        recyclerLocalBook.setAdapter(localAdapter);
        localAdapter.setOnItemClickLIstener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new PrepareCMS(MXBookStoreActivity.this).insertCMS(listFiles.get(position).getAbsolutePath());
                FileUtils.getInstance().openFile(MXBookStoreActivity.this, listFiles.get(position));
                lastReadFile = listFiles.get(position).getAbsoluteFile() + "";
                File file = listFiles.remove(position);
                listFiles.add(0, file);
                localAdapter.notifyDataSetChanged();
                String filePaths = "";
                for (int i = 0; i < (listFiles.size() < 8 ? listFiles.size() : 8); i++) {
                    filePaths += (listFiles.get(i).getPath() + "@#@");
                }
                share.setCache("mx_read_local_books", filePaths);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        initData();
    }

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
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle
            outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

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
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v,
                            float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1,
                           float v, float v1) {
        float minMove = 60;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = motionEvent.getX();
        float endX = motionEvent1.getX();
        float beginY = motionEvent.getY();
        float endY = motionEvent1.getY();

        if (beginX - endX > minMove && Math.abs(v) > minVelocity) {   //左滑
            if (listNetBook != null) {
                if (page < listNetBook.size() - 1) {
                    page++;
                }
            }
        } else if (endX - beginX > minMove && Math.abs(v) > minVelocity) {   //右滑
            if (page > 0) {
                page--;
            }
        } else if (beginY - endY > minMove && Math.abs(v1) > minVelocity) {   //上滑
        } else if (endY - beginY > minMove && Math.abs(v1) > minVelocity) {   //下滑
        }
        resetAdapter(page);
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_PAGE_UP:
                if (page > 0) {
                    page--;
                }
                resetAdapter(page);
                return true;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                if (listNetBook != null) {
                    if (page < listNetBook.size() - 1) {
                        page++;
                    }
                }
                resetAdapter(page);
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_book_store_found_more:
                Intent netBook = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("net_book_type", bookTypes);
                netBook.putExtras(bundle);
                netBook.setClass(this, MXNetBookListActivity.class);
                startActivity(netBook);
                break;
            case R.id.ll_book_store_back:
                this.finish();
//                new ScanReadFile().execute();
                break;
            case R.id.ll_book_store_total:
                Intent localBook = new Intent();
                Bundle localBundle = new Bundle();
                localBundle.putSerializable("local_book_list", (Serializable) listFiles);
                localBook.putExtras(localBundle);
                localBook.setClass(this, MXStacksActivity.class);
                startActivity(localBook);
                break;
            default:
                break;
        }
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
                resetLocalAdapter();
            }
        }
    }

    /**
     * 扫描文件
     *
     * @param root
     * @param keyword
     */
    private void searchFile(File root, String[] keyword) {
        List<File> temp = new ArrayList<>();
        File[] files = root.listFiles();
        for (File file : files) {
            //不扫描 mx_exams文件下文件
            if (file.isDirectory() && file.canRead() && !file.getName().equals("mx_exams")) {//判断是否为文件夹并为keduwenjian
                searchFile(file, keyword);
            } else {
                if (file.getName().indexOf(keyword[0]) >= 0 ||
                        file.getName().indexOf(keyword[1]) >= 0 ||
                        file.getName().indexOf(keyword[2]) >= 0 ||
                        file.getName().indexOf(keyword[3]) >= 0) {
                    temp.add(file);
                }
            }
        }
        listFiles.addAll(temp);
        temp.clear();
    }
}
