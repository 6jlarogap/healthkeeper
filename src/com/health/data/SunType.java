package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.health.main.R;
import com.health.settings.Settings;

public class SunType extends ParameterType {
    private static ArrayList<ParameterType> mSunTypeList = null;

    public static ArrayList<ParameterType> getSunTypeList(Context context) {
    	if(mSunTypeList == null){
    		mSunTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_sun_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_sun_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			SunType sunType = new SunType(2000+i+1, graphNames[i], unitNames[i]);
    			mSunTypeList.add(sunType);
            }
    	}
        return mSunTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleSunTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> sunTypes = getSunTypeList(context);
    	sunTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_ASTRONOMY_TYPE, sunTypes);
    	for(ParameterType m : sunTypes){
    		if(m.isVisible()){
    			result.add(m);
    		}
    	}
        return result;
    }

    public static final int SUN_TYPE = 2001;

    public static final double SUN_TYPE_MIN_BORDER = 0;
    public static final double SUN_TYPE_MAX_BORDER = 24D * 60D * 60D * 1000D;
    public static final double SUN_TYPE_STEP = 10800000d;
    
	public SunType(int id, String name, String unitDimension) {
		super(ParameterType.GROUP_ASTRONOMY_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }

    
}
