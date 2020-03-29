package com.health.data;

public interface IGridItem {
	
	Long getId();
	
	String getName();
	
	Integer getOrdinalNumber();
	
	Integer getStatus();
	
	String getFullName();
	
	int getColor();
	
	long getUnitId();
	
	IGridGroup getGroup();
	
}
