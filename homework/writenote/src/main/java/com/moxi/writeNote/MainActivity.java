package com.moxi.writeNote;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moxi.handwritinglibs.asy.DeleteFilesAsy;
import com.moxi.handwritinglibs.asy.ExportFileAsy;
import com.moxi.handwritinglibs.db.WritPadModel;
import com.moxi.handwritinglibs.db.WritePadUtils;
import com.moxi.handwritinglibs.listener.DeleteListener;
import com.moxi.handwritinglibs.listener.ExportListener;
import com.moxi.writeNote.Activity.WriteNoteActivity;
import com.moxi.writeNote.Model.FloderInformation;
import com.moxi.writeNote.Model.SimpleWriteModel;
import com.moxi.writeNote.adapter.WriteItemAdapter;
import com.moxi.writeNote.config.ActivityUtils;
import com.moxi.writeNote.config.ConfigInfor;
import com.moxi.writeNote.config.ConfigerUtils;
import com.moxi.writeNote.listener.PasteListener;
import com.moxi.writeNote.utils.MoveFileConfig;
import com.moxi.writeNote.utils.PasteAsy;
import com.mx.mxbase.base.MyApplication;
import com.mx.mxbase.dialog.InputDialog;
import com.mx.mxbase.interfaces.InsureOrQuitListener;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.utils.ToastUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends WriteBaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    /**
     * 保存页数集合
     */
    private List<FloderInformation> informations = new ArrayList<>();
    /**
     * 当前保存文件夹路径
     */
    private String floder = "";
    /**
     * 当前显示页数
     */
    private int pageIndex = 0;
    /**
     * 每页显示个数
     */
    private final int pageSize = 12;
    /**
     * 页面总页数
     */
    private int totalPage;
    /**
     * 手写板手写记录models
     */
    private List<WritPadModel> writPadModels = new ArrayList<>();
    /**
     * 中间转换model
     */
    private List<WritPadModel> middleModels = new ArrayList<>();
    /**
     * 适配器adapter
     */
    private WriteItemAdapter adapter;

    //    控件初始化
    @Bind(R.id.show_title)
    TextView show_title;
    @Bind(R.id.page_status)
    TextView page_status;
    @Bind(R.id.new_marks)
    TextView new_marks;
    @Bind(R.id.select)
    TextView select;

    @Bind(R.id.write_item)
    GridView write_item;

    @Bind(R.id.paste_file)
    TextView paste;
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
    public GestureDetector gestureDetector;
    private HomeKeyBrodcast homeKeyBrodcast=new HomeKeyBrodcast();
    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            informations.clear();
            List<FloderInformation> infors = (List<FloderInformation>) savedInstanceState.getSerializable("informations");
            if (infors==null){
                android.os.Process.killProcess(android.os.Process.myPid());
                return;
            }
            informations.addAll(infors);
        }
        registerReceiver(homeKeyBrodcast, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        initView();

        ActivityUtils.getInstance().addActivity(this);
    }

    private void initView() {
        show_title.setOnClickListener(this);
        new_marks.setOnClickListener(this);
        select.setOnClickListener(this);

        write_item.setOnItemClickListener(this);
        write_item.setOnItemLongClickListener(this);

        paste.setOnClickListener(this);
        delete.setOnClickListener(this);
        move.setOnClickListener(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        gestureDetector = new GestureDetector(this, onGestureListener);
        write_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_title:
                onBackPressed();
                break;
            case R.id.new_marks:
                if (isSelect()) {
                    changeSelect();
                    return;
                }
                if (isMove()) {
                    changeMove();
                    return;
                }
                InputDialog.getdialog(this, getString(R.string.new_floder), ConfigerUtils.hitnInput, new InputDialog.InputListener() {
                    @Override
                    public void quit() {
                    }

                    @Override
                    public void insure(String name) {
                        if (name.equals("")) {
                            return;
                        }
                        if (ConfigerUtils.isFail(name))return;
                        if (WritePadUtils.getInstance().isSavedWrite(getFloder(name), 0)) {
                            ToastUtils.getInstance().showToastShort("文件夹名已存在");
                            return;
                        }
                        /**保存的唯一标示*/
                        String saveCode = getFloder(name);
                        WritPadModel model = new WritPadModel(name, saveCode, 0, floder, 0, "");
                        WritePadUtils.getInstance().saveData(model);
                        //刷新数据
                        initPageInformation();
                    }
                });
                break;
            case R.id.select:
                if (isMove()) {
                    changeMove();
                    return;
                }
                changeSelect();
                break;
            case R.id.paste_file://粘贴
                if (MoveFileConfig.getInstance().getParentCode()!=null&&MoveFileConfig.getInstance().getParentCode().equals(floder)){
                    ToastUtils.getInstance().showToastShort("当前目录无法移动");
                    return;
                }
                dialogShowOrHide(true,"移动中...");
                new PasteAsy(floder, new PasteListener() {
                    @Override
                    public void onPaste(boolean is, String hitn) {
                        if (isfinish)return;
                        dialogShowOrHide(false,"");
                        changeMove();
                        if (!is){
                            insureDialog(hitn,"提示",null);
                        }else {
                            ToastUtils.getInstance().showToastShort("移动完成");
                        }
                        initPageInformation();
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.delete:
                if (adapter.getIsSelectWritePads().size() == 0) {
                    ToastUtils.getInstance().showToastShort("请选择删除文件");
                    return;
                }
                //选中的文件集合
              deleteFiles(adapter.getIsSelectWritePads());
                break;
            case R.id.move:
                if (adapter.getIsSelectWritePads().size() == 0) {
                    ToastUtils.getInstance().showToastShort("请选移动文件");
                    return;
                }
                List<SimpleWriteModel> commonModels = new ArrayList<>();
                for (int i = 0; i < adapter.getIsSelectWritePads().size(); i++) {
                    WritPadModel model1 = adapter.getIsSelectWritePads().get(i);
                    SimpleWriteModel simple = new SimpleWriteModel();
                    simple.id=model1.id;
                    simple.name=model1.name;
                    simple.isFloder = model1.isFolder;
                    simple.saveCode = model1.saveCode;
                    simple.parentCode = model1.parentCode;
                    commonModels.add(simple);
                }
                MoveFileConfig.getInstance().init(commonModels, true);
                changeSelect();
                changeMove();
                break;
            case R.id.last_page:
                moveLeft();
                break;
            case R.id.next_page:
                moveRight();
                break;
            default:
                break;
        }
    }

    /**
     * 删除文件集合
     * @param isSelects
     */
    private void deleteFiles(final List<WritPadModel> isSelects ){
        insureDialog("请确认删除选中文件", "删除文件", new InsureOrQuitListener() {
            @Override
            public void isInsure(Object code, final boolean is) {
                if (is) {
                    dialogShowOrHide(true, "文件删除中...");
                    new DeleteFilesAsy(isSelects, new DeleteListener() {
                        @Override
                        public void onDelete(boolean isDelete) {
                            if (isfinish)return;
                            dialogShowOrHide(false, "文件删除中...");
                            if (isDelete) {
                                if (isSelect()) {
                                    changeSelect();
                                }else {
                                    adapter.setShowAelect(false,null);
                                }
                                initPageInformation();
                                ToastUtils.getInstance().showToastShort("删除成功");
                            } else {
                                ToastUtils.getInstance().showToastShort("删除失败");
                            }
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WritPadModel model = middleModels.get(position);
        if (isSelect()) {
            //选择操作文件
            adapter.setIsSelectWritePads(model);
            adapter.updateSelect(position, write_item);
            return;
        }

        //新建文件/打开文件
        if ((model.isFolder == -1 || model.isFolder == 1) && !isMove()) {
//            isToNoteActivity = true;
            Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", model);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (model.isFolder == 0) {
            //文件夹
            informations.add(new FloderInformation(model.saveCode, model.name, 0));
            initPageInformation();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSelect() || isMove()) return true;
        final WritPadModel model = middleModels.get(position);
        if (model.isFolder == -1) {
            //新建文件
        } else if (model.isFolder == 0) {
            //文件夹重命名
            reName(model);
        } else if (model.isFolder == 1) {
            //文件导出或者重命名
            showManagerOneFilePopupWindow(view, model);
        }
        return true;
    }
private InputDialog inputDialog;
    /**
     * 重命名
     *
     * @param model
     */
    private void reName(final WritPadModel model) {
        inputDialog=InputDialog.getdialog(this, getString(R.string.re_name), ConfigerUtils.hitnInput, new InputDialog.InputListener() {
            @Override
            public void quit() {
            }

            @Override
            public void insure(String name) {
                if (name.equals("")) {
                    return;
                }
                if (ConfigerUtils.isFail(name))return;
                if (WritePadUtils.getInstance().isSavedWrite(getFloder(name), model.isFolder)) {
                    ToastUtils.getInstance().showToastShort("文件名已存在");
                    return;
                }
                /**保存的唯一标示*/
                WritePadUtils.getInstance().upDateName(name, model.id);
                //刷新数据
                initPageInformation();
            }
        });
    }

    /**
     * 更新当前页面显示信息
     */
    private void initPageInformation() {
        if (informations.size() == 0) {
            //保存根目录
            informations.add(new FloderInformation(ConfigInfor.rootDir, "笔记", 0));
        }
        FloderInformation information = informations.get(informations.size() - 1);
        floder = information.floder;
        pageIndex = information.pageIndex;
        writPadModels.clear();
        writPadModels.addAll(WritePadUtils.getInstance().getListMirks(floder));
        show_title.setText(information.name);
        changePage();
    }

    /**
     * 修改页面
     */
    private void changePage() {
        int size = writPadModels.size();
        totalPage = (size / pageSize) + ((size % pageSize == 0) ? 0 : 1);
        if (totalPage <= pageIndex) {
            pageIndex = totalPage - 1;
        }
        initAdapter();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        //修改当前页面的索引值
        informations.get(informations.size() - 1).pageIndex = pageIndex;
        middleModels.clear();
        if (totalPage > 0 && pageIndex >= 0) {
            if ((totalPage - 1) > pageIndex) {
                middleModels.addAll(writPadModels.subList(pageSize * pageIndex, pageSize * (pageIndex + 1)));
            } else {
                middleModels.addAll(writPadModels.subList(pageSize * pageIndex, writPadModels.size()));
            }
        }
        if (adapter == null) {
            adapter = new WriteItemAdapter(this, middleModels);
            write_item.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        show_index.setText(String.valueOf(pageIndex + 1) + "/" + String.valueOf(totalPage));
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        initPageInformation();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("informations", (Serializable) informations);
    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityUtils.getInstance().ClearActivity(this);
        unregisterReceiver(homeKeyBrodcast);
        if (inputDialog!=null&&inputDialog.isShowing()){
            inputDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        if (isSelect()) {
            changeSelect();
            return;
        }
//        if (isMove()) {
//            changeMove();
//            return;
//        }
        if (informations == null || informations.size() <= 1) {
//            android.os.Process.killProcess(android.os.Process.myPid());
            this.finish();
        } else {
            informations.remove(informations.size() - 1);
            initPageInformation();
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

    private void moveRight() {
        if (pageIndex >= totalPage - 1) {
            ToastUtils.getInstance().showToastShort("已经是最后一页");
            return;
        } else {
            pageIndex++;
            initAdapter();
        }
    }

    private void moveLeft() {
        if (pageIndex > 0 && (pageIndex <= totalPage - 1)) {
            pageIndex--;
            initAdapter();
        } else {
            ToastUtils.getInstance().showToastShort("已经是第一页");
        }
    }

    private void changeSelect() {
        if (isSelect()) {
            adapter.setShowAelect(false,write_item);
            selec_control.setVisibility(View.INVISIBLE);
        } else {
            adapter.setShowAelect(true,write_item);
            selec_control.setVisibility(View.VISIBLE);
        }
    }

    private void changeMove() {
        if (isMove()) {
            paste.setVisibility(View.GONE);
            page_status.setText("");
            adapter.setShowAelect(false,write_item);
//            adapter.updateAllSelect(write_item);
            MoveFileConfig.getInstance().ClearMove();
        } else {
            paste.setVisibility(View.VISIBLE);
            page_status.setText("移动文件");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MoveFileConfig.getInstance().ClearMove();
        if (isMove()) {
            changeMove();
        }
        dialogShowOrHide(false,"");
    }

    private boolean isSelect() {
        if (adapter == null) return false;
        return adapter.isShowAelect();
    }

    /**
     * 是否正在移动
     *
     * @return
     */
    private boolean isMove() {
        return paste.getVisibility() == View.VISIBLE;
    }

    private String getFloder(String name) {
        return floder + "/" + name;
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override//此方法必须重写且返回真，否则onFling不起效
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (Math.abs(x) < 40 || Math.abs(y) > 40) return false;
                    if (x > 0) {
                        moveLeft();
                    } else if (x < 0) {
                        moveRight();
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            };

    private PopupWindow onefile;

    /**
     * 单个文件操作pop
     *
     * @param view
     */
    public void showManagerOneFilePopupWindow(View view, WritPadModel model) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_one_file_manager, null);
        // 设置按钮的点击事件

        TextView export = (TextView) contentView.findViewById(R.id.export);
        TextView delete = (TextView) contentView.findViewById(R.id.delete);
        TextView rename = (TextView) contentView.findViewById(R.id.rename);

        onefile = new PopupWindow(contentView,
                MyApplication.dip2px(160), LinearLayout.LayoutParams.WRAP_CONTENT, true);

        onefile.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    onefile.dismiss();
                    onefile = null;
                    return true;
                }
                return false;
            }
        });
        export.setTag(model);
        rename.setTag(model);
        delete.setTag(model);
        export.setOnClickListener(onfile);
        delete.setOnClickListener(onfile);
        rename.setOnClickListener(onfile);
        // 设置好参数之后再show
        onefile.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (location[0] + view.getWidth() + 100 > MyApplication.ScreenWidth) {
            //左边
            onefile.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - onefile.getWidth(), location[1]);
        } else {
            //右边
            onefile.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth(), location[1]);
        }


    }

    View.OnClickListener onfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WritPadModel model = (WritPadModel) v.getTag();
            switch (v.getId()) {
                case R.id.export://导出
                    if (!StringUtils.haveSD(10)) {
                        File sdDir = null;
                        boolean sdCardExist = Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
                        if (sdCardExist) {
                            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
                        } else {
                            ToastUtils.getInstance().showToastShort("内存卡准备有误，请检查！");
                            return;
                        }
                        File file = new File(sdDir, "writingPad");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        newName(file, model.name, model);
                    }
                    break;
                case R.id.rename://重命名
                    reName(model);
                    break;
                case R.id.delete://删除
                     List<WritPadModel> isSelect=new ArrayList<>();
                    isSelect.add(model);
                    deleteFiles(isSelect);
                    break;
                default:
                    break;
            }
            onefile.dismiss();
            onefile = null;
        }
    };

    /**
     * 查看指定文件夹下面的文件是否存在
     *
     * @param file 文件夹路径文件
     * @param name 文件名字
     * @return 是否存在文件
     */
    private boolean isExist(File file, String name) {
        File file1 = new File(file, name);
        return file1.exists();
    }

    public void newName(final File file, String name, final WritPadModel model) {
        if (isExist(file, name)) {
            ToastUtils.getInstance().showToastShort("文件夹名已存在，请重新输入");
            //文件导出
            inputDialog=InputDialog.getdialog(this, getString(R.string.re_name), "请输入存储文件夹名", new InputDialog.InputListener() {
                @Override
                public void quit() {
                }

                @Override
                public void insure(String name) {
                    newName(file, name, model);
                }
            });
        } else {
            //文件路径
            String filePath = (new File(file, name)).getAbsolutePath();
            dialogShowOrHide(true, "文件导出中...");
            new ExportFileAsy(model, filePath, new ExportListener() {
                @Override
                public void onExport(boolean is) {
                    if (isfinish)return;
                    dialogShowOrHide(false, "文件导出中...");
                    ToastUtils.getInstance().showToastShort(is ? "文件已导入writingPad目录" : "导出失败");
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
