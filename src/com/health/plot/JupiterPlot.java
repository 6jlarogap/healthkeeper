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

public class JupiterPlot extends DataPlot {
	private List<SimpleXYSeries> jupiterRiseSerieses;
	private List<SimpleXYSeries> jupiterSetSerieses;
	
	private LineAndPointFormatter jupiterRiseFormatter;
	private LineAndPointFormatter jupiterSetFormatter;
	
	private List<Planet> jupiterList = new ArrayList<Planet>();
	
	public JupiterPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> jupiterList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.JUPITER_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_jupiter;
		setupJupiterPlot();
		// получаем форматтер для серии
		jupiterRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		jupiterSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(jupiterList);
	}
	
	private void setupJupiterPlot(){
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
				jupiterList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateJupiterRiseSeries();
		updateJupiterSetSeries();
    
    	// задаем новую серию
		if(jupiterRiseSerieses.size() > 0){
			plot.addSeries(jupiterRiseSerieses.get(0), jupiterRiseFormatter);
		}
		if(jupiterSetSerieses.size() > 0){
			plot.addSeries(jupiterSetSerieses.get(0), jupiterSetFormatter);
		}
		
    	for(int i = 1; i < jupiterRiseSerieses.size(); i++){
    		plot.addSeries(jupiterRiseSerieses.get(i), jupiterRiseFormatter);
    	}
    	for(int i = 1; i < jupiterSetSerieses.size(); i++){
    		plot.addSeries(jupiterSetSerieses.get(i), jupiterSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateJupiterRiseSeries() {
		jupiterRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : jupiterList) {
			if (p.getJupiterRise() != null && (jupiterRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getJupiterRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(jupiterRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					jupiterRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				jupiterRiseSerieses.get(jupiterRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateJupiterSetSeries() {
		jupiterSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : jupiterList) {
			if (p.getJupiterSet() != null && (jupiterSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getJupiterSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(jupiterSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					jupiterSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				jupiterSetSerieses.get(jupiterSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
