package com.health.settings;

import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.dialog.ColorDialogFragment;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.repository.IRepository;

public class SettingsActivity extends PreferenceActivity {
	
	public static final String EXTRA_MAIN_FRAGMENT_ID = "EXTRA_MAIN_FRAGMENT_ID";
		
	@Override
	public void onBuildHeaders(List<Header> target) {		
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	public void onHeaderClick(Header header, int position) {
		super.onHeaderClick(header, position);
		if (header.id == R.id.header4) {

		}
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		return MainPreferenceFragment.class.getName().equals(fragmentName) || GraphPreferenceFragment.class.getName().equals(fragmentName);
	}
	
	@Override
	public void onAttachFragment (Fragment fragment) {
	    super.onAttachFragment(fragment);
	    if(fragment instanceof MainPreferenceFragment){
	    	getIntent().putExtra(EXTRA_MAIN_FRAGMENT_ID, fragment.getId());
	    }
	    
	}
	
	
}