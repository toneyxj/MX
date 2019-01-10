package com.moxi.assistbooklist.controler;

import android.os.Handler;
import android.os.Message;

import com.moxi.assistbooklist.mode.BookStoreFile;
import com.mx.mxbase.constant.PhotoConfig;
import com.mx.mxbase.utils.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Created by xj on 2017/6/15.
 */

public class ScanReadFile implements Runnable {
    private List<String> fileTypes= PhotoConfig.getAllFileType();
    private int size=0;

    private boolean isRun=true;

    private ScanReadListner listner;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (listner==null)return;
            switch (msg.what){
                case 0:
//                    listner.onScanReading();
                    break;
                case 1:
                    listner.onScanReadFile((BookStoreFile) msg.obj);
                    break;
                case 2:
                    listner.onScanReadEnd();
                    break;
            }
        }
    };

    public void setRun(boolean run) {
        isRun = run;
    }

    public ScanReadFile(ScanReadListner listner) {
        this.listner=listner;
    }

    private void searchFile(String path) {
        File root=new File(path);
        if (!isRun||!root.exists()) return ;
        String rootPath = root.getAbsolutePath();
        String ex1 = StringUtils.getSDCardPath() + "/mx_exams";
        String ex2 = StringUtils.getSDCardPath() + "/exams";
        String ex3 = StringUtils.getSDCardPath() + "/Exams";

        if (rootPath.equals(ex1) || rootPath.equals(ex2) || rootPath.equals(ex3)||rootPath.contains("-Exported.pdf")) {
            return ;
        }
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!isRun) return;
            if (file.isDirectory()) {//判断是否为文件夹并为keduwenjian
                if ( file.canRead())
                searchFile(file.getAbsolutePath());
            } else {
                if ( !file.canRead())return;
                String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                prefix = prefix.toLowerCase();
                if (fileTypes.contains(prefix)) {
                    BookStoreFile file1 = SacnReadFileUtils.getInstance(null).saveMode(file.getAbsolutePath());
                    if (file1 != null) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = file1;
                        handler.sendMessage(message);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(0);
                searchFile("/mnt/sdcard");
                searchFile("/mnt/extsd");
        handler.sendEmptyMessage(2);
        }

   public interface ScanReadListner{
        void onScanReadEnd();

        /**
         * 读取了一个文件
         * @param file
         */
        void onScanReadFile(BookStoreFile file);
    }
}
