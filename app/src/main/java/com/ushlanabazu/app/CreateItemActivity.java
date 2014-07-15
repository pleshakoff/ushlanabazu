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
import com.ushlanabazu.ORM.Customer;
import com.ushlanabazu.utils.BitmapUtils;
import com.ushlanabazu.utils.CommonUtils;
import com.ushlanabazu.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateItemActivity extends ActionBarActivity {

    private Customer customer = Customer.getCustomerInstance();
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

//    private MenuItem asButtonAccept;
//    private MenuItem asButtonEdit;
//    private MenuItem asButtonCancel;


    private ImageView mineImageView;
    private int mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        mineImageView = (ImageView) findViewById(R.id.mainItemImageView);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", CommonUtils.MODE_NEW);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_item_activity_actions, menu);
        setMode(menu);
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

                long id = customer.addRec(name, null);
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
                        customer.updRec(String.valueOf(id), img);
                    } finally {
                        if (out != null)
                            try {
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

    private void setMode(Menu menu) {

        MenuItem asButtonAccept = menu.findItem(R.id.action_accept);
        MenuItem asButtonCancel = menu.findItem(R.id.action_cancel);
        MenuItem asButtonEdit = menu.findItem(R.id.action_edit);


        if (mode == CommonUtils.MODE_VIEW) {
            asButtonCancel.setVisible(false);
            asButtonAccept.setVisible(false);
            asButtonEdit.setVisible(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.mode_view));
        } else {
            asButtonCancel.setVisible(true);
            asButtonAccept.setVisible(true);
            asButtonEdit.setVisible(false);

            switch (mode) {
                case CommonUtils.MODE_EDIT: {
                    setTitle(getString(R.string.mode_edit));
                    break;
                }
                case CommonUtils.MODE_NEW: {
                    setTitle(getString(R.string.mode_new));

                    break;
                }

            }


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
//                break;
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
            }

        }
    }


}
