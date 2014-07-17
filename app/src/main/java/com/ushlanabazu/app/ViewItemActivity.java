package com.ushlanabazu.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.ushlanabazu.ORM.Customer;
import com.ushlanabazu.utils.BitmapUtils;
import com.ushlanabazu.utils.CommonUtils;


public class ViewItemActivity extends ActionBarActivity {

    private Customer customer = Customer.getCustomerInstance();

    private ImageView mineImageView;
    private EditText name;
    private long id;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        name = (EditText) findViewById(R.id.edit_item_name);
        name.setFocusable(false);
        mineImageView = (ImageView) findViewById(R.id.mainItemImageView);
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(getString(R.string.mode_view));
        findViewById(R.id.image_buttons_layout).setVisibility(View.GONE);

        refreshContent();
    }

    private void refreshContent() {
        Cursor c = customer.getRecordById(id);
        try
        {
            if (c != null) {
                if (c.moveToFirst()) {
                    path = c.getString(c.getColumnIndex(Customer.COLUMN_PHOTO));
                    if (path != null)
                        BitmapUtils.loadFileIntoImageView(path, mineImageView);
                    else
                        mineImageView.setImageDrawable(null);

                    name.setText(c.getString(c.getColumnIndex(Customer.COLUMN_TITLE)));
                }
            }}
        finally {
            if (c!=null) {
                c.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_item_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_edit: {
                Intent intent = new Intent(this, EditItemActivity.class);
                intent.putExtra("mode", CommonUtils.MODE_EDIT);
                intent.putExtra("id",id);
                startActivityForResult(intent, CommonUtils.MODE_EDIT);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mainItemImageView: {
                if (path != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CommonUtils.MODE_EDIT:
                    refreshContent();
                    Toast.makeText(this, "Изменено ", Toast.LENGTH_SHORT).show();
                    break;
                }
            }


    }


}
