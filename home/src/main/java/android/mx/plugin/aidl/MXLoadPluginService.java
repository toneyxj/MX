package android.mx.plugin.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;
import com.mx.home.activity.HomeActivity;
import com.mx.home.utils.AsyncCheckVersion;
import com.mx.mxbase.constant.Constant;
import com.mx.mxbase.utils.Log;

import java.io.File;

/**
 * 加载插件service
 * Created by Archer on 16/8/30.
 */
public class MXLoadPluginService extends Service {

    private LoadPlugin loadPlugin = new LoadPlugin();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return loadPlugin;
    }

    public class LoadPlugin extends ILoadPlugin.Stub {
        @Override
        public void loadPluginFile(String fileName) throws RemoteException {
            Log.e("测试aidl通讯", fileName);
            new HomeLoadPlugin(fileName).execute();
        }

        @Override
        public void selfRestaet() throws RemoteException {
            //重新安装更新插件
            File file = new File(Environment.getExternalStorageDirectory() + Constant.UPDATE_ZIP_PATH);
            File[] files = file.listFiles();
            if (files != null) {
                //预安装文件夹下面所有apk
                for (final File apk : files) {
                    new StartAsyncApp(apk).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
            //自动重启
            PackageManager pm = MXLoadPluginService.this.getPackageManager();
            Intent intent;
            while (true) {
                Log.e("while (true)", System.currentTimeMillis());
                intent = pm.getLaunchIntentForPackage(Constant.MAIN_PACKAGE);
                if (intent != null) {
                    break;
                }
            }
            Log.e("while (true)", "重启");
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        @Override
        public boolean checkVersion(int flag) throws RemoteException {
            if (flag == 0) {
                new AsyncCheckVersion(MXLoadPluginService.this).execute();
            } else {

            }
            return false;
        }
    }


    class StartAsyncApp extends AsyncTask<Void, Void, Void> {

        private File apk;
        private PackageManager pm;

        public StartAsyncApp(File apk) {
            pm = MXLoadPluginService.this.getPackageManager();
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


    class HomeLoadPlugin extends AsyncTask<Void, Void, Void> {

        private String name;

        public HomeLoadPlugin(String name) {
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            startLoadInner(name);
            return null;
        }
    }


    /**
     * 启动加载插件
     *
     * @param fileName 插件文件名称
     */
    private void startLoadInner(String fileName) {
        String positionPath = Environment.getExternalStorageDirectory() +
                Constant.UPDATE_ZIP_PATH + "/" + fileName;
        final File file = new File(positionPath);
        final PackageManager pm = this.getPackageManager();
        final PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), 0);
        new Thread() {
            @Override
            public void run() {
                doInstall(file, info);
            }
        }.start();
    }

    private void doInstall(File apk, PackageInfo info) {
        try {
            int re = PluginManager.getInstance().installPackage(apk.getAbsolutePath(), 0);
            PackageManager pm = this.getPackageManager();
            Log.e("packageName", info.packageName);
            Intent intent = pm.getLaunchIntentForPackage(info.packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
