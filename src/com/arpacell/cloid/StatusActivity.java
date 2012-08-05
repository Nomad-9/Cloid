package com.arpacell.cloid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arpacell.cloid.utils.Screener;

import static com.arpacell.cloid.utils.MessageKeys.STATUS;
import static com.arpacell.cloid.utils.MessageKeys.ERR_MSG;
import static com.arpacell.cloid.utils.MessageKeys.TASK;
import static com.arpacell.cloid.utils.Screener.DELETE;

/**
 * User: Nomad
 * Date: 7/5/12
 * Time: 9:35 PM
 */
public class StatusActivity extends Activity {
	
	private int operation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);
        setContentView(R.layout.status);
        // 2nd layer of transparency for visibility
        LinearLayout lay = (LinearLayout)findViewById(R.id.result);
        lay.setBackgroundColor(R.drawable.trans);

        Intent in = this.getIntent();
        boolean success = in.getBooleanExtra(STATUS, false);
        String err_msg = in.getStringExtra(ERR_MSG);
        if(err_msg == null){
        	err_msg = "";
        }  
        operation = in.getIntExtra(TASK, -1);
        String opLabel = Screener.getlabel(this, operation);
        
        String successStr = opLabel + getString(R.string.success);
        String failStr =  opLabel + getString(R.string.error) + "\n\n" + err_msg;
        
        TextView statusView = (TextView)findViewById(R.id.status);
        statusView.setText( success ? successStr : failStr);
        statusView.setTextColor( success ? Color.WHITE : Color.YELLOW );
        
    }
    


    
	/** Handler for close button */
    public void close(View v) {
    	// if we were deleting, go back directly to list view and refresh
    	if(operation == DELETE){
    		 Intent in = new Intent(this, BrowseActivity.class);
             in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(in);
    	}
        this.finish();
    }

}