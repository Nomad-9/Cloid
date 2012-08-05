package com.arpacell.cloid.utils;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arpacell.cloid.R;

public class StorageListAdapter extends ArrayAdapter<Item> {
	
	private final List<Item> items;
	private final Activity act;
	
	
	public StorageListAdapter(Activity act,  List<Item> items) { 
		super(act, R.layout.rowlayout, items);
		this.act = act;
		this.items= items;
	}
	
	/***/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = act.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.rowlayout, null, true);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		
		final Item item = items.get(position);
		final String str = item.getName();
		textView.setText(str); 
		

		// background alternate colors
	    //rowView.setBackgroundResource( (position % 2 == 0) ? R.drawable.alterselector1: R.drawable.alterselector2 );		

		return rowView;
	}

}
