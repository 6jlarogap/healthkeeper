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

public class Electron08MeVPlot extends DataPlot {
	private SimpleXYSeries electron08MeVSeries;
	private LineAndPointFormatter electron08MeVFormatter;
	private List<Particle> particleList = new ArrayList<Particle>();
	
	public Electron08MeVPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Particle> particleList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit);

		ownSerieses.add(electron08MeVSeries);
		
		dataTypeId = ParticleType.ELECTRON_08MEV_TYPE;
		isHaveHistogramm = true;
		layoutHelpDialogId = R.layout.dialog_plot_help_electron08mev;
		histogrammFloorStep = 100000000;
		
		setupElectron08MeVPlot();
		// получаем форматтер для серии
		electron08MeVFormatter = getLineAndPointSeriesFormatter(ParticleType.ELECTRON_08MEV_TYPE_COLOR, ParticleType.ELECTRON_08MEV_TYPE_COLOR, ParticleType.ELECTRON_08MEV_TYPE_COLOR,
				PlotService.plf, POINT_LABELER_TIMEOFFSET_EXP_FOR_DOMAIN_TICK, null, null, null, 3, 5);
    	
		updateData(particleList);
	}
	
	private void setupElectron08MeVPlot(){
		plot.setIndentY(100000000, 100000000);
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
    	updateElectron08MeVSeries();

    	// задаем новую серию
    	plot.addSeries(electron08MeVSeries, electron08MeVFormatter);
		if(isHaveJuxtapose()){
			plot.redraw();
			updateJuxtaposeSeries();
			addJuxtaposeSeries();
		}

		updateRangeStep();
		
    	plot.redraw();
	}
	
	private void updateElectron08MeVSeries() {
		ownSerieses.remove(electron08MeVSeries);
		electron08MeVSeries = new SimpleXYSeries("");
		ownSerieses.add(electron08MeVSeries);

		for (Particle p : particleList) {
			if (p.getElectron08MeV() != null) {
				electron08MeVSeries.addLast(p.getInfoDate().getTime(), p.getElectron08MeV());
			}
		}
	}
}
