package com.arpacell.cloid.push;

import android.content.Context;
import android.content.Intent;

public class Pusher {
	
 /* GCM */
    
    /**
     * Whether server push with GCM has been enabled
     */
    
    public static boolean PUSH_ENABLED ;
    
    /**
     * Base URL of the Server (such as http://host:8080/gcm-server)
     */
    public static String SERVER_URL;

    /**
     * Google API project id registered to use GCM.
     */
    public static String SENDER_ID;

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "Cloid";
    
    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.arpacell.cloid.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
    	
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
