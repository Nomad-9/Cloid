package com.arpacell.cloid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arpacell.cloid.utils.Screener;

import static com.arpacell.cloid.utils.MessageKeys.FILE_PATH;
import static com.arpacell.cloid.utils.MessageKeys.TASK;
import static com.arpacell.cloid.utils.Screener.UPLOAD;

/**
 * User: Nomad
 * Date: 6/30/12
 * Time: 1:48 PM
 */
public class GalleryActivity extends Activity {

    private ImageView img;
    private static final int IMG_REQUEST_CODE = 1;
    private String path;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);
        //System.gc(); // get the most amt of mem at start
        setContentView(R.layout.gallery);
        img = (ImageView) findViewById(R.id.storage);

        // start Android Gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivityForResult(intent, IMG_REQUEST_CODE);

        // cancel button
        Button canBtn = (Button) findViewById(R.id.btnCancel);
        canBtn.setBackgroundColor(R.drawable.trans);
        Button putBtn = (Button) findViewById(R.id.btnSave);
        putBtn.setBackgroundColor(R.drawable.trans);

    }

    @Override
    public void onResume(){

    	super.onResume();
    	//this.finish();
    }

    /** Handler for view Gallery button */
    public void viewGallery(View v)  {
    	
        // close this screen
        this.finish();
        // restart fresh
        Intent in = new Intent(this, GalleryActivity.class);
        startActivityForResult(in, IMG_REQUEST_CODE);
    }

    /** Handler for Save button */
    public void save(View v)  {
    	
       if(path == null){
          this.finish();
          return;
       }
       Intent in = new Intent(this, CloudActivity.class);
       in.putExtra(FILE_PATH, path);
       in.putExtra(TASK, UPLOAD);
       startActivity(in);
    }


    /**
     *   Called once an Image is selected
     * */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (resultCode != RESULT_OK || requestCode != IMG_REQUEST_CODE) {
              return; // don't care
        }
        Uri selectedImageUri = data.getData();

        String[] pathColumn = { MediaStore.Images.Media.DATA };
        // managedQuery will close the cursor when needed
        // NOTE: method deprecated as of version 3.0
        // TODO use a CursorLoader  when moving to upper versions
        Cursor cursor = managedQuery(selectedImageUri, pathColumn, null, null, null);
        if(cursor == null){
            Toast.makeText(this, R.string.img_db_error, Toast.LENGTH_SHORT).show();
            return;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        // path to pass along next activity
        path = cursor.getString(column_index);

        Bitmap bmp = BitmapFactory.decodeFile(path);
        img.setImageBitmap( bmp );
        // Scale the image uniformly on full screen
        img.setScaleType(ImageView.ScaleType.CENTER_CROP );
    }

}