package com.arpacell.cloid.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.arpacell.cloid.R;

/**
 * Utility class
 * 
 * User: Nomad
 * Date: 7/6/12
 * Time: 3:34 PM
 */
public class Screener {
	

    /** Makes an Activity occupy the whole screen */
    public static void fullScreen(Activity act){

        //Remove title bar
        act.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        act.getWindow()
           .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                     WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    
    
    /** Get file name out of file path string */
    public static String getNameFromPath(String path){
    	
        if(path == null || path.length() == 0){
           return "";
        }
        int mid = path.lastIndexOf("/");
        if (mid == -1){
          return path;
        }
        return path.substring( mid +1 );
    }
    
    
    /***/
   	public static String getPathExtension(String path){
   		  
   		 if(path == null)
   			 return null;
   		 
   	  	  int mid = path.lastIndexOf(".");
   	  	  if (mid == -1){
   	  		  return null;
   	  	  }
   	  	  //String nam = path.substring( 0, mid );
   	  	  String ext = path.substring( mid + 1, path.length() );
   	  	  
   	  	  return ext.toLowerCase();
   	}
   	
   	
   	/***/
   	public static String getImageMimeType(String path){
   			
   	    String ext = getPathExtension(path);
   	    if("jpg".equals(ext) || "jpe".equals(ext)){
   	    	ext = "jpeg";
   	    }
   		return "image/" + ext;
   		
   	}
    
    
    /** Cloud operations */
 	public static final int UPLOAD = 0;
 	public static final int DOWNLOAD = 1;
 	public static final int BROWSE = 2;
 	public static final int DELETE = 3;
    
    /** show operation type in status message */
    public static String getlabel(Activity act, int operation) {
    	
    	String label = "";
    	switch(operation){     
    	
          case UPLOAD :   label = act.getString(R.string.uploading) ;
                          break;
          case BROWSE :   label = act.getString(R.string.browsing) ;
                          break;
          case DOWNLOAD : label = act.getString(R.string.retrieving) ;
                          break;
          case DELETE :   label = act.getString(R.string.deleting) ;
                          break;                                         
        }
		return label;
	}
    
    
    /** Checks if any number of string fields are filled */
    public static boolean checkAllFilled(String... fields){
    	for (String field:fields){
    		if(field == null || field.length() == 0){
    			return false;
    		}
    	}
    	return true;
    }
       
 
}
