package com.arpacell.cloid.store;


import android.app.Activity;
import android.content.Context;

/**
 * User: Nomad
 * Date: 7/5/12
 * Time: 2:55 PM
 */
public class StorageFactory {
	
	private static CloudPreferences prefs;

    public enum Provider {
        MOCK_STORAGE,
        AZURE_STORAGE
    }

    /**
     * Will use Mock storage if it was set in Settings,
     * regardless of Provider arg.
     * That enables to switch to Mock testing without changing 
     * the cloud provider's code.
     * 
     * This method is typically called from a worker thread
     * @throws OperationException 
     * 
     * */
    public static Storage getStorageInstance(final Activity act, Provider which) 
    		                                         throws OperationException  {
    	
        Storage instance = null;
        Context ctx = act.getApplicationContext();
        
        if(prefs == null){
        	prefs = new CloudPreferences(ctx); 
        }       
        if(prefs.isMock()){
        	// override provider
        	which = Provider.MOCK_STORAGE;
        }
        
        // decide which instance to return
        switch (which) {
            case MOCK_STORAGE:  instance = new MockStorage( ctx );
                                 break;
            case AZURE_STORAGE:  instance = new AzureStorage( ctx );
            	                 break; 
        }
        return instance;
    }
}
