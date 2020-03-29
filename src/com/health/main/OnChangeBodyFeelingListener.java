package com.health.main;

import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.Factor;

public interface OnChangeBodyFeelingListener {
			
	public void onAddBodyFeeling(BodyFeeling bodyFeeling);
	
	public void onDeleteBodyFeeling(BodyFeeling bodyFeeling);
	
	public void onAddCommonFeeling(CommonFeeling commonFeeling);
	
	public void onDeleteCommonFeeling(CommonFeeling commonFeeling);
	
	public void onAddFactor(Factor factor);
	
	public void onDeleteFactor(Factor factor);
	
	public void onCompleteChangeUserData();

}
