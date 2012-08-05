package com.arpacell.cloid.store;

//import java.io.File;

import android.content.Context;

/**
 * User: Nomad
 * Date: 7/5/12
 * Time: 2:17 PM
 */
public abstract class Storage {

	/** All providers will have accesss to context*/
    protected Context context;
    
    /** All providers will have accesss to SharedPreferences */
    protected CloudPreferences prefs;
    
    /** All downloads from providers will be saved on SD card */
    protected String DOWNLOAD_PATH = "/sdcard/DCIM/Camera/";

    /**
     * @throws OperationException 
     * */
    public Storage(Context ctx) throws OperationException {
        context = ctx;
        prefs = new CloudPreferences(ctx);
        //DOWNLOAD_PATH = ctx.getFilesDir() + File.separator;
    }

    /**
     * @throws OperationException 
     * */
    public abstract void uploadToStorage(String file_path) throws OperationException;

    /**
     * @throws OperationException 
     * */
    public abstract void downloadFromStorage(String file_name) throws OperationException;

    /**
     * @throws OperationException 
     * */
    public abstract void browseStorage() throws OperationException;

    /**
     * @throws OperationException 
     * */
    public abstract void deleteInStorage(String file_name) throws OperationException;


}
