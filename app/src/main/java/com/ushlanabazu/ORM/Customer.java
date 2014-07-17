package com.ushlanabazu.ORM;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import com.ushlanabazu.utils.CommonUtils;

import java.io.File;


public class Customer {
    public static final String COLUMN_ID = "_id";
    public static final int INDEX_PHOTO = 1;
    public static final String COLUMN_PHOTO = "photo";
//    public static final int INDEX_TITLE = 2;
    public static final String COLUMN_TITLE = "title";
    static final String DB_TABLE_CUSTOMERS = "unb_customers";

    private static Customer customerInstance = new Customer();


    public static Customer getCustomerInstance() {
        return  customerInstance;
    }


    public Cursor getAllData() {
        return DB.getDbInstance().getSQLiteDatabase().query(Customer.DB_TABLE_CUSTOMERS, null, null, null, null, null, null);
    }


    // получить все данные из таблицы DB_TABLE_CUSTOMERS
    public Cursor getRecordById(long id) {
        return DB.getDbInstance().getSQLiteDatabase().query(Customer.DB_TABLE_CUSTOMERS, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
    }


    // добавить запись в DB_TABLE_CUSTOMERS
    public long addRec(String name, String img) {
        ContentValues cv = new ContentValues();

        cv.put(Customer.COLUMN_TITLE, name);
        cv.put(Customer.COLUMN_PHOTO, img);


        return DB.getDbInstance().getSQLiteDatabase().insert(Customer.DB_TABLE_CUSTOMERS, null, cv);
    }

    public void updRec(String id, String name,String img) {

        ContentValues cv = new ContentValues();
        cv.put(Customer.COLUMN_TITLE, name);
        cv.put(Customer.COLUMN_PHOTO, img);

        int updCount = DB.getDbInstance().getSQLiteDatabase().update(Customer.DB_TABLE_CUSTOMERS, cv, "_id = ?", new String[]{id});
        Log.d(CommonUtils.LOG_TAG, "updated rows count = " + updCount);
    }


    // удалить запись из DB_TABLE_CUSTOMERS
    public void delRec(long id) {
        delFile(id);
        DB.getDbInstance().getSQLiteDatabase().delete(Customer.DB_TABLE_CUSTOMERS, Customer.COLUMN_ID + " = " + id, null);
    }

    public String getPathById(long id) {
        String path="";
        Cursor c = getRecordById(id);
        try
        {
            if (c != null) {
                if (c.moveToFirst()) {
                    path = c.getString(c.getColumnIndex(Customer.COLUMN_PHOTO));
                }
            }}
        finally {
           if (c!=null) {
               c.close();
           }
        }
        return path;
    }

   public void delFile(long id) {
        // извлекаем id записи и удаляем соответствующую запись в БД
        String patch = getPathById(id);
        if (patch!=null) {
            File f = new File(patch);
            if ((f!=null)&&(f.exists()))
                f.delete();
            else
                Log.e(CommonUtils.LOG_TAG, "Patch not found: "+patch);
        }
        else
            Log.e(CommonUtils.LOG_TAG, "Patch is null");

    }






}
