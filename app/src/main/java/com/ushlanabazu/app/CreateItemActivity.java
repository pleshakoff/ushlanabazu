package com.ushlanabazu.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.ushlanabazu.utils.BitmapUtils;
import com.ushlanabazu.utils.CommonUtils;
import com.ushlanabazu.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apleshakov on 25.06.2014.
 */
public class CreateItemActivity extends ActionBarActivity {

    private final DB db = DB.getDbInstance();
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ImageView mineImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        mineImageView = (ImageView) findViewById(R.id.mainItemImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_item_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_edit:
                return true;
            case R.id.action_accept:

                String name = ((EditText) findViewById(R.id.create_itemm_name)).getText().toString();

                long id = db.addRec(name, null);
                if ((id != -1) && (mineImageView.getDrawable() != null)) {
                    FileOutputStream out = null;
                    String img = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + id + "_" + (new SimpleDateFormat("ddMMyyyHHmmss").format(new Date())) + ".jpg";
                    try {
                        try {
                            Log.d(CommonUtils.LOG_TAG, String.valueOf(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                            out = new FileOutputStream(img);
                            Bitmap bitmap = ((BitmapDrawable) mineImageView.getDrawable()).getBitmap();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        db.updRec(String.valueOf(id), img);
                    } finally {
                        if (out!= null)
                        try {
                          if (out != null)
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.get_image_from_galery_button: {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                break;
            }

            case R.id.get_image_from_camera_button: {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }


            case R.id.mainItemImageView: {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse("file://" + "/sdcard/test.jpg"), "image/*");
//                startActivity(intent);
                Toast.makeText(this, "under construction" , Toast.LENGTH_SHORT).show();
                break;
            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                case REQUEST_IMAGE_CAPTURE: {
                    super.onActivityResult(requestCode, resultCode, data);
                    String path = FileUtils.getRealPathFromURI(this, data.getData());
                    BitmapUtils.loadFileIntoImageView(path, mineImageView);
                    break;
                }

//                case REQUEST_IMAGE_CAPTURE : {
//                    super.onActivityResult(requestCode, resultCode, data);
//                    String path = FileUtils.getRealPathFromURI(this, data.getData());
//                    BitmapUtils.loadFileIntoImageView(path, mineImageView);
//
////                    Bundle extras = data.getExtras();
////                    Bitmap imageBitmap = (Bitmap) extras.get("data");
////                    mineImageView.setImageBitmap(imageBitmap);
//
////                    String path = Utils.getRealPathFromURI(this, data.getData());
////                    Utils.loadFileIntoImageView(path, mineImageView);
//                    break;
//                }




            }

        }
    }


}
