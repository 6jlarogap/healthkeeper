package com.health.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Particle;
import com.health.data.ParticleType;
import com.health.main.R;

public class Electron2MeVPlot extends DataPlot {
	private SimpleXYSeries electron2MeVSeries;
	private LineAndPointFormatter electron2MeVFormatter;
	private List<Particle> particleList = new ArrayList<Particle>();
	
	public Electron2MeVPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Particle> particleList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(electron2MeVSeries);
		
		dataTypeId = ParticleType.ELECTRON_2MEV_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_electron2mev;
		histogrammFloorStep = 100000000;
		
		setupElectron2MeVPlot();
		// получаем форматтер для серии
		electron2MeVFormatter = getLineAndPointSeriesFormatter(ParticleType.ELECTRON_2MEV_TYPE_COLOR, ParticleType.ELECTRON_2MEV_TYPE_COLOR, ParticleType.ELECTRON_2MEV_TYPE_COLOR,
		        PlotService.plf, POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(particleList);
	}
	
	private void setupElectron2MeVPlot(){
		plot.setIndentY(1000000, 1000000);
		plot.getGraphWidget().setRangeValueFormat(PlotService.expFormat);
		plot.AddOnChangeDomainBoundaryListener(new OnChangeDomainBoundaryListener(){
			@Override
			public void OnChangeDomainBoundary(){
				updateRangeStep();
			}
		});
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Particle){
				particleList = (List<Particle>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
    	updateElectron2MeVSeries();

    	// задаем новую серию
    	plot.addSeries(electron2MeVSeries, electron2MeVFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
    	
    	plot.redraw();
	}
	
	private void updateElectron2MeVSeries() {
		ownSerieses.remove(electron2MeVSeries);
		electron2MeVSeries = new SimpleXYSeries("");
		ownSerieses.add(electron2MeVSeries);

		for (Particle p : particleList) {
			if (p.getElectron2MeV() != null && (electron2MeVSeries.size() == 0 || p.getInfoDate().getTime() - electron2MeVSeries.getX(electron2MeVSeries.size() - 1).longValue() >= MIN_TIME_INTERVAL)) {
				electron2MeVSeries.addLast(p.getInfoDate().getTime(), p.getElectron2MeV());
			}
		}
	}
}
