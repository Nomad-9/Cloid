package com.arpacell.cloid;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.arpacell.cloid.store.CloudPreferences;
import com.arpacell.cloid.utils.Screener;

/**
 * User: Nomad
 * Date: 7/1/12
 * Time: 12:43 PM
 */
public class SettingsActivity extends Activity {

    private TextView txt_name;
    private TextView txt_key;
    private TextView txt_container;
	
	private EditText acct_name;
    private EditText access_key;
    private EditText container;
    
    ViewSwitcher switcher;

    private CloudPreferences prefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Screener.fullScreen(this);
        setContentView(R.layout.credentials);
        
        switcher = (ViewSwitcher)findViewById(R.id.switcher);
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleStorage);
        
        // set values saved in prefs if any
        prefs = new CloudPreferences(this);
        
        // remember user prefs as storage to use (i.e. cloud or mock)
        if( ! prefs.isMock()){
        	switcher.showNext();
        	tb.setChecked(true);
        }
        // fields for cloud storage
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_key = (TextView) findViewById(R.id.txt_key);
        txt_container = (TextView) findViewById(R.id.txt_container);
       
        // if not set in res/values/string.xml, set them via prefs
        acct_name = (EditText) findViewById(R.id.acct_name);
        if(acct_name.length() == 0)
           setText(acct_name, prefs.getAccountName());
        
        access_key = (EditText) findViewById(R.id.access_key);
        if(access_key.length() == 0)
           setText(access_key, prefs.getAccessKey());
        
        container = (EditText)findViewById(R.id.container);
        if(container.length() == 0)
           setText(container, prefs.getContainer());

    }

    /** handler for choosing Mock storage view or Real Storage view */
    public void onOff(View v){   
    	
    	ToggleButton tb = (ToggleButton)v;
    	if( tb.isChecked() ){
    		switcher.showNext();
    	}
    	else{
    		switcher.showPrevious();
    	}
    }
    
    
    /** Handler for SAVE button 
     * 
     *  If current view is "Mock Storage", 
     *     save view pref (i.e. Cloud OFF) 
     *  
     *  If current view is Cloud Storage,
     *     1) check if all fields are entered
     *     2) save those fields to prefs
     *     3) save view pref (i.e. Cloud ON)     
     *     
     */
    public void save(View v)  {
    	
    	// first view => will use mock storage
    	// second view => will use cloud
    	boolean is_mock =  (switcher.getDisplayedChild() == 0);
    	
    	// save fields value if storage is cloud
    	if( ! is_mock){
    		if(! checkCredentials()){
    			return;
    		}
           fillCredentials();
    	}
    	
    	// save storage option in prefs   	
    	prefs.setMock( is_mock );    
    	
        Toast.makeText(this, is_mock ? R.string.use_mock_storage : R.string.settings_saved, Toast.LENGTH_SHORT).show();
        this.finish();
    }


   /**
    * Gets user inputs and stored them in Preferences.
    * If there were initial values stored there, those will
    * be overwritten by the new ones.
    *
    * */
     private void fillCredentials(){
         // save in prefs
         prefs.setCredentials(getString(acct_name),
                              getString(access_key),
                              getString(container) );

     }
     
     
     /***/
     private boolean checkCredentials(){
    	 
    	 if( getString(acct_name).length() == 0 ){
    		 highlight(txt_name);
    		 return false;
    	 }
    	 if( getString(access_key).length() == 0 ){
    		 highlight(access_key);
    		 return false;
    	 }
    	 if( getString(container).length() == 0 ){
    		 highlight(container);
    		 return false;
    	 }
    	
    	 return true;
     }
     
     
     /***/
     private void highlight(TextView tview){
    	 
    	 tview.setTextColor(Color.GREEN);
    	 tview.setTypeface(Typeface.DEFAULT_BOLD);
     }
     
     
     /** Gets String value of TextView/EditText
      *  by taking out all space chars
      * */
     private String getString(TextView tview){
    	 
         return tview.getText().toString().trim();
     }


     /** Sets value for TextView / EditText*/
     private void setText(TextView tview, String value){
    	 
         if(tview == null){
            return;
         }
         if(value == null) {
             value = "";
         }
         tview.setText( value.toCharArray(), 0, value.length());
     }

}