package com.arpacell.cloid;

import com.arpacell.cloid.push.PushPrefsActivity;
import com.arpacell.cloid.utils.Screener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsDialogActivity extends Activity {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        Screener.fullScreen(this);

	        setContentView(R.layout.settings_dialog);
	        
	 }
	 
	 /** handler for cloud settingd */
	 public void setCloud(View v){
		 
		 Intent in = new Intent(this, SettingsActivity.class);
	     startActivity(in);
	 }
	 
	 /** handler for push settings */
	 public void setPush(View v){
		 
		 Intent in = new Intent(this, PushPrefsActivity.class);
	     startActivity(in);
	 }
}
