package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import com.health.main.R;
import com.health.settings.Settings;

public class HelioPhysicsType extends ParameterType {
	
	private static ArrayList<ParameterType> mHelioPhysicsTypeList = null;

    public static ArrayList<ParameterType> getHelioPhysicsTypeList(Context context) {    	
    	
    	if(mHelioPhysicsTypeList == null){
    		mHelioPhysicsTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_helio_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_helio_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			HelioPhysicsType helioPhysicsType = new HelioPhysicsType(5000+i+1, graphNames[i], unitNames[i]);
    			mHelioPhysicsTypeList.add(helioPhysicsType);
            }
    	}

        return mHelioPhysicsTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleHelioPhysicsTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> helioPhysicsTypes = getHelioPhysicsTypeList(context);
    	helioPhysicsTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_GEOHELIOPHYSICS_TYPE, helioPhysicsTypes);
    	for(ParameterType gp : helioPhysicsTypes){
    		if(gp.isVisible()){
    			result.add(gp);
    		}
    	}
        return result;
    }

    public static final int F10_7_TYPE = 5001;
    public static final int SUNSPOT_NUMBER_TYPE = 5002;
    public static final int SUNSPOT_AREA_TYPE = 5003;
    public static final int NEW_REGIONS_TYPE = 5004;
    public static final int FLARES_XRAY_TYPE = 5005;
    public static final int FLARES_OPTICAL_TYPE = 5006;
    public static final int XBKGD_TYPE = 5007;
    
    public static final int F10_7_TYPE_COLOR = Color.GREEN;
    public static final int SUNSPOT_NUMBER_TYPE_COLOR = Color.GRAY;
    public static final int SUNSPOT_AREA_TYPE_COLOR = Color.BLACK;
    public static final int NEW_REGIONS_TYPE_COLOR = Color.rgb(0, 0, 127);
    public static final int FLARES_C_TYPE_COLOR = Color.RED;
    public static final int FLARES_M_TYPE_COLOR = Color.GREEN;
    public static final int FLARES_X_TYPE_COLOR = Color.BLUE;
    public static final int FLARES_S_TYPE_COLOR = Color.RED;
    public static final int FLARES_1_TYPE_COLOR = Color.GREEN;
    public static final int FLARES_2_TYPE_COLOR = Color.BLUE;
    public static final int FLARES_3_TYPE_COLOR = Color.YELLOW;
    public static final int XBKGD_TYPE_COLOR = Color.BLUE;
    
    public static final double F10_7_TYPE_MIN_BORDER = 100;
    public static final double F10_7_TYPE_MAX_BORDER = 250;
    public static final double F10_7_TYPE_STEP = 30;
    public static final double SUNSPOT_NUMBER_TYPE_MIN_BORDER = 0;
    public static final double SUNSPOT_NUMBER_TYPE_MAX_BORDER = 200;
    public static final double SUNSPOT_NUMBER_TYPE_STEP = 40;
    public static final double SUNSPOT_AREA_TYPE_MIN_BORDER = 0;
    public static final double SUNSPOT_AREA_TYPE_MAX_BORDER = 4000;
    public static final double SUNSPOT_AREA_TYPE_STEP = 500;
    public static final double NEW_REGIONS_TYPE_MIN_BORDER = 0;
    public static final double NEW_REGIONS_TYPE_MAX_BORDER = 10;
    public static final double NEW_REGIONS_TYPE_STEP = 2;
    public static final double FLARES_XRAY_TYPE_MIN_BORDER = 0;
    public static final double FLARES_XRAY_TYPE_MAX_BORDER = 20;
    public static final double FLARES_XRAY_TYPE_STEP = 4;
    public static final double FLARES_OPTICAL_TYPE_MIN_BORDER = 0;
    public static final double FLARES_OPTICAL_TYPE_MAX_BORDER = 40;
    public static final double FLARES_OPTICAL_TYPE_STEP = 8;
    public static final double XBKGD_TYPE_MIN_BORDER = 0;
    public static final double XBKGD_TYPE_MAX_BORDER = Math.pow(10, -6) * 3;
    public static final double XBKGD_TYPE_STEP = Math.pow(10, -7) * 5;

    
	public HelioPhysicsType(int id, String name, String unitDimension) {
		super(GROUP_GEOHELIOPHYSICS_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }    
}
