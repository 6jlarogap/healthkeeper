package com.health.viewmodel;

import com.health.data.CommonFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.IFeelingTypeInfo;
import com.health.data.UnitDimension;
import com.health.repository.IRepository;

public class CommonFeelingTypeInfo implements IFeelingTypeInfo {

	private CommonFeelingType commonFeelingType;
	
	private CustomCommonFeelingType customCommonFeelingType;

	private int color = IRepository.DEFAULT_USER_BODY_FEELING_TYPE_COLOR;

	public CommonFeelingTypeInfo(CommonFeelingType commmonFeelingType, CustomCommonFeelingType customCommonFeelingType) {
		this.commonFeelingType = commmonFeelingType;
		this.customCommonFeelingType = customCommonFeelingType;
	}
	
	public CustomCommonFeelingType getCustomCommonFeelingType() {
		return customCommonFeelingType;
	}

	public void setCustomCommonFeelingType(CustomCommonFeelingType customCommonFeelingType) {
		this.customCommonFeelingType = customCommonFeelingType;
	}
	
	public CommonFeelingType getCommonFeelingType() {
		return commonFeelingType;
	}


	public void setCommonFeelingType(CommonFeelingType commonFeelingType) {
		this.commonFeelingType = commonFeelingType;
	}	
	
	@Override
	public String getName() {
		if(this.commonFeelingType != null){
			return String.format("%s", this.commonFeelingType.getName());
		} else {
			return String.format("%s", this.customCommonFeelingType.getName());
		}
		
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
    public Long getId() {
		if(this.commonFeelingType != null){
			return this.commonFeelingType.getId();
		} else {
			return this.customCommonFeelingType.getId();
		}
    }

	@Override
    public long getUnitId() {
	    if(commonFeelingType != null && commonFeelingType.getUnitDimension() != null){
	    	return commonFeelingType.getUnitDimension().getId();	    	
	    }
	    if(customCommonFeelingType != null && customCommonFeelingType.getUnitDimension() != null){
	    	return customCommonFeelingType.getUnitDimension().getId();
	    }
	    return UnitDimension.BOOLEAN_TYPE;
    }

	

}
