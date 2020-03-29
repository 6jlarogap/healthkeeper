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

public class Proton100MeVPlot extends DataPlot {
	private SimpleXYSeries proton100MeVSeries;
	
	private LineAndPointFormatter proton100MeVFormatter;
	
	private List<Particle> particleList = new ArrayList<Particle>();
	
	public Proton100MeVPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Particle> particleList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(proton100MeVSeries);
		
		dataTypeId = ParticleType.PROTON_100MEV_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_proton100mev;
		histogrammFloorStep = 1000;
		
		setupProton100MeVPlot();
		// получаем форматтер для серии
		proton100MeVFormatter = getLineAndPointSeriesFormatter(ParticleType.PROTON_100MEV_TYPE_COLOR, ParticleType.PROTON_100MEV_TYPE_COLOR, ParticleType.PROTON_100MEV_TYPE_COLOR,
				PlotService.plf, POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(particleList);
	}
	
	private void setupProton100MeVPlot(){
		plot.setIndentY(500, 500);
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
    	updateProton100MeVSeries();

    	// задаем новую серию
    	plot.addSeries(proton100MeVSeries, proton100MeVFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateProton100MeVSeries() {
		ownSerieses.remove(proton100MeVSeries);
		proton100MeVSeries = new SimpleXYSeries("");
		ownSerieses.add(proton100MeVSeries);

		for (Particle p : particleList) {
			if (p.getProton100MeV() != null) {
				proton100MeVSeries.addLast(p.getInfoDate().getTime(), p.getProton100MeV());
			}
		}
	}
}
