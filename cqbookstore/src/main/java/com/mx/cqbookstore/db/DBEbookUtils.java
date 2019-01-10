package com.mx.cqbookstore.db;

import android.database.Cursor;

import com.mx.cqbookstore.http.bean.DBebookbean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源记录工具类
 * Created by Administrator on 2016/9/30.
 */
public class DBEbookUtils {

    /**
     * 获得历史立即集合
     *
     * @return
     */
    public static List<DBebookbean> getDBBooks() {
        Cursor cursor = DataSupport.findBySQL("select * from DBebookbean order by readTime desc");
        List<DBebookbean> listEvents = new ArrayList<>();
        while (cursor.moveToNext()) {
            DBebookbean book = new DBebookbean();
            book.id= cursor.getLong(0);
            book.author = cursor.getString(1);
            book.bookCover=cursor.getString(2);
            book.bookName = cursor.getString(3);
            book.resouceId=cursor.getString(4);
            book.downUrl=cursor.getString(5);
            book.downprogress=cursor.getDouble(6);
            book.readprogress=cursor.getString(8);
            book.isBought=cursor.getInt(9);
            book.path=cursor.getString(10);
            book.pubTime=cursor.getString(11);
            book.publisher=cursor.getString(12);
            book.readTime=cursor.getLong(13);
            book.downTime=cursor.getLong(14);
            book.epubKey=cursor.getString(7);
            listEvents.add(book);
        }
        if (cursor != null)
            cursor.close();

        return listEvents;
    }

    /**
     * 插入book
     * @param book
     */
    public static void addBook(DBebookbean book){
        book.saveThrows();
    }

    /**
     * 删除指定(resouceId)book
     * @param book
     */
    public static void deletBook(DBebookbean book){
        DataSupport.deleteAll(book.getClass(),"resouceId=?",book.resouceId);
    }

    /**
     *更新
     * @param book
     */
    public static void updataBought(DBebookbean book){
       book.updateAll("resouceId=?",book.resouceId);
    }

    public static DBebookbean selectBook(String id){
        Cursor cursor=DataSupport.findBySQL("select * from DBebookbean where resouceId=?",id);
        List<DBebookbean> listEvents = new ArrayList<>();
        while (cursor.moveToNext()) {
            DBebookbean book = new DBebookbean();
            book.id= cursor.getLong(0);
            book.author = cursor.getString(1);
            book.bookCover=cursor.getString(2);
            book.bookName = cursor.getString(3);
            book.resouceId=cursor.getString(4);
            book.downUrl=cursor.getString(5);
            book.downprogress=cursor.getDouble(6);
            book.readprogress=cursor.getString(8);
            book.isBought=cursor.getInt(9);
            book.path=cursor.getString(10);
            book.pubTime=cursor.getString(11);
            book.publisher=cursor.getString(12);
            book.readTime=cursor.getLong(13);
            book.downTime=cursor.getLong(14);
            book.epubKey=cursor.getString(7);
            listEvents.add(book);
        }
        if (cursor != null)
            cursor.close();
        if (listEvents.size()>0)
            return listEvents.get(0);
        else
            return null;
    }
}
