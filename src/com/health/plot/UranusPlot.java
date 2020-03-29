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

public class UranusPlot extends DataPlot {
	private List<SimpleXYSeries> uranusRiseSerieses;
	private List<SimpleXYSeries> uranusSetSerieses;
	
	private LineAndPointFormatter uranusRiseFormatter;
	private LineAndPointFormatter uranusSetFormatter;
	
	private List<Planet> uranusList = new ArrayList<Planet>();
	
	public UranusPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> uranusList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.URANUS_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_uranus;
		setupUranusPlot();
		// получаем форматтер для серии
		uranusRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		uranusSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(uranusList);
	}
	
	private void setupUranusPlot(){
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
				uranusList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updateUranusRiseSeries();
		updateUranusSetSeries();
    
    	// задаем новую серию
		if(uranusRiseSerieses.size() > 0){
			plot.addSeries(uranusRiseSerieses.get(0), uranusRiseFormatter);
		}
		if(uranusSetSerieses.size() > 0){
			plot.addSeries(uranusSetSerieses.get(0), uranusSetFormatter);
		}
		
    	for(int i = 1; i < uranusRiseSerieses.size(); i++){
    		plot.addSeries(uranusRiseSerieses.get(i), uranusRiseFormatter);
    	}
    	for(int i = 1; i < uranusSetSerieses.size(); i++){
    		plot.addSeries(uranusSetSerieses.get(i), uranusSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updateUranusRiseSeries() {
		uranusRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : uranusList) {
			if (p.getUranusRise() != null && (uranusRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getUranusRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(uranusRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					uranusRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				uranusRiseSerieses.get(uranusRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updateUranusSetSeries() {
		uranusSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : uranusList) {
			if (p.getUranusSet() != null && (uranusSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getUranusSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(uranusSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					uranusSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				uranusSetSerieses.get(uranusSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
