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

public class PlutoPlot extends DataPlot {
	private List<SimpleXYSeries> plutoRiseSerieses;
	private List<SimpleXYSeries> plutoSetSerieses;
	
	private LineAndPointFormatter plutoRiseFormatter;
	private LineAndPointFormatter plutoSetFormatter;
	
	private List<Planet> plutoList = new ArrayList<Planet>();
	
	public PlutoPlot(Context context, Date plotDateFrom, Date plotDateTo, Date markedDate, String name, String unit, List<Planet> plutoList){
		super(context, plotDateFrom, plotDateTo, markedDate, name, unit, PlanetType.TYPE_MIN_BORDER, PlanetType.TYPE_MAX_BORDER, PlanetType.TYPE_STEP);
		dataTypeId = PlanetType.PLUTO_TYPE;
		layoutHelpDialogId = R.layout.dialog_plot_help_pluto;
		setupPlutoPlot();
		// получаем форматтер для серии
		plutoRiseFormatter = getLineAndPointSeriesFormatter(Color.YELLOW, Color.YELLOW, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		plutoSetFormatter = getLineAndPointSeriesFormatter(Color.RED, Color.RED, null, PlotService.plf, POINT_LABELER_TIME, null, null, null, 3, 5);
		
		updateData(plutoList);
	}
	
	private void setupPlutoPlot(){
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
				plutoList = (List<Planet>) data[0];
			}
		}
		if(!plot.isEmpty()){
			plot.clear();
		}
		
		// обновляем серию
		updatePlutoRiseSeries();
		updatePlutoSetSeries();
    
    	// задаем новую серию
		if(plutoRiseSerieses.size() > 0){
			plot.addSeries(plutoRiseSerieses.get(0), plutoRiseFormatter);
		}
		if(plutoSetSerieses.size() > 0){
			plot.addSeries(plutoSetSerieses.get(0), plutoSetFormatter);
		}
		
    	for(int i = 1; i < plutoRiseSerieses.size(); i++){
    		plot.addSeries(plutoRiseSerieses.get(i), plutoRiseFormatter);
    	}
    	for(int i = 1; i < plutoSetSerieses.size(); i++){
    		plot.addSeries(plutoSetSerieses.get(i), plutoSetFormatter);
    	}
    	
    	plot.redraw();
	}
	
	private void updatePlutoRiseSeries() {
		plutoRiseSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : plutoList) {
			if (p.getPlutoRise() != null && (plutoRiseSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getPlutoRise().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(plutoRiseSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					plutoRiseSerieses.add(new SimpleXYSeries("Восход"));
				}
				
				plutoRiseSerieses.get(plutoRiseSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
	
	private void updatePlutoSetSeries() {
		plutoSetSerieses = new ArrayList<SimpleXYSeries>();
		long lastX = -1;
		long lastY = -1;

		for (Planet p : plutoList) {
			if (p.getPlutoSet() != null && (plutoSetSerieses.size() == 0 || p.getInfoDate().getTime() - lastX >= MIN_TIME_INTERVAL)) {
				long currentY = (p.getPlutoSet().getTime() - p.getInfoDate().getTimezoneOffset() * 60 * 1000) % PlotService.DAY;
				
				if(plutoSetSerieses.size() == 0 || Math.abs(lastY - currentY) > (12L * PlotService.HOUR)){
					plutoSetSerieses.add(new SimpleXYSeries("Заход"));
				}
				
				plutoSetSerieses.get(plutoSetSerieses.size() - 1).addLast(p.getInfoDate().getTime(), currentY);
				lastX = p.getInfoDate().getTime();
				lastY = currentY;
			}
		}
	}
}
