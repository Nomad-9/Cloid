package com.arpacell.cloid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arpacell.cloid.push.ServerUtilities;
import com.arpacell.cloid.store.CloudPreferences;
import com.arpacell.cloid.utils.Screener;
import com.google.android.gcm.GCMRegistrar;

// GCM
import static com.arpacell.cloid.push.Pusher.PUSH_ENABLED;
import static com.arpacell.cloid.push.Pusher.SERVER_URL;
import static com.arpacell.cloid.push.Pusher.SENDER_ID;
import static com.arpacell.cloid.push.Pusher.DISPLAY_MESSAGE_ACTION;
import static com.arpacell.cloid.push.Pusher.EXTRA_MESSAGE;



public class MainActivity extends Activity {

	TextView pushStatus;
	LinearLayout laymenu;
	
	//GCM
	 AsyncTask<Void, Void, Void> registerTask;
	 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);

        setContentView(R.layout.main);
        
        // underline settings text -> link
        TextView settingsTxt = (TextView)findViewById(R.id.btnSet);
        SpannableString content = new SpannableString( settingsTxt.getText().toString() );
        content.setSpan( new UnderlineSpan(), 0, content.length(), 0 );
        settingsTxt.setText(content);
        
        pushStatus = (TextView) findViewById(R.id.title);
        laymenu = (LinearLayout) findViewById(R.id.menu); 
        
        registerReceiver(handleMessageReceiver,
                new IntentFilter(DISPLAY_MESSAGE_ACTION));
       
        processPush();
    }
    
    /***/
    @Override
    public void onResume(){
    	
    	super.onResume();
    	turnOnScreen();   
    	processPush();
    }
    


    /** Don't allow access if preferences are not set */
    public void download(View v) {
      
        Intent in = new Intent(this, BrowseActivity.class);
        startActivity(in);
        turnOffScreen();
    }

    /** Don't allow access if preferences are not set */
    public void upload(View v) {
    
        Intent in = new Intent(this, GalleryActivity.class);
        startActivity(in);
    }

    /** bring up preferences dialog*/
    public void settingsDialog(View v) {  	
    	
    	turnOffScreen();
    	Intent in = new Intent(MainActivity.this, SettingsDialogActivity.class);
	    startActivity(in);
    }
    
    
    /***/
	private void turnOffScreen(){
	
		pushStatus.setVisibility(View.INVISIBLE);
		laymenu.setVisibility(View.INVISIBLE);	
	}
	
	/***/
	private void turnOnScreen(){
		
		pushStatus.setVisibility(View.VISIBLE);
		laymenu.setVisibility(View.VISIBLE);
	}
	
	
	/* GCM **********************************************************************************************/
	
	
	/**
	 * Enable user to register/unregister for push notifications
	 *  1. Register user if all fields in prefs are filled AND flag is set
     *  2. Un-register if flag is un-set and user is registered
	 *  
	 * */
    private void processPush(){
    	
        if( checkPushPrefs() && PUSH_ENABLED ){
    	   register();
        }
        if(! PUSH_ENABLED && GCMRegistrar.isRegisteredOnServer(this) ){
        	 GCMRegistrar.unregister(this);
        }
    }
    
	
	/** Server Push preferences */
    private boolean checkPushPrefs(){
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	String name     =  prefs.getString("sname", "");
    	String ip       =  prefs.getString("sip", "");
    	String port     = prefs.getString("sport", "");
    	String senderID =  prefs.getString("sid", "");
    	PUSH_ENABLED    = prefs.getBoolean("enable", false);
    	
    	boolean allFilled = Screener.checkAllFilled(name, ip, port, senderID); 
    	if( allFilled ){
    		SENDER_ID = senderID;
    		SERVER_URL = "http://" + ip + ":" + port + "/" + name;
    		//Toast.makeText(this, "Server Push URL: " + SERVER_URL, Toast.LENGTH_LONG).show();
    	}   	
    	return allFilled;
    }
    
	
	/***/
	 private final BroadcastReceiver handleMessageReceiver =
	            new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            pushStatus.setText(newMessage + "\n");
	        }
	    };
	    
	 
	    /***/
	    private void register(){
	    	    	
	    	 // Make sure the device has the proper dependencies.
	        GCMRegistrar.checkDevice(this);
	        
	        // Make sure the manifest was properly set - comment out this line
	        // while developing the app, then uncomment it when it's ready.
	        GCMRegistrar.checkManifest(this);
	        
	        final String regId = GCMRegistrar.getRegistrationId(this);	        
	        if ("".equals(regId)) {
	            GCMRegistrar.register(this, SENDER_ID);
	        } 
	        else {
	            // Device is already registered on GCM, check server.
	            if (GCMRegistrar.isRegisteredOnServer(this)) {
	                // Skip registration.	            	
	            	 pushStatus.setText( (getString(R.string.already_registered) + "\n") );
	            } 
	            else {
	                // Try to register again, but not in the UI thread.
	                // It's also necessary to cancel the thread onDestroy(),
	                // hence the use of AsyncTask instead of a raw thread.
	                final Context context = this;
	                registerTask = new AsyncTask<Void, Void, Void>() {

	                    @Override
	                    protected Void doInBackground(Void... params) {
	                        boolean registered =
	                                ServerUtilities.register(context, regId);
	                        // At this point all attempts to register with the app
	                        // server failed, so we need to unregister the device
	                        // from GCM - the app will try to register again when
	                        // it is restarted. Note that GCM will send an
	                        // unregistered callback upon completion, but
	                        // GCMIntentService.onUnregistered() will ignore it.
	                        if (! registered) {
	                            GCMRegistrar.unregister(context);
	                        }
	                        return null;
	                    }

	                    @Override
	                    protected void onPostExecute(Void result) {
	                        registerTask = null;
	                    }
	                }; // task
	                registerTask.execute(null, null, null);
	            }
	         }
	     }
	    
	    
	    /***/
	    @Override
	    protected void onDestroy() {
	        if (registerTask != null) {
	            registerTask.cancel(true);
	        }
	        unregisterReceiver(handleMessageReceiver);
	        GCMRegistrar.onDestroy(this);
	        super.onDestroy();
	    }

    
}
