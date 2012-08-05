package com.arpacell.cloid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;

import android.os.Handler;

import com.arpacell.cloid.store.OperationException;
import com.arpacell.cloid.store.Storage;
import com.arpacell.cloid.store.StorageFactory;
import com.arpacell.cloid.utils.Screener;

import static com.arpacell.cloid.utils.MessageKeys.*;
import static com.arpacell.cloid.utils.Screener.*;

/**
 * User: Nomad
 * Date: 6/30/12
 * Time: 3:13 PM
 */
public class CloudActivity extends Activity {

    private TextView statusView;
    private ProgressBar bar;
    
    private volatile int operation;

    private int workCounter;
    private final int INCREMENT = 10;
    private final int COMPLETE = 100;

    private Handler barHandler = new Handler();
    private Thread barThread;

    /** Thread cancellation flag */
    private volatile boolean stop;

    /** Operation success flag */
     private volatile boolean success;
     
     /** Caught exception while calling stoage API*/
     private volatile String caughtError;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);
        setContentView(R.layout.progress);

        // 2nd layer of transparency for visibility
        LinearLayout lay = (LinearLayout)findViewById(R.id.progress);
        lay.setBackgroundColor(R.drawable.trans);

        Intent in = this.getIntent();
        String path = in.getStringExtra(FILE_PATH);
        operation = in.getIntExtra(TASK, -1);

        statusView = (TextView)findViewById(R.id.storing);
        statusView.setPadding(25, 5, 25, 5);
        statusView.setText( Screener.getlabel(this, operation) );

        bar = (ProgressBar) findViewById(R.id.bar);
        // start incrementing progress bar
        bar.incrementProgressBy( workCounter = INCREMENT );
        // dispatch
        doWork(path);
    }

    /** Handler for Cancel (Stop) button */
    public void stop(View v){
    	
        statusView.setText(R.string.stopping);

        if(barThread != null) {
            //  will not automatically interrupt the thread
            barThread.interrupt();
            //  set cancellation flag
            stop = true;
        }
        this.finish();
    }
    


    /** Task dispatcher */
    private void doWork(final String path){
    	
    	String status = statusView.getText().toString();
        if( "".equals(status) ){
        	Toast.makeText(this, R.string.no_op, Toast.LENGTH_SHORT).show();
            return;
        }        
        statusView.setText(status + Screener.getNameFromPath(path));

        // start progress bar worker thread
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                while (workCounter < COMPLETE && ! stop) {
                	
                    // process tasks
                	 success = execute(operation, path);
                	 
                    // Update the progress bar
                    barHandler.post(new Runnable() {
                    public void run() {
                      bar.setProgress(workCounter);
                    }
                    });                   
                }  // end loop
                
                // we're done
                if (workCounter >= 100) {
                    // sleep a second
                    // to make the completion visible
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    // close the progress bar dialog
                    // and report operation status
                    CloudActivity.this.goToStatus(success);
                }
            } // end run
        }; // end runner
        barThread = new Thread(runner);
        barThread.start();
    }


    /***/
    private boolean execute(int operation, String path){
    	
        incrementWorkCount(false);        
        try {
             Storage store = StorageFactory.getStorageInstance(this,
                          StorageFactory.Provider.AZURE_STORAGE);             
             incrementWorkCount(false); 
             
             switch(operation){    
             
               case UPLOAD :   store.uploadToStorage(path);
                               break;
               case BROWSE :   store.browseStorage();
                               break;
               case DOWNLOAD : store.downloadFromStorage(path);
                               // refresh Gallery
                               sendBroadcast( new Intent( Intent.ACTION_MEDIA_MOUNTED, 
                            		   Uri.parse("file://"+ Environment.getExternalStorageDirectory()) ) );

                               break;
               case DELETE :   store.deleteInStorage(path);
                               break;                                
             }			
		} 
        catch (OperationException e) {
        	recordError(e);
			return false;
		}
        incrementWorkCount(true);
        return true;
    }
    


    /**
     * Increment progress bar
     * either partially or to completion
     * */
    private void incrementWorkCount(boolean toCompletion){
    	
        if(toCompletion) {
            while(workCounter < COMPLETE){
               bar.incrementProgressBy(INCREMENT);
               workCounter += INCREMENT;
            }
        }
        else{
            if(workCounter < COMPLETE){
               bar.incrementProgressBy(INCREMENT);
               workCounter += INCREMENT;
            }
        }
    }

    
    /***/
    private void recordError(OperationException e){
    	
    	caughtError = e.getMessage();
    	if(e.getCause() != null){
    		caughtError += "\n" + e.getCause().getMessage();
    	}
    }

    /**
     * Status: success or failure
     * Don't report anything if job was cancelled by user
     * */
    private void goToStatus(boolean success){
    	
       this.finish();
       
       // for browsing, no need to go to status if successful
       if(operation == BROWSE && success){
    	   return;
       }
       
       if( ! stop){
          Intent in = new Intent(this, StatusActivity.class);
          in.putExtra(STATUS, success);
          in.putExtra(TASK, operation);
          if(caughtError != null){
        	  in.putExtra(ERR_MSG, caughtError);
          }
          startActivity(in);
       }

    }
}