package com.moxi.haierc.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moxi.haierc.R;
import com.moxi.haierc.callback.UpdateCallBack;
import com.moxi.haierc.model.Advertisement;
import com.moxi.haierc.model.BookStoreFile;
import com.moxi.haierc.model.UserInfoModel;
import com.moxi.haierc.util.CheckVersionCode;
import com.moxi.haierc.util.ReadManagerPicUtils;
import com.moxi.haierc.util.Utils;
import com.moxi.updateapp.UpdateUtil;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.AppUtil;
import com.mx.mxbase.utils.DeleteDDReaderBook;
import com.mx.mxbase.utils.GlideUtils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.MXUamManager;
import com.mx.mxbase.utils.ToastUtils;
import com.mx.mxbase.utils.Toastor;
import com.onyx.android.sdk.device.DeviceInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

public class MoXiB1MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.icon)
    ImageView imageViewIcon;
    @Bind(R.id.adversing)
    ImageView adversing;
    @Bind(R.id.tv_new_main_username)
    TextView tvUsername;
    @Bind(R.id.new_main_week)
    TextView tvWeek;
    @Bind(R.id.new_main_day)
    TextView tvDay;

    @Bind(R.id.wssc)
    RelativeLayout wssc;
    @Bind(R.id.sxbwl)
    RelativeLayout sxbwl;
    @Bind(R.id.wjglq)
    RelativeLayout wjglq;
    @Bind(R.id.rili)
    RelativeLayout rili;
    @Bind(R.id.yy)
    RelativeLayout yy;
    @Bind(R.id.zd)
    RelativeLayout zd;
    @Bind(R.id.sz)
    RelativeLayout sz;
    @Bind(R.id.is_update_app)
    TextView is_update_app;

    @Bind(R.id.my_bookstore)
    TextView my_bookstore;
    @Bind(R.id.recently_read_book)
    TextView recently_read_book;
    @Bind(R.id.continue_read)
    Button continue_read;

    private boolean NEED_REGET = false;
    private List<BookStoreFile> listRecent = new ArrayList<>();
    private int[] res = new int[]{
            R.mipmap.mx_img_new_avatar_0,
            R.mipmap.mx_img_new_avatar_1,
            R.mipmap.mx_img_new_avatar_2,
            R.mipmap.mx_img_new_avatar_3};

    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private String stuFlag = "";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        initView();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.moxi.haierc.UPDATE_APPLICATION");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        stuFlag = share.getString("flag_version_stu");
        if (stuFlag.equals("")) {
            stuFlag = "标准版";
        }
    }


    /**
     * 初始化view
     */
    private void initView() {
        imageViewIcon.setOnClickListener(this);
        initReadBook();
        IntentFilter filter = new IntentFilter("com.moxi.broadcast.external.action");
        localBroadcastManager.registerReceiver(external, filter);

        //初始化底部显示模块

        wssc.setOnClickListener(this);
        sxbwl.setOnClickListener(this);
        wjglq.setOnClickListener(this);
        rili.setOnClickListener(this);
        yy.setOnClickListener(this);
        zd.setOnClickListener(this);
        sz.setOnClickListener(this);

        my_bookstore.setOnClickListener(this);
        continue_read.setOnClickListener(this);

    }


    /**
     * 获得最近阅读书籍路径
     */
    public List<BookStoreFile> getrecentlyRead() {
        List<BookStoreFile> files = new ArrayList<>();
        try {
            ContentResolver contentResolver = getContentResolver();
            Uri selecturi = Uri.parse("content://com.moxi.bookstore.provider.RecentlyProvider/Recently");
            Cursor cursor = contentResolver.query(selecturi, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    dialogShowOrHide(false, "请稍后...");
                    try {
                        BookStoreFile info = getModel(cursor);
                        files.add(info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (cursor != null) cursor.close();

        } catch (Exception e) {
            getHandler().sendEmptyMessageAtTime(1000, 1000);
        }
        return files;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1000) {
            initReadBook();
        }
    }

    private BookStoreFile getModel(Cursor cursor) {
        /** 索引id */
        long id = cursor.getLong(0);
        /** 文件保存路径 */
        String filePath = cursor.getString(1);
        /** 文件图片路径 */
        String photoPath = cursor.getString(2);
        /** 文件排序索引 */
        long _index = cursor.getInt(3);
        /** 文件全拼 */
        String fullPinyin = cursor.getString(4);
        /**是否为当当书记**/
        int isDdBook = cursor.getInt(6);
        /**书籍图片*/
        String bookImageUrl = cursor.getString(7);
        String progress = "";
        try {
            progress = cursor.getString(8);
        } catch (Exception e) {
        }


        BookStoreFile model = new BookStoreFile();
        model.id = id;
        model.filePath = filePath;
        model.photoPath = photoPath;
        model._index = _index;
        model.fullPinyin = fullPinyin;
        model.isDdBook = isDdBook;
        model.bookImageUrl = bookImageUrl;
        model.progress = progress;

        return model;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    BroadcastReceiver external = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initReadBook();
        }
    };

    private void initReadBook() {
        listRecent = getrecentlyRead();
        if (listRecent == null || listRecent.size() == 0) {
            recently_read_book.setText("阅读可以让人快乐");
            continue_read.setText("选书阅读");
        } else {
            recently_read_book.setText(listRecent.get(0).getName());
            continue_read.setText("继续阅读");
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (NEED_REGET) {
            ReadManagerPicUtils.getInstance().clearMetdata();
            initReadBook();
            NEED_REGET = false;
            stuFlag = share.getString("flag_version_stu");
            if (stuFlag.equals("")) {
                stuFlag = "标准版";
            }
        }
        DeviceInfo.currentDevice.showSystemStatusBar(this);
        new DeleteDDReaderBook(MoXiB1MainActivity.this, false, null).execute();
        getUserInfo();
        setDate();
        getADData();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO 开启电磁屏
                openOrOff(true);
            }
        }, 1000);

        CheckVersionCode.getInstance(this).checkframework(new UpdateCallBack() {
            @Override
            public void needUpdate(boolean need) {
                if (isfinish) return;
                APPLog.e("needUpdate",need);
                is_update_app.setVisibility(need ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void setDate() {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
        tvDay.setText(sim.format(date));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        tvWeek.setText(dayNames[dayOfWeek]);
    }

    private void getUserInfo() {
        String temp = MXUamManager.querUserBId(this);
        UserInfoModel userInfoModel = GsonTools.getPerson(temp, UserInfoModel.class);
        if (userInfoModel != null) {
            tvUsername.setText(userInfoModel.getResult().getMember().getName());
            int aaa = userInfoModel.getResult().getMember().getHeadPortrait();
            if (aaa != -99) {
                imageViewIcon.setImageResource(res[aaa]);
            } else {
                imageViewIcon.setImageResource(res[0]);
            }
        }
    }

    private void getADData() {
//        if(Constant.isMoXi){
//            GlideUtils.getInstance().loadGreyImage(MoXiB1MainActivity.this, adversing, Constant.HTTP_HOST + "/adimage/1_20170710170106_274_265.jpg");
//            return;
//        }
        OkHttpUtils.post().url(Constant.GET_ADVSING3_JSON).addParams("versionName", AppUtil.getPackageInfo(this).
                versionName).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (isfinish || MoXiB1MainActivity.this.isFinishing())
                    return;
                try {
                    APPLog.e("response",response);
                    final Advertisement advertisement = GsonTools.getPerson(response, Advertisement.class);
                    if (advertisement != null) {
                        GlideUtils.getInstance().loadGreyImage(MoXiB1MainActivity.this, adversing, Constant.HTTP_HOST + advertisement.getResult().getImageUrl());
                        adversing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    if (advertisement.getResult().getExtLink() != null) {
                                        Uri uri = Uri.parse(advertisement.getResult().getExtLink());
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Log.i("advertisment is null");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void openOrOff(boolean open) {
        if (open) {
            writeStringValueToFile("/sys/devices/platform/onyx_misc.0/tp_disable", "0");
        } else {
            writeStringValueToFile("/sys/devices/platform/onyx_misc.0/tp_disable", "1");
        }
    }

    private boolean writeStringValueToFile(final String path, String value) {
        FileOutputStream fout = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                String command = "touch " + path;
                do_exec(command);
            }
            fout = new FileOutputStream(file);
            byte[] bytes = value.getBytes();
            fout.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void do_exec(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 继续阅读
     *
     * @param path 最近阅读书籍文件路径
     */
    private void continueReader(String path) {
        if (!path.equals("")) {
            File file = new File(path);
            if (file.exists()) {
                Intent input = new Intent();
                input.putExtra("file", path);
                ComponentName cnInput = new ComponentName("com.moxi.bookstore", "com.moxi.bookstore.activity.RecentlyActivity");
                input.setComponent(cnInput);
                startActivity(input);
            } else {
                showToast("阅读文件已异常，请确认文件是否存在！！");
            }
        } else {
            showToast("还没有阅读记录哟！");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        NEED_REGET = true;
        dialogShowOrHide(false, "");
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
        localBroadcastManager.unregisterReceiver(external);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_mo_xi_b1_main;
    }

    @Override
    public void onClick(View v) {
        if (!Utils.isFastClick()) {
            return;
        }
        if (!Utils.isSDCardCanReadAndWrite()) {
            ToastUtils.getInstance().showToastShort("SD卡准备未完成！");
            return;
        }
        switch (v.getId()) {
            case R.id.tv_new_main_enter_library:
                if (Utils.isSDCardCanReadAndWrite()) {
                    try {
                        Intent sound = new Intent();
                        sound.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ComponentName cnSound = new ComponentName("com.moxi.bookstore", "com.moxi.bookstore.activity.bookManager.MXStacksActivity");
                        sound.setComponent(cnSound);
                        sound.putExtra("flag_version_stu", "");
                        startActivity(sound);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toastor.showToast(this, "启动失败，请检测是否正常安装");
                        new UpdateUtil(this, this).checkInstall("com.moxi.bookstore");
                    }
                } else {
                    Toastor.showToast(this, "sd卡未准备好");
                }
                break;
            case R.id.icon://用户登录
                try {
                    Intent sound = new Intent();
                    sound.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ComponentName cnSound = new ComponentName("com.moxi.user", "com.mx.user.activity.MXLoginActivity");
                    sound.setComponent(cnSound);
                    sound.putExtra("flag_version_stu", "");
                    startActivity(sound);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toastor.showToast(this, "启动失败，请检测是否正常安装");
                    new UpdateUtil(this, this).checkInstall("com.moxi.user");
                }
                break;
            case R.id.tv_re_load_recent_book:
                if (Utils.isSDCardCanReadAndWrite()) {
                    try {
                        Intent sound = new Intent();
                        sound.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ComponentName cnSound = new ComponentName("com.moxi.bookstore", "com.moxi.bookstore.activity.bookManager.MXBookStoreActivity");
                        sound.setComponent(cnSound);
                        sound.putExtra("flag_version_stu", "");
                        startActivity(sound);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toastor.showToast(this, "启动失败，请检测是否正常安装");
                        new UpdateUtil(this, this).checkInstall("com.moxi.bookstore");
                    }
                } else {
                    Toastor.showToast(this, "sd卡未准备好");
                }
                break;
            case R.id.wssc:
                startActivity("com.moxi.bookstore", "com.moxi.bookstore.activity.OnlieBookCityActivity");
                break;
            case R.id.sxbwl:
                startActivity("com.moxi.writeNote", "com.moxi.writeNote.MainActivity");
                break;
            case R.id.wjglq:
                startActivity("com.moxi.filemanager", "com.moxi.filemanager.FileManagerActivity");
                break;
            case R.id.rili:
                startActivity("com.moxi.calendar", "com.moxi.calendar.MainActivity");
                break;
            case R.id.yy:
                startActivity("com.onyx", "com.onyx.content.browser.activity.ApplicationsActivity");
                break;
            case R.id.zd:
                startActivity("com.onyx.dict", "com.onyx.dict.activity.DictMainActivity");
                break;
            case R.id.sz:
                startActivity("com.moxi.haierc", "com.moxi.haierc.activity.MXNewSettingActivity");
                break;
            case R.id.my_bookstore:
                try {
                    Intent sound = new Intent();
                    sound.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ComponentName cnSound = new ComponentName("com.moxi.bookstore", "com.moxi.bookstore.activity.bookManager.MXStacksActivity");
                    sound.setComponent(cnSound);
                    sound.putExtra("flag_version_stu", "");
                    startActivity(sound);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toastor.showToast(this, "启动失败，请检测是否正常安装");
                    new UpdateUtil(this, this).checkInstall("com.moxi.bookstore");
                }
                break;
            case R.id.continue_read:
                if (listRecent == null || listRecent.size() == 0) {
//                    onClick(wssc);
                    startActivity("com.moxi.bookstore", "com.moxi.bookstore.activity.OnlieBookCityActivity");
                } else {
                    continueReader(listRecent.get(0).getFilePath());
                }

                break;
            default:
                break;
        }
    }

    public void startActivity(String pakege, String classStr) {
        try {
            Intent calendar = new Intent();
            calendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ComponentName cnSound = new ComponentName(pakege, classStr);
            calendar.setComponent(cnSound);
            startActivity(calendar);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MoXiB1MainActivity.this, "启动失败，请检测是否正常安装", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
