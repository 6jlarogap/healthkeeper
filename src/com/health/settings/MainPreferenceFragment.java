package com.health.settings;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.db.UserDB;
import com.health.dialog.ColorDialogFragment;
import com.health.main.HealthApplication;
import com.health.main.R;
import com.health.repository.IRepository;

public class MainPreferenceFragment extends PreferenceFragment {
	
	public static final int CHOOSE_COLOR_DIALOG_FRAGMENT = 1;
    
    private SwitchPreference mSwitchPreference;
    private ListPreference mPeriodSyncDataListPreference;
    private User mUser;
    
    private PreferenceColorDialog mPreferenceColorDialog;
    private PreferenceDefaultColorDialog mPreferenceDefaultColorDialog;
    
    private IRepository mRepository;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
        this.mUser = UserDB.getCurrentUser();        
        addPreferencesFromResource(R.xml.preference_main);
        this.mSwitchPreference = (SwitchPreference) findPreference("isAutoSync");
        this.mPeriodSyncDataListPreference = (ListPreference) findPreference("isPeriodSync");
        this.mPreferenceColorDialog = (PreferenceColorDialog) findPreference("colorDialog");
        this.mPreferenceDefaultColorDialog = (PreferenceDefaultColorDialog) findPreference("defaultColorDialog");
        boolean isAutoSync = UserDB.isAutoSyncData();
        this.mSwitchPreference.setChecked(isAutoSync);        
        this.mSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                UserDB.saveAutoSyncData(Boolean.parseBoolean(newValue.toString()));
                return true;
            }
        });
        this.mPeriodSyncDataListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				UserDB.savePeriodSyncData(Integer.parseInt(newValue.toString()));
				return true;
			}
		});        
        this.mPeriodSyncDataListPreference.setValue(Integer.toString(mUser.getPeriodSyncData()));        
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == Activity.RESULT_OK) {			
			if(requestCode == MainPreferenceFragment.CHOOSE_COLOR_DIALOG_FRAGMENT){
				int color = (Integer) data.getExtras().get(ColorDialogFragment.EXTRA_COLOR);
				Long bodyFeelingTypeId = (Long) data.getExtras().get(ColorDialogFragment.EXTRA_BODYFEELINGTYPEID);
				if(bodyFeelingTypeId == null){
					User currentUser = UserDB.getCurrentUser();
	                UserBodyFeelingType userBodyFeelingType = mRepository.getDefaultUserBodyFeelingType();
	                userBodyFeelingType.setColor(color);
	                userBodyFeelingType.setUserId(currentUser.getId());
	                mRepository.updateUserBodyFeelingType(userBodyFeelingType);
	                if(mPreferenceDefaultColorDialog != null){
	                	mPreferenceDefaultColorDialog.updateData(color);
	                }
				} else {
	                UserBodyFeelingType userBodyFeelingType = mRepository.getUserBodyFeelingType(bodyFeelingTypeId);
	                if(userBodyFeelingType != null){
	                    userBodyFeelingType.setColor(color);
	                } else {
	                    User user = mRepository.getCurrentUser();
	                    userBodyFeelingType = new UserBodyFeelingType();
	                    userBodyFeelingType.setBodyFeelingTypeId(bodyFeelingTypeId);                    
	                    userBodyFeelingType.setColor(color);
	                    userBodyFeelingType.setUserId(user.getId());
	                    userBodyFeelingType.setRowId(UUID.randomUUID().toString());
	                }
	                mRepository.updateUserBodyFeelingType(userBodyFeelingType);
	                if(mPreferenceColorDialog != null){
	                	mPreferenceColorDialog.updateData();
	                }
				}
			}
		}
	}
}