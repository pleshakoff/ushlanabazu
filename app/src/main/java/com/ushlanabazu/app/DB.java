package com.ushlanabazu.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ushlanabazu.utils.CommonUtils;

public class DB {


    public static DB dbInstance = new DB();

    private static final String DB_NAME = "unbDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "unb_customers";

    public static final String COLUMN_ID = "_id";
    public static final int INDEX_PHOTO = 1;
    public static final String COLUMN_PHOTO = "photo";
    public static final int INDEX_TITLE = 2;
    public static final String COLUMN_TITLE = "title";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_PHOTO + " text, " +
                    COLUMN_TITLE + " text" +
                    ");";



    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public static DB getDbInstance() {
        return  dbInstance;
    }

    // открыть подключение
    public void open(Context ctx) {
        mDBHelper = new DBHelper(ctx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {

        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }


    // получить все данные из таблицы DB_TABLE
    public Cursor getRecordById(long id) {
        return mDB.query(DB_TABLE, null,"_id = ?",new String[] {String.valueOf(id)}, null, null, null);
    }


    // добавить запись в DB_TABLE
    public long addRec(String txt, String img) {
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, txt);
        cv.put(COLUMN_PHOTO, img);


        return mDB.insert(DB_TABLE, null, cv);
    }

    public void updRec(String id, String img) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PHOTO, img);

        int updCount = mDB.update(DB_TABLE, cv, "_id = ?",new String[] { id });
        Log.d(CommonUtils.LOG_TAG, "updated rows count = " + updCount);


    }


    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

//            ContentValues cv = new ContentValues();
//            for (int i = 1; i < 5; i++) {
//                cv.put(COLUMN_TITLE, "sometext " + i);
//                cv.put(COLUMN_PHOTO, R.drawable.babka);
//                db.insert(DB_TABLE, null, cv);
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}