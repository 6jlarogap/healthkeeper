package com.health.plot;

import java.util.Date;
import java.util.List;

import com.androidplot.xy.SimpleXYSeries;

public interface IDataPlot extends IFactorPlot {
	String getName();
	String getUnit();

	double getHistogrammFloorStep();
	void setHistogrammFloorStep(double hfs);
	
	void updateData(List<?> ... data);
	void setMarkedDate(Date dt);
	void setMarkedValue(String val);
    void setJuxtaposeSeries(IDataPlot jp);
    void removeJuxtaposeSeries();
    void setJuxtapose(boolean isJuxtapose);
	
	int getLayoutHelpDialogId();
	int getDataTypeId();
	List<SimpleXYSeries> getOwnSerieses();

	boolean isHaveHistogramm();
	
	boolean isHaveHourlyData();
	boolean isHaveDailyData();
	
	boolean isShowHourlyData();
	boolean isShowDailyData();
	boolean isShowHourlyChange();
	boolean isShowDailyChange();
	
	boolean isHaveJuxtapose();
	
	void showHourlyData(boolean isShow);
	void showDailyData(boolean isShow);
	void showHourlyChange(boolean isShow);
	void showDailyChange(boolean isShow);

}
