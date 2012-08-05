package com.arpacell.cloid.store;

import android.content.Context;

/**
 *  A simulated Storage provider
 *
 * User: Nomad
 * Date: 7/5/12
 * Time: 2:40 PM
 */
public class MockStorage extends Storage {

    private final long SLEEP_TIME = 5000L;

    public MockStorage(Context ctx) throws OperationException{
        super(ctx);
    }

    @Override
    public void uploadToStorage(String file_path) throws OperationException {
        doNothingButSleep();
        //throw new OperationException( "Test error message", new Throwable("Reason: upload test") );
    }

    @Override
    public void downloadFromStorage(String file_name)throws OperationException {
        doNothingButSleep();
        // TEST exception handling
        throw new OperationException( "Test error message", 
        		          new Throwable("Reason: download test") );
    }
    
    @Override
	public void browseStorage() {
    	 doNothingButSleep();		
	}

	@Override
	public void deleteInStorage(String file_name) throws OperationException {
		 doNothingButSleep();		
	}

    /***/
    private void doNothingButSleep(){
         try{
             Thread.sleep(SLEEP_TIME);
         }
         catch (InterruptedException iex){
            return;
         }
    }
}
