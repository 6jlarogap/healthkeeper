package com.health.data;

import java.util.ArrayList;
import java.util.List;

import com.health.main.R;
import com.health.settings.Settings;

import android.content.Context;
import android.graphics.Color;

public class WeatherType extends ParameterType {

    private static ArrayList<ParameterType> mWeatherTypeList = null;

    public static ArrayList<ParameterType> getWeatherTypeList(Context context) {    	
    	if(mWeatherTypeList == null){
    		mWeatherTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_weather_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_weather_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			WeatherType weatherType = new WeatherType(1000+i+1, graphNames[i], unitNames[i]);    			   			
    			mWeatherTypeList.add(weatherType);
            }
    	}
        return mWeatherTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleWeatherTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> weatherTypes = getWeatherTypeList(context);
    	List<ParameterType> params = Settings.getGraphsVisibility(context, GROUP_WEATHER_TYPE, weatherTypes);
    	for(ParameterType p : params){
    		if(p.isVisible()){
    			result.add(p);
    		}
    	}
        return result;
    }

    public static final int PRESSURE_TYPE = 1001;
    public static final int TEMPERATURE_TYPE = 1002;
    public static final int HUMIDITY_TYPE = 1003;
    public static final int CLOUD_TYPE = 1004;
    public static final int PRECIPITATION_TYPE = 1005;
    public static final int WINDSPEED_TYPE = 1006;

    public static final int PRESSURE_TYPE_COLOR = Color.DKGRAY;
    public static final int TEMPERATURE_TYPE_COLOR = Color.RED;
    public static final int HUMIDITY_TYPE_COLOR = Color.rgb(173,216,230);
    public static final int CLOUD_TYPE_COLOR = Color.BLUE;
    public static final int PRECIPITATION_TYPE_COLOR = Color.rgb(0,0,128);
    public static final int WINDSPEED_TYPE_COLOR = Color.MAGENTA;
    
	public WeatherType(int id, String name, String unitDimension) {
		super(ParameterType.GROUP_WEATHER_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }

    public String getName() {
        return this.Name;
    }

    public String getUnitDimension() {
        return this.UnitDimension;
    }

}
