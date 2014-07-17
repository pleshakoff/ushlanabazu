package com.ushlanabazu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apleshakov on 11.07.2014.
 *
 *
 *
 */



public class BitmapUtils {


    static final int DEF_SAMPLE_SIZE = 2;


    //изменение размера картинки под view
    public static int calculateInSampleSize(
            BitmapFactory.Options options, ImageView view) {
        // Raw height and width of image
        int reqWidth = view.getWidth();
        int reqHeight = view.getHeight();

        if ((reqWidth != 0) && (reqHeight != 0)) {
            final int height = options.outHeight;
            final int width = options.outWidth;

            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;

        } else {
            return DEF_SAMPLE_SIZE;
        }
    }

    //помещение файла в imageView
    public static void loadFileIntoImageView(String path, ImageView view) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, view);
        options.inJustDecodeBounds = false;

        Bitmap btmp = BitmapFactory.decodeFile(path, options);


        int angel = getOrentation(path);


        if (angel > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angel);
            btmp = Bitmap.createBitmap(btmp, 0, 0, btmp.getWidth(), btmp.getHeight(), matrix, true);
        }


        view.setImageBitmap(btmp);
    }

    private static int getOrentation(String path) {
        int angel = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(CommonUtils.LOG_TAG, e.getMessage());
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    angel = 90;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180: {
                    angel = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270: {
                    angel = 270;
                    break;
                }
            }
        }
        return angel;
    }

    public static String loadImageViewIntofile(Context ct, ImageView view, Long id) {
        String img = null;
        if (view.getDrawable() != null) {
            FileOutputStream out = null;
            img = ct.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + id + "_" + (new SimpleDateFormat("ddMMyyyHHmmss").format(new Date())) + ".jpg";
            try {
                try {
                    Log.d(CommonUtils.LOG_TAG, String.valueOf(ct.getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                    out = new FileOutputStream(img);
                    Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        return img;
    }


}
