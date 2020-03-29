package com.health.main;

import android.os.Bundle;

import com.health.repository.IRepository;

public class PersonalDataActivity extends BaseSyncDataActivity {
	
	private IRepository mRepository;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mRepository = ((HealthApplication) getApplication()).getRepository();
		setContentView(R.layout.personal_data_activity);		
	}

	
}
