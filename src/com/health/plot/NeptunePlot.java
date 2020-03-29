package com.health.plot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.graphics.Color;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.health.data.Planet;
import com.health.data.PlanetType;
import com.health.main.R;

public class NeptunePlot extends DataPlot {
	private List<SimpleXYSeries> neptuneRiseSerieses;
	private List<SimpleXYSeries> neptuneSetSerieses;
	
	private LineAndPointFormatter neptuneRiseFormatter;
	private LineAndPointFormatter neptuneSetFormatter;
	
	private List<Planet> neptuneList = new ArrayList<Planet>();
	
	public NeptunePlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> neptuneList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.NEPTUNE_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_neptune;
		setupNeptunePlot();
		// получаем форматтер для серии
		neptuneRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		neptuneSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(neptuneList);
	}
	
	private void setupNeptunePlot(){
		plot.setRangeValueFormat(new Format() {

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				Date date = new Date(((Number) obj).longValue());
				return PlotService.hourFormat.format(date, toAppendTo, pos);
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		});

		plot.getLegendWidget().setVisible(true);
		plot.getLegendWidget().setWidth(PixelUtils.dpToPix(150), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().setHeight(PixelUtils.dpToPix(20), SizeLayoutType.ABSOLUTE);
		plot.getLegendWidget().position(
                60,
                XLayoutStyle.ABSOLUTE_FROM_LEFT,
                -7,
                YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.LEFT_TOP);
		DynamicTableModel t = new DynamicTableModel(2, 1);
		plot.getLegendWidget().setTableModel(t);
	}

	public void updateData(List<?> ... data){
		if(data != null){
			if(data.length > 0 && data[0].size() > 0 && data[0].get(0) instanceof Planet){
				neptuneList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateNeptuneRiseSeries();
		updateNeptuneSetSeries();
    
    	// задаем новую серию
		if(neptuneRiseSerieses.size() > 0){
			plot.addSeries(neptuneRiseSerieses.get(0), neptuneRiseFormatter);
		}
		if(neptuneSetSerieses.size() > 0){
			plot.addSeries(neptuneSetSerieses.get(0), neptuneSetFormatter);
		}
		
    	for(int i = 1; i < neptuneRiseSerieses.size(); i++){
    		plot.addSeries(neptuneRiseSerieses.get(i), neptuneRiseFormatter);
    	}
    	for(int i = 1; i < neptuneSetSerieses.size(); i++){
    		plot.addSeries(neptuneSetSerieses.get(i), neptuneSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateNeptuneRiseSeries() {
		neptuneRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : neptuneList) {
			if (p.getNeptuneRise() != null && (neptuneRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getNeptuneRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(neptuneRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					neptuneRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				neptuneRiseSerieses.get(neptuneRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateNeptuneSetSeries() {
		neptuneSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : neptuneList) {
			if (p.getNeptuneSet() != null && (neptuneSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getNeptuneSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(neptuneSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					neptuneSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				neptuneSetSerieses.get(neptuneSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
