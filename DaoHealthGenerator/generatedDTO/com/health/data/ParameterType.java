package com.health.data;

public class ParameterType {

	public static final int GROUP_WEATHER_TYPE = 1;
	public static final int GROUP_ASTRONOMY_TYPE = 2;
	public static final int GROUP_GEOHELIOPHYSICS_TYPE = 3;
	public static final int GROUP_USER_TYPE = 4;
	
	protected int GroupId;
	
	public int Id;

    public String Name;
    
    public String UnitDimension;
    
    private boolean Visible;

	protected ParameterType(int groupId) {
        this.GroupId = groupId;        
    }
	
	public boolean isVisible() {
		return Visible;
	}

	public void setVisible(boolean visible) {
		Visible = visible;
	}
	
	public int getId() {
        return this.Id;
    }

    public String getName() {
        return this.Name;
    }

    public String getUnitDimension() {
        return this.UnitDimension;
    }

}
