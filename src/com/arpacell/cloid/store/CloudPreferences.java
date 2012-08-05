package com.arpacell.cloid.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * User: Nomad
 * Date: 7/4/12
 * Time: 10:19 AM
 */
public class CloudPreferences {

    /** This application's preferences label */
    private static final String PREFS_NAME = "BlobberPrefs";

    /** This application's preferences */
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    /** Cloud info fields */
    private static final String ACCT_NAME_KEY = "acctName";
    private static final String ACCESS_KEY = "acctKey";
    private static final String CONTAINER_KEY = "container";

    /** Control access : if all fields above are filled */
    private static final String OK_KEY =  "OK_KEY";
    
    /** Toggle Mock-Cloud Storage button */
    private static final String MOCK_KEY = "mock";


    /** Ctor */
    public CloudPreferences(Context ctx){
    	
        if(settings == null) {
          settings = ctx.getSharedPreferences(PREFS_NAME,
                                              Context.MODE_PRIVATE);         
          editor = settings.edit();
        }
    }

    /***/
    public String getAccountName(){
       return settings.getString(ACCT_NAME_KEY,"");
    }

    /***/
    public String getAccessKey(){
        return settings.getString(ACCESS_KEY,"");
    }

    /***/
    public String getContainer(){
        return settings.getString(CONTAINER_KEY, "");
    }


    /** Access control */
    public boolean isOK(){
        return settings.getBoolean(OK_KEY, false);
    }
    
    /***/
    public boolean isMock(){
    	return settings.getBoolean(MOCK_KEY, true);
    }
    
    /***/
    public void setMock(boolean onOff){
    	
    	editor.putBoolean(MOCK_KEY, onOff);
    	editor.commit();
    }

    /**
     *  Saves user inputs in preferences
     *  Sets access flag so that user will be allowed
     *  to performed cloud operations only if flag OK_KEY is set,
     *  i.e. all fields are filled up by the user.
     * */
    public void setCredentials( String acctName, String acctKey, String container){

        editor.putString(ACCT_NAME_KEY, acctName);
        editor.putString(ACCESS_KEY, acctKey);
        editor.putString(CONTAINER_KEY, container);

        // not allowed functionality unless all fields are filled
        boolean notFilled = isEmpty(acctName) ||
                            isEmpty(acctKey) ||
                            isEmpty(container) ;

        editor.putBoolean(OK_KEY, notFilled ? false : true);
        // commit changes
        editor.commit();
    }

    /***/
    private static boolean isEmpty(String input){
         return ( input == null || input.trim().length() == 0 );
    }
    
    
    @Override
    public String toString(){
    	
    	return "Cloud preferences: " +
    	       "\nAccount Name: " + getAccountName() +
    	       "\nAccesskey: " + getAccessKey() +
    	       "\nContainer: " + getContainer() +
    	       "\nCredentials Filled? " + isOK() +
    	       "\nUsing Mock Storage? " + isMock();
    }
}
