package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.HelioPhysics;
import com.health.data.HelioPhysicsType;
import com.health.main.R;

public class SunspotAreaPlot extends DataPlot {
	private SimpleXYSeries sunspotAreaSeries;
	
	private LineAndPointFormatter sunspotAreaFormatter;
	
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public SunspotAreaPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> helioPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(sunspotAreaSeries);
		
		dataTypeId = HelioPhysicsType.SUNSPOT_AREA_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_sunspot_area;
		histogrammFloorStep = 100;
		
		setupSunspotAreaPlot();
		// получаем форматтер для серии
    	sunspotAreaFormatter = getLineAndPointSeriesFormatter(HelioPhysicsType.SUNSPOT_AREA_TYPE_COLOR, HelioPhysicsType.SUNSPOT_AREA_TYPE_COLOR, HelioPhysicsType.SUNSPOT_AREA_TYPE_COLOR,
    			PlotService.plf, POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(helioPhysicsList);
	}
	
	private void setupSunspotAreaPlot(){
		plot.setIndentY(10, 10);
		plot.getGraphWidget().setRangeValueFormat(PlotService.numPositiveFormat);
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof HelioPhysics){
				helioPhysicsList = (List<HelioPhysics>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
    	updateSunspotAreaSeries();

    	// задаем новую серию
    	plot.addSeries(sunspotAreaSeries, sunspotAreaFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateSunspotAreaSeries() {
		ownSerieses.remove(sunspotAreaSeries);
		sunspotAreaSeries = new SimpleXYSeries("");
		ownSerieses.add(sunspotAreaSeries);

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getSunspotArea() != null) {
				sunspotAreaSeries.addLast(hp.getInfoDate().getTime(), hp.getSunspotArea());
			}
		}
	}
}
