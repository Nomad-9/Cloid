package com.arpacell.cloid.push;

import com.arpacell.cloid.R;
import com.arpacell.cloid.utils.Screener;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PushPrefsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	   super.onCreate(savedInstanceState);
	   addPreferencesFromResource(R.xml.preferences);
	}
	
}
