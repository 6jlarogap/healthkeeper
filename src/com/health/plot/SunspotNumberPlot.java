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

public class SunspotNumberPlot extends DataPlot {
	private SimpleXYSeries sunspotNumberSeries;
	
	private LineAndPointFormatter sunspotNumberFormatter;
	
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public SunspotNumberPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> helioPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);
		
		ownSerieses.add(sunspotNumberSeries);
		
		dataTypeId = HelioPhysicsType.SUNSPOT_NUMBER_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_sunspot_number;
		histogrammFloorStep = 10;
		
		setupSunspotNumberPlot();
		// получаем форматтер для серии
    	sunspotNumberFormatter = getLineAndPointSeriesFormatter(HelioPhysicsType.SUNSPOT_NUMBER_TYPE_COLOR, HelioPhysicsType.SUNSPOT_NUMBER_TYPE_COLOR, HelioPhysicsType.SUNSPOT_NUMBER_TYPE_COLOR,
    			PlotService.plf, POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(helioPhysicsList);
	}
	
	private void setupSunspotNumberPlot(){
		plot.setIndentY(5, 5);
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
    	updateSunspotNumberSeries();

    	// задаем новую серию
    	plot.addSeries(sunspotNumberSeries, sunspotNumberFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateSunspotNumberSeries() {
		ownSerieses.remove(sunspotNumberSeries);
		sunspotNumberSeries = new SimpleXYSeries("");
		ownSerieses.add(sunspotNumberSeries);

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getSunspotNumber() != null) {
				sunspotNumberSeries.addLast(hp.getInfoDate().getTime(), hp.getSunspotNumber());
			}
		}
	}
}
