package com.health.viewmodel;

import com.health.data.BodyFeelingType;
import com.health.data.BodyRegion;
import com.health.data.CustomBodyFeelingType;
import com.health.data.IFeelingTypeInfo;
import com.health.data.UnitDimension;
import com.health.data.UserBodyFeelingType;

public class BodyFeelingTypeInfo implements IFeelingTypeInfo {

	private BodyFeelingType bodyFeelingType;
	
	private CustomBodyFeelingType customBodyFeelingType;

	private BodyRegion bodyRegion;
		
	private UserBodyFeelingType userBodyFeelingType;

	public BodyFeelingTypeInfo(BodyFeelingType bodyFeelingType, CustomBodyFeelingType customBodyFeelingType, BodyRegion bodyRegion, UserBodyFeelingType userBodyFeelingType) {
		this.bodyFeelingType = bodyFeelingType;
		this.bodyRegion = bodyRegion;
		this.customBodyFeelingType = customBodyFeelingType;
		this.userBodyFeelingType = userBodyFeelingType;
	}

	public BodyFeelingType getBodyFeelingType() {
		return bodyFeelingType;
	}

	public void setBodyFeelingType(BodyFeelingType bodyFeelingType) {
		this.bodyFeelingType = bodyFeelingType;
	}
	
	public CustomBodyFeelingType getCustomBodyFeelingType() {
		return customBodyFeelingType;
	}

	public void setCustomBodyFeelingType(CustomBodyFeelingType customBodyFeelingType) {
		this.customBodyFeelingType = customBodyFeelingType;
	}

	public UserBodyFeelingType getUserBodyFeelingType() {
		return userBodyFeelingType;
	}

	public void setUserBodyFeelingType(UserBodyFeelingType userBodyFeelingType) {
		this.userBodyFeelingType = userBodyFeelingType;
	}

	public BodyRegion getBodyRegion() {
		return bodyRegion;
	}

	public void setBodyRegion(BodyRegion bodyRegion) {
		this.bodyRegion = bodyRegion;
	}

	
	@Override
	public String getName() {
		if(this.bodyFeelingType != null){
			return String.format("%s: %s", this.bodyRegion.getFullName(), this.bodyFeelingType.getName());
		} else  {
			return String.format("%s: %s", this.bodyRegion.getFullName(), this.customBodyFeelingType.getName());
		}
	}

	@Override
	public int getColor() {
		return userBodyFeelingType.getColor();
	}

	@Override
    public Long getId() {
		if(this.bodyFeelingType != null){
			return this.bodyFeelingType.getId();
		} else {
			return this.customBodyFeelingType.getId();
		}
	    
    }

	@Override
    public long getUnitId() {
	    return UnitDimension.BOOLEAN_TYPE;
    }

	

}
