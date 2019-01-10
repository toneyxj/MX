package com.moxi.handwritten.utils;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.moxi.handwritten.model.FloderBeen;
import com.mx.mxbase.constant.APPLog;
import com.moxi.handwritten.model.WriteFileBeen;
import com.mx.mxbase.utils.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 手写备忘录数据库操作类
 * Created by Administrator on 2016/8/2.
 */
public class WriteDataUtils {
    // 初始化类实列
    private static WriteDataUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static WriteDataUtils getInstance() {
        if (instatnce == null) {
            synchronized (WriteDataUtils.class) {
                if (instatnce == null) {
                    instatnce = new WriteDataUtils();
                }
            }
        }
        return instatnce;
    }
    /**
     * 历史记录文件
     */
    private final File dbFile = DataUtils.getDBPath("write");
    SQLiteDatabase sqlitedb =null;

    public List<WriteFileBeen> getDatas(String tableName, String fileNames) {
        CheckDb();
        List<WriteFileBeen> date = new ArrayList<WriteFileBeen>();
        date.add(new WriteFileBeen("-1", fileNames));
        createTable(tableName);
        Cursor cur = null;
        try {
//            sqlitedb.beginTransaction();

            cur = sqlitedb.rawQuery("select * from " + tableName + " order by createDate desc", null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    String type = cur.getString(0);//文件类型0代表文件夹，1代表文件,-1代表新增界面
                    String image = cur.getString(1);//type=1文件的保存图片本地地址
                    String fileName = cur.getString(2);//文件名
                    String createDate = cur.getString(3);//文件创建日期
                    String PathsOrName = cur.getString(4);//1文件的路径文件保存地址,0新建表名
//                    String foloderName = cur.getString(5);//1文件的路径文件保存地址,0新建表名

                    date.add(new WriteFileBeen(type, image, fileName, createDate, PathsOrName));
                }
            }

//            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            return date;
        } finally {
            if (cur!=null)
                cur.close();
//            sqlitedb.endTransaction();
        }
        return date;
    }
    public List<FloderBeen> getTableDatas(String tableName,String lastTable) {
        CheckDb();
        List<FloderBeen> date = new ArrayList<FloderBeen>();
        Cursor cur = null;
        try {
//            sqlitedb.beginTransaction();

            cur = sqlitedb.rawQuery("select * from " + tableName + " order by createDate desc", null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    String type = cur.getString(0);//文件类型0代表文件夹，1代表文件,-1代表新增界面
                    String image = cur.getString(1);//type=1文件的保存图片本地地址
                    String fileName = cur.getString(2);//文件名
                    String createDate = cur.getString(3);//文件创建日期
                    String PathsOrName = cur.getString(4);//1文件的路径文件保存地址,0新建表名
                    String lastTableName=lastTable;
                    if (type.equals("0")){
                        lastTableName+="/"+fileName;
                        date.add(new FloderBeen(lastTableName,PathsOrName));
                    }

                }
            }
//            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            return date;
        } finally {
            if (cur!=null)cur.close();
//            sqlitedb.endTransaction();
        }
        return date;
    }
    public List<WriteFileBeen> getDatas(String tableName) {
        CheckDb();
        List<WriteFileBeen> date = new ArrayList<WriteFileBeen>();
        Cursor cur = null;
        try {
//            sqlitedb.beginTransaction();

            cur = sqlitedb.rawQuery("select * from " + tableName + " order by createDate desc", null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    String type = cur.getString(0);//文件类型0代表文件夹，1代表文件,-1代表新增界面
                    String image = cur.getString(1);//type=1文件的保存图片本地地址
                    String fileName = cur.getString(2);//文件名
                    String createDate = cur.getString(3);//文件创建日期
                    String PathsOrName = cur.getString(4);//1文件的路径文件保存地址,0新建表名

                    date.add(new WriteFileBeen(type, image, fileName, createDate, PathsOrName));
                }
            }
//            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            return date;
        } finally {
            if (cur!=null)cur.close();
//            sqlitedb.endTransaction();
        }
        return date;
    }

    public void insertData(String tableName, String type, String image, String fileName, String createDate, String PathsOrName) {
        List<WriteFileBeen> beens = new ArrayList<>();
        beens.add(new WriteFileBeen(type, image, fileName, createDate, PathsOrName));
        insertData(tableName, beens);
    }
    public void insertData(String tableName, WriteFileBeen been) {
        List<WriteFileBeen> beens = new ArrayList<>();
        beens.add(been);
        insertData(tableName, beens);
    }

    public void insertData(String tableName, List<WriteFileBeen> list) {
        CheckDb();
        Cursor judgecur=null;
        try {
            sqlitedb.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                WriteFileBeen been=list.get(i);
                judgecur= sqlitedb.rawQuery("select * from "+tableName+" where fileName=?", new String[]{ been.fileName});
                int size = judgecur.getCount();
                APPLog.e("size" + size);
                if (size > 0) {
                    continue;
                }
                sqlitedb.execSQL("insert into "+tableName+" values(?,?,?,?,?)",
                        new String[]{been.type,been.image,been.fileName,been.createDate,been.PathsOrName});
            }


            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e(e.getMessage());
            createTable(tableName);
        } finally {
            if (judgecur!=null)  judgecur.close();
            sqlitedb.endTransaction();
        }
    }
    /**
     * 判断表中是否存在文件
     *
     * @param tableName
     * @param fileName
     * @return
     */
    public boolean judgeExist(String tableName, String fileName) {
        CheckDb();
        Cursor cur = null;
        try {
//            sqlitedb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            sqlitedb.beginTransaction();

            cur = sqlitedb.rawQuery("select * from "+tableName+" where fileName=?", new String[]{ fileName});
            int size = cur.getCount();
            if (size > 0) {
                return true;
            }

            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            createTable(tableName);
            return false;
        } finally {
            if (cur!=null)
            cur.close();
            sqlitedb.endTransaction();
        }
        return false;
    }
    public void deleteLinesData(String tableName,List<String> fileNames){
        CheckDb();
        try {
            sqlitedb.beginTransaction();
            String deleteTag="";
            for (int i = 0; i < fileNames.size(); i++) {
                if (i==fileNames.size()-1) {
                    deleteTag += " fileName='" + fileNames.get(i) + "'";
                }else {
                    deleteTag += " fileName='" + fileNames.get(i) + "' or";
                }
            }
            String sql="delete from " + tableName + " where"+deleteTag;
            APPLog.e(sql);
            sqlitedb.execSQL(sql);
            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e("删除数据",e.getMessage());
            createTable(tableName);
        } finally {
            sqlitedb.endTransaction();
        }
    }
    public void getAllFloderTableDelete(String tableName){
        CheckDb();
        try {
            sqlitedb.beginTransaction();
            List<WriteFileBeen> tableDatas=getDatas(tableName);
            for (WriteFileBeen been:tableDatas) {
                if (been.type.equals("0")){
                    getAllFloderTableDelete(been.PathsOrName);
                }
            }
            String sql="drop table "+tableName;
            APPLog.e(sql);
            sqlitedb.execSQL(sql);
            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e("删除表",e.getMessage());
            createTable(tableName);
        } finally {
            sqlitedb.endTransaction();
        }
    }
    public List<FloderBeen> getAllFloderTable(String tableName,String directory){
        List<FloderBeen> list=new ArrayList<>();
            List<FloderBeen> tableDatas=getTableDatas(tableName, directory);
            list.addAll(tableDatas);
            for (FloderBeen been:tableDatas) {
                list.addAll( getAllFloderTable(been.dbTableName,been.fileName));
            }
        return list;
    }
    /**
     * 将SD卡文件删除
     *
     * @param file 删除路径
     */
    public  void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }
    /**
     * 更新
     */
    public void updateDowlaod(String tableName, String fileName,String olderName) {
        CheckDb();
        try {
            sqlitedb.beginTransaction();
            String sq = String.format("update %s set fileName='%s' ,createDate='%s' where fileName=?", tableName, fileName,String.valueOf(System.currentTimeMillis()));
            sqlitedb.execSQL(sq,
                    new String[]{olderName});
            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e("删除数据", e.getMessage());
        } finally {
            sqlitedb.endTransaction();
        }
    }
    /**
     * 更新
     */
    public void updateImageDowlaod(String tableName, String fileName,String olderName){
        CheckDb();
        try {
            sqlitedb.beginTransaction();
                String sq=String.format("update %s set PathsOrName='%s' where PathsOrName=?",tableName,fileName);
                sqlitedb.execSQL(sq,
                        new String[]{olderName});
            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e("删除数据",e.getMessage());
        } finally {
            sqlitedb.endTransaction();
        }
    }
    /**
     * 更新
     */
    public void updateImagePathDowlaod(String tableName, String fileName,String olderName){
        CheckDb();
        try {
            sqlitedb.beginTransaction();
                String sq=String.format("update %s set image='%s',createDate='%s' where image=?",tableName,fileName,String.valueOf(System.currentTimeMillis()));
                sqlitedb.execSQL(sq,
                        new String[]{olderName});
            sqlitedb.setTransactionSuccessful();
        } catch (SQLException e) {
            APPLog.e("删除数据",e.getMessage());
        } finally {
            sqlitedb.endTransaction();
        }
    }

    public void createTable(String tableName) {
        createDBTable(dbFile, String.format(DataUtils.write, tableName));
    }
    public void closeDb(){
        if (sqlitedb!=null) {
            APPLog.e("执行了关闭数据库");
            sqlitedb.close();
            sqlitedb = null;
        }
    }
    private void CheckDb(){
        if (sqlitedb==null){
            createDBTable(dbFile,null);
        }
    }
    /**
     * 创建db
     *
     * @param DBfile db文件名
     * @param table  创建语句
     * @return 返回是否创建，true表示已存在，false表示不存在
     */
    private   boolean createDBTable(File DBfile, String table) {
        boolean save = true;
        if (!DBfile.exists()&&table!=null) {
            try {
                DBfile.createNewFile();
                save = false;
            } catch (IOException e) {
                APPLog.e("保存数据库", e.getMessage());
                return  false;
            }
        }
        if (sqlitedb==null) {
            sqlitedb = SQLiteDatabase.openOrCreateDatabase(DBfile, null);
        }
        if (table!=null)
        sqlitedb.execSQL(table);
        return save;
    }
}
