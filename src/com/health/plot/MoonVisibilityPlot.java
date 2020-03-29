package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Moon;
import com.health.data.MoonType;
import com.health.main.R;

public class MoonVisibilityPlot extends DataPlot {
	private SimpleXYSeries moonVisibilitySeries;
	private LineAndPointFormatter moonVisibilityFormatter;
	private List<Moon> moonList = new ArrayList<Moon>();
	
	public MoonVisibilityPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, double yAxisMin, double yAxisMax, double yAxisStep, List<Moon> moonList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, yAxisMin, yAxisMax, yAxisStep);
		
		dataTypeId = MoonType.MOON_VISIBILITY_TYPE;
		
		layoutHelpDialogId = R.layout.dialog_plot_help_moon_visibility;
		setupMoonPlot();
		// получаем форматтер для серии
		moonVisibilityFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, Color.YELLOW, PlotService.plf, POINT_LABELER_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		updateData(moonList);
	}
	
	private void setupMoonPlot(){
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Moon){
				moonList = (List<Moon>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateMoonVisibilitySeries();

    	// задаем новую серию
    	plot.addSeries(moonVisibilitySeries, moonVisibilityFormatter);
    	
    	plot.redraw();
	}
	
	private void updateMoonVisibilitySeries() {
		moonVisibilitySeries = new SimpleXYSeries("");

		for (Moon m : moonList) {
			if (m.getMoonVisibility() != null && (moonVisibilitySeries.size() == 0 || m.getInfoDate().getTime() - moonVisibilitySeries.getX(moonVisibilitySeries.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				moonVisibilitySeries.addLast(m.getInfoDate().getTime(), m.getMoonVisibility());
			}
		}
	}
}
