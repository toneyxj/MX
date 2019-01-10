package com.mx.home.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.mx.plugin.aidl.ILoadPlugin;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;
import com.mx.home.activity.HomeActivity;
import com.mx.home.activity.MXUpdateActivity;
import com.mx.home.view.MXUpdateDialog;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.ActivitysManager;
import com.mx.mxbase.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Archer on 16/9/2.
 */
public class UnzipFromFile {

    public static UnzipFromFile getInstance() {
        return new UnzipFromFile();
    }

    private Context unZipcontext;

    /**
     * 解压文件到目标目录
     *
     * @param context
     * @param fileName
     * @param outputDirectory
     * @param isReWrite
     * @param dialog
     */
    public void unzip(Context context, String fileName, String outputDirectory
            , MXUpdateDialog dialog, boolean isReWrite) throws IOException {
        unZipcontext = context;
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = new FileInputStream(new File(fileName));
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        dialog.setMsg("解压完成正在安装....");
        startLauncher(context);
//        startCheckVersion();
    }


    private void startLauncher(final Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + Constant.UPDATE_ZIP_PATH);
        File[] files = file.listFiles();
        if (files != null) {
            //预安装文件夹下面所有apk
            for (final File apk : files) {
                new StartAsyncApp(apk, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        //自动重启
        PackageManager pm = context.getPackageManager();
        Intent intent;
        while (true) {
            Log.e("while (true)", System.currentTimeMillis());
            intent = pm.getLaunchIntentForPackage(Constant.MAIN_PACKAGE);
            if (intent != null) {
                break;
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("while (true)", "重启");
                Intent home = new Intent(context, HomeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivitysManager.getAppManager().finishActivity(MXUpdateActivity.class);
                context.startActivity(home);
            }
        }, 30000);

//        ActivitysManager.getAppManager().finishActivity(HomeActivity.class);
    }


    class StartAsyncApp extends AsyncTask<Void, Void, Void> {

        private File apk;
        private PackageManager pm;
        private Context context;

        public StartAsyncApp(File apk, Context context) {
            this.context = context;
            pm = context.getPackageManager();
            this.apk = apk;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
//                try {
//                    PluginManager.getInstance().deletePackage(info.packageName, 0);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                try {
                    int re = PluginManager.getInstance().installPackage(apk.getAbsolutePath(), PackageManagerCompat.INSTALL_REPLACE_EXISTING);
                    Log.e("update", apk.getName() + re);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private static final String ACTION_BIND_SERVICE = "android.mx.aidl.LOAD_PLUGIN";
    private ILoadPlugin iLoadPlugin;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iLoadPlugin = ILoadPlugin.Stub.asInterface(iBinder);
            try {
                iLoadPlugin.selfRestaet();
                if (iLoadPlugin != null) {
                    unZipcontext.unbindService(mServiceConnection);
                    iLoadPlugin = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iLoadPlugin = null;
        }
    };

    private void startCheckVersion() {
        Intent intentService = new Intent(ACTION_BIND_SERVICE);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        unZipcontext.bindService(intentService, mServiceConnection, unZipcontext.BIND_AUTO_CREATE);
    }
}