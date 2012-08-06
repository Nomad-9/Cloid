package com.arpacell.cloid.push;


import com.arpacell.cloid.R;
import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static com.arpacell.cloid.push.Pusher.SERVER_URL;
import static com.arpacell.cloid.push.Pusher.TAG;
import static com.arpacell.cloid.push.Pusher.displayMessage;

/**
 * Helper class used to communicate with the server.
 */
public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Register this account/device pair within the server.
     * 
     *  Retries the operation using exponential back-off. 
     *  In exponential back-off, each time there is a failure, 
     *  it should wait twice the previous amount of time before trying again
     *
     * @return whether the registration succeeded or not.
     */
    public static boolean register(final Context context, final String regId) {
    
    	
        Log.i(TAG, "registering device (regId = " + regId + ")");
        
        String serverUrl = SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        
        // Once GCM returns a registration id, we need to register it in the
        // server. As the server might be down, we will retry it a couple
        // times.
        
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
        	
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                
                post(serverUrl, params);
                
                GCMRegistrar.setRegisteredOnServer(context, true);
                
                String message = context.getString(R.string.server_registered);
                Pusher.displayMessage(context, message);
                return true;
            } 
            catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } 
                catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        Pusher.displayMessage(context, message);
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
    	
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            Pusher.displayMessage(context, message);
        } 
        catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            Pusher.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {
    	
        URL url;
        
        try {
            url = new URL(endpoint);
        } 
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        
        Log.v(TAG, "Posting '" + body + "' to " + url);
        
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } 
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}