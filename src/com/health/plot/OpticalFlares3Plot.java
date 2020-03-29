package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.HelioPhysics;
import com.health.data.HelioPhysicsType;
import com.health.main.R;

public class OpticalFlares3Plot extends DataPlot {
	private SimpleXYSeries flares3Series;
	
	private LineAndPointFormatter flares3Formatter;
	
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public OpticalFlares3Plot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> helioPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);
		layoutHelpDialogId = R.layout.dialog_plot_help_flares_optical;
		setupSunspotAreaPlot();
		// получаем форматтер для серии
		flares3Formatter = getLineAndPointSeriesFormatter(HelioPhysicsType.FLARES_3_TYPE_COLOR, HelioPhysicsType.FLARES_3_TYPE_COLOR, null, new PointLabelFormatter(HelioPhysicsType.FLARES_3_TYPE_COLOR),
				POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(helioPhysicsList);
	}
	
	private void setupSunspotAreaPlot(){
		plot.setIndentY(1, 2);
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
		updateFlares3Series();
    
    	// задаем новую серию
    	plot.addSeries(flares3Series, flares3Formatter);

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateFlares3Series() {
		flares3Series = new SimpleXYSeries("");

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getFlares3() != null && (flares3Series.size() == 0 || hp.getInfoDate().getTime() - flares3Series.getX(flares3Series.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				flares3Series.addLast(hp.getInfoDate().getTime(), hp.getFlares3());
			}
		}
	}
}
