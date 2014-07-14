package com.ushlanabazu.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by apleshakov on 11.07.2014.
 */
public class FileUtils {
    public static File getIdStorageDir(Context context, String dir) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), dir);
        if ((!file.exists()) && (!file.mkdirs())) {
            Log.e(CommonUtils.LOG_TAG, "Directory not created");
        }
        return file;
    }

    //проверка доступности SD
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //получить путь к файлу из uri
    public static String getRealPathFromURI(Context ct, Uri contentURI) {
        String result;
        Cursor cursor = ct.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
