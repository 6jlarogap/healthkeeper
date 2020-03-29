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

public class Proton10MeVPlot extends DataPlot {
	private SimpleXYSeries proton10MeVSeries;
	
	private LineAndPointFormatter proton10MeVFormatter;
	
	private List<Particle> particleList = new ArrayList<Particle>();
	
	public Proton10MeVPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Particle> particleList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);
		
		ownSerieses.add(proton10MeVSeries);
		
		dataTypeId = ParticleType.PROTON_10MEV_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_proton10mev;
		histogrammFloorStep = 100000;
		
		setupProton10MeVPlot();
		// получаем форматтер для серии
		proton10MeVFormatter = getLineAndPointSeriesFormatter(ParticleType.PROTON_10MEV_TYPE_COLOR, ParticleType.PROTON_10MEV_TYPE_COLOR, ParticleType.PROTON_10MEV_TYPE_COLOR,
				PlotService.plf, POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(particleList);
	}
	
	private void setupProton10MeVPlot(){
		plot.setIndentY(5000, 5000);
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
    	updateProton10MeVSeries();

    	// задаем новую серию
    	plot.addSeries(proton10MeVSeries, proton10MeVFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateProton10MeVSeries() {
		ownSerieses.remove(proton10MeVSeries);
		proton10MeVSeries = new SimpleXYSeries("");
		ownSerieses.add(proton10MeVSeries);

		for (Particle p : particleList) {
			if (p.getProton10MeV() != null) {
				proton10MeVSeries.addLast(p.getInfoDate().getTime(), p.getProton10MeV());
			}
		}
	}
}
