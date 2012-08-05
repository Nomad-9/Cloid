package com.arpacell.cloid.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates a blob image item in a cloud container
 * */
public class Item {
	
	private URI uri;
	private String name;
	private long ksize;
	private String type;
	private Date lastModified;
	
	/** List of tems displayed when browsing the Blob Storage */
	public static List<Item> itemList = new ArrayList<Item>();

	
	/** Ctor */
	public Item(URI uri, String name, long ksize, String type, Date lastModified){
		this.uri   = uri;
		this.name  = name;
		this.ksize = ksize;
		this.type = type;
		this.lastModified = lastModified;
	}
	
	
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/***/
	@Override
	public String toString(){
		
		StringBuilder display = new StringBuilder();
		return  display.append("File: ").append(name) 
				.append("\n\nSize: ").append(ksize).append("KB")
				.append("\nType: ").append(type)
				.append("\nLast Edit: ").append( lastModified.toLocaleString() )
				.toString();
	}
	
	
	
	// Mocklist  for testing UI ............................................


	public static String[] names = { 
			 "picture1",
			 "picture2", 
	         "picture3",
	         "picture4",
	         "picture5",
	         "picture6",
	         "picture7",
	         "picture8",
	         "picture9",
	         "picture10"
	};
	
   
   private static List<Item> mockList;
	
	/**
	 * Creates a fake list for Mock storage browsing 
	 * */
	public static List<Item> createMockList(){
		
		if(mockList != null ){
			return mockList;
		}
		mockList = new ArrayList<Item>(10);
		
		String base = "http://username:password@host:8080/directory/file?query";
		Date now = new Date();
	    int counter = 0;
	    while(counter < 10){	
	        try {	    	
	    	   URI uri = new URI( base + "fragment" + counter );
	    	   Item muri = new Item(uri, names[counter], 100L, "image/jpeg", now);
	    	   mockList.add(muri);
	         } 
	         catch(URISyntaxException e){
	        	 e.printStackTrace();
	        	 break;
	         }
	      ++counter;
	    }
		return mockList;
	}
	

}
