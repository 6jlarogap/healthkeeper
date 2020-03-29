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

public class Proton1MeVPlot extends DataPlot {
	private SimpleXYSeries proton1MeVSeries;
	
	private LineAndPointFormatter proton1MeVFormatter;
	
	private List<Particle> particleList = new ArrayList<Particle>();
	
	public Proton1MeVPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Particle> particleList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(proton1MeVSeries);
		
		dataTypeId = ParticleType.PROTON_1MEV_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_proton1mev;
		histogrammFloorStep = 1000000;
		
		setupProton1MeVPlot();
		// получаем форматтер для серии
		proton1MeVFormatter = getLineAndPointSeriesFormatter(ParticleType.PROTON_1MEV_TYPE_COLOR, ParticleType.PROTON_1MEV_TYPE_COLOR, ParticleType.PROTON_1MEV_TYPE_COLOR,
				PlotService.plf, POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(particleList);
	}
	
	private void setupProton1MeVPlot(){
		plot.setIndentY(100000, 100000);
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
    	updateProton1MeVSeries();

    	// задаем новую серию
    	plot.addSeries(proton1MeVSeries, proton1MeVFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateProton1MeVSeries() {
		ownSerieses.remove(proton1MeVSeries);
		proton1MeVSeries = new SimpleXYSeries("");
		ownSerieses.add(proton1MeVSeries);

		for (Particle p : particleList) {
			if (p.getProton1MeV() != null) {
				proton1MeVSeries.addLast(p.getInfoDate().getTime(), p.getProton1MeV());
			}
		}
	}
}
