package com.arpacell.cloid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arpacell.cloid.utils.Screener;


import static com.arpacell.cloid.utils.MessageKeys.FILE_PATH;
import static com.arpacell.cloid.utils.MessageKeys.ITEM;
import static com.arpacell.cloid.utils.MessageKeys.ITEM_DESC;
import static com.arpacell.cloid.utils.MessageKeys.TASK;
import static com.arpacell.cloid.utils.Screener.DELETE;
import static com.arpacell.cloid.utils.Screener.DOWNLOAD;

/**
 * User: Nomad
 * Date: 7/1/12
 * Time: 6:37 PM
 */
public class DownloadActivity extends Activity {

    private TextView desc;
    private String fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);
        setContentView(R.layout.download);
        
        // underline go-to-gallery text -> link
        TextView galTxt = (TextView)findViewById(R.id.btnGallery);
        SpannableString content = new SpannableString( galTxt.getText().toString() );
        content.setSpan( new UnderlineSpan(), 0, content.length(), 0 );
        galTxt.setText(content);
        
        // from BrowseActivity
        Intent in = this.getIntent();
        fileName = in.getStringExtra(ITEM);
        String details = in.getStringExtra(ITEM_DESC);

        desc = (TextView)findViewById(R.id.downloadName);
        desc.setText( (details== null) ? fileName : details);
   
    }

    /** Close button handler */
    public void close(View v){
        this.finish();
    }

    /** Download button handler */
    public void download(View v) {
    	
        if(fileName.length() == 0){
            return;
        }
        Intent in = new Intent(this, CloudActivity.class);
        in.putExtra(FILE_PATH, fileName);
        in.putExtra(TASK, DOWNLOAD);
        startActivity(in);
    }
    
    /** Browse button handler */
    public void browse(View v){
    	
    	 Intent in = new Intent(this, BrowseActivity.class);
    	 startActivity(in);
    }
    
    
    /** Delete button handler */
    public void delete(View v){
    	
         if(fileName.length() == 0){
             return;
         }        
         Intent in = new Intent(this, DeleteActivity.class);
         in.putExtra(FILE_PATH, fileName);
         in.putExtra(TASK, DELETE);
         startActivity(in);
    }
    
    /** Go to Gallery handler */
    public void goToGallery(View v){
    	
    	Intent in = new Intent(this, GalleryActivity.class);
        startActivity(in);
    }

}