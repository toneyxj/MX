package com.mx.reader.readerprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Archer on 16/10/25.
 */
public class LastReaderProvider extends ContentProvider {

    DBHelper dbHelper;
    SQLiteDatabase db;

    final static int DB_VERSION = 3;
    final static String DB_NAME = "mxUSER";
    final static String READER_TABLE = "last_read";    //table name

    final static String FILE_ID = "_id";            //table field _id
    final static String FILE_PATH = "filepath";         //table field user name
    final static String USER_TOKEN = "usertoken";       //用户token
//    final static String READ_TIME = "readtime";     //最近阅读时间
//    final static String READ_PAGE = "readpage";     //阅读页码

    final static String DB_CREATE = "create table " +   //create table script
            READER_TABLE + "(" +
            FILE_ID + " integer primary key autoincrement, " +
            FILE_PATH + " text, " +
            USER_TOKEN + " text" +
            ");";

    //Uri const
    final static String AUTHORITY = "com.moxi.providers.moxiread";
    final static String PATH = "mxread";

    public final static Uri READ_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    //MIME-type
    final static String READ_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;
    final static String READ_CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH;
    //UriMatcher
    final static int READ_FILES = 1;
    final static int READ_FILE_ID = 2;

    private final static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, READ_FILES);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", READ_FILE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case READ_FILES:
                Log.e("READ_FILES", "READ_FILES");
                //Default order by name, unless otherwise specified
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = FILE_PATH + " ASC";
                }
                break;
            case READ_FILE_ID:
                //Getting id from path
                String id = uri.getLastPathSegment();
                Log.e("READ_FILE_ID", "READ_FILE_ID: " + id);
                if (TextUtils.isEmpty(selection)) {
                    //Where _id = id
                    selection = FILE_ID + " = " + id;
                } else {
                    selection = FILE_ID + " AND " + selection + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(READER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), READ_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case READ_FILES:
                return READ_CONTENT_TYPE;
            case READ_FILE_ID:
                return READ_CONTENT_ITEMTYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uriMatcher.match(uri) != READ_FILES) {
            throw new IllegalArgumentException("Wrong uri: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        long rowId = db.insert(READER_TABLE, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(READ_CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case READ_FILES:
                Log.e("READ_FILES", "READ_FILES");
                selection = null;
                break;
            case READ_FILE_ID:
                String id = uri.getLastPathSegment();
                Log.e("READ_FILE_ID: ", id + "");
                if (TextUtils.isEmpty(selection)) {
                    selection = FILE_ID + " = " + id;
                } else {
                    selection = FILE_ID + " AND " + selection + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int count = db.delete(READER_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case READ_FILES:
                Log.e("READ_FILES", "READ_FILES");
                break;
            case READ_FILE_ID:
                String id = uri.getLastPathSegment();
                Log.e("READ_FILES: ", id + "");
                if (TextUtils.isEmpty(selection)) {
                    selection = FILE_ID + " = " + id;
                } else {
                    selection = FILE_ID + " AND " + selection + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int count = db.update(READER_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
