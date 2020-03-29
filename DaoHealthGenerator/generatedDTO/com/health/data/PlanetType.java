package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.health.main.R;
import com.health.settings.Settings;

public class PlanetType extends ParameterType {
    private static ArrayList<ParameterType> mPlanetTypeList = null;

    public static ArrayList<ParameterType> getPlanetTypeList(Context context) {
    	if(mPlanetTypeList == null){
    		mPlanetTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_planet_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_planet_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			PlanetType planetType = new PlanetType(7000+i+1, graphNames[i], unitNames[i]);
    			mPlanetTypeList.add(planetType);
            }
    	}
        return mPlanetTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisiblePlanetTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> planetTypes = getPlanetTypeList(context);
    	planetTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_ASTRONOMY_TYPE, planetTypes);
    	for(ParameterType m : planetTypes){
    		if(m.isVisible()){
    			result.add(m);
    		}
    	}
        return result;
    }

    public static final int MERCURY_TYPE = 7001;
    public static final int VENUS_TYPE = 7002;
    public static final int MARS_TYPE = 7003;
    public static final int JUPITER_TYPE = 7004;
    public static final int SATURN_TYPE = 7005;
    public static final int URANUS_TYPE = 7006;
    public static final int NEPTUNE_TYPE = 7007;
    public static final int PLUTO_TYPE = 7008;

    public static final double TYPE_MIN_BORDER = -10800000d;
    public static final double TYPE_MAX_BORDER = 97200000d;
    public static final double TYPE_STEP = 21600000d;
    
	public PlanetType(int id, String name, String unitDimension) {
		super(ParameterType.GROUP_ASTRONOMY_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }

    
}
