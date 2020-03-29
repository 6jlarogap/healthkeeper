package com.health.data;

import java.util.Date;

public interface IGridItemValue {
	
	Long getId();
	
	Date getStartDate();
	
	void setStartDate(Date dt);	
	
	IGridItem getGridItem();
	
	void setGridItem(IGridItem gridItem);
	
	Double getValue1();
	
	Double getValue2();
	
	Double getValue3();
	
	void setValue1(Double value);
	
	void setValue2(Double value);
	
	void setValue3(Double value);
	
}
