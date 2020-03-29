package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import com.health.main.R;
import com.health.settings.Settings;

public class GeoPhysicsType extends ParameterType {
	private static ArrayList<ParameterType> mGeoPhysicsTypeList = null;

    public static ArrayList<ParameterType> getGeoPhysicsTypeList(Context context) {    	
    	
    	if(mGeoPhysicsTypeList == null){
    		mGeoPhysicsTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_geophysics_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_geophysics_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			GeoPhysicsType geoPhysicsType = new GeoPhysicsType(4000+i+1, graphNames[i], unitNames[i]);
    			mGeoPhysicsTypeList.add(geoPhysicsType);
            }
    	}
    	/*if(mGeoPhysicsTypeList == null){
    		mGeoPhysicsTypeList = new ArrayList<GeoPhysicsType>();
			mGeoPhysicsTypeList.add(new GeoPhysicsType(1, "Индекс Kp", "ед."));
			mGeoPhysicsTypeList.add(new GeoPhysicsType(2, "Индекс Ap", "нТл"));
    	}*/
        return mGeoPhysicsTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleGeoPhysicsTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> geoPhysicsTypes = getGeoPhysicsTypeList(context);
    	geoPhysicsTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_GEOHELIOPHYSICS_TYPE, geoPhysicsTypes);
    	for(ParameterType gp : geoPhysicsTypes){
    		if(gp.isVisible()){
    			result.add(gp);
    		}
    	}
        return result;
    }

    public static final int KP_TYPE = 4001;
    public static final int AP_TYPE = 4002;
    
    public static final int KP_TYPE_COLOR = Color.GREEN;    
    public static final int AP_TYPE_COLOR = Color.RED;

    public static final double KP_TYPE_MIN_BORDER = 0;
    public static final double KP_TYPE_MAX_BORDER = 30;
    public static final double KP_TYPE_STEP = 3;
    public static final double AP_TYPE_MIN_BORDER = 0;
    public static final double AP_TYPE_MAX_BORDER = 100;
    public static final double AP_TYPE_STEP = 20;

    

	public GeoPhysicsType(int id, String name, String unitDimension) {
		super(GROUP_GEOHELIOPHYSICS_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }
    
}
