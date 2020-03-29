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

public class NewRegionsPlot extends DataPlot {
	private SimpleXYSeries newRegionsSeries;
	private LineAndPointFormatter newRegionsFormatter;
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public NewRegionsPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> helioPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(newRegionsSeries);
		
		dataTypeId = HelioPhysicsType.NEW_REGIONS_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_new_regions;
		
		setupNewRegionsPlot();
		// получаем форматтер для серии
    	newRegionsFormatter = getLineAndPointSeriesFormatter(HelioPhysicsType.NEW_REGIONS_TYPE_COLOR, HelioPhysicsType.NEW_REGIONS_TYPE_COLOR, HelioPhysicsType.NEW_REGIONS_TYPE_COLOR,
    			PlotService.plf, POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(helioPhysicsList);
	}
	
	private void setupNewRegionsPlot(){
		plot.setIndentY(1, 1);
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
    	updateNewRegionsSeries();

    	// задаем новую серию
    	plot.addSeries(newRegionsSeries, newRegionsFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateNewRegionsSeries() {
		ownSerieses.remove(newRegionsSeries);
		newRegionsSeries = new SimpleXYSeries("");
		ownSerieses.add(newRegionsSeries);

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getNewRegions() != null) {
				newRegionsSeries.addLast(hp.getInfoDate().getTime(), hp.getNewRegions());
			}
		}
	}
}
