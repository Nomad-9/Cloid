package com.arpacell.cloid;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.arpacell.cloid.utils.Screener.DELETE;
import static com.arpacell.cloid.utils.MessageKeys.FILE_PATH;
import static com.arpacell.cloid.utils.MessageKeys.TASK;

import com.arpacell.cloid.utils.Screener;

public class DeleteActivity extends Activity {
	
	 private String fileName;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
	     super.onCreate(savedInstanceState);
	     Screener.fullScreen(this);
	     setContentView(R.layout.delete);
	     
	     // 2nd layer of transparency for visibility
	     LinearLayout lay = (LinearLayout)findViewById(R.id.dialog_confirm);
	     lay.setBackgroundColor(R.drawable.trans);
	     
	     Intent in = this.getIntent();
	     fileName = in.getStringExtra(FILE_PATH);
	     
	     // display filename at R.string.delete_confirm place holder
	     String confirm_msg = getString(R.string.delete_confirm);
	     String txt = String.format(confirm_msg, fileName);
	     
	     TextView confirmV = (TextView) findViewById(R.id.confirm);
	     confirmV.setText(txt);
	     confirmV.setTypeface(Typeface.DEFAULT_BOLD);
	 }
	 
	 /** handler for cancel button */
	 public void cancel(View v){
		 this.finish();
	 }
	 
	 /** handler for confirm delete button */
	 public void delete(View v){
		 
		    if(fileName == null || fileName.length() == 0){
	            return;
	        }
		    Intent in = new Intent(DeleteActivity.this, CloudActivity.class);
	        in.putExtra(FILE_PATH, fileName);
	        in.putExtra(TASK, DELETE);
	        startActivity(in);	
	        this.finish();
	 }

}
