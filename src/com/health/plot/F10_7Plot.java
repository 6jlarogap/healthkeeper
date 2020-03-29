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

public class F10_7Plot extends DataPlot {
	private SimpleXYSeries F10_7Series;
	private LineAndPointFormatter F10_7Formatter;
	private List<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
	
	public F10_7Plot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<HelioPhysics> helioPhysicsList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(F10_7Series);
		
		dataTypeId = HelioPhysicsType.F10_7_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_f10_7;
		histogrammFloorStep = 10;
		
		setupF10_7Plot();
		// получаем форматтер для серии
    	F10_7Formatter = getLineAndPointSeriesFormatter(HelioPhysicsType.F10_7_TYPE_COLOR, HelioPhysicsType.F10_7_TYPE_COLOR, HelioPhysicsType.F10_7_TYPE_COLOR,
		        PlotService.plf, POINT_LABELER_TIMEOFFSET_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(helioPhysicsList);
	}
	
	private void setupF10_7Plot(){
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
    	updateF10_7Series();

    	// задаем новую серию
    	plot.addSeries(F10_7Series, F10_7Formatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateF10_7Series() {
		ownSerieses.remove(F10_7Series);
		F10_7Series = new SimpleXYSeries("");
		ownSerieses.add(F10_7Series);

		for (HelioPhysics hp : helioPhysicsList) {
			if (hp.getF10_7() != null) {
				F10_7Series.addLast(hp.getInfoDate().getTime(), hp.getF10_7());
			}
		}
	}
}
