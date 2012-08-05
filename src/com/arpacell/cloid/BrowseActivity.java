package com.arpacell.cloid;


import java.util.ArrayList;
import java.util.List;

import com.arpacell.cloid.store.CloudPreferences;
import com.arpacell.cloid.utils.Item;
import com.arpacell.cloid.utils.Screener;
import com.arpacell.cloid.utils.StorageListAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import static com.arpacell.cloid.utils.Screener.BROWSE;
import static com.arpacell.cloid.utils.MessageKeys.ITEM;
import static com.arpacell.cloid.utils.MessageKeys.ITEM_DESC;
import static com.arpacell.cloid.utils.MessageKeys.TASK;


public class BrowseActivity extends ListActivity {
	
	private List<Item> items = new ArrayList<Item>();
	private StorageListAdapter adapter;
	private CloudPreferences prefs;
	private View header;
	
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   
	   super.onCreate(savedInstanceState);
	   Screener.fullScreen(this);
	   setContentView(R.layout.browse);
	  
	   header = findViewById(R.id.header);	 
	   
	   prefs = new CloudPreferences(this);
	   
	   // get list of items
	   setList();
	   
	   // set adapter
	   adapter = new StorageListAdapter(this, items); 
	   setListAdapter(adapter);

	}
   
  
   @Override
   protected void onResume(){
	   
   	  super.onResume();
   	  
  	 // make view visible when regaining focus
   	  ListView view = this.getListView();
	  view.setVisibility(View.VISIBLE);
	  header.setVisibility(View.VISIBLE);
	
   	  // refresh  
	  updateAdapterForList(items);  	
   }

   
   @Override
   protected void onPause(){
	   
   	  super.onPause();
   	
     // make view invisible when losing focus
   	  ListView view = this.getListView();
	  view.setVisibility(View.INVISIBLE);
   	
   }
   
   
   @Override
  	protected void onListItemClick(ListView l, View v, int position, long id) {
	   
  		    super.onListItemClick(l, v, position, id);
  		    
  		    header.setVisibility(View.GONE);
  		
  			Item item =  items.get(position) ;
  			  			
  			// construct intent to pass along the following activity
  	        Intent in = new Intent(this, DownloadActivity.class);
  	        in.putExtra(ITEM, item.getName());
  	        in.putExtra(ITEM_DESC, item.toString());
  	        // call Activity
  	        startActivity(in);
  		
  	}

	
	/**
	 * Constructs a dummy URI list if Mock Storage is used
	 * Otherwise, does the real thing.
	 * */
	private void setList(){
		
		// This activity moves to the background,
		//  onPause() is called -> make this list view invisible
		if(items.isEmpty()){
		   Intent in = new Intent(BrowseActivity.this, CloudActivity.class);
           in.putExtra(TASK, BROWSE);
           startActivity(in);
		}
        
        // call to onResume() makes list view visible again...
		
		if(prefs.isMock()){		
			// construct fake list
			items = Item.createMockList();
		}
		else{
			items = Item.itemList;
		}
	}
	
	
	/***/
    @SuppressWarnings("rawtypes")
	private void updateAdapterForList(List list){
   	 
   	     adapter = new StorageListAdapter(this,list); 
		 setListAdapter(adapter);
		 adapter.notifyDataSetChanged();   	 
    }

}
