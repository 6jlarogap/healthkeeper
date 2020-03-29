package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.health.main.R;
import com.health.settings.Settings;

public class MoonType extends ParameterType {
    private static ArrayList<ParameterType> mMoonTypeList = null;

    public static ArrayList<ParameterType> getMoonTypeList(Context context) {    	
    	
    	if(mMoonTypeList == null){
    		mMoonTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_moon_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_moon_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			MoonType moonType = new MoonType(3000+i+1, graphNames[i], unitNames[i]);
    			mMoonTypeList.add(moonType);
            }
    	}
    	
    	/*if(mMoonTypeList == null){
    		mMoonTypeList = new ArrayList<MoonType>();
			mMoonTypeList.add(new MoonType(1, "Видимость диска луны", "%"));
			mMoonTypeList.add(new MoonType(2, "Возраст Луны", "дни, часы"));
			mMoonTypeList.add(new MoonType(3, "Лунные ритмы", ""));
    	}*/
        return mMoonTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleMoonTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> moonTypes = getMoonTypeList(context);
    	moonTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_ASTRONOMY_TYPE, moonTypes);
    	for(ParameterType m : moonTypes){
    		if(m.isVisible()){
    			result.add(m);
    		}
    	}
        return result;
    }

    public static final int MOON_VISIBILITY_TYPE = 3001;
    public static final int MOON_OLD_TYPE = 3002;
    public static final int MOON_TYPE = 3003;
    
    public static final int MOON_VISIBILITY_TYPE_COLOR = Color.YELLOW;
    public static final int MOON_OLD_TYPE_COLOR = Color.rgb(255, 127, 0);

    public static final double MOON_VISIBILITY_TYPE_MIN_BORDER = 0;
    public static final double MOON_VISIBILITY_TYPE_MAX_BORDER = 120;
    public static final double MOON_VISIBILITY_TYPE_STEP = 20;
    public static final double MOON_OLD_TYPE_MIN_BORDER = 0;
    public static final double MOON_OLD_TYPE_MAX_BORDER = 3024000000d;
    public static final double MOON_OLD_TYPE_STEP = 518400000d;
    public static final double MOON_TYPE_MIN_BORDER = 0;
    public static final double MOON_TYPE_MAX_BORDER = 24D * 60D * 60D * 1000D;
    public static final double MOON_TYPE_STEP = 10800000d;

	public MoonType(int id, String name, String unitDimension) {
		super(ParameterType.GROUP_ASTRONOMY_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }    
}
