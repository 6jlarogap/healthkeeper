package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.GeoPhysics;
import com.health.data.GeoPhysicsType;
import com.health.main.R;

public class ApPlot extends DataPlot {
	private SimpleXYSeries ApSeries;
	private LineAndPointFormatter ApFormatter;
	private List<GeoPhysics> geoPhysicsList = new ArrayList<GeoPhysics>();
	
	public ApPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<GeoPhysics> geoPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(ApSeries);
		
		dataTypeId = GeoPhysicsType.AP_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_ap;
		isHaveHistogramm = true;
		histogrammFloorStep = 5;
		
		setupApPlot();
		// получаем форматтер для серии
    	ApFormatter = getLineAndPointSeriesFormatter(GeoPhysicsType.AP_TYPE_COLOR, GeoPhysicsType.AP_TYPE_COLOR, GeoPhysicsType.AP_TYPE_COLOR,
		        PlotService.plf, POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
		updateData(geoPhysicsList);
	}
	
	private void setupApPlot(){
		plot.setIndentY(3, 3);
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
	}
	
	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof GeoPhysics){
				geoPhysicsList = (List<GeoPhysics>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
    	updateApSeries();

    	// задаем новую серию
    	plot.addSeries(ApSeries, ApFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateApSeries() {
		ownSerieses.remove(ApSeries);
		ApSeries = new SimpleXYSeries("");
		ownSerieses.add(ApSeries); 
		
		for(GeoPhysics gp : geoPhysicsList) {
			if (gp.getAp() != null) {
				ApSeries.addLast(gp.getInfoDate().getTime(), gp.getAp());
			}
		}
	}
}
