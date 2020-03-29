package com.health.settings;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import com.health.data.GeoPhysicsType;
import com.health.data.HelioPhysicsType;
import com.health.data.MoonType;
import com.health.data.ParameterType;
import com.health.data.ParticleType;
import com.health.data.PlanetType;
import com.health.data.SunType;
import com.health.data.WeatherType;
import com.health.main.R;

public class GraphPreferenceFragment extends PreferenceFragment {
    
	public static final String EXTRA_GROUP_ID = "groupid";
	
	public int mParameterGroupId;
	
    private List<ParameterType> mTypes;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	mTypes = new ArrayList<ParameterType>();
    	setRetainInstance(false);
    	this.mParameterGroupId = getArguments().getInt(EXTRA_GROUP_ID);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_graph);
        PreferenceCategory category = (PreferenceCategory)findPreference("category_graph");
        category.removeAll();
        switch (this.mParameterGroupId) {
		case ParameterType.GROUP_WEATHER_TYPE:
			this.mTypes.addAll(WeatherType.getWeatherTypeList(getActivity()));
	        break;
		case ParameterType.GROUP_ASTRONOMY_TYPE:
			this.mTypes.addAll(SunType.getSunTypeList(getActivity()));
			this.mTypes.addAll(MoonType.getMoonTypeList(getActivity()));
			this.mTypes.addAll(PlanetType.getPlanetTypeList(getActivity()));
	        break;
		case ParameterType.GROUP_GEOHELIOPHYSICS_TYPE:
			this.mTypes.addAll(GeoPhysicsType.getGeoPhysicsTypeList(getActivity()));
			this.mTypes.addAll(HelioPhysicsType.getHelioPhysicsTypeList(getActivity()));
			this.mTypes.addAll(ParticleType.getParticleTypeList(getActivity()));
	        break;
		case ParameterType.GROUP_USER_TYPE:
			this.mTypes.addAll(WeatherType.getWeatherTypeList(getActivity()));
			this.mTypes.addAll(SunType.getSunTypeList(getActivity()));
			this.mTypes.addAll(MoonType.getMoonTypeList(getActivity()));
			this.mTypes.addAll(PlanetType.getPlanetTypeList(getActivity()));
			this.mTypes.addAll(GeoPhysicsType.getGeoPhysicsTypeList(getActivity()));
			this.mTypes.addAll(HelioPhysicsType.getHelioPhysicsTypeList(getActivity()));
			this.mTypes.addAll(ParticleType.getParticleTypeList(getActivity()));
			break;
		default:
			break;
		}
        this.mTypes =  Settings.getGraphsVisibility(getActivity(), this.mParameterGroupId, this.mTypes);
        for(ParameterType w : this.mTypes){
        	CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getActivity());        	
            checkBoxPreference.setKey(String.format("%d", w.Id));
            checkBoxPreference.setTitle(w.getName());                                 
            checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {                
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                	int id = Integer.parseInt(preference.getKey());
                	for (ParameterType w : mTypes) {
                		if(w.Id == id){
                			w.setVisible(Boolean.parseBoolean(newValue.toString()));
                			break;
                		}
                    }                	
                	Settings.saveGraphsVisibility(getActivity(), mParameterGroupId, mTypes);
                    return true;
                }
            });
            category.addPreference(checkBoxPreference);
            checkBoxPreference.setChecked(w.isVisible()); 
        }
    }
}