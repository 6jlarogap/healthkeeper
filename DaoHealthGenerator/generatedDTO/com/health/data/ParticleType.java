package com.health.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import com.health.main.R;
import com.health.settings.Settings;

public class ParticleType extends ParameterType {
	
	private static ArrayList<ParameterType> mParticleTypeList = null;

    public static ArrayList<ParameterType> getParticleTypeList(Context context) {    	
    	
    	if(mParticleTypeList == null){
    		mParticleTypeList = new ArrayList<ParameterType>();
    		String[] graphNames = context.getResources().getStringArray(R.array.graph_particle_list_array);
    		String[] unitNames = context.getResources().getStringArray(R.array.graph_particle_dimension_list_array);
    		for (int i = 0; i < graphNames.length; i++) {
    			ParticleType particleType = new ParticleType(6000+i+1, graphNames[i], unitNames[i]);
    			mParticleTypeList.add(particleType);
            }
    	}

        return mParticleTypeList;
    }
    
    public static ArrayList<ParameterType> getOnlyVisibleParticleTypeList(Context context) {
    	ArrayList<ParameterType> result = new ArrayList<ParameterType>(); 
    	List<ParameterType> particleTypes = getParticleTypeList(context);
    	particleTypes = Settings.getGraphsVisibility(context, ParameterType.GROUP_GEOHELIOPHYSICS_TYPE, particleTypes);
    	for(ParameterType gp : particleTypes){
    		if(gp.isVisible()){
    			result.add(gp);
    		}
    	}
        return result;
    }

    public static final int PROTON_1MEV_TYPE = 6001;
    public static final int PROTON_10MEV_TYPE = 6002;
    public static final int PROTON_100MEV_TYPE = 6003;
    public static final int ELECTRON_08MEV_TYPE = 6004;
    public static final int ELECTRON_2MEV_TYPE = 6005;

    public static final int PROTON_1MEV_TYPE_COLOR = Color.CYAN;
    public static final int PROTON_10MEV_TYPE_COLOR = Color.MAGENTA;
    public static final int PROTON_100MEV_TYPE_COLOR = Color.YELLOW;
    public static final int ELECTRON_08MEV_TYPE_COLOR = Color.LTGRAY;
    public static final int ELECTRON_2MEV_TYPE_COLOR = Color.DKGRAY;

    public static final double PROTON_1MEV_TYPE_MIN_BORDER = 0;
    public static final double PROTON_1MEV_TYPE_MAX_BORDER = Math.pow(10, 6) * 100;
    public static final double PROTON_1MEV_TYPE_STEP = Math.pow(10, 6) * 20;
    public static final double PROTON_10MEV_TYPE_MIN_BORDER = 0;
    public static final double PROTON_10MEV_TYPE_MAX_BORDER = Math.pow(10, 5) * 5;
    public static final double PROTON_10MEV_TYPE_STEP = Math.pow(10, 5);
    public static final double PROTON_100MEV_TYPE_MIN_BORDER = 0;
    public static final double PROTON_100MEV_TYPE_MAX_BORDER = Math.pow(10, 3) * 5;
    public static final double PROTON_100MEV_TYPE_STEP = Math.pow(10, 3);
    public static final double ELECTRON_08MEV_TYPE_MIN_BORDER = 0;
    public static final double ELECTRON_08MEV_TYPE_MAX_BORDER = Math.pow(10, 9) * 10;
    public static final double ELECTRON_08MEV_TYPE_TYPE_STEP = Math.pow(10, 9) * 2;
    public static final double ELECTRON_2MEV_TYPE_MIN_BORDER = 0;
    public static final double ELECTRON_2MEV_TYPE_MAX_BORDER = Math.pow(10, 8) * 10;
    public static final double ELECTRON_2MEV_TYPE_TYPE_STEP = Math.pow(10, 8) * 2 ;

    
	public ParticleType(int id, String name, String unitDimension) {
		super(GROUP_GEOHELIOPHYSICS_TYPE);
        this.Id = id;
        this.Name = name;
        this.UnitDimension = unitDimension;
    }    
}
