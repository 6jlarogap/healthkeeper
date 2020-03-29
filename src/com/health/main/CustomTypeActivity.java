package com.health.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.health.data.CommonFeelingGroup;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.DaoSession;
import com.health.data.FactorGroup;
import com.health.db.DB;
import com.health.repository.IRepository;

public class CustomTypeActivity extends BaseSyncDataActivity {
	
	public static final String EXTRA_PARENT_ID = "parentid";
	public static final String EXTRA_ID = "id";	
	public static final String EXTRA_TYPE_ID = "typeid";
	public static final String EXTRA_UNITDIMENSION_ID = "unitdimensionid";
	public static final String EXTRA_VALUE1 = "value1";
	public static final String EXTRA_VALUE2 = "value2";
	public static final String EXTRA_VALUE3 = "value3";
	public static final String EXTRA_ANONIMUSER = "anonimuser";
	
	public static final int CUSTOMFACTOR_TYPE_ID = 1;
	public static final int CUSTOMCOMMONFEELING_TYPE_ID = 2;
	
	private IRepository mRepository;
	private long mGroupId;
	private int mTypeId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mGroupId = getIntent().getExtras().getLong(CustomTypeActivity.EXTRA_PARENT_ID);
		this.mTypeId = getIntent().getExtras().getInt(CustomTypeActivity.EXTRA_TYPE_ID);
		this.mRepository = ((HealthApplication) getApplication()).getRepository();
		setContentView(R.layout.custom_type_activity);
		DaoSession daoSession = DB.db().newSession();
		switch(mTypeId){
		case CUSTOMFACTOR_TYPE_ID:
			FactorGroup factorGroup = daoSession.getFactorGroupDao().load(this.mGroupId);
			setTitle(String.format("%s - %s", factorGroup.getName(), "Добавить новый фактор"));
			break;
		case CUSTOMCOMMONFEELING_TYPE_ID:
			CommonFeelingGroup commonFeelingGroup = daoSession.getCommonFeelingGroupDao().load(this.mGroupId);
			setTitle(String.format("%s - %s", commonFeelingGroup.getName(), "Добавить новое ощущение"));
			break;		
		}
	}
	
	public void finish(CustomFactorType customFactorType, Double value1, Double value2, Double value3){
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_PARENT_ID, customFactorType.getFactorGroupId());
		resultIntent.putExtra(EXTRA_ID, customFactorType.getId());
		resultIntent.putExtra(EXTRA_TYPE_ID, CUSTOMFACTOR_TYPE_ID);
		resultIntent.putExtra(EXTRA_UNITDIMENSION_ID, customFactorType.getUnitDimensionId());
		resultIntent.putExtra(EXTRA_ANONIMUSER, false);
		resultIntent.putExtra(EXTRA_VALUE1, value1);
		resultIntent.putExtra(EXTRA_VALUE2, value2);
		resultIntent.putExtra(EXTRA_VALUE3, value3);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	public void finish(CustomCommonFeelingType customCommonFeelingType, Double value1, Double value2, Double value3){
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_PARENT_ID, customCommonFeelingType.getCommonFeelingGroupId());
		resultIntent.putExtra(EXTRA_ID, customCommonFeelingType.getId());
		resultIntent.putExtra(EXTRA_TYPE_ID, CUSTOMCOMMONFEELING_TYPE_ID);
		resultIntent.putExtra(EXTRA_UNITDIMENSION_ID, customCommonFeelingType.getUnitDimensionId());
		resultIntent.putExtra(EXTRA_VALUE1, value1);
		resultIntent.putExtra(EXTRA_VALUE2, value2);
		resultIntent.putExtra(EXTRA_VALUE3, value3);
		resultIntent.putExtra(EXTRA_ANONIMUSER, false);	
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	public void finishForAnonimUser(){
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_ANONIMUSER, true);		
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}	

}
