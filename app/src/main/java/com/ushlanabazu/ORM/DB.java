package com.ushlanabazu.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {


    public static DB dbInstance = new DB();

    private static final String DB_NAME = "unbDB";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE =
            "create table " + Customer.DB_TABLE_CUSTOMERS + "(" +
                    Customer.COLUMN_ID + " integer primary key autoincrement, " +
                    Customer.COLUMN_PHOTO + " text, " +
                    Customer.COLUMN_TITLE + " text" +
                    ");";

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public static DB getDbInstance() {
        return  dbInstance;
    }

    public  SQLiteDatabase getSQLiteDatabase() {
        return  mDB;
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

    // получить все данные из таблицы DB_TABLE_CUSTOMERS

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
//                db.insert(DB_TABLE_CUSTOMERS, null, cv);
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}