package com.ushlanabazu.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
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


public class EditItemActivity extends ActionBarActivity {

    private Customer customer = Customer.getCustomerInstance();
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;


    private ImageView mineImageView;
    private EditText name;
    private int mode;
    private long id;
    private String path = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mineImageView = (ImageView) findViewById(R.id.mainItemImageView);
        name = (EditText) findViewById(R.id.edit_item_name);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        setMode(intent.getIntExtra("mode", CommonUtils.MODE_NEW));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_item_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
            case R.id.action_cancel:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
            case R.id.action_accept:
                String name = ((EditText) findViewById(R.id.edit_item_name)).getText().toString();

                switch (mode) {
                    case CommonUtils.MODE_NEW:
                        id = customer.addRec(name, null);
                        break;
                    case CommonUtils.MODE_EDIT:
                        customer.delFile(id);
                        break;
                }
                path = BitmapUtils.loadImageViewIntofile(this, mineImageView, id);
                customer.updRec(String.valueOf(id), name, path);

                intent = new Intent();
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setMode(int mode) {

        this.mode = mode;
        switch (mode) {
            case CommonUtils.MODE_EDIT: {
                getSupportActionBar().setSubtitle(getString(R.string.mode_edit));

                refreshContent();
                break;
            }
            case CommonUtils.MODE_NEW: {
                getSupportActionBar().setSubtitle(getString(R.string.mode_new));

                break;
            }

        }
    }

    private void refreshContent() {
        Cursor c = customer.getRecordById(id);
        try {
            if (c != null) {
                if (c.moveToFirst()) {
                    path = c.getString(c.getColumnIndex(Customer.COLUMN_PHOTO));
                    if (path != null)
                        BitmapUtils.loadFileIntoImageView(path, mineImageView);
                    name.setText(c.getString(c.getColumnIndex(Customer.COLUMN_TITLE)));
                }
            }
        } finally {
            if (c != null) {
                c.close();
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

            case R.id.image_discard_button: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message_discard)
                        .setTitle(R.string.dialog_title_discard);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mineImageView.setImageDrawable(null);
                        path = null;
                    }
                });
                builder.setNegativeButton(R.string.cancel, null );
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }


            case R.id.mainItemImageView: {
                if (path != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                    startActivity(intent);
                }
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
