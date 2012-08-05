package com.arpacell.cloid.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;

import com.arpacell.cloid.utils.Item;
import com.arpacell.cloid.utils.Screener;
import com.windowsazure.samples.android.storageclient.BlobProperties;
import com.windowsazure.samples.android.storageclient.CloudBlob;
import com.windowsazure.samples.android.storageclient.CloudBlobClient;
import com.windowsazure.samples.android.storageclient.CloudBlobContainer;
import com.windowsazure.samples.android.storageclient.CloudBlockBlob;
import com.windowsazure.samples.android.storageclient.CloudStorageAccount;

/**
 *  Windows Azure Cloud Storage
 *  https://www.windowsazure.com/en-us/develop/overview/
 *  
 *  Code samples /Java SDK) in:
 *  https://www.windowsazure.com/en-us/develop/java/how-to-guides/blob-storage/
 *
 * User: Nomad
 * Date: 7/5/12
 * Time: 2:40 PM
 */
public class AzureStorage extends Storage {

    
	private CloudBlobContainer container;
    
	/**
	 * Ctor
     * @throws OperationException */
    public AzureStorage(Context ctx) throws OperationException {
        super(ctx);
  
        // set from prefs
        String acct_name = prefs.getAccountName();
        String access_key = prefs.getAccessKey();
        
        // get connection string
        String storageConn = "DefaultEndpointsProtocol=http;" + 
                "AccountName=" + acct_name +
                ";AccountKey=" + access_key;
        	    
        // get CloudBlobContainer    	  	 
        try {
        	// Retrieve storage account from storageConn
        	CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConn);
        	
			// Create the blob client
        	// to get reference objects for containers and blobs
        	CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        	
	    	// Retrieve reference to a previously created container
	    	container = blobClient.getContainerReference( prefs.getContainer() );
	    	container.createIfNotExist();
		} 
        catch (Exception e) {	
			throw new OperationException("Error from initBlob: " + e.getMessage(), e);
		}
    }

    /**
     * @throws OperationException */
    @Override
    public void uploadToStorage(String file_path) throws OperationException {

		try {			
	    	// Create or overwrite blob with contents from a local file
			// use same name than in local storage
	    	CloudBlockBlob blob = container.getBlockBlobReference( 
	    			               Screener.getNameFromPath(file_path) );	    	
	    	File source = new File(file_path); 
	    	blob.upload( new FileInputStream(source), source.length() );	
	    	blob.getProperties().contentType = Screener.getImageMimeType(file_path);
			blob.uploadProperties();
		} 
		catch (Exception e) {
			throw new OperationException("Error from uploadToStorage: " + e.getMessage(), e);
		} 
    }
    
    /** Cannot resolve ListBlobItem (Java SDK)
     *  Use CloudBlob instead (Android)
     * @throws OperationException 
     * */	
    @Override
    public void browseStorage() throws OperationException{
    	
    	// reset uri list
    	Item.itemList.clear();
    	   	
    	// Loop over blobs within the container and output the URI to each of them
    	try {
			for (CloudBlob blob : container.listBlobs()) {
				
				blob.downloadAttributes();
				BlobProperties props = blob.getProperties();
				long ksize =  props.length/1024;
				String type = props.contentType;
				Date lastModified = props.lastModified;
				
				Item item = new Item(blob.getUri(), blob.getName(), ksize, type, lastModified);
			    Item.itemList.add(item);
			}		
		}
    	catch (Exception e) {
			throw new OperationException("Error from browseStorage: " + e.getMessage(), e);
		}
    }

   
    /** Cannot resolve ListBlobItem (Java SDK)
     *  Use CloudBlob instead (Android)
     * @throws OperationException 
     * */
    @Override
    public void downloadFromStorage(String file_name) throws OperationException{
   	
    	try {
			for (CloudBlob blob : container.listBlobs()) {    	    
			    // Download the item and save it to a file with the same name as arg
				if(blob.getName().equals(file_name)){
			         blob.download( new FileOutputStream(DOWNLOAD_PATH + blob.getName()) );
			         break;
				}
			}
		} 
    	catch (Exception e) {
			throw new OperationException("Error from downloadFromStorage: " + e.getMessage(), e);
		}    	
    }
    
    /**
     * @throws OperationException */
    @Override
    public void deleteInStorage(String file_name) throws OperationException{
    	
		try {
	    	// Retrieve reference to a blob named file_name
	    	CloudBlockBlob blob = container.getBlockBlobReference(file_name);
	    	// Delete the blob
	    	blob.delete(); 	    	
		} 
		catch (Exception e) {
			throw new OperationException("Error from deleteInStorage: " + e.getMessage(), e);
		}     	 
    }
    

}
